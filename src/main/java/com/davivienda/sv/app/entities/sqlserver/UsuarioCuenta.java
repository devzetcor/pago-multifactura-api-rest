package com.davivienda.sv.app.entities.sqlserver;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "BS_UsuarioCuenta", schema = "dbo", catalog = "BancaEmpresaPlus")
public class UsuarioCuenta implements Serializable {

    @Id
    @Getter @Setter
    @Column(name = "Usuario")
    private String usuario;
    
    @Getter @Setter
    @Column(name = "Permisos1")
    private String permisos1;
    
    @Getter @Setter
    @Column(name = "Permisos2")
    private String permisos2;
    
    @Getter @Setter
    @Column(name = "Permisos3")
    private String permisos3;
    
    @Getter @Setter
    @Column(name = "Permisos4")
    private String permisos4;
    
    @Getter @Setter
    @Column(name = "Permisos5")
    private String permisos5;
    
    @Getter @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "Cuenta", referencedColumnName = "Cuenta", insertable = false, updatable = false),
        @JoinColumn(name = "Cliente", referencedColumnName = "Cliente", insertable = false, updatable = false)
    })
    private Cuenta cuenta;

    public UsuarioCuenta() {
    }

    public UsuarioCuenta(String usuario, String permisos1, String permisos2, String permisos3, String permisos4,
            String permisos5, Cuenta cuenta) {
        this.usuario = usuario;
        this.permisos1 = permisos1;
        this.permisos2 = permisos2;
        this.permisos3 = permisos3;
        this.permisos4 = permisos4;
        this.permisos5 = permisos5;
        this.cuenta = cuenta;
    }

}