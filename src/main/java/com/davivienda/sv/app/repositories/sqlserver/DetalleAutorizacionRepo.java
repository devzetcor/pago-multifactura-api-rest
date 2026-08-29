package com.davivienda.sv.app.repositories.sqlserver;

import com.davivienda.sv.app.entities.sqlserver.DetalleAutorizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetalleAutorizacionRepo extends JpaRepository<DetalleAutorizacion, Long> {

    @Query(value = "select * from BS_DetalleAutorizacion where Rol = :rol and DefinicionAutorizacion IN :definicion_autorizacion", nativeQuery = true)
    List<DetalleAutorizacion> findByDefinicionAutorizacion(
        @Param("rol") Long rol,
        @Param("definicion_autorizacion") List<Long> definicionAutorizacion
    );

    @Query(value = "select * from BS_DetalleAutorizacion where Rol IN :roles and DefinicionAutorizacion IN :definicion_autorizacion", nativeQuery = true)
    List<DetalleAutorizacion> findByDefinicionAutorizacion(
        @Param("roles") List<Long> roles,
        @Param("definicion_autorizacion") List<Long> definicionAutorizacion
    );

    @Query(value = "select * from BS_DetalleAutorizacion where Rol = :rol and DefinicionAutorizacion IN :definiciones", nativeQuery = true)
    List<DetalleAutorizacion> findByRolAndDefinicionAutorizacionIn(
        @Param("rol") Integer rol, 
        @Param("definiciones") List<Integer> definiciones
    );

    @Query(value = "select * from BS_DetalleAutorizacion where DefinicionAutorizacion = :definicion", nativeQuery = true)
    DetalleAutorizacion findByDefinicionAutorizacion(
        @Param("definicion") Long definicionAutorizacion
    );

    @Query(value = "select * from BS_DetalleAutorizacion where DefinicionAutorizacion in :definiciones", nativeQuery = true)
    List<DetalleAutorizacion> findByDefinicionAutorizacion(
        @Param("definiciones") List<Long> definiciones
    );

    @Query(value = "SELECT * FROM BS_DetalleAutorizacion da WHERE da.DefinicionAutorizacion IN :definiciones AND da.Rol = :rol", nativeQuery = true)
    List<DetalleAutorizacion> findByDefinicionAutorizacionAndRol(
        @Param("definiciones") List<Long> definiciones,
        @Param("rol") Integer rol
    );

    @Query(value = "SELECT * FROM BS_DetalleAutorizacion da WHERE da.DefinicionAutorizacion IN :definiciones AND da.Rol IN :roles", nativeQuery = true)
    List<DetalleAutorizacion> findAllByRolesAndDefinicionAutorizacionIn(
        @Param("roles") List<Integer> rolesIds,
        @Param("definiciones") List<Integer> definicionesIds
    );

}
