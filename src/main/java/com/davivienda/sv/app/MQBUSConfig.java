package com.davivienda.sv.app;

import com.davivienda.sv.app.util.MQCliente;
import com.davivienda.sv.app.util.R;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jndi.JndiObjectFactoryBean;

@Configuration
public class MQBUSConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQBUSConfig.class);

    @Value("${mq.bus.connection-factory}")
    private String busConnectionFactoryJndi;
    @Value("${mq.bus.queue-request-jndi}")
    private String busQueueRequestJndi;
    @Value("${mq.bus.queue-response-jndi}")
    private String busQueueResponseJndi;

    @Bean(name = R.MQCliente.BUS.JMS_TEMPLATE)
    public JmsTemplate jmsTemplate() throws Exception {
        LOGGER.info("Cargando BUS JNDI ConnectionFactory: {}", busConnectionFactoryJndi);
        JndiObjectFactoryBean jndi = new JndiObjectFactoryBean();
        jndi.setJndiName(busConnectionFactoryJndi);
        jndi.setResourceRef(false);
        jndi.afterPropertiesSet();

        ConnectionFactory cf = (ConnectionFactory) jndi.getObject();
        JmsTemplate jmsTemplate = new JmsTemplate(cf);
        jmsTemplate.setSessionTransacted(true);
        jmsTemplate.setReceiveTimeout(R.MQCliente.JMSPropertie.DEFAULT_JMS_TIMEOUT);
        jmsTemplate.setExplicitQosEnabled(true);
        jmsTemplate.setTimeToLive(R.MQCliente.JMSPropertie.DEFAULT_JMS_TIMEOUT);
        return jmsTemplate;
    }

    @Bean(name = R.MQCliente.BUS.QREQUEST)
    public Queue queueRequest() throws Exception {
        LOGGER.info("Cargando Cola de Petición BUS via JNDI: {}", busQueueRequestJndi);
        JndiObjectFactoryBean jndi = new JndiObjectFactoryBean();
        jndi.setJndiName(busQueueRequestJndi);
        jndi.setResourceRef(false);
        jndi.afterPropertiesSet();
        return (Queue) jndi.getObject();
    }

    @Bean(name = R.MQCliente.BUS.QRESPONSE)
    public Queue queueResponse() throws Exception {
        LOGGER.info("Cargando Cola de Respuesta BUS via JNDI: {}", busQueueResponseJndi);
        JndiObjectFactoryBean jndi = new JndiObjectFactoryBean();
        jndi.setJndiName(busQueueResponseJndi);
        jndi.setResourceRef(false);
        jndi.afterPropertiesSet();
        return (Queue) jndi.getObject();
    }

    @Bean(name = R.MQCliente.BUS.NAME)
    public MQCliente getMQClienteBUS(@Qualifier(R.MQCliente.BUS.JMS_TEMPLATE) JmsTemplate jmsTemplate) {
        return new MQCliente(jmsTemplate);
    }
}