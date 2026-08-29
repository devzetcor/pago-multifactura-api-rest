package com.davivienda.sv.app.services;

import com.davivienda.sv.app.dto.CuentaRequestDto;
import com.davivienda.sv.app.dto.CuentaResponseDto;
import com.davivienda.sv.app.entities.sqlserver.Cuenta;
import com.davivienda.sv.app.entities.sqlserver.UsuarioCuenta;
import com.davivienda.sv.app.repositories.sqlserver.CuentaRepository;
import com.davivienda.sv.app.repositories.sqlserver.UsuarioCuentaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CuentaService {


    private static final Logger LOGGER = LogManager.getLogger(CryptoService.class);

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private UsuarioCuentaRepository usuarioCuentaRepository;

    public List<CuentaResponseDto> getCuentasByClienteAndUsuario(CuentaRequestDto request) {
        try {
            // Buscar las relaciones usuario-cuenta
            List<UsuarioCuenta> usuarioCuentas = usuarioCuentaRepository
                .findByClienteAndUsuario(request.getCliente(), request.getUsuario());
            
            if (usuarioCuentas.isEmpty()) {
                return new ArrayList<>();
            }
            
            // Buscar las cuentas completas
            List<Cuenta> cuentas = cuentaRepository
                .findCuentasByClienteAndUsuario(request.getCliente(), request.getUsuario());
            
            // Convertir a DTOs
            List<CuentaResponseDto> response = new ArrayList<>();
            
            for (Cuenta cuenta : cuentas) {
                // Buscar los permisos correspondientes
                UsuarioCuenta usuarioCuenta = usuarioCuentas.stream()
                    .filter(uc -> uc.getCuenta().getCuenta().equals(cuenta.getCuenta()))
                    .findFirst()
                    .orElse(null);
                
                CuentaResponseDto dto = new CuentaResponseDto();
                dto.setCuenta(cuenta.getCuenta());
                dto.setCliente(cuenta.getCliente());
                dto.setTipoCuenta(cuenta.getTipoCuentaEntity());
                dto.setTipoCuentaNombre(cuenta.getTipoCuentaEntity() != null ? cuenta.getTipoCuentaEntity().getNombre() : null);
                dto.setNombre(cuenta.getNombre());
                dto.setAliasCuenta(cuenta.getAliasCuenta());
                dto.setEstatus(cuenta.getEstatus());
                dto.setEstatusHost(cuenta.getEstatusHost());
                dto.setMoneda(cuenta.getMoneda());
                dto.setLimite(cuenta.getLimite());
                dto.setFechaOtorgado(cuenta.getFechaOtorgado());
                dto.setFechaVencimiento(cuenta.getFechaVencimiento());
                dto.setEsInteligente(cuenta.getEsInteligente());
                dto.setEsCrediExpress(cuenta.getEsCrediExpress());
                
                if (usuarioCuenta != null) {
                    dto.setPermisos1(usuarioCuenta.getPermisos1());
                    dto.setPermisos2(usuarioCuenta.getPermisos2());
                    dto.setPermisos3(usuarioCuenta.getPermisos3());
                    dto.setPermisos4(usuarioCuenta.getPermisos4());
                    dto.setPermisos5(usuarioCuenta.getPermisos5());
                }
                
                response.add(dto);
            }
            
            return response;
            
        } catch (Exception e) {
            LOGGER.error("Error al buscar cuentas: " + e.getMessage(),e);
            return new ArrayList<>();
        }
    }
}