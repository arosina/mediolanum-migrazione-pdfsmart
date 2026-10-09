/* 
 * Copyright (C) 2002 Kees Schotanus
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

import java.util.StringTokenizer;


/**
 * Collection of String methods.
 * @author Kees Schotanus
 * 
 */
public class CString {

    /*
     * Design considerations: Since the java.lang.String class is final
     * inheritance is out of the question so I made this a collection of
     * separate String related methods. In order to avoid ambiguity I added a C
     * to the String class name.
     */

    /**
     * Contains the escape sequence for the first 32 characters.
     * <br>Can be used to put back in the escape codes that the compiler might
     * have removed. Can also be used to display the content of a String without
     * disturbing the output due to control characters.
     */
    public static final String [] ESCAPES = {
        "\\u0000", "\\u0001", "\\u0002", "\\u0003",
        "\\u0004", "\\u0005", "\\u0006", "\\u0007",
        "\\b",     "\\t",     "\\n",     "\\u000B",
        "\\f",     "\\r",     "\\u000E", "\\u000F",
        "\\u0010", "\\u0011", "\\u0012", "\\u0013",
        "\\u0014", "\\u0015", "\\u0016", "\\u0017",
        "\\u0018", "\\u0019", "\\u001A", "\\u001B",
        "\\u001C", "\\u001D", "\\u001E", "\\u001F",
    };

    /**
     * Private constructor prevents creation of an instance.
     */
    private CString() {
    }

    /**
     * Tokenizes a String.
     * <br>That is to say that the supplied input String is split into tokens.
     * This method assumes the following delimiter set: space, tab, newline,
     * carriage-return and the form-feed character.
     * @param input Input string to split into tokens.
     * @return Array containing a String for each token.
     * @see StringTokenizer#StringTokenizer(String)
     */
    public static String [] tokenize(final String input) {
        // A null reference should return an empty array not an exception.
        final StringTokenizer stringTokenizer =
            new StringTokenizer(input == null ? "" : input);

        // Allocate a String array of just the right size.
        final String [] tokens = new String[stringTokenizer.countTokens()];

        // Get all tokens from the input and store them.
        for (int i = 0; stringTokenizer.hasMoreTokens(); ++i) {
            tokens[i] = stringTokenizer.nextToken();
        }

        return tokens;
    }

    /**
     * Tokenizes a String.
     * <br>That is to say that the supplied input String is split into tokens.
     * @param input Input string to split into tokens.
     * @param delimiters Every character in this String is a delimiter.
     * @return Array containing a String for each token.
     * @throws NullPointerException When the supplied delimiters is null.
     * @throws IllegalArgumentException When the supplied delimiters is empty.
     * @see StringTokenizer#StringTokenizer(String,String)
     */
    public static String [] tokenize(
            final String input, final String delimiters) {
        CDebug.checkParameterTrue(delimiters != null && delimiters.length() > 0,
            "delimiters may neither be null, nor empty!");

        // A null reference should return an empty array not an exception.
        final StringTokenizer stringTokenizer =
            new StringTokenizer(input == null ? "" : input, delimiters);

        // Allocate a String array of just the right size.
        final String [] tokens = new String[stringTokenizer.countTokens()];

        // Get all tokens from the input and store them.
        for (int i = 0; stringTokenizer.hasMoreTokens(); ++i) {
            tokens[i] = stringTokenizer.nextToken();
        }

        return tokens;
    }

