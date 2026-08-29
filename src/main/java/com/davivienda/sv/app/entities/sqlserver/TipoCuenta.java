package com.davivienda.sv.app.entities.sqlserver;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "BS_TipoCuenta", schema = "dbo", catalog = "BancaEmpresaPlus")
public class TipoCuenta implements Serializable {
    
    @Id
    @Getter @Setter
    @Column(name = "TipoCuenta")
    private Integer tipoCuenta;
    
    @Getter @Setter
    @Column(name = "Nombre")
    private String nombre;
    
    @Getter @Setter
    @Column(name = "NombreCorto")
    private String nombreCorto;
    
    @Getter @Setter
    @Column(name = "idAS400")
    private String idAS400;
    
    @Getter @Setter
    @Column(name = "idTipoOperacion")
    private String idTipoOperacion;
}
