//package com.davivienda.sv.app.data.beans.mq;
//
//import com.ibm.msg.client.jakarta.wmq.WMQConstants;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jms.core.JmsTemplate;
//import com.ibm.mq.jakarta.jms.MQQueueConnectionFactory;
//
//
//@Configuration
//public class MQConfiguration {
//
//	@Autowired
//	private MQPFSClientData mqPFSData;
//
//	@Autowired
//	private MQBUSClientData mqBUSData;
//
//	private static final Logger LOGGER = LogManager.getLogger(MQBUSClientData.class);
//
//
//	@Bean
//	public MQQueueConnectionFactory jmsMQConnectionFactoryPFS() {
//	    MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
//
//	    try {
//	        mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
//	        mqQueueConnectionFactory.setClientReconnectOptions(WMQConstants.WMQ_CLIENT_RECONNECT_Q_MGR);
//	        mqQueueConnectionFactory.setQueueManager(mqPFSData.getQueueManager());
//	        mqQueueConnectionFactory.setHostName(mqPFSData.getHost());
//	        mqQueueConnectionFactory.setPort(mqPFSData.getPort());
//	        mqQueueConnectionFactory.setChannel(mqPFSData.getChannel());
//	    } catch (Exception e) {
//	    	LOGGER.error("Error configurando MQ para PFS: " + e.getMessage(), e);
//	    }
//	    return mqQueueConnectionFactory;
//	}
//
//	@Bean(name = "jmsTemplatePFS_1")
//    public JmsTemplate jmsTemplatePFS() {
//        JmsTemplate jmsTemplate = new JmsTemplate();
//        jmsTemplate.setConnectionFactory(jmsMQConnectionFactoryPFS());
//        return jmsTemplate;
//    }
//
//	@Bean
//	public MQQueueConnectionFactory jmsMQConnectionFactoryBUS() {
//	    MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
//
//	    try {
//	        mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
//	        mqQueueConnectionFactory.setClientReconnectOptions(WMQConstants.WMQ_CLIENT_RECONNECT_Q_MGR);
//	        mqQueueConnectionFactory.setQueueManager(mqBUSData.getQueueManager());
//	        mqQueueConnectionFactory.setHostName(mqBUSData.getHost());
//	        mqQueueConnectionFactory.setPort(mqBUSData.getPort());
//	        mqQueueConnectionFactory.setChannel(mqBUSData.getChannel());
//	    } catch (Exception e) {
//	    	LOGGER.error("Error configurando MQ para BUS: " + e.getMessage(), e);
//	    }
//	    return mqQueueConnectionFactory;
//	}
//
//	@Bean(name = "jmsTemplateBUS_1")
//    public JmsTemplate jmsTemplateBUS() {
//        JmsTemplate jmsTemplate = new JmsTemplate();
//        jmsTemplate.setConnectionFactory(jmsMQConnectionFactoryBUS());
//        return jmsTemplate;
//    }
//}
