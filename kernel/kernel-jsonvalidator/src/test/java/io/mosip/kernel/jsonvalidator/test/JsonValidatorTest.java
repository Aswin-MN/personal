package io.mosip.kernel.jsonvalidator.test;

import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;

import io.mosip.kernel.core.jsonvalidator.exception.FileIOException;
import io.mosip.kernel.core.jsonvalidator.exception.HttpRequestException;
import io.mosip.kernel.core.jsonvalidator.exception.JsonIOException;
import io.mosip.kernel.core.jsonvalidator.exception.JsonSchemaIOException;
import io.mosip.kernel.core.jsonvalidator.exception.JsonValidationProcessingException;
import io.mosip.kernel.jsonvalidator.constant.JsonValidatorErrorConstant;
import io.mosip.kernel.jsonvalidator.impl.JsonValidatorImpl;

/**
 * 
 * @author Swati Raj
 * @since 1.0.0
 *
 */


public class JsonValidatorTest {

	/*
	 * Mocking JsonValidator class object
	 */
	private JsonValidatorImpl jsonValidator= Mockito.mock(JsonValidatorImpl.class);

	@Test(expected = JsonValidationProcessingException.class)
	public void testForReportProcessingException()
			throws HttpRequestException, JsonValidationProcessingException, JsonIOException, IOException, JsonSchemaIOException, FileIOException {

		when(jsonValidator.validateJson(Mockito.any(), Mockito.any()))
		.thenThrow(new JsonValidationProcessingException(JsonValidatorErrorConstant.JSON_VALIDATION_PROCESSING_EXCEPTION.getMessage(),
				JsonValidatorErrorConstant.JSON_VALIDATION_PROCESSING_EXCEPTION.errorCode));
		jsonValidator.validateJson("{}", "");


	}
}
