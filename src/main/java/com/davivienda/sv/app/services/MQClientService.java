package com.davivienda.sv.app.services;

import jakarta.jms.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.SessionCallback;

import java.util.Optional;

// Import actualizado al paquete Jakarta de IBM MQ


public abstract class MQClientService {
	public static final int DEFAULT_JMS_PRIORITY = 4;
	public static final int DEFAULT_JMS_EXPIRY = 600;
	public static final int DEFAULT_JMS_TIMEOUT = 600;

	private static final Logger LOGGER = LogManager.getLogger(MQClientService.class);

	public abstract Optional<String> execute(String colaPeticion, String colaRespuesta, String mensajePeticion);
	public abstract Optional<String> execute(String colaPeticion, String mensajePeticion);

	public Optional<String> execute(String colaPeticion, String colaRespuesta, String mensajePeticion, JmsTemplate jmsTemplate) {
		LOGGER.info("Request queue: " + colaPeticion);
		LOGGER.info("Response queue: " + colaRespuesta);
		LOGGER.info("Expiration time: " + DEFAULT_JMS_EXPIRY);

		return jmsTemplate.execute(new SessionCallback<Optional<String>>() {

			@Override
			public Optional<String> doInJms(Session session) throws JMSException {
				try {
					// Extracción segura del Factory para Jakarta MQ
					logConnectionInfo(jmsTemplate);

					MessageProducer producer = session.createProducer(session.createQueue(colaPeticion));
					TextMessage msgReq = session.createTextMessage(mensajePeticion);
					msgReq.setJMSPriority(DEFAULT_JMS_PRIORITY);
					msgReq.setJMSExpiration(DEFAULT_JMS_EXPIRY);
					msgReq.setJMSReplyTo(session.createQueue(colaRespuesta));

					LOGGER.info("JMS PUT to queue " + colaPeticion + " message " + mensajePeticion);
					LOGGER.info("JMS PUT to queue msgReq " + msgReq.toString());
					producer.send(msgReq);

					String filtro = "JMSCorrelationID = '" + msgReq.getJMSMessageID() + "'";
					MessageConsumer consumer = session.createConsumer(session.createQueue(colaRespuesta), filtro);

					LOGGER.info("JMS GET message from queue " + colaRespuesta + "?" + filtro);
					Message msgResp = consumer.receive(DEFAULT_JMS_TIMEOUT * 1000);
					if (msgResp == null) {
						LOGGER.info("TIMEOUT MESSAGE...");
						return Optional.empty();
					}
					LOGGER.info("JMS GET message from queue VALUE " + msgResp + " " + msgResp.toString() + " " + msgResp.getClass());
					if (msgResp instanceof TextMessage) {
						String msgRespText = ((TextMessage) msgResp).getText();
						LOGGER.info("JMS message received: " + msgRespText);
						return Optional.of(msgRespText);
					} else {
						LOGGER.info("Mensaje recibido no es tipo TextMessage");
						return Optional.empty();
					}
				} catch (JMSException e) {
					LOGGER.error("JMSException: " + e.getMessage(), e);
					return Optional.empty();
				}
			}
		}, true);
	}

	public Optional<String> execute(String colaPeticion, String mensajePeticion, JmsTemplate jmsTemplate) {
		LOGGER.info("Request queue: " + colaPeticion);
		LOGGER.info("Expiration time: " + DEFAULT_JMS_EXPIRY);

		return jmsTemplate.execute(new SessionCallback<Optional<String>>() {

			@Override
			public Optional<String> doInJms(Session session) throws JMSException {
				try {
					logConnectionInfo(jmsTemplate);

					MessageProducer producer = session.createProducer(session.createQueue(colaPeticion));
					TextMessage msgReq = session.createTextMessage(mensajePeticion);
					msgReq.setJMSPriority(DEFAULT_JMS_PRIORITY);
					msgReq.setJMSExpiration(DEFAULT_JMS_EXPIRY);

					LOGGER.info("JMS PUT to queue " + colaPeticion + " message " + mensajePeticion);
					producer.send(msgReq);

					if (msgReq.getJMSMessageID() != null && !msgReq.getJMSMessageID().isEmpty()) {
						String filtro = "JMSCorrelationID = '" + msgReq.getJMSMessageID() + "'";
						LOGGER.info("JMS GET ID message from queue " + filtro);
					}
				} catch (JMSException e) {
					LOGGER.error("JMSException: " + e.getMessage(), e);
				}
				return Optional.empty();
			}
		}, true);
	}

	private void logConnectionInfo(JmsTemplate jmsTemplate) {
		try {
			jakarta.jms.ConnectionFactory targetFactory = jmsTemplate.getConnectionFactory();
			if (targetFactory != null) {
				LOGGER.info("JMS ConnectionFactory implementada por: {}", targetFactory.getClass().getName());
			}
		} catch (Exception e) {
			LOGGER.warn("No se pudo obtener información de la fábrica JMS: {}", e.getMessage());
		}
	}
}