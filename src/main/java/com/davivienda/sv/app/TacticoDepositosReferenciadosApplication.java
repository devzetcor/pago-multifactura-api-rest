package com.davivienda.sv.app;

import com.davivienda.sv.app.util.R;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableJms
@SpringBootApplication(
		exclude = {
				DataSourceAutoConfiguration.class,
				JmsAutoConfiguration.class,
				JndiConnectionFactoryAutoConfiguration.class
		}
)
@EnableAsync
@EnableScheduling
public class TacticoDepositosReferenciadosApplication extends SpringBootServletInitializer {

	static {
		System.setProperty("log4j2.Configuration.allowedProtocols", "vfs,file,jar,http,https");
		System.setProperty("log4j2.allowedProtocols", "vfs,file,jar,http,https");
	}

	public static final String DATETIME_DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String CANAL = "MIB";

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TacticoDepositosReferenciadosApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(TacticoDepositosReferenciadosApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins(R.Configuracion.ALLOWED_ORIGINS);
			}
		};
	}
}