package io.mosip.authentication.core.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import io.mosip.authentication.core.constant.IdAuthenticationErrorConstants;
import io.mosip.authentication.core.dto.indauth.AuthRequestDTO;
import io.mosip.authentication.core.exception.IdValidationFailedException;

@RunWith(MockitoJUnitRunner.class)
public class IdValidationFailedExceptionTest {

	AuthRequestDTO authReq = new AuthRequestDTO();

	private Errors errors = new org.springframework.validation.BindException(authReq, "authReq");
	
	@Test(expected = IdValidationFailedException.class)
	public void testDataValidationExceptionDefaultCons() throws IdValidationFailedException {
		errors.rejectValue(null, "test error", "test error");
		throw new IdValidationFailedException();
	}

	@Test(expected = IdValidationFailedException.class)
	public void testDataValidationException() throws IdValidationFailedException {
		errors.rejectValue(null, "test error", "test error");
		throw new IdValidationFailedException(IdAuthenticationErrorConstants.DATA_VALIDATION_FAILED, errors);
	}

	@Test(expected = IdValidationFailedException.class)
	public void testDataValidationExceptionWithErrors() throws IdValidationFailedException {
		throw new IdValidationFailedException(errors);
	}
	
	@Test(expected = IdValidationFailedException.class)
	public void testDataValidationException1() throws IdValidationFailedException {
		throw new IdValidationFailedException(IdAuthenticationErrorConstants.OTP_NOT_PRESENT, new Throwable());
	}

	@Test(expected = IdValidationFailedException.class)
	public void testDataValidationExceptionWithErrors1() throws IdValidationFailedException {
		throw new IdValidationFailedException(IdAuthenticationErrorConstants.OTP_NOT_PRESENT);
	}

}
