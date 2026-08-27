package com.davivienda.sv.app.dto;

import com.davivienda.sv.app.util.ToXML;

/**
 *
 * @author Christian Guillén
 * @since 2 jul 2023
 * @version 1.0
 * @param <T>
 * 
 */
public abstract class AbstractPeticionJ2Entorno <H, B> implements ToXML{

    protected H header;
    protected B body;
    
    public AbstractPeticionJ2Entorno() {
    	
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
		return "AbstractPeticionJ2Entorno [header=" + header + ", body=" + body + "]";
	}
	
}
