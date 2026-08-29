package com.davivienda.sv.app.repositories.sqlserver;

import com.davivienda.sv.app.entities.sqlserver.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, String> {

       @Query("SELECT DISTINCT c FROM Cuenta c JOIN UsuarioCuenta uc ON c.cuenta = uc.cuenta.cuenta AND c.cliente = uc.cuenta.cliente WHERE c.cliente = :cliente AND uc.usuario = :usuario")
       List<Cuenta> findCuentasByClienteAndUsuario(
               @Param("cliente") Long cliente,
               @Param("usuario") String usuario
       );
}