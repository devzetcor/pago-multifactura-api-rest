package com.davivienda.sv.app.services.operaciones;


import com.davivienda.sv.app.data.beans.WRINTAS;
import com.davivienda.sv.app.util.MQCliente;
import com.davivienda.sv.app.util.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;

@Service
public class RegistraWRINTASService {

	@Autowired
	@Qualifier(R.MQCliente.PFS.NAME)
    MQCliente mqcService;
	private static final Logger LOGGER = LogManager.getLogger(RegistraWRINTASService.class);
    private SimpleDateFormat FORMATO_FECHA_CANAL =  new SimpleDateFormat("dd-MM-yyyy");
	public void loginExitoso(String usuario, long niu, String ip, String imei, String so, String idTransaccion, String idSesion, int codigo) {
		try {
			WRINTAS wrintas = new WRINTAS();
			wrintas.setUsuario(usuario);
			wrintas.setNiu(niu);
			wrintas.setCodAccion("INE");
			wrintas.setActividad("0E");
			wrintas.setIp(ip);
			wrintas.setDireccionMAC(imei);
			wrintas.setSistemaOperativo(so);
			wrintas.setIdTransaccion(idTransaccion);
			wrintas.setIdSesion(idSesion);
			wrintas.setCodigoRespuesta(codigo);
			
			
			escribirWrintas(wrintas);
			
		} catch (Throwable e) {
			LOGGER.error("ERROR en escritura WRINTAS:"+e.getMessage(),e);
		}
	}
	
	public void loginFallidoPassword(String usuario, long niu, String ip, String imei, String so, String idTransaccion, String idSesion, int codigo) {
		try {
			WRINTAS wrintas = new WRINTAS();
			wrintas.setUsuario(usuario);
			wrintas.setNiu(niu);
			wrintas.setCodAccion("IPW");
			wrintas.setActividad("0F");
			wrintas.setIp(ip);
			wrintas.setDireccionMAC(imei);
			wrintas.setSistemaOperativo(so);
			wrintas.setIdTransaccion(idTransaccion);
			wrintas.setIdSesion(idSesion);
			wrintas.setCodigoRespuesta(codigo);

			escribirWrintas(wrintas);
			
		} catch (Throwable e) {
			LOGGER.error("ERROR en escritura WRINTAS LOGIN FALLIDO:"+e.getMessage(),e);
		}
	}
	public void loginInactividad(String usuario, long niu, String ip, String imei, String so, String idTransaccion, String idSesion, int codigo) {
		try {
			WRINTAS wrintas = new WRINTAS();
			wrintas.setUsuario(usuario);
			wrintas.setNiu(niu);
			wrintas.setCodAccion("I90");
			wrintas.setActividad("0F");
			wrintas.setIp(ip);
			wrintas.setDireccionMAC(imei);
			wrintas.setSistemaOperativo(so);
			wrintas.setIdTransaccion(idTransaccion);
			wrintas.setIdSesion(idSesion);
			wrintas.setCodigoRespuesta(codigo);

			escribirWrintas(wrintas);
			
		} catch (Throwable e) {
			LOGGER.error( "ERROR en escritura WRINTAS LOGIN INACTIVIDAD:"+e.getMessage(),e);
			
		}
		
	}
	
