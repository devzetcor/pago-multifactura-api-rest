package com.davivienda.sv.app.util;

import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import java.util.Optional;

public class MQCliente {

	private static final Logger LOGGER = LoggerFactory.getLogger(MQCliente.class);

	public static final int DEFAULT_JMS_PRIORITY = 4;
	public static final int DEFAULT_JMS_EXPIRY = 600;
	public static final int DEFAULT_JMS_TIMEOUT = 60000;

	private JmsTemplate jmsTemplate;
	
	public MQCliente (JmsTemplate jmsTemplate) {
		this.jmsTemplate=jmsTemplate;
	}
	
	public Optional<String> execute(String queueReq, String queueResp, String xml) {

		try {

			LOGGER.info("Enviando mensaje con queueReq: {}", queueReq);
			LOGGER.info("Enviando mensaje con queueResp: {}", queueResp);
			LOGGER.info("Enviando mensaje con message: {}", xml);

			Message sentMessage = jmsTemplate.execute(session -> {

				TextMessage message = session.createTextMessage(xml);
				message.setJMSPriority(DEFAULT_JMS_PRIORITY);
				message.setJMSExpiration(DEFAULT_JMS_EXPIRY);
				message.setJMSReplyTo(session.createQueue(queueResp));
				session.createProducer(session.createQueue(queueReq)).send(message);

				return message;
			});

			if (sentMessage == null) {
				LOGGER.error("No se pudo enviar mensaje a MQ");
				return null;
			}

			String messageId = sentMessage.getJMSMessageID();

			LOGGER.info("JMSMessageID generado por broker: {}", messageId);

			String selector = "JMSCorrelationID = '" + messageId + "'";
			LOGGER.info("Selector usado: {}", selector);

			Message responseMessage = jmsTemplate.receiveSelected(queueResp, selector);

			if (responseMessage == null) {
				LOGGER.error("Timeout esperando respuesta en {} con selector {}", queueResp, selector);
				return null;
			}

			LOGGER.info(" {}", queueResp);
			LOGGER.info("CorrelationID respuesta: {}", responseMessage.getJMSCorrelationID());

			if (responseMessage instanceof TextMessage textMessage) {

				String responseText = textMessage.getText();
				if (responseText != null)
					responseText = responseText.replaceAll("\n", "").replaceAll(">[ ]+<", "><");

				LOGGER.info("Texto completo:\n{}", responseText);

				return Optional.of(responseText);
			} else {
				LOGGER.info("Mensaje recibido no es tipo TextMessage");
				return Optional.empty();
			}

		} catch (Exception e) {
			LOGGER.error("Error en MQ", e);
			throw new RuntimeException(e);
		}
	}
	
	public void execute(String queueReq, String xml) {

		try {

			LOGGER.info("Enviando mensaje con queueReq: {}", queueReq);
			LOGGER.info("Enviando mensaje con message: {}", xml);

			Message sentMessage = jmsTemplate.execute(session -> {

				TextMessage message = session.createTextMessage(xml);
				message.setJMSPriority(DEFAULT_JMS_PRIORITY);
				message.setJMSExpiration(DEFAULT_JMS_EXPIRY);
				session.createProducer(session.createQueue(queueReq)).send(message);

				return message;
			});

			if (sentMessage == null) {
				LOGGER.error("No se pudo enviar mensaje a MQ");
			}

		
		} catch (Exception e) {
			LOGGER.error("Error en MQ", e);
			throw new RuntimeException(e);
		}
	}
}