    /**
     * Replaces all occurrences of oldString with newString in the supplied
     * input String.
     * <br>Note: As of jdk 1.2 you can use a replace method on a StringBuffer
     * and as of jdk 1.4 you can use replaceAll on a String (using regular
     * expressions).
     * @param input The input String that contains the Strings (zero or more)
     *  that need to be replaced.
     * @param oldString The String in the input String that must be replaced.
     * @param newString This String replaces the String oldString in the input
     *  String.
     * @return A String resulting from replacing all occurrences of oldString
     *  in the input String with newString.
     *  <br>When oldString does not occur in input or when oldString == null
     *  or empty then no new String is constructed, the input String is simply
     *  returned.<br>
     *  When input is null then null is returned.
     */
    public static String replace(final String input, final String oldString,
            final String newString) {

        String result = input;
        if (input != null && oldString != null && !"".equals(oldString)) {
            int newPosition = input.indexOf(oldString);
            if (newPosition != -1) {
                final StringBuffer buffer = new StringBuffer();
                buffer.append(input.substring(0, newPosition));
                buffer.append(newString == null ? "" : newString);

                int oldPosition = newPosition + oldString.length();
                while ((newPosition = input.indexOf(oldString, oldPosition))
                        != -1) {
                    buffer.append(input.substring(oldPosition, newPosition));
                    buffer.append(newString == null ? "" : newString);
                    oldPosition = newPosition + oldString.length();
                }
                buffer.append(input.substring(oldPosition, input.length()));
                result = buffer.toString();
            }
        }
        return result;
    }

    /**
     * Creates a String that consists of a repetition, determined by the
     * supplied repeat count, of the supplied input String.
     * <br>To create a String of 10 spaces for example, execute:
     * CString.repeat(" ", 10);
     * @param input Input String that will be repeated.
     * @param repeat The number of times the supplied input String will be
     *  repeated.
     *  <br>Supplying a value of 0 will result in an empty String.
     * @return String consisting of a concatenation of repeat times the supplied
     *  input String.
     * @throws NullPointerException When the supplied input String is null.
     * @throws IllegalArgumentException When the supplied repeat count is
     *  negative.
     */
    public static String repeat(final String input, final int repeat) {
        CDebug.checkParameterNotNull(input, "input");
        CDebug.checkParameterTrue(repeat >= 0, "repeat may not be negative!");

        final StringBuffer result = new StringBuffer(input.length() * repeat);
        for (int i = 1; i <= repeat; i++) {
            result.append(input);
        }

        return result.toString();
    }

    /**
     * Left aligns the supplied input String in a String with a length equal
     * to the supplied length.
     * <br>Examples:
     * <table>
     *   <tr><th>Parameters</th><th>Returns</th></tr>
     *   <tr><td>"1234", 6, '0'</td><td>123400</td></tr>
     *   <tr><td>"1234", 2, '0'</td><td>12</td></tr>
     * </table>
     * @param input The input String to align left.
     * @param length The length of the resulting left aligned String.
     *  <br>When this length is less than the length of the supplied input
     *  String, the input String is truncated to fit the supplied length.
     * @param pad The pad char used to fill the vacated positions.
     *  <br>Normally you will supply a space or a zero character to fill the
     *  vacated positions with spaces or zeroes respectively.
     * @return The left aligned input String.
     * @throws NullPointerException When the supplied input String is null.
     * @throws IllegalArgumentException When the supplied length is less than
     *  one.
     */
    public static String alignLeft(
            final String input, final int length, final char pad) {
        CDebug.checkParameterNotNull(input, "input");
        CDebug.checkParameterTrue(length >= 1, "length must be >= 1!");

        if (input.length() == length) {
            return input;
        } else if (input.length() < length) {
            return input + repeat(String.valueOf(pad), length - input.length());
        } else {
            return input.substring(0, length);
        }
    }

    /**
     * Right aligns the supplied input String in a String with a length equal
     * to the supplied length.
     * <br>Examples:
     * <table>
     *   <tr><th>Parameters</th><th>Returns</th></tr>
     *   <tr><td>"1234", 6, '0'</td><td>001234</td></tr>
     *   <tr><td>"1234", 2, '0'</td><td>34</td></tr>
     * </table>
     * @param input The input String to align right.
     * @param length The length of the resulting right aligned String.
     *  <br>When this length is less than the length of the supplied input
     *  String, the input String is truncated to fit the supplied length.
     * @param pad The pad char used to fill the vacated positions.
     *  <br>Normally you will supply a space or a zero character to fill the
     *  vacated positions with spaces or zeroes respectively.
     * @return The right aligned input String.
     * @throws NullPointerException When the supplied input String is null.
     * @throws IllegalArgumentException When the supplied length is less than
     *  one.
     */
    public static String alignRight(
            final String input, final int length, final char pad) {
        CDebug.checkParameterNotNull(input, "input");
        CDebug.checkParameterTrue(length >= 1, "length must be >= 1!");

        if (input.length() == length) {
            return input;
        } else if (input.length() < length) {
            return repeat(String.valueOf(pad), length - input.length()) + input;
        } else {
            return input.substring(input.length() - length);
        }
    }

