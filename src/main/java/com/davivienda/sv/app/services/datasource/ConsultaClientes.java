package com.davivienda.sv.app.services.datasource;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.dto.ClienteDto;
import com.davivienda.sv.app.dto.ListaEmpresasRequest;
import com.davivienda.sv.app.entities.sqlserver.Cliente;
import com.davivienda.sv.app.entities.sqlserver.UsuarioWC;
import com.davivienda.sv.app.repositories.sqlserver.ClienteRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ConsultaClientes {

    private static final Logger LOGGER = LogManager.getLogger(ConsultaClientes.class);
    final ClienteRepo clienteRepo;

    ConsultaClientes(ClienteRepo clienteRepo) {
        this.clienteRepo = clienteRepo;
    }

    public Response<List<ClienteDto>> findByUsername(Request<ListaEmpresasRequest> request) {
        try {
            ListaEmpresasRequest datos = request.getBody();
            String documento = datos.getDocumento();

            Response<List<ClienteDto>> respuesta = new Response<>(request, new ArrayList<ClienteDto>());
            List<Cliente> clientes = clienteRepo.findByNumeroDocumento(documento);

            List<ClienteDto> clientesDto = clientes.stream()
                    .map((cliente) -> ClienteDto.builder()
                        .cliente(cliente.getCliente())
                        .usuario(cliente.getUsuarios().stream().filter(user -> user.getNumeroDocumento().trim().toLowerCase().equals(documento.trim().toLowerCase()))
                        .map(UsuarioWC::getUsuarioPk).map(String::trim)
                        .findFirst().orElse(null))
                        .nombre(cliente.getNombre().trim())
                        .modulos(cliente.getModulos())
                        .direccion(cliente.getDireccion().trim())
                        .telefono(cliente.getTelefono().trim())
                        .fax(cliente.getFax().trim())
                        .email(cliente.getEmail().trim())
                        .nombreContacto(cliente.getNombreContacto().trim())
                        .apellidoContacto(cliente.getApellidoContacto().trim())
                        .build())
                    .collect(Collectors.toMap(
                        ClienteDto::getCliente,
                        Function.identity(),
                        (existente, duplicado) -> existente
                    ))
                    .values()
                    .stream()
                    .collect(Collectors.toList());

            respuesta.setBody(clientesDto);
            return respuesta;
        } catch (Throwable ex) {
            LOGGER.error("Error al buscar cliente: " + ex.getMessage(),ex);
            throw ex;
        }
    }

}
