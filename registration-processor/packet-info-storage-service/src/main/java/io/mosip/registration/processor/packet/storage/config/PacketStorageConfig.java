package io.mosip.registration.processor.packet.storage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The Class PacketStorageConfig.
 */
@Configuration	
@EnableSwagger2
public class PacketStorageConfig {

	/**
	 * Registration status bean.
	 *
	 * @return the docket
	 */
	@Bean
	public Docket registrationStatusBean() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Packet Storage").select()
				.apis(RequestHandlerSelectors.basePackage("io.mosip.registration.processor.packet.storage"))
				.paths(PathSelectors.ant("/v0.1/registration-processor/packet-info-storage-service/*")).build();
	}

}