package com.davivienda.sv.app.entities.sqlserver;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

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