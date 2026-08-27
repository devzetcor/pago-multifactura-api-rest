package com.davivienda.sv.app.dto.colecturia.pagar;

import java.util.List;
import com.davivienda.sv.app.util.SubContenedor;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.ToString;


@SubContenedor
@ToString
public class PeticionPagoFactura {

	private String idPagoServicio;
    private String identificadorCanal;
    private String idColector;
    private String nombre;
    private String codCanal;
    @JacksonXmlProperty(localName = "NPE")
    private String npe;
    @JacksonXmlProperty(localName = "Barra")
    private String barra;
    private String cuentaAbono;
    private String tipoCuentaAbono;
    private String cuentaCargo;
    private String tipoCuentaCargo;
    private double montoTotal;
    private double montoParcial;
    private double valorMora;
    private String usuario;
    private String lote;
    private String codigotranIbs;
    private String online;
    @JacksonXmlProperty(localName = "Reversa")
    private String reversa;
    @JacksonXmlProperty(localName = "SQN")
    private String sqn;
    @JacksonXmlProperty(localName = "secuenciaATH")
    private String secuenciaATH;
    
    @JacksonXmlElementWrapper(localName = "atributos")
    @JacksonXmlProperty(localName = "atributoColector")
    private List<com.davivienda.sv.app.dto.colecturia.detalle.AtributoColectorFull> atributos;

    // Getters y Setters
    public String getIdentificadorCanal() {
        return identificadorCanal;
    }

    public String getIdPagoServicio() {
		return idPagoServicio;
	}

	public void setIdPagoServicio(String idPagoServicio) {
		this.idPagoServicio = idPagoServicio;
	}

	public void setIdentificadorCanal(String identificadorCanal) {
        this.identificadorCanal = identificadorCanal;
    }

    public String getIdColector() {
        return idColector;
    }

    public void setIdColector(String idColector) {
        this.idColector = idColector;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodCanal() {
        return codCanal;
    }

    public void setCodCanal(String codCanal) {
        this.codCanal = codCanal;
    }

    public String getNpe() {
        return npe;
    }

    public void setNpe(String NPE) {
        this.npe = NPE;
    }

    public String getBarra() {
        return barra;
    }

    public void setBarra(String barra) {
        this.barra = barra;
    }

    public String getCuentaAbono() {
        return cuentaAbono;
    }

    public void setCuentaAbono(String cuentaAbono) {
        this.cuentaAbono = cuentaAbono;
    }

    public String getTipoCuentaAbono() {
        return tipoCuentaAbono;
    }

    public void setTipoCuentaAbono(String tipoCuentaAbono) {
        this.tipoCuentaAbono = tipoCuentaAbono;
    }

    public String getCuentaCargo() {
        return cuentaCargo;
    }

    public void setCuentaCargo(String cuentaCargo) {
        this.cuentaCargo = cuentaCargo;
    }

    public String getTipoCuentaCargo() {
        return tipoCuentaCargo;
    }

    public void setTipoCuentaCargo(String tipoCuentaCargo) {
        this.tipoCuentaCargo = tipoCuentaCargo;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public double getMontoParcial() {
        return montoParcial;
    }

    public void setMontoParcial(double montoParcial) {
        this.montoParcial = montoParcial;
    }

    public double getValorMora() {
        return valorMora;
    }

    public void setValorMora(double valorMora) {
        this.valorMora = valorMora;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getCodigotranIbs() {
        return codigotranIbs;
    }

    public void setCodigotranIbs(String codigotranIbs) {
        this.codigotranIbs = codigotranIbs;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

   

    public String getSecuenciaATH() {
        return secuenciaATH;
    }

    public void setSecuenciaATH(String secuenciaATH) {
        secuenciaATH = secuenciaATH;
    }

	public List<com.davivienda.sv.app.dto.colecturia.detalle.AtributoColectorFull> getAtributos() {
		return atributos;
	}

	public void setAtributos(List<com.davivienda.sv.app.dto.colecturia.detalle.AtributoColectorFull> atributos) {
		this.atributos = atributos;
	}

	public String getReversa() {
		return reversa;
	}

	public void setReversa(String reversa) {
		this.reversa = reversa;
	}

	public String getSqn() {
		return sqn;
	}

	public void setSqn(String sqn) {
		this.sqn = sqn;
	}
  
}
