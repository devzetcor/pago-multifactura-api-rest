package com.davivienda.sv.app.entities.sqlserver;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "BS_Cuenta",schema = "dbo", catalog = "BancaEmpresaPlus")
public class Cuenta implements Serializable {
    
    @Id
    @Getter @Setter
    @Column(name = "Cuenta")
    private String cuenta;
    
    @Getter @Setter
    @Column(name = "Cliente")
    private Long cliente;
    
    @Getter @Setter
    @Column(name = "Nombre")
    private String nombre;
    
    @Getter @Setter
    @Column(name = "AliasCuenta")
    private String aliasCuenta;
    
    @Getter @Setter
    @Column(name = "FechaEstatus")
    private LocalDateTime fechaEstatus;
    
    @Getter @Setter
    @Column(name = "Estatus")
    private String estatus;
    
    @Getter @Setter
    @Column(name = "EstatusHost")
    private Integer estatusHost;
    
    @Getter @Setter
    @Column(name = "Moneda")
    private String moneda;
    
    @Getter @Setter
    @Column(name = "NIU")
    private String niu;
    
    @Getter @Setter
    @Column(name = "Limite")
    private BigDecimal limite;
    
    @Getter @Setter
    @Column(name = "CuotaOrdinaria")
    private BigDecimal cuotaOrdinaria;
    
    @Getter @Setter
    @Column(name = "FechaOtorgado")
    private LocalDateTime fechaOtorgado;
    
    @Getter @Setter
    @Column(name = "FechaVencimiento")
    private LocalDateTime fechaVencimiento;
    
    @Getter @Setter
    @Column(name = "EsInteligente")
    private Boolean esInteligente;
    
    @Getter @Setter
    @Column(name = "EsquemaCtaInteligente")
    private String esquemaCtaInteligente;
    
    @Getter @Setter
    @Column(name = "CuentaMadre")
    private String cuentaMadre;
    
    @Getter @Setter
    @Column(name = "FechaUltimaActualizacion")
    private LocalDateTime fechaUltimaActualizacion;
    
    @Getter @Setter
    @Column(name = "EsCrediExpress")
    private Boolean esCrediExpress;
   
    @Getter @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TipoCuenta", insertable = false, updatable = false)
    private TipoCuenta tipoCuentaEntity;
}