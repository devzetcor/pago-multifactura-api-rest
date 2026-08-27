package com.davivienda.sv.app.dto.colecturia.detalle;

import java.util.List;
import com.davivienda.sv.app.util.SubContenedor;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.ToString;

@SubContenedor
@ToString
public class RespuestaInfoColector {

	@JacksonXmlProperty(localName = "idColector")
	private String idColector;

	@JacksonXmlProperty(localName = "nombre")
	private String nombre;

	@JacksonXmlProperty(localName = "poseecodigobarra")
	private String poseeCodigoBarra;

	@JacksonXmlProperty(localName = "idCategoriaColector")
	private String idCategoriaColector;

	@JacksonXmlProperty(localName = "nombreCategoria")
	private String nombreCategoria;

	@JacksonXmlProperty(localName = "codTipoCuenta")
	private String codTipoCuenta;

	@JacksonXmlProperty(localName = "DescripcionTipoCuenta")
	private String descripcionTipoCuenta;

	@JacksonXmlProperty(localName = "numeroCuentaAbono")
	private String numeroCuentaAbono;

	@JacksonXmlProperty(localName = "referenciaImagen")
	private String referenciaImagen;

	@JacksonXmlProperty(localName = "referenciaAudioIVR")
	private String referenciaAudioIVR;

	@JacksonXmlProperty(localName = "idTipologia")
	private String idTipologia;

	@JacksonXmlProperty(localName = "esCodigoDiesco")
	private String esCodigoDiesco;

	@JacksonXmlProperty(localName = "descripcionTipologia")
	private String descripcionTipologia;

	@JacksonXmlProperty(localName = "valDuplicidadPago")
	private String valDuplicidadPago;

	@JacksonXmlProperty(localName = "obtencionFecVencimiento")
	private String obtencionFecVencimiento;

	@JacksonXmlProperty(localName = "prefijoBarra")
	private String prefijoBarra;

	@JacksonXmlProperty(localName = "prefijoNPE")
	private String prefijoNPE;

	@JacksonXmlProperty(localName = "niu")
	private String niu;

	@JacksonXmlProperty(localName = "numeroIdIBS")
	private String numeroIdIBS;

	@JacksonXmlProperty(localName = "cuentaContable")
	private String cuentaContable;

	@JacksonXmlProperty(localName = "cuentaFinalColector")
	private String cuentaFinalColector;

	@JacksonXmlProperty(localName = "validacionesJerarquica")
	private ValidacionesJerarquica validacionesJerarquica;

	@JacksonXmlProperty(localName = "fechaVencimiento")
	private FechaVencimiento fechaVencimiento;

	@JacksonXmlProperty(localName = "calculoMora")
	private CalculoMora calculoMora;

