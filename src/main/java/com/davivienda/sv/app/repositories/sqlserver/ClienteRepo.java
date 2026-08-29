package com.davivienda.sv.app.repositories.sqlserver;

import com.davivienda.sv.app.entities.sqlserver.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepo extends JpaRepository<Cliente, Long> {

    // 1. JPQL: Se navega directamente a través del atributo 'usuarios' de la entidad BSCliente
    @Query(
            "SELECT DISTINCT c FROM BSCliente c " +
                    "JOIN c.usuarios u " +
                    "WHERE u.numeroDocumento = :numero_documento AND u.usuarioPk = :usuario"
    )
    Optional<Cliente> findByNumeroDocumentoAndUsuario(
            @Param("numero_documento") String numeroDocumento,
            @Param("usuario") String usuario
    );

    // 2. JPQL: Filtro directo por número de documento
    @Query(
            "SELECT DISTINCT c FROM BSCliente c " +
                    "JOIN c.usuarios u " +
                    "WHERE u.numeroDocumento = :numero_documento"
    )
    List<Cliente> findByNumeroDocumento(
            @Param("numero_documento") String numeroDocumento
    );

    // 3. JPQL: Filtro directo por el PK de UsuarioWC (usuarioPk)
    @Query(
            "SELECT DISTINCT c FROM BSCliente c " +
                    "JOIN c.usuarios u " +
                    "WHERE u.usuarioPk = :username"
    )
    List<Cliente> findByUsername(
            @Param("username") String username
    );
}