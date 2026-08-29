package com.davivienda.sv.app.services.operaciones;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.data.beans.token.CuentasRequest;
import com.davivienda.sv.app.dto.Cuenta;
import com.davivienda.sv.app.dto.ListaCuenta;
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
public class CuentasService extends TransaccionService<CuentasRequest, ListaCuenta> {

	@Autowired
	@Qualifier(R.MQCliente.PFS.NAME)
    MQCliente mqcService;
	private static final Logger LOGGER = LogManager.getLogger(CuentasService.class);

	@Override
	public Optional<String> construirPeticion(Request<CuentasRequest> request) {
		String xmlPeticion = "<peticionEntorno><header><fabrica>" + R.Fabricas.ESBeBanca
				+ "</fabrica><servicio>LISTA_CUENTAS_CLIENTE</servicio></header><body>" + "<contenedor>"
				+ "    <peticionListaCuentas>" + "        <usuario>" + request.getBody().getUsuario() + "</usuario>"
				+ "        <niuCliente>" + request.getBody().getNiu() + "</niuCliente>"
				+ "        <listarCuentasPropias>S</listarCuentasPropias>"
				+ "        <listarCuentasTerceros>N</listarCuentasTerceros>" + "    </peticionListaCuentas>"
				+ "</contenedor></body></peticionEntorno>";

		return Optional.of(xmlPeticion);
	}

	@Override
	public Response<ListaCuenta> evaluarRespuesta(Request<CuentasRequest> request, Document docResp) {
		Response<ListaCuenta> resp = new Response<>(request, new ListaCuenta());
		LOGGER.info("Obteniendo respuesta servicio CuentasService..." + docResp.asXML());

		long codResp = Long.parseLong(docResp.selectSingleNode("/respuestaEntorno/header/codigo").getText());
		String descResp = docResp.selectSingleNode("/respuestaEntorno/header/descripcion").getText();
		if (codResp != 0) {
			return resp;
		}
		List<Cuenta> listaCatalogo = new ArrayList<>();

		// FIX 1: Retrieve as List<org.dom4j.Node>
		List<org.dom4j.Node> lista = docResp.selectNodes("//cuenta");

		// FIX 2: Cast to Element inside loop
		for (org.dom4j.Node node : lista) {
			Element e = (Element) node;

			// FIX 3: Fixed relative XPath "saldo/disponible" (removed leading //)
			listaCatalogo.add(new Cuenta(
					e.selectSingleNode("numero").getText(),
					e.selectSingleNode("tipo").getText(),
					e.selectSingleNode("alias").getText(),
					e.selectSingleNode("saldo/disponible").getText()
			));
		}
		resp.getBody().setCuentas(listaCatalogo);
		return resp;
	}

	@Override
	public Response<ListaCuenta> ejecutar(Request<CuentasRequest> request, String nombreServicio) throws Throwable {
		return super.ejecutar(request, nombreServicio, mqcService);
	}

}
