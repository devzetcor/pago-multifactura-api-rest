package com.davivienda.sv.app.entities.sqlserver.pks;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCliente implements Serializable {

    @Column(name = "Cliente")
    private Long clientePk;
    
    @Column(name = "Usuario")
    private String usuarioPk;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioCliente that = (UsuarioCliente) o;
        return Objects.equals(clientePk, that.clientePk) &&
               Objects.equals(usuarioPk, that.usuarioPk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientePk, usuarioPk);
    }
}
