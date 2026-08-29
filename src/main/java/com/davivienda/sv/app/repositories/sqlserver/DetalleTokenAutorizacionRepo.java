package com.davivienda.sv.app.repositories.sqlserver;

import com.davivienda.sv.app.entities.sqlserver.DetalleTokenAutorizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetalleTokenAutorizacionRepo extends JpaRepository<DetalleTokenAutorizacion, Long> {
    
    @Query(value = "select * from BS_DetalleTokenAutorizacion where Token = :token", nativeQuery = true)
    List<DetalleTokenAutorizacion> findByToken(
        @Param("token") Long token
    );
}
