package com.davivienda.sv.app.entities.sqlserver;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "detalle_autorizacion")
@Table(name = "BS_DetalleAutorizacion", schema = "dbo", catalog = "BancaEmpresaPlus")
public class DetalleAutorizacion {
    
    @Id
    @Getter @Setter
    @Column(name = "IdPK")
    private Long idPk;

    @Getter @Setter
    @Column(name = "Estado")
    private Integer estado;

    @ManyToOne
    @Getter @Setter
    @JoinColumn(name = "Rol", referencedColumnName = "Rol")
    private RolWC rol;

    @ManyToOne
    @Getter @Setter
    @JoinColumn(name = "DefinicionAutorizacion", referencedColumnName = "DefinicionAutorizacion")
    private DefinicionAutorizacion definicion;

    @Getter @Setter
    @Column(name = "Nivel")
    private Integer nivel;
    
}
