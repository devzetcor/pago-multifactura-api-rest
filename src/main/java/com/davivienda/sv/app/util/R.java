package com.davivienda.sv.app.util;

public interface R {

	public static interface J2Entorno{

		public static final String INVOCADOR_BUS = "invocadorBus";

		public static interface BusMigrado{

			public static interface FabricaWebServicesCanales{
				public final static String NOMBRE = "FabricaWebServicesCanales1";
				public static enum Servicio {
					SERVICIO_GESTION_CACHE("srvGestionCache")
					;

					private String nombre;

					private Servicio(String nombre) {
						this.nombre = nombre;
					}

					public String getNombre() {
						return nombre;
					}

				}
			}
		}

	}
	public static interface Configuracion {
		// wildfly
		public final static String[] ALLOWED_ORIGINS= {
				//DESARROLLO
				"http://localhost:4200",
				"http://sv4166lap:8080",
				"https://localhost:4200",
				"https://sv4166lap:8443",
				"http://desarrollo.davivienda.com.sv:8280",
				"https://desarrollo.davivienda.com.sv:8643",
				//PRODUCCION
				"http://localhost:8180",
				"https://bancaelectronica.davivienda.com.sv"
		};
		public final static String ERROR_PROPERTIES = "/jboss/app/pagoMultifactura/mensajesError.properties";
		public final static String APPLICATION_PROPERTIES = "/jboss/app/pagoMultifactura/application.config";

		// ch
//		public final static String ERROR_PROPERTIES = "C:\\Productividad\\APP\\eclipse  2021-06 (4.20.0)\\Productividadworkspacesmobile.banking\\app-pyme_DEV\\src\\main\\mensajesError.properties";
//		public static final String APPLICATION_PROPERTIES = "C:\\Productividad\\APP\\eclipse  2021-06 (4.20.0)\\Productividadworkspacesmobile.banking\\app-pyme_DEV\\src\\main\\application.yml";

		// mike
		// public final static String
		// ERROR_PROPERTIES="C:\\Users\\51630\\Documents\\eclipseWorkspace\\app-pyme\\src\\main\\resources\\application.yml";
		// public static final String APPLICATION_PROPERTIES =
		// "C:\\Users\\51630\\Documents\\eclipseWorkspace\\app-pyme\\src\\main\\resources\\mensajesError.properties";
	}

	public static interface MQCliente {

		public final static String BUS_1 = "mqClienteBus";
		public final static String PFS_1 = "mqClientePFSService";

		static interface JMSTemplate {
			public final static String BUS = "jmsTemplateBUS";
			public final static String PFS = "jmsTemplatePFS";
		}
		static interface PFS {
			public final static String NAME = "mqClientePFS";
			public final static String JMS_TEMPLATE = "jmsTemplatePFS";
			public final static String QREQUEST = "pfsQueueRequestJndiObject";
			public final static String QRESPONSE = "pfsQueueResponseJndiObject";
		}

		static interface BUS {
			public final static String NAME = "mqClienteBUS";
			public final static String JMS_TEMPLATE = "jmsTemplateBUS";
			public final static String QREQUEST = "busQueueRequestJndiObject";
			public final static String QRESPONSE = "busQueueResponseJndiObject";
		}

		static interface JMSPropertie {
			public static final int DEFAULT_JMS_PRIORITY = 4;
			public static final int DEFAULT_JMS_EXPIRY = 600;
			public static final int DEFAULT_JMS_TIMEOUT = 60000;
		}
	}

	public static interface Fabricas {
		public final static String ESBeBanca = "FabricaESBeBanca_PYME_UT2";
	}


}
