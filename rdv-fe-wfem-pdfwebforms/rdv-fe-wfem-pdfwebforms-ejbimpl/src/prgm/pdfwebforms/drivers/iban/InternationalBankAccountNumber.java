/*
 * 
 * Copyright (C) 2003 Kees Schotanus
 *
* This library is free software; you can redistribute it and/or modify it under
* the terms of the GNU Lesser General Public License as published by the Free
* Software Foundation; either version 2.1 of the License, or (at your option)
* any later version.
*
* This library is distributed in the hope that it will be useful, but WITHOUT
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
* FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
* details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this library; if not, write to the Free Software Foundation, Inc.,
* 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package prgm.pdfwebforms.drivers.iban;

import java.math.BigInteger;


/**
 * Abstraction of an IBAN (International Bank Account Number).
 * <br>For further information about IBAN see:
 * <a href="http://www.ecbs.org/iban.htm">ECBS</a>.
 * @author Ahmed Saghir
 * @author Kees Schotanus
 * 
 */
public class InternationalBankAccountNumber {

    /**
     * Key to lookup: The bban is mandatory.
     * <br>Arguments:
     * <ul><li>BBAN</li></ul>.
     */
    public static final String MSG_BBAN_IS_MANDATORY = "bbanIsMandatory";

    /**
     * Key to lookup: The length of the BBAN is too long.
     * <br>Arguments:
     * <ul><li>BBAN</li></ul>.
     */
    public static final String MSG_BBAN_IS_TOO_LONG = "bbanIsTooLong";

    /**
     * Key to lookup: The BBAN's format is incorrect. Should contain alpha
     * numeric chracters only.
     * <br>Arguments:
     * <ul><li>BBAN</li></ul>.
     */
    public static final String MSG_BBAN_FORMAT_IS_INCORRECT =
        "bbanFormatIsIncorrect";

    /**
     * Key to lookup: The country is mandatory.
     * <br>Arguments:
     * <ul><li>Country</li></ul>.
     */
    public static final String MSG_COUNTRY_IS_MANDATORY = "countryIsMandatory";

    /**
     * Key to lookup: The country's length is incorrect.
     * <br>Arguments:
     * <ul><li>Country</li></ul>.
     */
    public static final String MSG_COUNTRY_LENGTH_IS_INCORRECT =
        "countryLengthIsIncorrect";

    /**
     * Key to lookup: The country's format (2 capital letters) is incorrect.
     * <br>Arguments:
     * <ul><li>Country</li></ul>.
     */
    public static final String MSG_COUNTRY_FORMAT_IS_INCORRECT =
        "countryFormatIsIncorrect";

    /**
     * Key to lookup: The country does not exist.
     * <br>Arguments:
     * <ul><li>Country</li></ul>.
     */
    public static final String MSG_COUNTRY_DOES_NOT_EXIST =
        "countryDoesNotExist";


    /**
     * Key to lookup: IBAN is null or empty.
     * <br>Arguments: None.
     */
    public static final String MSG_IBAN_IS_NULL_OR_EMPTY = "ibanIsNullOrEmpty";

    /**
     * Key to lookup: IBAN is too short.
     * <br>Arguments:
     * <ul><li>IBAN</li></ul>.
     */
    public static final String MSG_IBAN_IS_TOO_SHORT = "ibanIsTooShort";

    /**
     * Key to lookup: IBAN is too long.
     * <br>Arguments:
     * <ul><li>IBAN</li></ul>.
     */
    public static final String MSG_IBAN_IS_TOO_LONG = "ibanIsTooLong";

    /**
     * Key to lookup: IBAN contains lower case letters.
     * <br>Arguments:
     * <ul><li>IBAN</li></ul>.
     */
    public static final String MSG_IBAN_HAS_LOWER_CASE =
        "ibanHasLowerCaseLetters";

    /**
     * Key to lookup: IBAN contains non alpha numeric characters.
     * <br>Arguments:
     * <ul><li>IBAN</li></ul>.
     */
    public static final String MSG_IBAN_IS_NOT_ALPHA_NUMERIC =
        "ibanIsNotAlphaNumeric";

