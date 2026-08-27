package com.davivienda.sv.app.entities.sqlserver;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "movimiento_efectivo")
@Table(name = "BS_MovimientoEfectivo", schema = "dbo", catalog = "BancaEmpresaPlus")
public class MovimientoEfectivo {
    
    @Id
    @Getter @Setter
    @Column(name = "Movimiento")
    private Long movimiento;
    
    @Getter @Setter
    @Column(name = "token")
    private Long token;

    @Getter @Setter
    @Column(name = "Usuario")
    private String usuario;

    @Getter @Setter
    @Column(name = "Cliente")
    private Long cliente;
    
}