	public void loginInvalidUser(String usuario, long niu, String ip, String imei, String so, String idTransaccion, String idSesion, int codigo) {
		try {
			WRINTAS wrintas = new WRINTAS();
			wrintas.setUsuario(usuario);
			wrintas.setNiu(niu);
			wrintas.setCodAccion("IUS");
			wrintas.setActividad("0F");
			wrintas.setIp(ip);
			wrintas.setDireccionMAC(imei);
			wrintas.setSistemaOperativo(so);
			wrintas.setIdTransaccion(idTransaccion);
			wrintas.setIdSesion(idSesion);
			wrintas.setCodigoRespuesta(codigo);

			escribirWrintas(wrintas);
			
		} catch (Throwable e) {
			LOGGER.error( "ERROR en escritura WRINTAS LOGIN USUARIO INVALIDO:"+e.getMessage(),e);
			
		}
		
	}
	public void loginSuspendido(String usuario, long niu, String ip, String imei, String so, String idTransaccion, String idSesion, int codigo) {
		try {
			WRINTAS wrintas = new WRINTAS();
			wrintas.setUsuario(usuario);
			wrintas.setNiu(niu);
			wrintas.setCodAccion("IBK");
			wrintas.setActividad("0F");
			wrintas.setIp(ip);
			wrintas.setDireccionMAC(imei);
			wrintas.setSistemaOperativo(so);
			wrintas.setIdTransaccion(idTransaccion);
			wrintas.setIdSesion(idSesion);
			wrintas.setCodigoRespuesta(codigo);

			escribirWrintas(wrintas);
			
		} catch (Throwable e) {
			LOGGER.error("ERROR en escritura WRINTAS LOGIN BLOCKED USER:"+e.getMessage(),e);
			
		}
		
	}
	public void loginChangePassword(String usuario, long niu, String ip, String imei, String so, String idTransaccion, String idSesion, int codigo) {
		try {
			WRINTAS wrintas = new WRINTAS();
			wrintas.setUsuario(usuario);
			wrintas.setNiu(niu);
			wrintas.setCodAccion("ICP");
			wrintas.setActividad("0F");
			wrintas.setIp(ip);
			wrintas.setDireccionMAC(imei);
			wrintas.setSistemaOperativo(so);
			wrintas.setIdTransaccion(idTransaccion);
			wrintas.setIdSesion(idSesion);
			wrintas.setCodigoRespuesta(codigo);

			escribirWrintas(wrintas);
			
		} catch (Throwable e) {
			LOGGER.error("ERROR en escritura WRINTAS LOGIN CHANGE PASSWORD:"+e.getMessage(),e);
			
		}
		
	}
	public void logout(String usuario, String ip, String idTransaccion, String idSesion, int codigo) {
		try {
			WRINTAS wrintas = new WRINTAS();
			wrintas.setUsuario(usuario);
			wrintas.setCodAccion("OUT");
			wrintas.setActividad("0S");
			wrintas.setIp(ip);
			wrintas.setIdTransaccion(idTransaccion);
			wrintas.setIdSesion(idSesion);
			wrintas.setCodigoRespuesta(codigo);

			escribirWrintas(wrintas);
			
		} catch (Throwable e) {
			LOGGER.error("ERROR en escritura WRINTAS LOG OUT:"+e.getMessage(),e);
			
		}
		
	}
	public void recuperarClave(String usuario, String ip, String imei, String so, String idTransaccion, String idSesion, int codigo) {
		try {
			WRINTAS wrintas = new WRINTAS();
			wrintas.setUsuario(usuario);
			wrintas.setCodAccion("ICP");
			wrintas.setActividad("AC");
			wrintas.setIp(ip);
			wrintas.setDireccionMAC(imei);
			wrintas.setSistemaOperativo(so);
			wrintas.setIdTransaccion(idTransaccion);
			wrintas.setIdSesion(idSesion);
			wrintas.setCodigoRespuesta(codigo);

			escribirWrintas(wrintas);
			
		} catch (Throwable e) {
			LOGGER.error("ERROR en escritura WRINTAS recuperarClave:"+e.getMessage(),e);
			
		}
		
	}
	public void cambiarClave(String usuario, String ip, String idTransaccion, String idSesion, int codigo) {
		try {
			WRINTAS wrintas = new WRINTAS();
			wrintas.setUsuario(usuario);
			wrintas.setCodAccion("CPW");
			wrintas.setActividad("51");
			wrintas.setIp(ip);
			wrintas.setIdTransaccion(idTransaccion);
			wrintas.setIdSesion(idSesion);
			wrintas.setCodigoRespuesta(codigo);

			escribirWrintas(wrintas);
			
		} catch (Throwable e) {
			LOGGER.error("ERROR en escritura WRINTAS cambiarClave:"+e.getMessage(),e);
			
		}
		
	}
	public void generarOTP(String usuario,long niu, String ip, String idTransaccion, String idSesion, int codigo) {
		try {
			WRINTAS wrintas = new WRINTAS();
			wrintas.setUsuario(usuario);
			wrintas.setNiu(niu);
			wrintas.setCodAccion("OTP");
			wrintas.setActividad("74");
			wrintas.setIp(ip);
			wrintas.setIdTransaccion(idTransaccion);
			wrintas.setIdSesion(idSesion);
			wrintas.setCodigoRespuesta(codigo);

			escribirWrintas(wrintas);
			
		} catch (Throwable e) {
			LOGGER.error("ERROR en escritura WRINTAS generar OTP:"+e.getMessage(),e);
			
		}
		
	}
	public void validarOTP(String usuario,long niu, String ip, String idTransaccion, String idSesion, int codigo) {
		try {
			WRINTAS wrintas = new WRINTAS();
			wrintas.setUsuario(usuario);
			wrintas.setNiu(niu);
			wrintas.setCodAccion("VTK");
			wrintas.setActividad("74");
			wrintas.setIp(ip);
			wrintas.setIdTransaccion(idTransaccion);
			wrintas.setIdSesion(idSesion);
			wrintas.setCodigoRespuesta(codigo);

			escribirWrintas(wrintas);
			
		} catch (Throwable e) {
			LOGGER.error("ERROR en escritura WRINTAS validar OTP:"+e.getMessage(),e);
			
		}
		
	}
	public void realizarComprasQR(String usuario, String monto, long niu, String ip, String idTransaccion,
				   					 String idSesion,String cuentaOrigen, String cuentaCredito, int codigo) {
		try {
			LOGGER.info("Obteniendo monto de compra QR sin parseo... "+monto);
			BigDecimal montoParseado = BigDecimal.valueOf(Long.parseLong(monto)).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN);
			LOGGER.info("Monto parseado... "+montoParseado);
			WRINTAS wrintas = new WRINTAS();
			wrintas.setUsuario(usuario);
			wrintas.setNiu(niu);
			wrintas.setCuentaOrigen(cuentaOrigen);
			wrintas.setCodAccion("CQR");
			wrintas.setActividad("78");
			wrintas.setMonto(montoParseado);
			wrintas.setIp(ip);
			wrintas.setIdTransaccion(idTransaccion);
			wrintas.setIdSesion(idSesion);
			wrintas.setCodigoRespuesta(codigo);
			wrintas.setCuentaDestino(cuentaCredito);

			escribirWrintas(wrintas);
			
		} catch (Throwable e) {
			LOGGER.error("ERROR en escritura WRINTAS realizarComprasQR:"+e.getMessage(),e);
			
		}
		
	}
	public void consultarCuentas(String usuario, long niu, String ip, String idTransaccion,String idSesion, int codigo) {
		try {
		WRINTAS wrintas = new WRINTAS();
		wrintas.setUsuario(usuario);
		wrintas.setNiu(niu);
		wrintas.setCodAccion("CCT");
		wrintas.setActividad("01");
		wrintas.setIp(ip);
		wrintas.setIdTransaccion(idTransaccion);
		wrintas.setIdSesion(idSesion);
		wrintas.setCodigoRespuesta(codigo);
		
		escribirWrintas(wrintas);
		
		} catch (Throwable e) {
			LOGGER.error("ERROR en escritura WRINTAS consultarCuentas:"+e.getMessage(),e);
			
		}
		
	}
	public void consultarTarjetas(String usuario, long niu, String ip, String idTransaccion,String idSesion, int codigo) {
		try {
		WRINTAS wrintas = new WRINTAS();
		wrintas.setUsuario(usuario);
		wrintas.setNiu(niu);
		wrintas.setCodAccion("CTC");
		wrintas.setActividad("01");
		wrintas.setIp(ip);
		wrintas.setIdTransaccion(idTransaccion);
		wrintas.setIdSesion(idSesion);
		wrintas.setCodigoRespuesta(codigo);
		
		escribirWrintas(wrintas);
		
		} catch (Throwable e) {
			LOGGER.error("ERROR en escritura WRINTAS consultarTarjetas:"+e.getMessage(),e);
			
		}
		
	}
	public void consultarMovimientosCuentas(String usuario, String cuentaOrigen, String fechaIni, String fechaFin,
											   String ip, String idTransaccion,String idSesion, int codigo) {
		String fechas="0";
		try {
			if(!fechaIni.isEmpty() && !fechaFin.isEmpty()) {
				String fechaIniCore = new SimpleDateFormat("yyyyMMdd").format(this.FORMATO_FECHA_CANAL.parse(fechaIni));
				String fechaFinCore = new SimpleDateFormat("yyyyMMdd").format(this.FORMATO_FECHA_CANAL.parse(fechaFin));
				fechas = fechaIniCore+fechaFinCore;
			}else if(!fechaIni.isEmpty()) {
				String fechaIniCore = new SimpleDateFormat("yyyyMMdd").format(this.FORMATO_FECHA_CANAL.parse(fechaIni));
				fechas = fechaIniCore;
			}else if(!fechaFin.isEmpty()) {
				String fechaFinCore = new SimpleDateFormat("yyyyMMdd").format(this.FORMATO_FECHA_CANAL.parse(fechaFin));
				fechas = fechaFinCore;
			}
			
			WRINTAS wrintas = new WRINTAS();
			wrintas.setUsuario(usuario);
			wrintas.setCodAccion("HII");
			wrintas.setActividad("01");
			wrintas.setCuentaOrigen(cuentaOrigen);
			wrintas.setIp(ip);
			wrintas.setIdTransaccion(idTransaccion);
			wrintas.setIdSesion(idSesion);
			wrintas.setCodigoRespuesta(codigo);
			wrintas.setCuentaDestino(fechas);
			
			escribirWrintas(wrintas);
		
		} catch (Throwable e) {
			LOGGER.error("ERROR en escritura WRINTAS consultarMovimientosCuentas:"+e.getMessage(),e);
			
		}
		
	}
	public void consultarMovimientosTC(String usuario, String numTarjeta, String fechaIni, String fechaFin,
			   String ip, String idTransaccion,String idSesion, int codigo) {
		String fechas="0";
		try {
				if(!fechaIni.isEmpty() && !fechaFin.isEmpty()) {
				String fechaIniCore = new SimpleDateFormat("yyyyMMdd").format(this.FORMATO_FECHA_CANAL.parse(fechaIni));
				String fechaFinCore = new SimpleDateFormat("yyyyMMdd").format(this.FORMATO_FECHA_CANAL.parse(fechaFin));
				fechas = fechaIniCore+fechaFinCore;
				}else if(!fechaIni.isEmpty()) {
				String fechaIniCore = new SimpleDateFormat("yyyyMMdd").format(this.FORMATO_FECHA_CANAL.parse(fechaIni));
				fechas = fechaIniCore;
				}else if(!fechaFin.isEmpty()) {
				String fechaFinCore = new SimpleDateFormat("yyyyMMdd").format(this.FORMATO_FECHA_CANAL.parse(fechaFin));
				fechas = fechaFinCore;
				}
				
				WRINTAS wrintas = new WRINTAS();
				wrintas.setUsuario(usuario);
				wrintas.setCodAccion("UTT");
				wrintas.setActividad("01");
				wrintas.setCuentaOrigen(numTarjeta);
				wrintas.setIp(ip);
				wrintas.setIdTransaccion(idTransaccion);
				wrintas.setIdSesion(idSesion);
				wrintas.setCodigoRespuesta(codigo);
				wrintas.setCuentaDestino(fechas);
				
				escribirWrintas(wrintas);
		
		} catch (Throwable e) {
				LOGGER.error("ERROR en escritura WRINTAS consultarMovimientosTC:"+e.getMessage(),e);
				
		}
		
	}
	public void consultarEstadoCuentas(String usuario, long niu, String cuentaOrigen, String fecha,
			   String ip, String idTransaccion,String idSesion, int codigo) {
		try {
				
				
				WRINTAS wrintas = new WRINTAS();
				wrintas.setUsuario(usuario);
				wrintas.setCodAccion("CEC");
				wrintas.setActividad("01");
				wrintas.setNiu(niu);
				wrintas.setCuentaOrigen(cuentaOrigen);
				wrintas.setIp(ip);
				wrintas.setIdTransaccion(idTransaccion);
				wrintas.setIdSesion(idSesion);
				wrintas.setCodigoRespuesta(codigo);
				wrintas.setCuentaDestino(fecha);
				
				escribirWrintas(wrintas);
		
		} catch (Throwable e) {
				LOGGER.error("ERROR en escritura WRINTAS consultarEstadoCuentas:"+e.getMessage(),e);
				
		}
		
	}
	public void consultarEstadoTC(String usuario, long niu, String numTarjeta, String fecha,
			   String ip, String idTransaccion,String idSesion, int codigo) {
		try {
				
				
				WRINTAS wrintas = new WRINTAS();
				wrintas.setUsuario(usuario);
				wrintas.setCodAccion("IET");
				wrintas.setActividad("01");
				wrintas.setNiu(niu);
				wrintas.setCuentaOrigen(numTarjeta);
				wrintas.setIp(ip);
				wrintas.setIdTransaccion(idTransaccion);
				wrintas.setIdSesion(idSesion);
				wrintas.setCodigoRespuesta(codigo);
				wrintas.setCuentaDestino(fecha);
				
				escribirWrintas(wrintas);
		
		} catch (Throwable e) {
				LOGGER.error("ERROR en escritura WRINTAS consultarEstadoTC:"+e.getMessage(),e);
				
		}
		
	}
	/**
	 * Metodo que escribe en WRINTAS para las consultas prestamos y credinegocio
	 * @param usuario
	 * @param niu
	 * @param ip
	 * @param idTransaccion
	 * @param idSesion
	 * @param codigo
	 * @return
	 */
	public void consultarPrestamosLC(String usuario, long niu, String ip, String idTransaccion,String idSesion, int codigo) {
		try {
				
				
				WRINTAS wrintas = new WRINTAS();
				wrintas.setUsuario(usuario);
				wrintas.setCodAccion("CGD");
				wrintas.setActividad("01");
				wrintas.setNiu(niu);
				wrintas.setIp(ip);
				wrintas.setIdTransaccion(idTransaccion);
				wrintas.setIdSesion(idSesion);
				wrintas.setCodigoRespuesta(codigo);
				
				escribirWrintas(wrintas);
		
		} catch (Throwable e) {
				LOGGER.error("ERROR en escritura del servicio WRINTAS consultarPrestamosLC:"+e.getMessage(),e);
				
		}
		
	}
	public void consultarMovimientosCredi(String usuario, String cuentaOrigen, String ip, String idTransaccion,String idSesion, int codigo) {
		try {
						
				WRINTAS wrintas = new WRINTAS();
				wrintas.setUsuario(usuario);
				wrintas.setCodAccion("HPD");
				wrintas.setActividad("01");
				wrintas.setCuentaOrigen(cuentaOrigen);
				wrintas.setIp(ip);
				wrintas.setIdTransaccion(idTransaccion);
				wrintas.setIdSesion(idSesion);
				wrintas.setCodigoRespuesta(codigo);
				
				escribirWrintas(wrintas);
		
		} catch (Throwable e) {
				LOGGER.error("ERROR en escritura del servicio WRINTAS consultarMovimientosCredi:"+e.getMessage(),e);
				
		}
		
	}
	public void consultarPlanPagos(String usuario, String cuentaOrigen, String ip, String idTransaccion,String idSesion, int codigo) {
		try {
						
				WRINTAS wrintas = new WRINTAS();
				wrintas.setUsuario(usuario);
				wrintas.setCodAccion("CPD");
				wrintas.setActividad("01");
				wrintas.setCuentaOrigen(cuentaOrigen);
				wrintas.setIp(ip);
				wrintas.setIdTransaccion(idTransaccion);
				wrintas.setIdSesion(idSesion);
				wrintas.setCodigoRespuesta(codigo);
				
				escribirWrintas(wrintas);
		
		} catch (Throwable e) {
				LOGGER.error("ERROR en escritura WRINTAS consultarPlanPagos:"+e.getMessage(),e);
				
		}
		
	}
	public void comprarDavipuntos(String usuario, long niu, int cantidaDP, String numTarjeta, float monto,
									 String ip, String idTransaccion,String idSesion, int codigo) {
		try {
						
				WRINTAS wrintas = new WRINTAS();
				wrintas.setUsuario(usuario);
				wrintas.setCodAccion("CDP");
				wrintas.setActividad("33");
				wrintas.setCuentaOrigen(numTarjeta);
				wrintas.setMontoDavipuntos((long)(monto));
				wrintas.setNiu(niu);
				wrintas.setIp(ip);
				wrintas.setIdTransaccion(idTransaccion);
				wrintas.setIdSesion(idSesion);
				wrintas.setCodigoRespuesta(codigo);
				wrintas.setCuentaDestino(String.valueOf(cantidaDP));
				
				escribirWrintas(wrintas);
		
		} catch (Throwable e) {
				LOGGER.error("ERROR en escritura del servicio WRINTAS consultarMovimientosCredi:"+e.getMessage(),e);
				
		}
		
	}
	public void prestarDavipuntos(String usuario, long niu, int cantidaDP, String numTarjeta, 
			String ip, String idTransaccion,String idSesion, int codigo) {
		try {
			
				WRINTAS wrintas = new WRINTAS();
				wrintas.setUsuario(usuario);
				wrintas.setCodAccion("PDP");
				wrintas.setActividad("33");
				wrintas.setCuentaOrigen(numTarjeta);
				wrintas.setNiu(niu);
				wrintas.setIp(ip);
				wrintas.setIdTransaccion(idTransaccion);
				wrintas.setIdSesion(idSesion);
				wrintas.setCodigoRespuesta(codigo);
				wrintas.setCuentaDestino(String.valueOf(cantidaDP));
				
				escribirWrintas(wrintas);
		
		} catch (Throwable e) {
			LOGGER.error("ERROR en escritura del servicio WRINTAS consultarMovimientosCredi:"+e.getMessage(),e);
		}
	}

	public void escribirWrintas(WRINTAS wrintas) {
		String xmlPeticion = "<peticionEntorno><header><fabrica>"+R.Fabricas.ESBeBanca+"</fabrica><servicio>REGISTRO_WRINTAS</servicio></header>"
				+ "<body><contenedor>" + 
				"    <peticionEscribeWRIntas>" + 
				"        <usuario>"+wrintas.getUsuario()+"</usuario>" + 
				"        <niu>"+wrintas.getNiu()+"</niu>" + 
				"        <cuentaOrigen>"+wrintas.getCuentaOrigen()+"</cuentaOrigen>" + 
				"        <codActividad>"+wrintas.getCodAccion()+"</codActividad>" + 
				"        <codAccion>"+wrintas.getActividad()+"</codAccion>" + 
				"        <monto>"+(wrintas.getMontoDavipuntos()>0?wrintas.getMontoDavipuntos():wrintas.getMonto())+"</monto>" + 
				"        <ip>"+wrintas.getIp()+"</ip>" + 
				"        <direccionMAC>"+wrintas.getDireccionMAC()+"</direccionMAC>" + 
				"        <sistemaOperativo>"+wrintas.getSistemaOperativo()+"</sistemaOperativo>" + 
				"        <idTransaccion>"+wrintas.getIdTransaccion()+"</idTransaccion>" + 
				"        <idSession>"+wrintas.getIdSesion()+"</idSession>" +
				"        <codRespuesta>"+wrintas.getCodigoRespuesta()+"</codRespuesta>" + 
				"        <cuentaDestino>"+wrintas.getCuentaDestino()+"</cuentaDestino>" + 
				"    </peticionEscribeWRIntas>" + 
				"</contenedor></body></peticionEntorno>";
		this.LOGGER.info("XML peticion... " + xmlPeticion);
        this.LOGGER.info("Peticion a WRINTAS...");
        this.mqcService.execute("REGISTRO_WRINTAS.REQ", "REGISTRO_WRINTAS.RESP", xmlPeticion);
        this.LOGGER.info("Peticion a WRINTAS... OK");
        
	}

	public void realizarPagoDeServicio(
		String usuario,
		String cuentaOrigen,
		String cuentaDestino,
		BigDecimal monto,
		String ip,
		String idTransaccion,
		String idSesion,
		int codigo,
		String timestamp,
		Long niu
	){
		try {
				WRINTAS wrintas = new WRINTAS();
				wrintas.setUsuario(usuario);
				wrintas.setCuentaOrigen(cuentaOrigen);
				wrintas.setCuentaDestino(cuentaDestino);
				wrintas.setNiu(niu);
				wrintas.setCodAccion("32");
				wrintas.setActividad("CPSE");
				wrintas.setMonto(monto);
				wrintas.setIp(ip);
				wrintas.setIdTransaccion(idTransaccion);
				wrintas.setIdSesion(idSesion);
				wrintas.setCodigoRespuesta(codigo);
				escribirWrintas(wrintas);
		} catch (Throwable e) {
				LOGGER.error("ERROR en escritura del servicio WRINTAS realizarPagoDeServicio:"+e.getMessage(),e);
		}
	}
}