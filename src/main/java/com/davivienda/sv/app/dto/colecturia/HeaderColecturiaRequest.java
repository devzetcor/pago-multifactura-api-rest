package com.davivienda.sv.app.dto.colecturia;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class HeaderColecturiaRequest {

	@JacksonXmlProperty(localName = "esb:esbHdr")
	private EsbHdr esbHdr = new EsbHdr();

	// Getters y Setters
	public EsbHdr getEsbHdr() {
		return esbHdr;
	}

	public void setEsbHdr(EsbHdr esbHdr) {
		this.esbHdr = esbHdr;
	}

	public static class EsbHdr {

		@JacksonXmlProperty(localName = "xmlns:esb", isAttribute = true)
		private final String namespace = "http://www.cysce.com/esb/esbcore/v2/header";

		@JacksonXmlProperty(localName = "Service")
		private String service;

		@JacksonXmlProperty(localName = "Operation")
		private String operation;

		@JacksonXmlProperty(localName = "Version")
		private String version;

		@JacksonXmlProperty(localName = "Code")
		private String code;

		@JacksonXmlProperty(localName = "Reason")
		private String reason;

		// Getters y Setters
		public String getService() {
			return service;
		}

		public void setService(String service) {
			this.service = service;
		}

		public String getOperation() {
			return operation;
		}

		public void setOperation(String operation) {
			this.operation = operation;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}
	}
}
