package com.davivienda.sv.app.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davivienda.sv.app.dto.ConsultaEnrolamientoDTO;
import com.davivienda.sv.app.entities.db2.DrefFacturaTransaccion;
import com.davivienda.sv.app.entities.db2.DrefTransaccion;
import com.davivienda.sv.app.entities.db2.FacturaTransaccion;
import com.davivienda.sv.app.entities.db2.TransaccionDTO;
import com.davivienda.sv.app.services.datasource.ConsultaAutorizacion;
import com.davivienda.sv.app.util.AppException;
import com.davivienda.sv.app.util.TransactionStatus;

@Service
public class TransaccionDetalleService {
    private static final Logger LOGGER = LogManager.getLogger(TransaccionDetalleService.class);
    private final ConsultaAutorizacion consultaAutorizacion;
    
    @Autowired
    DrefTransaccionRestClient drefTransaccionRestClient;
   
    @Autowired
    DreFacturaTransaccionRestClient dreFacturaTransaccionRestClient;
    
    public TransaccionDetalleService(
        ConsultaAutorizacion consultaAutorizacion
    ) {
        this.consultaAutorizacion = consultaAutorizacion;
    }

    public TransaccionDTO crearTransaccion(TransaccionDTO transaccion) {
        // Validar que la transacción tenga al menos una factura
        if (transaccion.getFacturas() == null || transaccion.getFacturas().isEmpty()) {
            throw new IllegalArgumentException("La transacción debe tener al menos una factura");
        }

        // Calcular el monto total de la transacción
        BigDecimal montoTotal = BigDecimal.ZERO;
        for (FacturaTransaccion factura : transaccion.getFacturas()) {
            montoTotal = montoTotal.add(factura.getMonto());
        }
        transaccion.setMontoTotal(montoTotal);

        // Establecer valores por defecto
        transaccion.setEstado("PENDIENTE");
        transaccion.setFechaCreacion(LocalDateTime.now());

        // Guardar la transacción y sus facturas
        TransaccionDTO transaccionResult = drefTransaccionRestClient.crearTransaccion(transaccion);
        LOGGER.info("transaccion creada..."+transaccionResult);
        consultaAutorizacion.saveTransactionAuthorization(transaccionResult.getIdTransaccion().intValue(),
                transaccion.getUsuarioCreacion());
        LOGGER.info("guardo  consultaAutorizacion...");
        return transaccionResult;
    }

    public TransaccionDTO obtenerTransaccion(Long idTransaccion) {
        return drefTransaccionRestClient.obtenerTransaccionConFacturas(idTransaccion);
    }

    public List<TransaccionDTO> listarTransacciones(ConsultaEnrolamientoDTO consultaEnrolamientoDTO) {
        List<TransaccionDTO> transacciones = drefTransaccionRestClient
                .listarTransacciones(consultaEnrolamientoDTO);
        transacciones = consultaAutorizacion.checkIfRequiresUserSignature(
            consultaEnrolamientoDTO.getUsuario(),
            consultaEnrolamientoDTO.getIdEmpresa().intValue(),
            transacciones
        );
        return transacciones;
    }

    public List<TransaccionDTO> listarTransacciones(ConsultaEnrolamientoDTO consultaEnrolamientoDTO, Integer page, Integer size) {
        List<TransaccionDTO> transacciones = drefTransaccionRestClient
                .listarTransaccionesPaginado(consultaEnrolamientoDTO, page, size);
        transacciones = consultaAutorizacion.checkIfRequiresUserSignature(
            consultaEnrolamientoDTO.getUsuario(),
            consultaEnrolamientoDTO.getIdEmpresa().intValue(),
            transacciones
        );
        return transacciones;
    }

    public List<TransaccionDTO> listarTransaccionesPorEstado(
        ConsultaEnrolamientoDTO consultaEnrolamientoDTO,
        String estado
    ) {
        List<TransaccionDTO> transacciones = drefTransaccionRestClient
                .listarTransaccionesPorEstado(consultaEnrolamientoDTO, estado);
        transacciones = consultaAutorizacion.checkIfRequiresUserSignature(
            consultaEnrolamientoDTO.getUsuario(),
            consultaEnrolamientoDTO.getIdEmpresa().intValue(), 
            transacciones
        );
        return transacciones;
    }

