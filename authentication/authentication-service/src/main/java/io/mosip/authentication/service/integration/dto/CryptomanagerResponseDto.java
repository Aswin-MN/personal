/*
 * 
 * 
 * 
 * 
 */
package io.mosip.authentication.service.integration.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Crypto-Manager-Response model
 * 
 * @author Arun Bose
 *
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model representing a Crypto-Manager-Service Response")
public class CryptomanagerResponseDto {
	/**
	 * Data Encrypted/Decrypted in BASE64 encoding
	 */
	@ApiModelProperty(notes = "Data encrypted/decrypted in BASE64 encoding")
	private String data;
}
