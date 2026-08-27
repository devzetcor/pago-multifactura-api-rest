package com.davivienda.sv.app.services;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.dto.InfoUsuarioDto;
import com.davivienda.sv.app.dto.RolProductoDto;
import com.davivienda.sv.app.dto.UsuarioClienteRequestDto;
import com.davivienda.sv.app.entities.sqlserver.UsuarioWC;
import com.davivienda.sv.app.repositories.sqlserver.UsuarioWCRepo;
import com.davivienda.sv.app.util.UserStatus;

@Service
public class UsuarioWCService {

    private static final Logger LOGGER = LogManager.getLogger(UsuarioWCService.class);
    private final UsuarioWCRepo usuarioWCRepo;

    UsuarioWCService(UsuarioWCRepo usuarioWCRepo){
        this.usuarioWCRepo = usuarioWCRepo;
    }
    
    public Response<InfoUsuarioDto> getInfoUsuario(Request<UsuarioClienteRequestDto> request) {
        try {
            UsuarioClienteRequestDto datos = request.getBody();
            String usuario = datos.getUsuario();
            Long cliente = datos.getCliente();
    
            UsuarioWC resultado = usuarioWCRepo.findByUsuarioAndCliente(usuario, cliente);
    
            if (resultado == null) {
                return new Response<>(request, null);
            }
    
            UsuarioWC usuarioWC = resultado;
    
            List<RolProductoDto> roles = usuarioWC.getRolesUsuarios()
                    .stream()
                    .filter(rolUsuario -> rolUsuario.getRolWC() != null)
                    .map(rolUsuario -> RolProductoDto.builder()
                            .nombreRol(rolUsuario.getRolWC().getNombre().trim())
                            .producto(rolUsuario.getProducto())
                            .rol(rolUsuario.getRolWC().getRol())
                            .build())
                    .collect(Collectors.toList());
    
            InfoUsuarioDto infoUsuario = InfoUsuarioDto.builder()
                    .cliente(usuarioWC.getCliente().getCliente())
                    .usuario(usuarioWC.getUsuarioPk().trim())
                    .nombre(usuarioWC.getNombre().trim())
                    .apellido(usuarioWC.getApellido().trim())
                    .puesto(usuarioWC.getPuesto().trim())
                    .email(usuarioWC.getEmail().trim())
                    .numeroDocumento(usuarioWC.getNumeroDocumento().trim())
                    .tipoDocumento(usuarioWC.getTipoDocumento())
                    .estado(UserStatus.fromId(usuarioWC.getStatus()))
                    .roles(roles)
                    .build();
    
            return new Response<>(request, infoUsuario);
    
        } catch (Exception ex) {
            LOGGER.error("Error al obtener información de usuario: " + ex.getMessage(), ex);
            throw ex;
        }
    }
    
}