    public List<TransaccionDTO> listarTransaccionesPorEstado(
        ConsultaEnrolamientoDTO consultaEnrolamientoDTO,
        String estado,
        Integer page,
        Integer size
    ) {
        List<TransaccionDTO> transacciones = drefTransaccionRestClient
                .listarTransaccionesPorEstadoPaginado(consultaEnrolamientoDTO, estado, page, size);
//        if(!estado.equalsIgnoreCase("RECHAZADA"))
        transacciones = consultaAutorizacion.checkIfRequiresUserSignature(
            consultaEnrolamientoDTO.getUsuario(),
            consultaEnrolamientoDTO.getIdEmpresa().intValue(),
            transacciones
        );
        return transacciones;
    }

//    @Transactional("db2TransactionManager")
    public void aprobarTransaccion(Long idTransaccion, String usuarioAprobacion,
            ConsultaEnrolamientoDTO consultaEnrolamientoDTO, String estado) {
        // Verificar que la transacción exista y esté pendiente
        TransaccionDTO transaccion = drefTransaccionRestClient.obtenerTransaccionConFacturas(idTransaccion);
        if (transaccion == null) {
            throw new IllegalArgumentException("Transacción no encontrada");
        }

        if (!"PENDIENTE".equals(transaccion.getEstado())) {
            throw new IllegalStateException("Solo se pueden aprobar transacciones pendientes");
        }

        drefTransaccionRestClient.aprobarTransaccion(idTransaccion, usuarioAprobacion, estado);
    }

//    @Transactional("db2TransactionManager")
    public void rechazarTransaccion(Long idTransaccion, String motivoRechazo, String usuarioAprobacion) {
        // Verificar que la transacción exista y esté pendiente
        DrefTransaccion transaccion = drefTransaccionRestClient.findById(idTransaccion.intValue());
                                                         

        if (transaccion == null) {
            throw new IllegalArgumentException("Transacción no encontrada");
        }

        if (!"PENDIENTE".equals(transaccion.getEstado())) {
            throw new IllegalStateException("Solo se pueden rechazar transacciones pendientes");
        }

        transaccion.setMotivoRechazo(motivoRechazo);
        transaccion.setFechaAprobacion(LocalDateTime.now());
        transaccion.setUsuarioAprobacion(usuarioAprobacion);
        transaccion.setEstado("RECHAZADA");
        
        drefTransaccionRestClient.save(transaccion);
    }

    public void rechazarTransacciones(List<Long> transactionsIds, String motivoRechazo, String usuario) {
        List<DrefTransaccion> transactionsFound = drefTransaccionRestClient.findAllByIds(transactionsIds);
        if(transactionsFound.isEmpty()){
            throw new AppException("Transacciones no encontradas");
        }

        List<DrefTransaccion> pendingTransactions = transactionsFound.stream()
                            .filter(e -> e.getEstado().equals(TransactionStatus.PENDIENTE.getStatus()))
                            .collect(Collectors.toList());
        
        pendingTransactions.forEach(e -> {
            e.setEstado(TransactionStatus.RECHAZADA.getStatus());
            e.setFechaAprobacion(LocalDateTime.now());
            e.setUsuarioAprobacion(usuario);
            e.setMotivoRechazo(motivoRechazo);
        });

        drefTransaccionRestClient.saveAll(pendingTransactions);
    }

    public List<FacturaTransaccion> buscarFacturasPorColector(String colector) {
        List<DrefFacturaTransaccion> drefFacturas = dreFacturaTransaccionRestClient.findByColector(colector);
        List<FacturaTransaccion> facturas = drefFacturas.stream().map(e -> {
            FacturaTransaccion factura = new FacturaTransaccion();
            factura.setIdDetalle(e.getIdDetalle().longValue());
            factura.setIdTransaccion(e.getTransaccion().getIdTransaccion().longValue());
            factura.setNumeroFactura(e.getNumeroFactura());
            factura.setReferencia(e.getReferencia());
            factura.setMonto(e.getMonto());
            factura.setFechaVencimiento(e.getFechaVencimiento().toLocalDate());
            factura.setNombreCliente(e.getNombreCliente());
            factura.setEstado(e.getEstado());
            factura.setDescripcionError(e.getDescripcionError());
            factura.setFechaActualizacion(e.getFechaActualizacion().toLocalDate().toString());
            factura.setNpe(e.getNpe());
            return factura;
        }).collect(Collectors.toList());

        return facturas;
    }
}