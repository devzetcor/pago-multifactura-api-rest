package com.davivienda.sv.app.repositories.sqlserver;

import com.davivienda.sv.app.entities.sqlserver.DefinicionAutorizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface DefinicionAutorizacionRepo extends JpaRepository<DefinicionAutorizacion, Long> {

    @Query(value = "select * from BS_DefinicionAutorizacion where Cliente = :cliente and Producto = :producto", nativeQuery = true)
    List<DefinicionAutorizacion> findAllByClienteAndProducto(
            @Param("cliente") Long cliente,
            @Param("producto") Long producto
    );

    // FIX: Se cambia 'select *' por 'select definicion.*'
    @Query(value = "select definicion.* from BS_DefinicionAutorizacion definicion left join BS_DetalleAutorizacion detalle on detalle.DefinicionAutorizacion = definicion.DefinicionAutorizacion where detalle.Rol = :rol and definicion.Producto = :producto and definicion.Cliente = :cliente", nativeQuery = true)
    List<DefinicionAutorizacion> findAllByClienteAndRolAndProducto(
            @Param("cliente") Long cliente,
            @Param("rol") Integer rol,
            @Param("producto") Long producto
    );

    // FIX: Se cambia 'select *' por 'select definicion.*'
    @Query(value = "select definicion.* from BS_DefinicionAutorizacion definicion left join BS_DetalleAutorizacion detalle on detalle.DefinicionAutorizacion = definicion.DefinicionAutorizacion where detalle.Rol IN :roles and definicion.Producto = :producto and definicion.Cliente IN :clientes", nativeQuery = true)
    List<DefinicionAutorizacion> findAllByClienteAndRolesAndProducto(
            @Param("clientes") List<Integer> clientes,
            @Param("roles") Collection<Integer> roles,
            @Param("producto") Long producto
    );

    // FIX: Se cambia 'select *' por 'select definicion.*'
    @Query(value = "select definicion.* from BS_DefinicionAutorizacion definicion left join BS_DetalleAutorizacion detalle on detalle.DefinicionAutorizacion = definicion.DefinicionAutorizacion where definicion.Producto = :producto and definicion.Cliente IN :clientes", nativeQuery = true)
    List<DefinicionAutorizacion> findAllByClientesAndProducto(
            @Param("clientes") List<Integer> clientes,
            @Param("producto") Long producto
    );

    @Query(value =  "select definicion.* from BS_DetalleAutorizacion detalle " +
            "left join BS_DefinicionAutorizacion definicion on detalle.DefinicionAutorizacion = definicion.DefinicionAutorizacion " +
            "left join BS_Cliente cliente on cliente.Cliente = definicion.Cliente " +
            "left join BS_UsuarioWC usuarioWC on usuarioWC.Cliente = cliente.Cliente " +
            "where usuarioWC.Usuario = :username and cliente.Cliente = :client and definicion.Producto = 2 " +
            "order by definicion.FechaEstado DESC", nativeQuery = true)
    List<DefinicionAutorizacion> findAllByClientAndUsername(
            @Param("client") Integer client,
            @Param("username") String username
    );

    @Query(value =  "select definicion.* from BS_DetalleAutorizacion detalle " +
            "left join BS_DefinicionAutorizacion definicion on detalle.DefinicionAutorizacion = definicion.DefinicionAutorizacion " +
            "left join BS_Cliente cliente on cliente.Cliente = definicion.Cliente " +
            "left join BS_UsuarioWC usuarioWC on usuarioWC.Cliente = cliente.Cliente " +
            "where usuarioWC.Usuario = :username and cliente.Cliente in :clients and definicion.Producto = 2 " +
            "order by definicion.FechaEstado DESC", nativeQuery = true)
    List<DefinicionAutorizacion> findAllByClientsAndUsername(
            @Param("clients") List<Integer> clients,
            @Param("username") String username
    );
}