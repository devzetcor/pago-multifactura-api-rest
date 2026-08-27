package com.davivienda.sv.app.data.beans.mq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MQPFSClientData implements MQClientData {

	@Value("${com.davivienda.sv.app.mqpfs.queueManager}")
	private String queueManager;
	
	@Value("${com.davivienda.sv.app.mqpfs.channel}")
	private String channel;
	
	@Value("${com.davivienda.sv.app.mqpfs.host}")
	private String host;
	
	@Value("${com.davivienda.sv.app.mqpfs.port}")
	private int port;
	
	@Value("${com.davivienda.sv.app.mqpfs.user}")
	private String user;
	
	@Value("${com.davivienda.sv.app.mqpfs.password}")
	private String password;
	
	public String getQueueManager() {
		return queueManager;
	}
	public void setQueueManager(String queueManager) {
		this.queueManager = queueManager;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "MQPFSClientData [queueManager=" + queueManager + ", channel=" + channel + ", host=" + host + ", port="
				+ port + ", user=" + user + ", password=" + password + "]";
	}
}
