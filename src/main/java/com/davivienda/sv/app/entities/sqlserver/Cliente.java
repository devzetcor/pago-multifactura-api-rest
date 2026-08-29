package com.davivienda.sv.app.entities.sqlserver;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

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