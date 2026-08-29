package com.davivienda.sv.app.entities.sqlserver;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "BS_RolWC")
@Table(name = "BS_RolWC", schema = "dbo", catalog = "BancaEmpresaPlus")
public class RolWC {
    
    @Id
    @Getter @Setter
    @Column(name = "Rol")
    private Integer rol;

    @Getter @Setter
    @Column(name = "Nombre")
    private String nombre;

    @Getter @Setter
    @OneToMany(mappedBy = "rolWC")
    private Set<RolUsuario> usuariosConEsteRol = new HashSet<>();
}