    /**
     * Key to lookup: IBAN has incorrect country.
     * <br>Arguments:
     * <ul>
     *   <li>IBAN</li>
     *   <li>Country</li>
     * </ul>.
     */
    public static final String MSG_IBAN_COUNTRY_DOES_NOT_EXIST =
        "ibanCountryDoesNotExist";

    /**
     * Key to lookup: IBAN has non-numeric check digits.
     * <br>Arguments:
     * <ul>
     *   <li>IBAN</li>
     *   <li>Check digits</li>
     * </ul>.
     */
    public static final String MSG_IBAN_CHECK_DIGITS_ARE_NOT_NUMERIC =
        "ibanCheckDigitsAreNotNumeric";

    /**
     * Key to lookup: IBAN has illegal check digits.
     * <br>Due to Modula-97 check the check digits can't ever be 00, 01 or 99.
     * <br>Arguments:
     * <ul>
     *   <li>IBAN</li>
     *   <li>Check digits</li>
     * </ul>.
     */
    public static final String MSG_IBAN_CHECK_DIGITS_ARE_ILLEGAL =
        "ibanCheckDigitsAreNotNumeric";

    /**
     * Key to lookup: IBAN does not pass modulo 97 check.
     * <br>Arguments:
     * <ul><li>IBAN</li></ul>.
     */
    public static final String MSG_IBAN_NOT_MODULO_97 = "ibanNotModulo97";

    /**
     * To perform modulo 97 check.
     */
    private static final BigInteger NINETY_SEVEN = new BigInteger("97");

    /**
     * To perform modulo 97 check.
     */
    private static final BigInteger NINETY_EIGHT = new BigInteger("98");

    /**
     * The IBAN (International Bank Account Number).
     */
    private String iban;


    /**
     * Constructs an IBAN object from the supplied iban String.
     * @param iban An International Bank Account Number.
     *  <br>The iban should be in "electronic form", that is, it should not
     *  contain any separators.
     * @throws InvalidArgumentException When the supplied iban does not contain
     *  a valid IBAN.
     *  <br>See: {@link #checkIban(String)} for the definition of a valid IBAN.
     */
    public InternationalBankAccountNumber(final String iban) throws InvalidArgumentException {
        checkIban(iban);
        this.iban = iban;
    }

    /**
     * Constructs an IBAN object from the supplied country and bban (Basic Bank
     * Account Number).
     * <br>This constructor automatically computes the check digits.
     * @param country Two letter ISO-3166 country code.
     * @param bban Basic Bank Account Number.
     * @throws InvalidArgumentException When the supplied country or the
     *  supplied bban are not valid or when the combination does not form a
     *  valid IBAN.
     *  <br>See: {@link #checkIban(String)} for the definition of a valid IBAN.
     */
    public InternationalBankAccountNumber(final String country, final String bban) throws InvalidArgumentException {

        checkCountry(country);
        checkBasicBankAccountNumber(bban);

        // Calculate check digits
        BigInteger checkDigits =
            new BigInteger(lettersToNumbers(bban + country + "00"));
        checkDigits = NINETY_EIGHT.subtract(checkDigits.mod(NINETY_SEVEN));

        this.iban =
            country + CString.alignRight(checkDigits.toString(), 2, '0') + bban;
    }


    /**
     * Gets the country from this IBAN.
     * <br>A country is a 2 letter code (ISO-3166).
     * @return The country.
     */
    public String getCountry() {
        return iban.substring(0, 2);

    }

    /**
     * Gets the check digits from this IBAN.
     * <br>The check digits consist of a String containing 2 digits.
     * @return The check digits.
     */
    public String getCheckDigits() {
        return iban.substring(2, 4);
    }

    /**
     * Gets the Basic Bank Account Number (BBAN) from this IBAN.
     * <br>The BBAN's format is different from country to country. The length of
     * the BBAN per country however is fixed. In the Netherlands for example
     * the BBAN consists of 4 letters for the bank and 9 digits (zero filled)
     * for the bank account.
     * @return The Basic Bank Account Number.
     */
    public String getBasicBankAccountNumber() {
        return iban.substring(4);
    }

