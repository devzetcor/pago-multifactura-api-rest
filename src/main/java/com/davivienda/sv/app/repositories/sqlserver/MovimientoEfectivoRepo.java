package com.davivienda.sv.app.repositories.sqlserver;

import com.davivienda.sv.app.entities.sqlserver.MovimientoEfectivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovimientoEfectivoRepo extends JpaRepository<MovimientoEfectivo, Long> {
   
    @Query(value = "select top 1 token, * from BS_MovimientoEfectivo where cliente = :cliente order by fechaCreacion desc", nativeQuery = true)
    List<MovimientoEfectivo> findByCliente(
        @Param("cliente") Long cliente
    );

}
