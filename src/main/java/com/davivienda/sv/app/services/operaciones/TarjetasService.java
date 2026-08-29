package com.davivienda.sv.app.services.operaciones;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.data.beans.token.CuentasRequest;
import com.davivienda.sv.app.dto.ListaTarjeta;
import com.davivienda.sv.app.dto.Tarjeta;
import com.davivienda.sv.app.util.MQCliente;
import com.davivienda.sv.app.util.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TarjetasService extends TransaccionService<CuentasRequest, ListaTarjeta> {

	@Autowired
	@Qualifier(R.MQCliente.PFS.NAME)
    MQCliente mqcService;
	private static final Logger LOGGER = LogManager.getLogger(TarjetasService.class);

	@Override
	public Optional<String> construirPeticion(Request<CuentasRequest> request) {
		String xmlPeticion = "<peticionEntorno><header><fabrica>" + R.Fabricas.ESBeBanca
				+ "</fabrica><servicio>LISTA_TARJETAS_CREDITO</servicio></header><body>" + "<contenedor>"
				+ "    <peticionListaTarjetasCredito>" + "  	      <usuarioFE>" + request.getBody().getUsuario() + "</usuarioFE>"
				+ "        <niuCliente>" + request.getBody().getNiu() + "</niuCliente>"
				+"  <correlativo></correlativo>"
				+ " </peticionListaTarjetasCredito>"
				+ "</contenedor></body></peticionEntorno>";

		return Optional.of(xmlPeticion);
	}

	@Override
	public Response<ListaTarjeta> evaluarRespuesta(Request<CuentasRequest> request, Document docResp) {
		Response<ListaTarjeta> resp = new Response<>(request, new ListaTarjeta());
		LOGGER.info("Obteniendo respuesta servicio CuentasService..." + docResp.asXML());

		long codResp = Long.parseLong(docResp.selectSingleNode("/respuestaEntorno/header/codigo").getText());
		String descResp = docResp.selectSingleNode("/respuestaEntorno/header/descripcion").getText();
		if (codResp != 0) {
			return resp;
		}
		List<Tarjeta> listaCatalogo = new ArrayList<>();

		// Cambio principal: Iterar sobre Node y castear a Element
		List<org.dom4j.Node> lista = docResp.selectNodes("//parametro");
		for (org.dom4j.Node node : lista) {
			Element e = (Element) node;
			listaCatalogo.add(new Tarjeta(
					e.selectSingleNode("TARJETA").getText(),
					e.selectSingleNode("ALIASTC").getText(),
					e.selectSingleNode("mesExpiracion").getText(),
					e.selectSingleNode("anioExpiracion").getText()
			));
		}

		resp.getBody().setTarjetas(listaCatalogo);
		return resp;
	}
	@Override
	public Response<ListaTarjeta> ejecutar(Request<CuentasRequest> request, String nombreServicio) throws Throwable {
		return super.ejecutar(request, nombreServicio, mqcService);
	}

}