    /**
     * Changes &amp;, &lt;, &gt; and &quot; in the input string.
     * <br>The returned String can be used to show an html page as is. The
     * input string &lt;h1&gt;Hello&lt;/h1&gt; for example would be returned
     * as &amplt;h1&ampgt;Hello&amplt;/h1&ampgt;<br>
     * Note: Single quotes are not replaced mainly because Internet Explorer
     * does not accept existing entities for a single quote!
     * @param input Input string.
     * @return Supplied input string where all occurrences of ampersands,
     *  greater than, less than and quotes have been replaced.<br>
     *  A null input string will be returned as an empty string!
     */
    public static String quoteHTML(final String input) {
        if (input == null) {
            return "";
        } else {
            String tmp = replace(input, "&", "&amp;");
            tmp = replace(tmp, "<", "&lt;");
            tmp = replace(tmp, ">", "&gt;");
            tmp = replace(tmp, "\"", "&quot;");

            return tmp;
        }
    }

    /**
     * Escapes all quotes (single and double) with an escape character (unless
     * the quote has already been escaped).
     * @param input String
     * @return Supplied input string where all occurrences of quotes, have been
     *  replaced with an escape character and the quote.<br>
     *  A null input string will be returned as an empty string!
     */
    public static String escapeQuotes(final String input) {
        if (input == null) {
            return "";
        } else {
            // Change escaped quotes to unescaped quotes (to avoid double
            // escaping).
            String tmp = replace(input, "\\'", "'");
            tmp = replace(tmp, "\\\"", "\"");

            // Now escape all quotes
            tmp = replace(tmp, "\'", "\\'");
            tmp = replace(tmp, "\"", "\\\"");

            return tmp;
        }
    }

    /**
     * Replaces characters in the range 00-1F with escape characters.
     * @param input Input string.
     * @return Input string but with all control characters replaced with
     *  escape sequences.
     *  <br>Null is returned when the input string is null.
     */
    public static String escape(final String input) {
        String returnValue = null;
        if (input != null) {
            final StringBuffer escaped = new StringBuffer(input.length());
            for (int i = 0; i < input.length(); i++) {
                final char inputChar = input.charAt(i);
                if (inputChar < 32) {
                    escaped.append(ESCAPES[inputChar]);
                } else {
                    escaped.append(inputChar);
                }
            }
            returnValue = escaped.toString();
        }

        return returnValue;
    }

    /**
     * Escapes the % and _ wildcard characters with a backslash (\).
     * @param input Input string possibly containg EJBQL wildcard characters
     *  (% and _).
     * @return Input string but with all wildcard characters escaped with a
     *  backslash.
     *  <br>Null is returned when the input string is null.
     */
    public static String escapeEJBQL(final String input) {
        final String result = replace(input, "%", "\\%");
        return replace(result, "_", "\\_");
    }


    /**
     * Counts the number of occurrences of find in the source String.
     * @param source Input String.
     * @param find String that will be searched in the input String.
     * @return Number of times that the String find occurs in the input String.
     */
    public static int count(final String source, final String find) {
        if (source == null || source.length() == 0
               || find == null || find.length() ==0) {
            return 0;
        }

        int count = 0;
        for (int pos = source.indexOf(find, 0);
                 pos != -1; count++, pos = source.indexOf(find, pos + 1)) {
        }

        return count;
    }

