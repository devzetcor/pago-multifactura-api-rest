package com.davivienda.sv.app.entities.sqlserver;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "rol_usuario")
@Table(name = "BS_RolUsuario", schema = "dbo", catalog = "BancaEmpresaPlus")
public class RolUsuario {

    @Id
    @Getter @Setter
    @Column(name = "IdPK")
    private Long id;

    @Getter @Setter
    @Column(name = "Producto")
    private Integer producto;

    @Getter @Setter
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "Cliente", referencedColumnName = "Cliente"),
        @JoinColumn(name = "Usuario", referencedColumnName = "Usuario")
    })
    @JsonBackReference
    private UsuarioWC usuarioWC;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "Rol", referencedColumnName = "Rol")
    private RolWC rolWC;
}
