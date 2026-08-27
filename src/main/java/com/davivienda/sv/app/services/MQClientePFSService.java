package com.davivienda.sv.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.davivienda.sv.app.util.R;

@Component(R.MQCliente.PFS_1)
public class MQClientePFSService extends MQClientService {
	@Autowired
	@Qualifier(R.MQCliente.JMSTemplate.PFS)
	private JmsTemplate defaultTemplate;

	@Override
	public Optional<String> execute(String colaPeticion, String colaRespuesta, String mensajePeticion) {
		// TODO Auto-generated method stub
		return super.execute(colaPeticion, colaRespuesta, mensajePeticion, defaultTemplate);
	}

	@Override
	public Optional<String> execute(String colaPeticion, String mensajePeticion) {
		// TODO Auto-generated method stub
		return super.execute(colaPeticion, mensajePeticion, defaultTemplate);
	}

	

}
