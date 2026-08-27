package com.davivienda.sv.app.data.beans;

public class Response<T> {
	private ResponseHeader header;
	private T body;

	public Response() {
		this.header = new ResponseHeader();
		this.body = null;
	}
	
	public Response(Request<?> req) {
		this.setHeader(new ResponseHeader(req.getHeader()));
	}
	
	public Response(Request<?> req, int codigo) {
		this.setHeader(new ResponseHeader(req.getHeader(), codigo));
	}
	public Response(Request<?> req, int codigo,String desripcion) {
		this.setHeader(new ResponseHeader(req.getHeader(), codigo,desripcion));
	}
	
	public Response(Request<?> req, T body) {
		this(req, body, 0);
	}
	
	public Response(Request<?> req, T body, int codigo) {
		this(req, codigo);
		this.setBody(body);
	}
	
	public Response(Request<?> req, T body, int codigo, Object... paramsError) {
		this(req, body, codigo);
		this.setHeader(new ResponseHeader(req.getHeader(), codigo, paramsError));
	}
	
	public Response(ResponseHeader header, T body) {
		this.header = header;
		this.body = body;
	}

	public ResponseHeader getHeader() {
		return header;
	}
	public void setHeader(ResponseHeader header) {
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
		return "Response [header=" + header + ", body=" + body + "]";
	}
}