    /**
     * Checks whether the supplied iban is valid.
     * For details see: {@link #checkIban(String)} .
     * @param iban The iban to check.
     * @return True when the supplied iban is valid, otherwise false.
     */
    public static boolean isValidIban(final String iban) {

        try {
            checkIban(iban);
            return true;
        } catch (final InvalidArgumentException exception) {
            return false;
        }

    }

    /**
     * Checks whether the supplied iban is valid.
     * <br>AN iban (International Bank Account Number) consists of:<br>
     * ISO-3166 2 letter (uppercase) country code<br>
     * 2 numeric check digits<br>
     * Up to 30 alphanumeric bban (Basic Bank Account Number) which has a fixed
     * length that differs per country<br>
     * Currently all possible validations but the check on ban length are being
     * performed on the supplied iban.
     * @param iban The iban to check.
     * @throws InvalidArgumentException When the supplied iban is not valid.
     */
    public static void checkIban(final String iban) throws InvalidArgumentException {
        if (CString.isNullOrEmpty(iban)) {
            throw new InvalidArgumentException(InternationalBankAccountNumber.class.getName(),MSG_IBAN_IS_NULL_OR_EMPTY);
        } else if (iban.length() < 6) {
            throw new InvalidArgumentException(InternationalBankAccountNumber.class.getName(),MSG_IBAN_IS_TOO_SHORT, new Object [] {iban});
        } else if (iban.length() > 34) {
            throw new InvalidArgumentException(InternationalBankAccountNumber.class.getName(),MSG_IBAN_IS_TOO_LONG, new Object [] {iban});
        } else if (!iban.equals(iban.toUpperCase())) {
            throw new InvalidArgumentException(InternationalBankAccountNumber.class.getName(),MSG_IBAN_HAS_LOWER_CASE, new Object [] {iban});
        } else if (!CString.isAlphaNumeric(iban)) {
            throw new InvalidArgumentException(InternationalBankAccountNumber.class.getName(),MSG_IBAN_IS_NOT_ALPHA_NUMERIC, new Object [] {iban});
        }

        // Check for valid ISO 3166 country
        final String country = iban.substring(0, 2);
        if (!ISO3166CountryConstants.getInstance().isValidKey(country)) {        	
            throw new InvalidArgumentException(InternationalBankAccountNumber.class.getName(),MSG_IBAN_COUNTRY_DOES_NOT_EXIST, new Object [] {iban, country});
        }

        // Check for numeric and valid checkdigits
        final String checkDigits = iban.substring(2, 4);
        if (!CString.isNumeric(checkDigits)) {
            throw new InvalidArgumentException(InternationalBankAccountNumber.class.getName(),MSG_IBAN_CHECK_DIGITS_ARE_NOT_NUMERIC,new Object [] {iban, checkDigits});
        }
        if ("00".equals(checkDigits) || "01".equals(checkDigits) || "99".equals(checkDigits)) {
            throw new InvalidArgumentException(InternationalBankAccountNumber.class.getName(),MSG_IBAN_CHECK_DIGITS_ARE_ILLEGAL,new Object [] {iban, checkDigits});
        }


        // Check for MOD 97-10 check
        final String basicBankAccountNumber = iban.substring(4);
        final String numericIban = InternationalBankAccountNumber.lettersToNumbers(basicBankAccountNumber + country + checkDigits);
        if (!modulo97Check(new BigInteger(numericIban))) {
            throw new InvalidArgumentException(InternationalBankAccountNumber.class.getName(),MSG_IBAN_NOT_MODULO_97, new Object [] {iban});
        }
    }

