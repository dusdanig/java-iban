/*
   Copyright 2013 Barend Garvelink

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package nl.garvelink.iban;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Miscellaneous tests for the {@link IBAN} class.
 */
public class IBANTest {
    private static final String VALID_IBAN = "NL91ABNA0417164300";
    private static final String INVALID_IBAN = "NL12ABNA0417164300";

    @Test
    public void getCountryCodeShouldReturnTheCountryCode() {
        assertThat(IBAN.parse(VALID_IBAN).getCountryCode(), is("NL"));
    }

    @Test
    public void getCheckDigitsShouldReturnTheCheckDigits() {
        assertThat(IBAN.parse(VALID_IBAN).getCheckDigits(), is("91"));
    }

    @Test
    public void valueOfNullIsNull() {
        assertThat(IBAN.valueOf(null), is(nullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseShouldRejectNull() {
        IBAN.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseShouldRejectInvalidInput() {
        IBAN.parse("Shenanigans!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseShouldRejectLeadingWhitespace() {
        IBAN.parse(" " + VALID_IBAN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseShouldRejectTrailingWhitespace() {
        IBAN.parse(VALID_IBAN + ' ');
    }

    @Test(expected = UnknownCountryCodeException.class)
    public void parseShouldRejectUnknownCountryCode() {
        IBAN.parse("UU345678345543234");
    }

    @Test
    public void parseShouldRejectChecksumFailure() {
        try {
            IBAN.parse(INVALID_IBAN);
            fail("Invalid input should have been rejected for checksum mismatch.");
        } catch (WrongChecksumException e) {
            assertThat(e.getFailedInput(), is(INVALID_IBAN));
        }
    }

    @Test
    public void testEqualsContract() {
        IBAN x = IBAN.parse(VALID_IBAN);
        IBAN y = IBAN.parse(VALID_IBAN);
        IBAN z = IBAN.parse(VALID_IBAN);

        assertFalse("No object equals null", x.equals(null));
        assertTrue("An object equals itself", x.equals(x));
        assertTrue("Equality is symmetric", x.equals(y) && y.equals(x));
        assertTrue("Equality is transitive", x.equals(y) && y.equals(z) && x.equals(z));
        assertEquals("Equal objects have the same hash code", x.hashCode(), y.hashCode());
    }

    @Test
    public void testInvalidInputToGetLengthForCountry() {
        assertThat(IBAN.getLengthForCountryCode("nl"), is(-1));
        assertThat(IBAN.getLengthForCountryCode("Bogus"), is(-1));
    }
}
