package io.mosip.authentication.service.impl.indauth.service.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.mosip.authentication.core.exception.IdAuthenticationBusinessException;
import io.mosip.authentication.core.spi.indauth.match.MatchFunction;
import io.mosip.authentication.core.spi.indauth.match.MatchingStrategyType;

public class PhoneNoMatchingStrategyTest {

	/**
	 * Check for Exact type matched with Enum value of PhoneNo Matching Strategy
	 */
	@Test
	public void TestValidExactMatchingStrategytype() {
		assertEquals(PhoneNoMatchingStrategy.EXACT.getType(), MatchingStrategyType.EXACT);
	}

	/**
	 * Check for Exact type not matched with Enum value of PhoneNo Matching Strategy
	 */
	@Test
	public void TestInvalidExactMatchingStrategytype() {
		assertNotEquals(PhoneNoMatchingStrategy.EXACT.getType(), "PARTIAL");
	}

	/**
	 * Assert the PhoneNo Matching Strategy for Exact is Not null
	 */
	@Test
	public void TestValidExactMatchingStrategyfunctionisNotNull() {
		assertNotNull(PhoneNoMatchingStrategy.EXACT.getMatchFunction());
	}

	/**
	 * Assert the PhoneNo Matching Strategy for Exact is null
	 */
	@Test
	public void TestExactMatchingStrategyfunctionisNull() {
		MatchFunction matchFunction = PhoneNoMatchingStrategy.EXACT.getMatchFunction();
		matchFunction = null;
		assertNull(matchFunction);
	}

	/**
	 * Tests doMatch function on Matching Strategy Function
	 * 
	 * @throws IdAuthenticationBusinessException
	 */
	@Test
	public void TestValidExactMatchingStrategyFunction() throws IdAuthenticationBusinessException {
		MatchFunction matchFunction = PhoneNoMatchingStrategy.EXACT.getMatchFunction();

		int value = matchFunction.match("9876543210", "9876543210", null);
		assertEquals(100, value);

		int value1 = matchFunction.match("+91-9876543210", "+91-9876543210", null);
		assertEquals(100, value1);

		int value2 = matchFunction.match("413-3432-321", "413-3432-321", null);
		assertEquals(100, value2);
	}

	/**
	 * 
	 * Tests the Match function with in-valid values
	 * 
	 * @throws IdAuthenticationBusinessException
	 */
	@Test(expected = IdAuthenticationBusinessException.class)
	public void TestInvalidExactMatchingStrategyFunction() throws IdAuthenticationBusinessException {

		MatchFunction matchFunction = PhoneNoMatchingStrategy.EXACT.getMatchFunction();
		Map<String, Object> matchProperties = new HashMap<>();
		int value = matchFunction.match("9789438210", "1234567890", matchProperties);
		assertEquals(0, value);

		int value1 = matchFunction.match("76348798", "783248712", matchProperties);
		assertEquals(0, value1);

		int value2 = matchFunction.match("789-7389-783", "9832-129-322", matchProperties);
		assertEquals(0, value2);

		int value3 = matchFunction.match("+91-1234567890", "1234567890", matchProperties);
		assertEquals(0, value3);

		int value4 = matchFunction.match(1, "2", matchProperties);
		assertEquals(0, value4);

		int value5 = matchFunction.match(1, "123434545", matchProperties);
		assertEquals(0, value5);

	}
}
