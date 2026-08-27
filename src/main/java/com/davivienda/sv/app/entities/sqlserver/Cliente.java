package com.davivienda.sv.app.entities.sqlserver;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "BSCliente")
@Table(name = "BS_Cliente", schema = "dbo", catalog = "BancaEmpresaPlus")
public class Cliente implements Serializable {

    @Id
    @Getter @Setter
    @Column(name = "Cliente")
    private Long cliente;

    @Getter @Setter
    @Column(name = "Nombre")
    private String nombre;

    @Getter @Setter
    @Column(name = "Modulos")
    private Integer modulos;
    
    @Getter @Setter
    @Column(name = "Direccion")
    private String direccion;

    @Getter @Setter
    @Column(name = "Telefono")
    private String telefono;

    @Getter @Setter
    @Column(name = "Fax")
    private String fax;

    @Getter @Setter
    @Column(name = "Email")
    private String email;

    @Getter @Setter
    @Column(name = "NombreContacto")
    private String nombreContacto;

    @Getter @Setter
    @Column(name = "ApellidoContacto")
    private String apellidoContacto;

    @Getter @Setter
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private Set<UsuarioWC> usuarios;

	@Override
	public String toString() {
		return "Cliente [cliente=" + cliente + ", nombre=" + nombre + "]";
	}

}