	@JacksonXmlElementWrapper(localName = "atributos")
	@JacksonXmlProperty(localName = "atributoColector")
	private List<AtributoColectorFull> atributos;


	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "canalesHabilitados")
	private List<Canal> canalesHabilitados;

	public RespuestaInfoColector() {
		super();
	}


	public RespuestaInfoColector(String idColector, String nombre, String poseeCodigoBarra, String idCategoriaColector,
			String nombreCategoria, String codTipoCuenta, String descripcionTipoCuenta, String numeroCuentaAbono,
			String referenciaImagen, String referenciaAudioIVR, String idTipologia, String esCodigoDiesco,
			String descripcionTipologia, String valDuplicidadPago, String obtencionFecVencimiento, String prefijoBarra,
			String prefijoNPE, String niu, String numeroIdIBS, String cuentaContable, String cuentaFinalColector,
			ValidacionesJerarquica validacionesJerarquica, FechaVencimiento fechaVencimiento, CalculoMora calculoMora,
			List<AtributoColectorFull> atributos, List<Canal> canalesHabilitados) {
		super();
		this.idColector = idColector;
		this.nombre = nombre;
		this.poseeCodigoBarra = poseeCodigoBarra;
		this.idCategoriaColector = idCategoriaColector;
		this.nombreCategoria = nombreCategoria;
		this.codTipoCuenta = codTipoCuenta;
		this.descripcionTipoCuenta = descripcionTipoCuenta;
		this.numeroCuentaAbono = numeroCuentaAbono;
		this.referenciaImagen = referenciaImagen;
		this.referenciaAudioIVR = referenciaAudioIVR;
		this.idTipologia = idTipologia;
		this.esCodigoDiesco = esCodigoDiesco;
		this.descripcionTipologia = descripcionTipologia;
		this.valDuplicidadPago = valDuplicidadPago;
		this.obtencionFecVencimiento = obtencionFecVencimiento;
		this.prefijoBarra = prefijoBarra;
		this.prefijoNPE = prefijoNPE;
		this.niu = niu;
		this.numeroIdIBS = numeroIdIBS;
		this.cuentaContable = cuentaContable;
		this.cuentaFinalColector = cuentaFinalColector;
		this.validacionesJerarquica = validacionesJerarquica;
		this.fechaVencimiento = fechaVencimiento;
		this.calculoMora = calculoMora;
		this.atributos = atributos;
		this.canalesHabilitados = canalesHabilitados;
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

	public String getPoseeCodigoBarra() {
		return poseeCodigoBarra;
	}

	public void setPoseeCodigoBarra(String poseeCodigoBarra) {
		this.poseeCodigoBarra = poseeCodigoBarra;
	}

	public String getIdCategoriaColector() {
		return idCategoriaColector;
	}

	public void setIdCategoriaColector(String idCategoriaColector) {
		this.idCategoriaColector = idCategoriaColector;
	}

	public String getNombreCategoria() {
		return nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}

	public String getCodTipoCuenta() {
		return codTipoCuenta;
	}

	public void setCodTipoCuenta(String codTipoCuenta) {
		this.codTipoCuenta = codTipoCuenta;
	}

	public String getDescripcionTipoCuenta() {
		return descripcionTipoCuenta;
	}

	public void setDescripcionTipoCuenta(String descripcionTipoCuenta) {
		this.descripcionTipoCuenta = descripcionTipoCuenta;
	}

	public String getNumeroCuentaAbono() {
		return numeroCuentaAbono;
	}

	public void setNumeroCuentaAbono(String numeroCuentaAbono) {
		this.numeroCuentaAbono = numeroCuentaAbono;
	}

	public String getReferenciaImagen() {
		return referenciaImagen;
	}

	public void setReferenciaImagen(String referenciaImagen) {
		this.referenciaImagen = referenciaImagen;
	}

	public String getReferenciaAudioIVR() {
		return referenciaAudioIVR;
	}

	public void setReferenciaAudioIVR(String referenciaAudioIVR) {
		this.referenciaAudioIVR = referenciaAudioIVR;
	}

	public String getIdTipologia() {
		return idTipologia;
	}

	public void setIdTipologia(String idTipologia) {
		this.idTipologia = idTipologia;
	}

	public String getEsCodigoDiesco() {
		return esCodigoDiesco;
	}

	public void setEsCodigoDiesco(String esCodigoDiesco) {
		this.esCodigoDiesco = esCodigoDiesco;
	}

	public String getDescripcionTipologia() {
		return descripcionTipologia;
	}

	public void setDescripcionTipologia(String descripcionTipologia) {
		this.descripcionTipologia = descripcionTipologia;
	}

	public String getValDuplicidadPago() {
		return valDuplicidadPago;
	}

	public void setValDuplicidadPago(String valDuplicidadPago) {
		this.valDuplicidadPago = valDuplicidadPago;
	}

	public String getObtencionFecVencimiento() {
		return obtencionFecVencimiento;
	}

	public void setObtencionFecVencimiento(String obtencionFecVencimiento) {
		this.obtencionFecVencimiento = obtencionFecVencimiento;
	}

	public String getPrefijoBarra() {
		return prefijoBarra;
	}

	public void setPrefijoBarra(String prefijoBarra) {
		this.prefijoBarra = prefijoBarra;
	}

	public String getPrefijoNPE() {
		return prefijoNPE;
	}

	public void setPrefijoNPE(String prefijoNPE) {
		this.prefijoNPE = prefijoNPE;
	}

	public String getNiu() {
		return niu;
	}

	public void setNiu(String niu) {
		this.niu = niu;
	}

	public String getNumeroIdIBS() {
		return numeroIdIBS;
	}

	public void setNumeroIdIBS(String numeroIdIBS) {
		this.numeroIdIBS = numeroIdIBS;
	}

	public String getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	public String getCuentaFinalColector() {
		return cuentaFinalColector;
	}

	public void setCuentaFinalColector(String cuentaFinalColector) {
		this.cuentaFinalColector = cuentaFinalColector;
	}

	public ValidacionesJerarquica getValidacionesJerarquica() {
		return validacionesJerarquica;
	}

	public void setValidacionesJerarquica(ValidacionesJerarquica validacionesJerarquica) {
		this.validacionesJerarquica = validacionesJerarquica;
	}

	public FechaVencimiento getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(FechaVencimiento fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public CalculoMora getCalculoMora() {
		return calculoMora;
	}

	public void setCalculoMora(CalculoMora calculoMora) {
		this.calculoMora = calculoMora;
	}

	

	public List<AtributoColectorFull> getAtributos() {
		return atributos;
	}


	public void setAtributos(List<AtributoColectorFull> atributos) {
		this.atributos = atributos;
	}


	public List<Canal> getCanalesHabilitados() {
		return canalesHabilitados;
	}

	public void setCanalesHabilitados(List<Canal> canalesHabilitados) {
		this.canalesHabilitados = canalesHabilitados;
	}

}
