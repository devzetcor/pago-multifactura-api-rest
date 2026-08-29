package com.davivienda.sv.app.services.datasource;

import com.davivienda.sv.app.entities.db2.*;
import com.davivienda.sv.app.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditService {
    private static final Logger LOGGER = LogManager.getLogger(AuditService.class);

    private final List<String> estados;

    
    @Autowired
    DetalleTransaccionAutorizacionRestClient detalleTransaccionAutorizacionRestClient;
   
    @Autowired
    DreFacturaTransaccionRestClient dreFacturaTransaccionRestClient;
    @Autowired
    DrefTransaccionRestClient drefTransaccionRestClient;
    @Autowired
    DrefTransaccionesAuditRestClient drefTransaccionesAuditRestClient;
    @Autowired
    DrefFacturasTransaccionAuditRestClient drefFacturasTransaccionAuditRestClient;
    @Autowired
    DetalleTransaccionAutorizacionAuditRestClient detalleTransaccionAutorizacionAuditRestClient;


    public AuditService(
        @Value("#{'${audit.job.transaction.status:APROBADA,RECHAZADA,PAGADA_CON_ERROR}'.split(',')}") List<String> estados
    ) {
        this.estados = estados;
    }

    public void executeAudit(Timestamp desde, Timestamp hasta) {
        List<DrefTransaccion> transacciones = auditTransactions(desde, hasta);

        List<DrefFacturaTransaccion> facturas = transacciones.stream()
                .flatMap(e -> e.getFacturas().stream())
                .collect(Collectors.toList());

        auditBills(facturas);

        List<DetalleTransaccionAutorizacion> firmas = transacciones.stream()
                .flatMap(e -> e.getFirmas().stream())
                .collect(Collectors.toList());

        auditSigns(firmas);

        detalleTransaccionAutorizacionRestClient.deleteAll(firmas);
        dreFacturaTransaccionRestClient.deleteAll(facturas);
        drefTransaccionRestClient.deleteAll(transacciones);
    }

    @Transactional(rollbackFor = SQLException.class)
    private List<DrefTransaccion> auditTransactions(Timestamp desde, Timestamp hasta) {
        List<DrefTransaccion> transacciones = drefTransaccionRestClient
                .findByFechaCreacionBetweenOrFechaAprobacionBetween(estados, desde, hasta);

        List<DrefTransaccionesAudit> auditsTransacciones = transacciones.stream()
                .map(t -> {
                    DrefTransaccionesAudit a = new DrefTransaccionesAudit();
                    a.setIdTransaccion(t.getIdTransaccion());
                    a.setFechaCreacion(Timestamp.valueOf(t.getFechaCreacion()));
                    a.setMontoTotal(t.getMontoTotal());
                    a.setEstado(t.getEstado());
                    a.setUsuarioCreacion(t.getUsuarioCreacion());
                    a.setFechaAprobacion(Timestamp.valueOf(t.getFechaAprobacion()));
                    a.setUsuarioAprobacion(t.getUsuarioAprobacion());
                    a.setIdColector(t.getIdColector());
                    a.setCuentaAbono(t.getCuentaAbono());
                    a.setTipoCuentaAbono(t.getTipoCuentaAbono());
                    a.setCuentaCargo(t.getCuentaCargo());
                    a.setTipoCuentaCargo(t.getTipoCuentaCargo());
                    a.setCuentaPago(null);
                    a.setTipoCuentaPago(null);
                    a.setCategoria(t.getCategoria());
                    a.setColumn1(null);
                    a.setColumn2(null);
                    a.setEmpresa(t.getEmpresa());
                    a.setNombreCategoria(t.getNombreCategoria());
                    a.setNombreColector(t.getNombreColector());
                    a.setCuentaContable(t.getCuentaContable());
                    a.setMotivoRechazo(t.getMotivoRechazo());

                    a.setAuditAccion("UPDATE");
                    a.setAuditFecha(new Timestamp(System.currentTimeMillis()));
                    return a;
                }).collect(Collectors.toList());

        drefTransaccionesAuditRestClient.saveAll(auditsTransacciones);
        LOGGER.info("Transacciones auditadas: " + auditsTransacciones.size());
        return transacciones;
    }

    @Transactional(rollbackFor = SQLException.class)
    private void auditBills(List<DrefFacturaTransaccion> facturas) {
        List<DrefFacturasTransaccionAudit> auditsFacturas = facturas.stream()
                .map(f -> {
                    DrefFacturasTransaccionAudit a = new DrefFacturasTransaccionAudit();
                    a.setIdDetalle(f.getIdDetalle());
                    a.setIdTransaccion(f.getTransaccion().getIdTransaccion());
                    a.setNumeroFactura(f.getNumeroFactura());
                    a.setMonto(f.getMonto());
                    a.setColector(f.getColector());
                    a.setReferencia(f.getReferencia());
                    a.setFechaVencimiento(Timestamp.valueOf(f.getFechaVencimiento()));
                    a.setNombreCliente(f.getNombreCliente());
                    a.setNpe(f.getNpe());
                    a.setEstado(f.getEstado());
                    a.setDescripcionError(f.getDescripcionError());
                    a.setFechaActualizacion(Timestamp.valueOf(f.getFechaActualizacion()));

                    // Campos de auditoría
                    a.setAuditAccion("UPDATE");
                    a.setAuditFecha(new Timestamp(System.currentTimeMillis()));
                    return a;
                }).collect(Collectors.toList());

        drefFacturasTransaccionAuditRestClient.saveAll(auditsFacturas);
        LOGGER.info("Facturas auditadas: " + auditsFacturas.size());
    }

    @Transactional(rollbackFor = SQLException.class)
    private void auditSigns(List<DetalleTransaccionAutorizacion> firmas) {
        List<DetalleTransaccionAutorizacionAudit> auditsAutorizaciones = firmas.stream()
                .map(a -> {
                    DetalleTransaccionAutorizacionAudit au = new DetalleTransaccionAutorizacionAudit();
                    au.setId(a.getId());
                    au.setIdTransaccion(a.getTransaccion().getIdTransaccion());
                    au.setIdRol(a.getIdRol());
                    au.setNivel(a.getNivel());
                    au.setUsuario(a.getUsuario());
                    au.setCliente(a.getCliente());
                    au.setFechaEstado(Timestamp.valueOf(a.getFechaEstado()));
                    au.setEstado(a.getEstado());
                    au.setDefinicionAutorizacion(a.getDefinicionAutorizacion());

                    au.setAuditAccion("UPDATE");
                    au.setAuditFecha(new Timestamp(System.currentTimeMillis()));
                    return au;
                }).collect(Collectors.toList());

        detalleTransaccionAutorizacionAuditRestClient.saveAll(auditsAutorizaciones);
        LOGGER.info("Autorizaciones auditadas: " + auditsAutorizaciones.size());
    }
}
