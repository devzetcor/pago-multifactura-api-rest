package com.davivienda.sv.app.entities.sqlserver;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

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
