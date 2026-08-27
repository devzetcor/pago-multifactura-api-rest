package com.davivienda.sv.app.dto;

import com.davivienda.sv.app.util.GetStatusResponse;
import com.davivienda.sv.app.util.ToXML;
import com.davivienda.sv.app.util.XMLDeserializerUtil;




/**
 *
 * @author Christian Guillen
 * @param <T>
 */
public abstract class AbstractRespuestaJ2Entorno<H,B> implements GetStatusResponse , ToXML{


	protected H header;
	protected B body;
	
	public static<R extends AbstractRespuestaJ2Entorno<?, ?>> R parseXML(String xml, Class<R> res, Class<?> type) throws Throwable{
		return XMLDeserializerUtil.parseXML(xml, res , type);
	}

	public AbstractRespuestaJ2Entorno() {
	}

	public H getHeader() {
		return header;
	}

	public void setHeader(H header) {
		this.header = header;
	}

	public B getBody() {
		return body;
	}

	public void setBody(B body) {
		this.body = body;
	}
	
	@Override
	public String toString() {
		return "RespuestaEntorno [header=" + header + ", body=" + body + "]";
	}

}
