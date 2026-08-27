package com.davivienda.sv.app.repositories.sqlserver;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.davivienda.sv.app.entities.sqlserver.DetalleTokenAutorizacion;

public interface DetalleTokenAutorizacionRepo extends JpaRepository<DetalleTokenAutorizacion, Long> {
    
    @Query(value = "select * from BS_DetalleTokenAutorizacion where Token = :token", nativeQuery = true)
    List<DetalleTokenAutorizacion> findByToken(
        @Param("token") Long token
    );
}
