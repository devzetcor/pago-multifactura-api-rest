package com.davivienda.sv.app.repositories.sqlserver;

import com.davivienda.sv.app.entities.sqlserver.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolUsuarioRepo extends JpaRepository<RolUsuario, Long> {

    @Query(value = "select top 1 * from BS_RolUsuario where Usuario = :username and Cliente = :cliente", nativeQuery = true)
    Optional<RolUsuario> findByUsuarioAndCliente(
        @Param("username") String username, 
        @Param("cliente") Integer clientId
    );
    
    @Query(value = "select * from BS_RolUsuario where Usuario = :username and Cliente = :cliente", nativeQuery = true)
    List<RolUsuario> findAllByUsuarioAndCliente(
        @Param("username") String username, 
        @Param("cliente") Integer clientId
    );
}