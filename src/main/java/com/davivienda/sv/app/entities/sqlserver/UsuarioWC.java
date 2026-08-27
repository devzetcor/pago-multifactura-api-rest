package com.davivienda.sv.app.entities.sqlserver;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "UsuarioWC")
@Table(name = "BS_UsuarioWC", schema = "dbo", catalog = "BancaEmpresaPlus")
public class UsuarioWC implements Serializable {

    @Id
    @Getter @Setter
    @Column(name = "Usuario")
    private String usuarioPk;

    @Getter @Setter
    @Column(name = "NumeroDocumento")
    private String numeroDocumento;

    @Getter @Setter
    @Column(name = "Nombre")
    private String nombre;

    @Getter @Setter
    @Column(name = "Apellido")
    private String apellido;
    
    @Getter @Setter
    @Column(name = "Puesto")
    private String puesto;
    
    @Getter @Setter
    @Column(name = "ModoAutenticacion")
    private Integer modoAutenticacion;
    
    @Getter @Setter
    @Column(name = "Email")
    private String email;

    @Getter @Setter
    @Column(name = "TipoDocumento")
    private Integer tipoDocumento;

    @Getter @Setter
    @Column(name = "EmailOTP")
    private Integer emailOTP;

    @Getter @Setter
    @Column(name = "Estatus")
    private Integer status;

    @ManyToOne
    @Getter @Setter
    @JoinColumn(name = "Cliente", referencedColumnName = "Cliente")
    private Cliente cliente;

    @Getter @Setter
    @OneToMany(mappedBy = "usuarioWC", fetch = FetchType.EAGER)
    @JsonManagedReference // O usa @JsonIgnore
    private Set<RolUsuario> rolesUsuarios = new HashSet<>();
}