    /**
     * Determines whether the supplied input String is null or empty.
     * @param input Input String to test.
     * @return True when the supplied input String is null or empty, false
     *  otherwise.
     */
    public static boolean isNullOrEmpty(final String input) {
        return input == null || input.trim().length() == 0;
    }

    /**
     * Determines whether the supplied input String is not empty.
     * @param input Input String to test.
     * @return True when the supplied input String is not empty.
     *  <br>A String is not empty when it is not null and does not consist of
     *  whitespace characters only. A String containing a tab character is
     *  considered empty.
     */
    public static boolean isNotEmpty(final String input) {
        return !isNullOrEmpty(input);
    }

    /**
     * Determines whether the supplied input String consists of alphabetic
     * characters (letters) only.
     * @param input Input String to test.
     * @return True when for every character c in the supplied input String,
     *  Character.isLetter(c) == true, otherwise false is returned.
     *  <br>When the supplied input String is empty, true is returned.
     * @throws NullPointerExcption When the supplied input String is null.
     */
    public static boolean isAlphabetic(final String input) {
        CDebug.checkParameterNotNull(input, "input");

        for (int i = 0; i < input.length(); ++i) {
            if (!Character.isLetter(input.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines whether the supplied input String consists of numeric
     * characters (digits) only.
     * <br>Decimal separators and plus or minus signs are <b>not</b> considered
     * to be numeric!
     * @param input Input String to test.
     * @return True when for every character c in the supplied input String,
     *  Character.isDigit(c) == true, otherwise false is returned.
     *  <br>When the supplied input String is empty, true is returned.
     * @throws NullPointerExcption When the supplied input String is null.
     */
    public static boolean isNumeric(final String input) {
        CDebug.checkParameterNotNull(input, "input");

        for (int i = 0; i < input.length(); ++i) {
            if (!Character.isDigit(input.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines whether the supplied input String consists of alphabetic
     * letters and numeric characters (digits) only.
     * <br>Decimal separators and plus or minus signs are <b>not</b> considered
     * to be alphanumeric!
     * @param input Input String to test.
     * @return True when for every character c in the supplied input String,
     *  (Character.isLetter(c) || Character.isDigit(c)) == true, otherwise false
     *  is returned.
     *  <br>When the supplied input String is empty, true is returned.
     * @throws NullPointerExcption When the supplied input String is null.
     */
    public static boolean isAlphaNumeric(final String input) {
        CDebug.checkParameterNotNull(input, "input");

        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            if (!(Character.isLetter(c) || Character.isDigit(c))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Converts the supplied array to a String.
     * @param array The array to convert to a String.
     * @return String representatation of the supplied array.
     *  <br>When a null array was supplied, "null" is returned.<br>
     *  When an empty array was supplied, "[]" is returned.<br>
     *  In all other cases a comma separated list between square brackets is
     *  returned.
     */
    public static String arrayToString(final Object [] array) {
        final StringBuffer result = new StringBuffer();
        if (array == null) {
            result.append("null");
        } else if (array.length == 0) {
            result.append("[]");
        } else {
            result.append("[");
            result.append(array[0]);
            for (int i = 1; i < array.length; i++) {
                result.append(",").append(array[i]);
            }
            result.append("]");
        }

        return result.toString();
    }

    /**
     * Converts the supplied array of values to a string of comma separated
     * values.
     * <br>Note: This method assumes, but not enforces, that every item in the
     * supplied values array results in a String representation that <b>not</b>
     * contains a comma!
     * @param values The array of values that must be comma separated.
     * @return A list of comma separated values or an empty string when the
     *  supplied array of values is either null or empty.
     */
    public static String arrayToCommaSeparatedValues(final Object [] values) {

        if (values == null || values.length == 0) {
            return "";
        }

        final StringBuffer csv = new StringBuffer(values[0].toString());
        for (int i = 1; i < values.length; ++i) {
            csv.append(",").append(values[i]);
        }

        return csv.toString();
    }

}
