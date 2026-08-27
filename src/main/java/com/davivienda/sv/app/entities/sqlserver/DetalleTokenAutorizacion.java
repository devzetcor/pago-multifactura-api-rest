package com.davivienda.sv.app.entities.sqlserver;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "detalle_token_autorizacion")
@Table(name = "BS_DetalleTokenAutorizacion", schema = "dbo", catalog = "BancaEmpresaPlus")
public class DetalleTokenAutorizacion {

    @Id
    @Getter @Setter
    @Column(name = "Codigo")
    private Long codigo;

    @Getter @Setter
    @Column(name = "Token")
    private Long token;

    @Getter @Setter
    @Column(name = "Rol")
    private Integer rol;

    @Getter @Setter
    @Column(name = "Nivel")
    private Integer nivel;

    @Getter @Setter
    @Column(name = "Usuario")
    private String usuario;

    @Getter @Setter
    @Column(name = "Cliente")
    private Long cliente;

    @Getter @Setter
    @Column(name = "DefinicionAutorizacion")
    private Long DefinicionAutorizacion;

}
