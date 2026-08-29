package com.davivienda.sv.app.repositories.sqlserver;

import com.davivienda.sv.app.entities.sqlserver.UsuarioCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioCuentaRepository extends JpaRepository<UsuarioCuenta, String> {
    
    @Query(value = "SELECT * FROM BS_UsuarioCuenta " +
                   "WHERE Cliente = :cliente AND Usuario = :usuario", nativeQuery = true)
    List<UsuarioCuenta> findByClienteAndUsuario(
        @Param("cliente") Long cliente,
        @Param("usuario") String usuario
    );

}