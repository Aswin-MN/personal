package io.mosip.kernel.masterdata.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.kernel.masterdata.dto.BiometricTypeDto;
import io.mosip.kernel.masterdata.dto.RequestDto;
import io.mosip.kernel.masterdata.dto.getresponse.BiometricTypeResponseDto;
import io.mosip.kernel.masterdata.entity.BiometricType;
import io.mosip.kernel.masterdata.entity.id.CodeAndLanguageCodeID;
import io.mosip.kernel.masterdata.service.BiometricTypeService;
import io.swagger.annotations.Api;

/**
 * Controller APIs to get Biometric types details
 * 
 * @author Neha
 * @since 1.0.0
 *
 */
@RestController
@Api(tags = { "BiometricType" })
@RequestMapping("/v1.0/biometrictypes")
public class BiometricTypeController {

	@Autowired
	private BiometricTypeService biometricTypeService;

	/**
	 * API to fetch all Biometric types details
	 * 
	 * @return All Biometric types
	 */
	@GetMapping
	public BiometricTypeResponseDto getAllBiometricTypes() {
		return biometricTypeService.getAllBiometricTypes();
	}

	/**
	 * API to fetch all Biometric types details based on language code
	 * 
	 * @param langCode
	 *            The language code
	 * 
	 * @return All Biometric type details
	 */
	@GetMapping("/{langcode}")
	public BiometricTypeResponseDto getAllBiometricTypesByLanguageCode(@PathVariable("langcode") String langCode) {
		return biometricTypeService.getAllBiometricTypesByLanguageCode(langCode);
	}

	/**
	 * API to fetch Biometric type details based on code and language code
	 * 
	 * @param code
	 *            the code
	 * @param langCode
	 *            the language code
	 * @return Biometric type
	 */
	@GetMapping("/{code}/{langcode}")
	public BiometricTypeResponseDto getBiometricTypeByCodeAndLangCode(@PathVariable("code") String code,
			@PathVariable("langcode") String langCode) {
		return biometricTypeService.getBiometricTypeByCodeAndLangCode(code, langCode);
	}

	/**
	 * API to insert Biometric type
	 * 
	 * @param biometricType
	 *            is of type {@link BiometricType}
	 * 
	 * @return {@link CodeAndLanguageCodeID}
	 */
	@PostMapping
	public ResponseEntity<CodeAndLanguageCodeID> createBiometricType(
			@Valid @RequestBody RequestDto<BiometricTypeDto> biometricType) {
		return new ResponseEntity<>(biometricTypeService.createBiometricType(biometricType), HttpStatus.CREATED);
	}
}
