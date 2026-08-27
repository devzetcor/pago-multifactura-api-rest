package com.davivienda.sv.app.dto;

/**
 *
 * @author Christian Guillén
 * @since 2 jul 2023
 * @version 1.0
 * 
 */
public class HeaderRequest {

    protected String fabrica;
    protected String servicio;

    public HeaderRequest() {
    }

    public String getFabrica() {
        return fabrica;
    }

    public void setFabrica(String fabrica) {
        this.fabrica = fabrica;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }
    
    public void setServicio(Enum<?> servicio) {
        this.servicio = servicio.name();
    }

    @Override
    public String toString() {
        return "HeaderRequest{" + "fabrica=" + fabrica + ", servicio=" + servicio + '}';
    }

}
