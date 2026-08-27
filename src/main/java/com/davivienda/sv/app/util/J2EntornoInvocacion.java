package com.davivienda.sv.app.util;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hsbc.desarrollo.interconexion.remoto.impl.http.ConexionEscuchadorHTTP;
import com.hsbc.sv.desarrollo.J2Entorno;
import com.hsbc.sv.desarrollo.contenedores.Peticion;
import com.hsbc.sv.desarrollo.contenedores.Respuesta;
import com.hsbc.sv.desarrollo.especificacion.ConfiguracionPerfil;
import com.hsbc.sv.desarrollo.especificacion.Usuario;
import com.hsbc.sv.desarrollo.interconexion.FabricaServicios;
import com.hsbc.sv.desarrollo.interconexion.Interconector;
import com.hsbc.sv.desarrollo.interconexion.estandar.ConexionEscuchador;
import com.hsbc.sv.desarrollo.interconexion.remoto.DireccionEscuchador;

public class J2EntornoInvocacion {

	private static final Logger LOGGER = LogManager.getLogger(J2EntornoInvocacion.class);

	public static final String APP_CLIENTE_DEFAULT = "clienteEntorno";
	public static final String CLASE_IMPL_CONEXION = "com.hsbc.desarrollo.interconexion.remoto.impl.http.ConexionEscuchadorHTTP";
	public static final String CLASE_IMPL_FABRICAS = "com.hsbc.desarrollo.interconexion.remoto.impl.http.FabricaFachadaHTTP";
	public static final String CLASE_IMPL_CONTEXTO = "N/A";
	//public static final String CONTEXTO_ENTORNO_DEFAULT = "EscuchadorHTTPx/EscuchadorHTTP"; 
	
	private ServidorEntorno servidorEntorno;
	private String contextoEntorno;

	public J2EntornoInvocacion(ServidorEntorno servidorEntorno, String contextoEntorno) {
		this.servidorEntorno = servidorEntorno;
		this.contextoEntorno = contextoEntorno;
		LOGGER.info("servidor: " + this.servidorEntorno.getIp() + " puerto " + this.servidorEntorno.getPuerto() + " -- contexto: " + this.contextoEntorno );
	}
	
	public Respuesta obtenerDatos(String fabrica, String servicio,Peticion peticion, Usuario usuario, ConfiguracionPerfil configuracion) {
		LOGGER.info(this.servidorEntorno);
		if(this.servidorEntorno != null && this.servidorEntorno.getContextoServlet().trim().length() == 0) {
			this.servidorEntorno.setContextoServlet(this.contextoEntorno);
		}
		
		if (!J2Entorno.e().isIniciado() || J2Entorno.e().obtenerFabrica(fabrica) == null) {
			this.arrancarJ2Entorno();
		}
		
		FabricaServicios f = J2Entorno.e().obtenerFabrica(fabrica);
		if (f != null) {
			return f.obtenerDatos(peticion, usuario, servicio, configuracion);
		} else {
			return new Respuesta(1111, "No se encontro la fabrica denominada " + fabrica);
		}
	}

	private void arrancarJ2Entorno() {
		LOGGER.info("Agregando conexion para el servidor J2Entorno " + this.servidorEntorno);
		Interconector.recargarEscuchador(APP_CLIENTE_DEFAULT, this.servidorEntorno.getIp(), String.valueOf(this.servidorEntorno.getPuerto()), 
				CLASE_IMPL_CONEXION, this.servidorEntorno.getContextoServlet(), CLASE_IMPL_FABRICAS, CLASE_IMPL_CONTEXTO);
		LOGGER.info("Escuchador recargado, fabricas: ");
		for(Object objFab: J2Entorno.e().fabricas()) {
			LOGGER.info("== Fabrica de implementacion " + ((FabricaServicios)objFab).getClass() + " = " + ((FabricaServicios)objFab).getIdentificador());
		}
		LOGGER.info("Se iniciara escuchador...");
		J2Entorno.e().setIniciado(true);
		LOGGER.info("Escuchador iniciado.");
	}
	
	public Respuesta obtenerDatos(String fabrica, String servicio, Peticion peticion) {
		LOGGER.info("obtener datos...");
		return obtenerDatos(fabrica, servicio, peticion, null, null);
	}
	
	public Peticion buildPeticion(Map<String, String> peticion) {
		Peticion p = new Peticion();
		for (Map.Entry<String, String> dato: peticion.entrySet()) {
			p.agregarParametro(dato.getKey(), dato.getValue());
		}
		
		return p;
	}
	
	public Peticion buildPeticion(String[][] peticion) {
		Peticion p = new Peticion();
		for (String[] dato: peticion) {
			if (dato != null && dato.length == 2) {
				p.agregarParametro(dato[0], dato[1]);
			}
		}
		
		return p;
	}
	
	public String obtenerDato(String fabrica, String servicio, Peticion p, String nombreDato) {
		String dato = null;
		Respuesta r = obtenerDatos(fabrica, servicio, p);
		if (esCorrectaConDatos(r)) {
			dato = r.obtenerString(nombreDato);
		}
		
		return dato;
	}
	
	public boolean esCorrecta(Respuesta r) {
		return (r != null && r.getCodigo() == 0);
	}
	
	public boolean esCorrectaConDatos(Respuesta r) {
		return (esCorrecta(r) && r.siguiente()); 
	}
	
	public void recargarFabrica(String nombreFabrica, String claseImplementacion, String ubicacionJar) throws NullPointerException {
		ConexionEscuchador conEs = new ConexionEscuchadorHTTP();
		
		DireccionEscuchador direccionNueva = new DireccionEscuchador();
        direccionNueva.setIdentificadorEscuchador(APP_CLIENTE_DEFAULT);
        direccionNueva.setServidor(this.servidorEntorno.getIp());
        direccionNueva.setPuerto(String.valueOf(this.servidorEntorno.getPuerto()));
        direccionNueva.setIdentificadorRecursoEscuchador(this.contextoEntorno);
        direccionNueva.setContextoJNDIInicial(CLASE_IMPL_CONTEXTO);
        direccionNueva.setClaseFabricasFachada(CLASE_IMPL_FABRICAS);
        
        conEs.setDireccionEscuchador(direccionNueva);
        direccionNueva.setConexionEscuchador(conEs);
		
		J2Entorno.e().reiniciarFabricaFachada(nombreFabrica, ubicacionJar, claseImplementacion, direccionNueva);
	}
}
