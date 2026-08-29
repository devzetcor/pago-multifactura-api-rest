package com.davivienda.sv.app.services;

import com.davivienda.sv.app.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(R.MQCliente.BUS_1)
public class MQClienteBus extends MQClientService{
	@Autowired
	@Qualifier(R.MQCliente.JMSTemplate.BUS)
	private JmsTemplate busTemplate;

	@Override
	public Optional<String> execute(String colaPeticion, String colaRespuesta, String mensajePeticion) {
		// TODO Auto-generated method stub
		return super.execute(colaPeticion, colaRespuesta, mensajePeticion, busTemplate);
	}

	@Override
	public Optional<String> execute(String colaPeticion, String mensajePeticion) {
		// TODO Auto-generated method stub
		return super.execute(colaPeticion, mensajePeticion, busTemplate);
	}

}
