package io.mosip.authentication.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * The Class MaskUtilTest.
 * 
 * @author Manoj SP
 *
 */
public class MaskUtilTest {

    /**
     * Test mask email.
     */
    @Test
    public void testMaskEmail() {
	assertEquals("XXaXXhXXh@mail.com", MaskUtil.maskEmail("umamahesh@mail.com"));
    }

    /**
     * Test mask mobile number.
     */
    @Test
    public void testMaskMobileNumber() {
	assertEquals("XXXXXX7890", MaskUtil.maskMobile("1234567890"));
    }
    
    @Test
    public void testGenerateMaskValue() {
	assertEquals("XXXX34654512324", MaskUtil.generateMaskValue("123234654512324", 4));
    }
    
    @Test
    public void testGenerateMaskValue2() {
	assertNotEquals("XXXX34654512324", MaskUtil.generateMaskValue("123234654512324", 8));
    }

}
