package com.davivienda.sv.app.dto;

/**
 *
 * @author Christian Guillen
 * @since 4 jul 2023
 * @version 1.0
 */
public class HeaderResponse  {

    protected String codigo;
    protected String descripcion;

    public HeaderResponse() {
    }

    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "HeaderResponse[" + "codigo=" + codigo + ", descripcion=" + descripcion + ']';
    }

}
