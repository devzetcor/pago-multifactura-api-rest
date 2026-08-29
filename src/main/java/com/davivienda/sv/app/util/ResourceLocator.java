package com.davivienda.sv.app.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceLocator {
	private static final Logger LOGGER = LogManager.getLogger(ResourceLocator.class);

	public static Properties loadPropertiesFromPath(String propsFileName) {
		Properties props = new Properties();

		try (InputStream iStream = new FileInputStream(propsFileName)) {
			props.load(iStream);
		} catch (IOException e) {
			LOGGER.warn("No se pudo cargar el recurso " + propsFileName + ": " + e.getMessage(), e);
		}

		return props;
	}
	
	public static Properties loadPropertiesFromClasspath(String propsFileName) {
		Properties props = new Properties();

		try (InputStream iStream = new ClassPathResource(propsFileName).getInputStream()) {
			props.load(iStream);
		} catch (IOException e) {
			LOGGER.error("No se pudo cargar el recurso "+ propsFileName);
		}

		return props;
	}
	
	public static InputStream loadResource(String fileName) {
		try (InputStream iStream = new ClassPathResource(fileName).getInputStream()) {
			return iStream;
		} catch (IOException e) {
			LOGGER.error("No se pudo cargar el recurso "+ fileName);
			return null;
		}
	}
}
