package com.davivienda.sv.app.data.beans;

public class Request<T> {
	private RequestHeader header;
	private T body;
	public Request() {
	}
	public Request(Request<?> request) {
		this.header = request.getHeader();
	}
	public RequestHeader getHeader() {
		return header;
	}
	public void setHeader(RequestHeader header) {
		this.header = header;
	}
	public T getBody() {
		return body;
	}
	public void setBody(T body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "Request [header=" + header + ", body=" + body + "]";
	}
}
