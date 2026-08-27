package com.davivienda.sv.app.entities.sqlserver;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "definicion_autorizacion")
@Table(name = "BS_DefinicionAutorizacion", schema = "dbo", catalog = "BancaEmpresaPlus")
public class DefinicionAutorizacion implements Serializable {

    @Id
    @Getter @Setter
    @Column(name = "IdPK")
    private Long idPK;

    @Getter @Setter
    @Column(name = "DefinicionAutorizacion")
    private Long definicionAutorizacion;

    @Getter @Setter
    @Column(name = "Producto")
    private Integer producto;

    @Getter @Setter
    @Column(name = "FechaEstado")
    private LocalDateTime fechaEstado;

    @Getter @Setter
    @Column(name = "Estado")
    private Integer estado;

    @ManyToOne
    @Getter @Setter
    @JoinColumn(name = "Cliente", referencedColumnName = "Cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "definicion", fetch = FetchType.EAGER)
    @Getter @Setter
    private Set<DetalleAutorizacion> detalles;
}