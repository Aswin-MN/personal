package io.mosip.authentication.service.filter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.springframework.stereotype.Component;

import io.mosip.authentication.core.constant.IdAuthenticationErrorConstants;
import io.mosip.authentication.core.exception.IdAuthenticationAppException;
import io.mosip.authentication.core.logger.IdaLogger;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.DateUtils;

/**
 * The Class IdAuthFilter.
 *
 * @author Manoj SP
 */
@Component
public class IdAuthFilter extends BaseAuthFilter {

	private static Logger mosipLogger = IdaLogger.getLogger(IdAuthFilter.class);

	private static final String TXN_ID = "txnID";

	/** The Constant REQUEST. */
	private static final String REQUEST = "request";

	/** The Constant REQ_TIME. */
	private static final String REQ_TIME = "reqTime";

	/** The Constant RES_TIME. */
	private static final String RES_TIME = "resTime";

	/** The Constant DATETIME_PATTERN. */
	private static final String DATETIME_PATTERN = "datetime.pattern";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.authentication.service.filter.BaseAuthFilter#setTxnId(java.util.Map,
	 * java.util.Map)
	 */
	@Override
	protected Map<String, Object> setResponseParam(Map<String, Object> requestBody, Map<String, Object> responseBody)
			throws IdAuthenticationAppException {
		try {
			if (Objects.nonNull(requestBody.get(TXN_ID))) {
				responseBody.replace(TXN_ID, requestBody.get(TXN_ID));
			}

			if (Objects.nonNull(requestBody.get(REQ_TIME))) {
				ZoneId zone = ZonedDateTime.parse((CharSequence) requestBody.get(REQ_TIME)).getZone();
				responseBody.replace(RES_TIME,
						DateUtils.formatDate(
								DateUtils.parseToDate((String) responseBody.get(RES_TIME),
										env.getProperty(DATETIME_PATTERN), TimeZone.getTimeZone(zone)),
								env.getProperty(DATETIME_PATTERN), TimeZone.getTimeZone(zone)));
				return responseBody;
			} else {
				return responseBody;
			}
		} catch (DateTimeParseException e) {
			mosipLogger.error("sessionId", "IdAuthFilter", "setResponseParam", "\n" + ExceptionUtils.getStackTrace(e));
			return responseBody;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.authentication.service.filter.BaseAuthFilter#init(javax.servlet.
	 * FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.authentication.service.filter.BaseAuthFilter#decodedRequest(java.
	 * util.Map)
	 */
	@Override
	protected Map<String, Object> decodedRequest(Map<String, Object> requestBody) throws IdAuthenticationAppException {
		try {
			requestBody.replace(REQUEST, decode((String) requestBody.get(REQUEST)));
			if (null != requestBody.get(REQUEST)) {
				Map<String, Object> request = keyManager.requestData(requestBody, mapper);
				requestBody.replace(REQUEST, request);
			}
			return requestBody;
		} catch (ClassCastException e) {
			throw new IdAuthenticationAppException(IdAuthenticationErrorConstants.INVALID_AUTH_REQUEST.getErrorCode(),
					IdAuthenticationErrorConstants.INVALID_AUTH_REQUEST.getErrorMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.authentication.service.filter.BaseAuthFilter#encodedResponse(java.
	 * util.Map)
	 */
	@Override
	protected Map<String, Object> encodedResponse(Map<String, Object> responseBody)
			throws IdAuthenticationAppException {
		try {
			responseBody.replace(REQUEST, encode((String) responseBody.get(REQUEST)));
			return responseBody;
		} catch (ClassCastException e) {
			throw new IdAuthenticationAppException(IdAuthenticationErrorConstants.INVALID_AUTH_REQUEST.getErrorCode(),
					IdAuthenticationErrorConstants.INVALID_AUTH_REQUEST.getErrorMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.mosip.authentication.service.filter.BaseAuthFilter#validateSignature(java.
	 * lang.String, byte[])
	 */
	@Override
	protected boolean validateSignature(String signature, byte[] requestAsByte) throws IdAuthenticationAppException {
		return true;
	}

}