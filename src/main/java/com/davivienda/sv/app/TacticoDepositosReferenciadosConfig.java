package com.davivienda.sv.app;

import com.davivienda.sv.app.util.R;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class TacticoDepositosReferenciadosConfig {

	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();
		properties.setLocation(new FileSystemResource(R.Configuracion.APPLICATION_PROPERTIES));
	    properties.setIgnoreResourceNotFound(false);
	    return properties;
	}
}
