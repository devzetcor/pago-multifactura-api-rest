package com.davivienda.sv.app.services;

import com.davivienda.sv.app.dto.CuentaCargoResponseDto;
import com.davivienda.sv.app.dto.ResumenTransaccionesPorColectorDto;
import com.davivienda.sv.app.dto.TransaccionesPorEmpresaResponseDto;
import com.davivienda.sv.app.entities.db2.TransaccionDTO;
import com.davivienda.sv.app.entities.sqlserver.Cliente;
import com.davivienda.sv.app.repositories.sqlserver.ClienteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransaccionConsultaService {

    @Autowired
    private ClienteRepo clienteRepo;
    
    @Autowired
    private DrefTransaccionRestClient drefTransaccionRestClient;

    public TransaccionesPorEmpresaResponseDto consultarTransaccionesPorDocumento(String numeroDocumento) {
        // 1. Obtener clientes relacionados con el número de documento
        List<Cliente> clientes = clienteRepo.findByNumeroDocumento(numeroDocumento);
        
        if (clientes.isEmpty()) {
            return new TransaccionesPorEmpresaResponseDto(new ArrayList<>());
        }
        
        // 2. Crear mapa de empresa ID -> nombre de empresa
        Map<Long, String> empresaNombreMap = clientes.stream()
                .collect(Collectors.toMap(
                    Cliente::getCliente,
                    cliente -> cliente.getNombre() != null ? cliente.getNombre().trim() : "Sin nombre",
                    (existing, replacement) -> existing
                ));
        
        // 3. Extraer IDs de empresa
        List<Long> empresasRelacionadas = new ArrayList<>(empresaNombreMap.keySet());
        
        // 4. Consultar transacciones pendientes por esas empresas
        List<TransaccionDTO> transacciones = drefTransaccionRestClient.listarTransaccionesPendientesPorEmpresas(empresasRelacionadas);
        
        // 5. Agrupar y contar transacciones por empresa-colector
        Map<String, ResumenTransaccionesPorColectorDto> resumenMap = new HashMap<>();
        
        for (TransaccionDTO transaccion : transacciones) {
            String key = transaccion.getEmpresa() + "-" + transaccion.getIdColector();
            
            if (resumenMap.containsKey(key)) {
                ResumenTransaccionesPorColectorDto resumen = resumenMap.get(key);
                resumen.setCantidadTransaccionesPendientes(resumen.getCantidadTransaccionesPendientes() + 1);
            } else {
                String nombreEmpresa = empresaNombreMap.get(transaccion.getEmpresa());
                ResumenTransaccionesPorColectorDto nuevoResumen = new ResumenTransaccionesPorColectorDto(
                    transaccion.getEmpresa(),
                    nombreEmpresa,
                    transaccion.getIdColector(),
                    transaccion.getNombreColector(),
                    1
                );
                resumenMap.put(key, nuevoResumen);
            }
        }
        
        // 6. Convertir a lista y ordenar
        List<ResumenTransaccionesPorColectorDto> resumenTransacciones = new ArrayList<>(resumenMap.values());
        resumenTransacciones.sort((a, b) -> {
            int empresaComparison = a.getEmpresa().compareTo(b.getEmpresa());
            if (empresaComparison != 0) {
                return empresaComparison;
            }
            return a.getColector().compareTo(b.getColector());
        });
        
        return new TransaccionesPorEmpresaResponseDto(resumenTransacciones);
    }
    
    public CuentaCargoResponseDto consultarCuentaCargo(Long idColector, Long idEmpresa, String usuario) {
        TransaccionDTO ultimaTransaccion = drefTransaccionRestClient.obtenerUltimaTransaccionPorColectorEmpresaUsuario(idColector, idEmpresa, usuario);
        
        if (ultimaTransaccion == null) {
            return null;
        }
        
        return new CuentaCargoResponseDto(
            ultimaTransaccion.getCuentaCargo(),
            ultimaTransaccion.getTipoCuentaCargo(),
            ultimaTransaccion.getIdTransaccion(),
            ultimaTransaccion.getFechaCreacion(),
            ultimaTransaccion.getNombreColector(),
            ultimaTransaccion.getEmpresa(),
            ultimaTransaccion.getUsuarioCreacion()
        );
    }
}