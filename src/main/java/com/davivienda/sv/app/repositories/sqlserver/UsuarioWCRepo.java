package com.davivienda.sv.app.repositories.sqlserver;

import com.davivienda.sv.app.entities.sqlserver.UsuarioWC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioWCRepo extends JpaRepository<UsuarioWC, String> {

    @Query(value = "select * from BS_UsuarioWC buw where buw.Usuario IN :usuario and buw.Cliente = :cliente", nativeQuery = true)
    List<UsuarioWC> findByUsuariosAndCliente(
        @Param("usuario") List<String> usuario, 
        @Param("cliente") Long cliente
    );

    // Reemplaza la consulta nativa por JPQL limpia
    @Query("SELECT DISTINCT u FROM UsuarioWC u LEFT JOIN FETCH u.rolesUsuarios r WHERE u.usuarioPk = :usuario AND u.cliente.cliente = :cliente AND r.producto = 2")
    UsuarioWC findByUsuarioAndCliente(
            @Param("usuario") String usuario,
            @Param("cliente") Long cliente
    );

    @Query(value = "select * from BS_UsuarioWC buw where buw.Usuario = :usuario", nativeQuery = true)
    UsuarioWC findByUsuario(
        @Param("usuario") String usuario
    );

    @Query(value = "select * from BS_UsuarioWC buw where buw.Usuario IN :usuarios and buw.Cliente IN :clientes", nativeQuery = true)
    List<UsuarioWC> findAllByUsuariosAndClientes(
        @Param("usuarios") List<String> usuarios, 
        @Param("clientes") List<Integer> clientes
    );

    @Query(nativeQuery = true, value = "select * from BS_UsuarioWC buw where buw.Usuario = :username and buw.Cliente=:cliente")
    Optional<UsuarioWC> findByUsername(
        @Param("username") String username,
        @Param("cliente") Integer cliente
    );

}
