package com.davivienda.sv.app.data.beans.mq;

public interface MQClientData {
	public String getQueueManager();
	public String getChannel();
	public String getHost();
	public int getPort();
	public String getUser();
	public String getPassword();
}