    /**
     * Checks that the supplied country is valid.
     * <br>A country is valid when it is a valid ISO-3166 country code. This
     * code consists of 2 upper-case letters.<br>
     * @param country The country to check.
     * @throws InvalidArgumentException When the supplied country is not a valid
     *  ISO-3166 country code.
     */
    public static void checkCountry(final String country) throws InvalidArgumentException {
        if (CString.isNullOrEmpty(country)) {
            throw new InvalidArgumentException(InternationalBankAccountNumber.class.getName(),MSG_COUNTRY_IS_MANDATORY, new Object [] {country});
        } else if (country.length() != 2) {
            throw new InvalidArgumentException(InternationalBankAccountNumber.class.getName(),MSG_COUNTRY_LENGTH_IS_INCORRECT, new Object [] {country});
        } else if (!CString.isAlphabetic(country) || !country.equals(country.toUpperCase())) {
            throw new InvalidArgumentException(InternationalBankAccountNumber.class.getName(),MSG_COUNTRY_FORMAT_IS_INCORRECT, new Object[] {country});
        } else if (!ISO3166CountryConstants.getInstance().isValidKey(country)) {
            throw new InvalidArgumentException(InternationalBankAccountNumber.class.getName(),MSG_COUNTRY_DOES_NOT_EXIST, new Object[] {country});
        }
    }

    /**
     * Checks that the supplied bban (Basic Bank Account Number) is valid.
     * <br>A bban is valid when the following conditions are met:
     * <ul>
     *   <li>non null</li>
     *   <li>alpha-numeric</li>
     *   <li>Not longer than 30 characters</li>
     * </ul>
     * @param bban The Basic Bank Account Number to check.
     * @throws InvalidArgumentException When the supplied bban is not  valid.
     */
    public static void checkBasicBankAccountNumber(final String bban) throws InvalidArgumentException {
        if (CString.isNullOrEmpty(bban)) {
            throw new InvalidArgumentException(InternationalBankAccountNumber.class.getName(),MSG_BBAN_IS_MANDATORY, new Object [] {bban});
        } else if (bban.length() > 30) {
            throw new InvalidArgumentException(InternationalBankAccountNumber.class.getName(),MSG_BBAN_IS_TOO_LONG, new Object [] {bban});
        } else if (!CString.isAlphaNumeric(bban) || !bban.equals(bban.toUpperCase())) {
            throw new InvalidArgumentException(InternationalBankAccountNumber.class.getName(),MSG_BBAN_FORMAT_IS_INCORRECT, new Object[] {bban});
        }
    }

    /**
     * Converts letters to numbers.
     * <br>The letter A is converted to 10, B to 11 et cetera. This conversion
     * is necessary for iban (International Bank Account Numbers).
     * @param input The input to be converted to numbers.
     * @return The supplied input String where all letters have been converted
     *  to numbers.
     * @throws NullPointerException When the supplied input is null.
     */
    private static String lettersToNumbers(final String input) {
        CDebug.checkParameterNotNull(input, "input");

        final StringBuffer result = new StringBuffer();
        for (int i = 0; i < input.length(); ++i) {
            if (Character.isLetter(input.charAt(i))) {
                result.append(input.charAt(i) - 'A' + 10);
            } else {
                result.append(input.charAt(i));
            }
        }

        return result.toString();
    }

    /**
     * Checks whether the supplied input adheres to the MOD 97-10 check.
     * <br>See: ISO 7064.
     * @param input The input to check for modulo-97 adherance.
     * @return True when the supplied input adheres to the odule-97 check, false
     *  otherwise.
     * @throws NullPointerException When the supplied input is null.
     */
    public static boolean modulo97Check(final BigInteger input) {
        CDebug.checkParameterNotNull(input, "input");

        return BigInteger.ONE.equals(input.mod(NINETY_SEVEN));
    }

    /**
     * Creates the paper format of this IBAN.
     * <br>The paper format is created by adding a space to every fourth
     * character of the electronic form.
     * @return Paper format of this IBAN.
     */
    public String toPaperFormat() {
        final StringBuffer paperFormat = new StringBuffer(iban);
        for (int i = 4; i < paperFormat.length(); i+=5) {
            paperFormat.insert(i, ' ');
        }

        return paperFormat.toString();
    }

    /**
     * Creates a String representation of this IBAN consisting of the IBAN
     * itself.
     * @return The IBAN.
     */
    public String toString() {
        return iban;
    }

    /**
     * Simple test method.
     * @param args Not used.
     * @throws InvalidArgumentException When one of the iban's is not valid.
     */
    //public static void main(final String [] args) throws InvalidArgumentException {
        //checkIban("BE62510007547061");
        //checkIban(new InternationalBankAccountNumber("BE", "510007547061").toString());
    //}
}
