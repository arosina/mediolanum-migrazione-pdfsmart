/*
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

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;


/**
 * Base class for comparators.
 * <br>Use this class when you need a comparator capable of ordering on multiple
 * fields where each field is ordered ascending or descending.
 * @author Kees Schotanus
 * 
 */
public abstract class AbstractComparator implements Comparator, Serializable {

    /**
     * Array of allowed order-by-fields.
     */
    private OrderByField [] allowedOrderByFields;

    /**
     * Contains all the fields used in the comparison.
     * <br>This must be a subselection of allowedOrderByFields.
     */
    private OrderByField [] orderByFields;

    /**
     * The optional (may be null) locale.
     */
    private Locale locale;

    /**
     * Constructor to compare objects, using the supplied orderByField.
     * @param allowedOrderByFields All allowed (possible) fields that can be
     *  used to order the collection.
     * @param orderByField The field used to order the collection.
     *  <br>This field should exist in the supplied allowedOrderByFields.
     * @throws NullPointerException When either the supplied
     *  allowedOrderByFields or the supplied orderByField is null.
     * @throws IllegalArgumentException When the supplied allowedOrderByFields
     *  is empty.
     */
    public AbstractComparator(
            final OrderByField [] allowedOrderByFields,
            final OrderByField orderByField) {
        CDebug.checkParameterNotNull(allowedOrderByFields, "allowedOrderByFields");
        CDebug.checkParameterTrue(allowedOrderByFields.length > 0, "Can't have an empty array of allowedOrderByFields!");
        CDebug.checkParameterNotNull(orderByField, "orderByField");

        this.allowedOrderByFields = cloneOrderByFields(allowedOrderByFields);
        this.orderByFields = new OrderByField [] {new OrderByField(orderByField)};
    }

    /**
     * Constructor to compare objects, using the supplied orderByFields.
     * @param allowedOrderByFields All allowed (possible) fields that can be
     *  used to order the collection.
     * @param orderByFields Fields used to order the collection.
     *  <br>Fields in this array must be a subset of the elements of the
     *  supplied allowedOrderByFields array.
     * @throws NullPointerException When either the supplied
     *  allowedOrderByFields or the supplied orderByFields is null.
     * @throws IllegalArgumentException When either the supplied
     *  allowedOrderByFields or the supplied orderByFields is empty.
     */
    public AbstractComparator(
            final OrderByField [] allowedOrderByFields,
            final OrderByField [] orderByFields) {
        CDebug.checkParameterNotNull(allowedOrderByFields, "allowedOrderByFields");
        CDebug.checkParameterTrue(allowedOrderByFields.length > 0,"Can't have an empty array of allowedOrderByFields!");
        CDebug.checkParameterNotNull(orderByFields, "orderByFields");
        CDebug.checkParameterTrue(orderByFields.length > 0,"Can't have an empty array of orderByFields!");

        this.allowedOrderByFields = cloneOrderByFields(allowedOrderByFields);
        this.orderByFields = cloneOrderByFields(orderByFields);
    }

    /**
     * Utility method to compare two booleans.
     * <br>Booleans are ordered as follows:
     * <ol>
     *   <li>false</li>
     *   <li>true</li>
     * </ol>
     * @param booleanOne The first boolean.
     * @param booleanTwo The second boolean.
     * @return -1 when booleanOne is false and booleanTwo is true, 0 when both
     *  parameters are equal, 1 when booleanOne is true and booleanTwo is false.
     */
    public static int compareBooleans(final boolean booleanOne, final boolean booleanTwo) {
        return booleanOne == booleanTwo
            ? 0
            : (booleanOne ? 1 : -1);
    }

    /**
     * Utility method to compare two Booleans.
     * <br>Booleans are ordered as follows:
     * <ol>
     *   <li>FALSE</li>
     *   <li>TRUE</li>
     *   <li>null</li>
     * </ol>
     * @param booleanOne The first boolean.
     * @param booleanTwo The second boolean.
     * @return -1, 0 or 1 (see the following table for details):
     *  <table>
     *  <tr><th>booleanOne</th><th>booleanTwo</th><th>returns</th></tr>
     *  <tr><td>false</td><td>null</td><td>-1</td></tr>
     *  <tr><td>false</td><td>true</td><td>-1</td></tr>
     *  <tr><td>true</td><td>null</td><td>-1</td></tr>
     *  <tr><td>null</td><td>null</td><td>0</td></tr>
     *  <tr><td>false</td><td>false</td><td>0</td></tr>
     *  <tr><td>true</td><td>true</td><td>0</td></tr>
     *  <tr><td>null</td><td>false</td><td>1</td></tr>
     *  <tr><td>null</td><td>true</td><td>1</td></tr>
     *  <tr><td>true</td><td>false</td><td>1</td></tr>
     *  </table>
     */
    public static int compareBooleans(final Boolean booleanOne, final Boolean booleanTwo) {
        return compareBooleans(booleanOne, booleanTwo, true);
    }

    /**
     * Utility method to compare two Booleans.
     * <br>When nullIsHigh == true then Booleans are ordered as follows:
     * <ol>
     *   <li>FALSE</li>
     *   <li>TRUE</li>
     *   <li>null</li>
     * </ol>
     * When nullIsHigh == fale then Booleans are ordered as follows:
     * <ol>
     *   <li>null</li>
     *   <li>FALSE</li>
     *   <li>TRUE</li>
     * </ol>
     * @param booleanOne The first boolean.
     * @param booleanTwo The second boolean.
     * @param nullIsHigh Determines whether a null value is higher than a
     *  non-null value (true) or not (false).
     * @return -1, 0 or 1 (see the following table for details):
     *  <table>
     *  <tr>
     *    <th>booleanOne</th>
     *    <th>booleanTwo</th>
     *    <th>nullIsHigh</th>
     *    <th>returns</th>
     *  </tr>
     *  <tr><td>null</td><td>false</td><td>false</td><td>-1</td></tr>
     *  <tr><td>null</td><td>true</td><td>false</td><td>-1</td></tr>
     *  <tr><td>false</td><td>null</td><td>true</td><td>-1</td></tr>
     *  <tr><td>false</td><td>true</td><td>n/a</td><td>-1</td></tr>
     *  <tr><td>true</td><td>null</td><td>true</td><td>-1</td></tr>
     *  <tr><td>null</td><td>null</td><td>n/a</td><td>0</td></tr>
     *  <tr><td>false</td><td>false</td><td>n/a</td><td>0</td></tr>
     *  <tr><td>true</td><td>true</td><td>n/a</td><td>0</td></tr>
     *  <tr><td>null</td><td>false</td><td>true</td><td>1</td></tr>
     *  <tr><td>null</td><td>true</td><td>true</td><td>1</td></tr>
     *  <tr><td>false</td><td>null</td><td>false</td><td>1</td></tr>
     *  <tr><td>true</td><td>null</td><td>false</td><td>1</td></tr>
     *  <tr><td>true</td><td>false</td><td>n/a</td><td>1</td></tr>
     *  </table>
     */
    public static int compareBooleans(final Boolean booleanOne,final Boolean booleanTwo, final boolean nullIsHigh) {
        int result;

        if (booleanOne != null && booleanTwo != null) {
            result = booleanOne.equals(Boolean.TRUE) ? 1 : -1;
        } else {
            if (booleanOne == null && booleanTwo == null) {
                result = 0;
            } else {
                result = booleanOne == null
                    ? (nullIsHigh ? 1 : -1)
                    : (nullIsHigh ? -1 : 1);
            }
        }

        return result;
    }

    /**
     * Utility method to compare two Integers.
     * <br>Integers are ordered as follows:
     * <ol>
     *   <li>&lt; 0</li>
     *   <li>0</li>
     *   <li>&gt; 0</li>
     *   <li>null</li>
     * </ol>
     * @param integerOne The first integer.
     * @param integerTwo The second integer.
     * @return &lt; 0, 0, &gt; 0 (see the following table for details):
     *  <table>
     *  <tr><th>Pseudo expression</th><th>returns</th></tr>
     *  <tr><td>integerOne &lt; integerTwo</td><td>&lt; 0</td></tr>
     *  <tr><td>integerOne != null and integerTwo == null</td><td>-1</td></tr>
     *  <tr><td>integerOne == integerTwo</td><td>0</td></tr>
     *  <tr><td>integerOne == null and integerTwo != null</td><td>1</td></tr>
     *  <tr><td>integerOne &gt; integerTwo</td><td>&gt; 0</td></tr>
     *  </table>
     *  Note: When both integerOne and integerTwo are non-null then the result
     *  of {@link Integer#compareTo(Integer) integerOne.compareTo(integerTwo)}
     *  is returned.
     */
    public static int compareIntegers(final Integer integerOne, final Integer integerTwo) {
        return compareIntegers(integerOne, integerTwo, true);
    }

    /**
     * Utility method to compare two Integers.
     * <br>When nullIsHigh == true then Integers are ordered as follows:
     * <ol>
     *   <li>&lt; 0</li>
     *   <li>0</li>
     *   <li>&gt; 0</li>
     *   <li>null</li>
     * </ol>
     * When nullIsHigh == false then Integers are ordered as follows:
     * <ol>
     *   <li>null</li>
     *   <li>&lt; 0</li>
     *   <li>0</li>
     *   <li>&gt; 0</li>
     * </ol>
     * @param integerOne The first integer.
     * @param integerTwo The second integer.
     * @param nullIsHigh Determines whether a null value is higher than a
     *  non-null value (true) or not (false).
     * @return &lt; 0, 0, &gt; 0 (see the following table for details):
     *  <table>
     *  <tr><th>Pseudo expression</th><th>nullIsHigh</th><th>returns</th></tr>
     *  <tr>
     *    <td>integerOne == null and integerTwo != null</td>
     *    <td>true</td><td>-1</td>
     *  </tr>
     *  <tr><td>integerOne &lt; integerTwo</td><td>n/a</td><td>&lt; 0</td></tr>
     *  <tr>
     *    <td>integerOne != null and integerTwo == null</td>
     *    <td>true</td><td>-1</td>
     *  </tr>
     *  <tr><td>integerOne == integerTwo</td><td>n/a</td><td>0</td></tr>
     *  <tr>
     *    <td>integerOne == null and integerTwo != null</td>
     *    <td>true</td><td>1</td>
     *  </tr>
     *  <tr><td>integerOne &gt; integerTwo</td><td>n/a</td><td>&gt; 0</td></tr>
     *  <tr>
     *    <td>integerOne != null and integerTwo == null</td>
     *    <td>false</td><td>1</td>
     *  </tr>
     *  </table>
     *  Note: When both integerOne and integerTwo are non-null then the result
     *  of {@link Integer#compareTo(Integer) integerOne.compareTo(integerTwo)}
     *  is returned.
     */
    public static int compareIntegers(final Integer integerOne,final Integer integerTwo, final boolean nullIsHigh) {
        int result;

        if (integerOne != null && integerTwo != null) {
            result = integerOne.compareTo(integerTwo);
        } else {
            if (integerOne == null && integerTwo == null) {
                result = 0;
            } else {
                result = integerOne == null
                    ? (nullIsHigh ? 1 : -1)
                    : (nullIsHigh ? -1 : 1);
            }
        }

        return result;
    }

    /**
     * Utility method to compare two Longs.
     * <br>Longs are ordered as follows:
     * <ol>
     *   <li>&lt; 0</li>
     *   <li>0</li>
     *   <li>&gt; 0</li>
     *   <li>null</li>
     * </ol>
     * @param longOne The first long.
     * @param longTwo The second long.
     * @return &lt; 0, 0, &gt; 0 (see the following table for details):
     *  <table>
     *  <tr><th>Pseudo expression</th><th>returns</th></tr>
     *  <tr><td>longOne &lt; longTwo</td><td>&lt; 0</td></tr>
     *  <tr><td>longOne != null and longTwo == null</td><td>-1</td></tr>
     *  <tr><td>longOne == longTwo</td><td>0</td></tr>
     *  <tr><td>longOne == null and longTwo != null</td><td>1</td></tr>
     *  <tr><td>longOne &gt; longTwo</td><td>&gt; 0</td></tr>
     *  </table>
     *  Note: When both longOne and longTwo are non-null then the result of
     *  {@link Long#compareTo(Long) longOne.compareTo(longTwo)} is returned.
     */
    public static int compareLongs(final Long longOne, final Long longTwo) {
        return compareLongs(longOne, longTwo, true);
    }

    /**
     * Utility method to compare two Longs.
     * <br>When nullIsHigh == true then Longs are ordered as follows:
     * <ol>
     *   <li>&lt; 0</li>
     *   <li>0</li>
     *   <li>&gt; 0</li>
     *   <li>null</li>
     * </ol>
     * When nullIsHigh == false then Longs are ordered as follows:
     * <ol>
     *   <li>null</li>
     *   <li>&lt; 0</li>
     *   <li>0</li>
     *   <li>&gt; 0</li>
     * </ol>
     * @param longOne The first long.
     * @param longTwo The second long.
     * @param nullIsHigh Determines whether a null value is higher than a
     *  non-null value (true) or not (false).
     * @return &lt; 0, 0, &gt; 0 (see the following table for details):
     *  <table>
     *  <tr><th>Pseudo expression</th><th>nullIsHigh</th><th>returns</th></tr>
     *  <tr>
     *    <td>longOne == null and longTwo != null</td><td>true</td><td>-1</td>
     *  </tr>
     *  <tr><td>longOne &lt; longTwo</td><td>n/a</td><td>&lt; 0</td></tr>
     *  <tr>
     *    <td>longOne != null and longTwo == null</td><td>true</td><td>-1</td>
     *  </tr>
     *  <tr><td>longOne == longTwo</td><td>n/a</td><td>0</td></tr>
     *  <tr>
     *    <td>longOne == null and longTwo != null</td><td>true</td><td>1</td>
     *  </tr>
     *  <tr><td>longOne &gt; longTwo</td><td>n/a</td><td>&gt; 0</td></tr>
     *  <tr>
     *    <td>longOne != null and longTwo == null</td><td>false</td><td>1</td>
     *  </tr>
     *  </table>
     *  Note: When both longOne and longTwo are non-null then the result of
     *  {@link Long#compareTo(Long) longOne.compareTo(longTwo)} is returned.
     */
    public static int compareLongs(final Long longOne, final Long longTwo,final boolean nullIsHigh) {
        int result;

        if (longOne != null && longTwo != null) {
            result = longOne.compareTo(longTwo);
        } else {
            if (longOne == null && longTwo == null) {
                result = 0;
            } else {
                result = longOne == null
                    ? (nullIsHigh ? 1 : -1)
                    : (nullIsHigh ? -1 : 1);
            }
        }

        return result;
    }

    /**
     * Utility method to compare two Floats.
     * <br>Floats are ordered as follows:
     * <ol>
     *   <li>&lt; 0</li>
     *   <li>0</li>
     *   <li>&gt; 0</li>
     *   <li>null</li>
     * </ol>
     * @param floatOne The first float.
     * @param floatTwo The second float.
     * @return &lt; 0, 0, &gt; 0 (see the following table for details):
     *  <table>
     *  <tr><th>Pseudo expression</th><th>returns</th></tr>
     *  <tr><td>floatOne &lt; floatTwo</td><td>&lt; 0</td></tr>
     *  <tr><td>floatOne != null and floatTwo == null</td><td>-1</td></tr>
     *  <tr><td>floatOne == floatTwo</td><td>0</td></tr>
     *  <tr><td>floatOne == null and floatTwo != null</td><td>1</td></tr>
     *  <tr><td>floatOne &gt; floatTwo</td><td>&gt; 0</td></tr>
     *  </table>
     *  Note: When both floatOne and floatTwo are non-null then the result of
     *  {@link Float#compareTo(Float) floatOne.compareTo(floatTwo)} is returned.
     */
    public static int compareFloats(final Float floatOne, final Float floatTwo) {
        return compareFloats(floatOne, floatTwo, true);
    }

    /**
     * Utility method to compare two Floats.
     * <br>When nullIsHigh == true then Floats are ordered as follows:
     * <ol>
     *   <li>&lt; 0</li>
     *   <li>0</li>
     *   <li>&gt; 0</li>
     *   <li>null</li>
     * </ol>
     * When nullIsHigh == false then Floats are ordered as follows:
     * <ol>
     *   <li>null</li>
     *   <li>&lt; 0</li>
     *   <li>0</li>
     *   <li>&gt; 0</li>
     * </ol>
     * @param floatOne The first long.
     * @param floatTwo The second long.
     * @param nullIsHigh Determines whether a null value is higher than a
     *  non-null value (true) or not (false).
     * @return &lt; 0, 0, &gt; 0 (see the following table for details):
     *  <table>
     *  <tr><th>Pseudo expression</th><th>nullIsHigh</th><th>returns</th></tr>
     *  <tr>
     *    <td>floatOne == null and floatTwo != null</td><td>true</td><td>-1</td>
     *  </tr>
     *  <tr><td>floatOne &lt; floatTwo</td><td>n/a</td><td>&lt; 0</td></tr>
     *  <tr>
     *    <td>floatOne != null and floatTwo == null</td><td>true</td><td>-1</td>
     *  </tr>
     *  <tr><td>floatOne == floatTwo</td><td>n/a</td><td>0</td></tr>
     *  <tr>
     *    <td>floatOne == null and floatTwo != null</td><td>true</td><td>1</td>
     *  </tr>
     *  <tr><td>floatOne &gt; floatTwo</td><td>n/a</td><td>&gt; 0</td></tr>
     *  <tr>
     *    <td>floatOne != null and floatTwo == null</td><td>false</td><td>1</td>
     *  </tr>
     *  </table>
     *  Note: When both floatOne and floatTwo are non-null then the result of
     *  {@link Float#compareTo(Float) floatOne.compareTo(floatTwo)} is returned.
     */
    public static int compareFloats(final Float floatOne, final Float floatTwo,final boolean nullIsHigh) {
        int result;

        if (floatOne != null && floatTwo != null) {
            result = floatOne.compareTo(floatTwo);
        } else {
            if (floatOne == null && floatTwo == null) {
                result = 0;
            } else {
                result = floatOne == null
                    ? (nullIsHigh ? 1 : -1)
                    : (nullIsHigh ? -1 : 1);
            }
        }

        return result;
    }

    /**
     * Utility method to compare two Doubles.
     * <br>Doubles are ordered as follows:
     * <ol>
     *   <li>&lt; 0</li>
     *   <li>0</li>
     *   <li>&gt; 0</li>
     *   <li>null</li>
     * </ol>
     * @param doubleOne The first double.
     * @param doubleTwo The second double.
     * @return &lt; 0, 0, &gt; 0 (see the following table for details):
     *  <table>
     *  <tr><th>Pseudo expression</th><th>returns</th></tr>
     *  <tr><td>doubleOne &lt; doubleTwo</td><td>&lt; 0</td></tr>
     *  <tr><td>doubleOne != null and doubleTwo == null</td><td>-1</td></tr>
     *  <tr><td>doubleOne == doubleTwo</td><td>0</td></tr>
     *  <tr><td>doubleOne == null and doubleTwo != null</td><td>1</td></tr>
     *  <tr><td>doubleOne &gt; doubleTwo</td><td>&gt; 0</td></tr>
     *  </table>
     *  Note: When both doubleOne and doubleTwo are non-null then the result of
     *  {@link Double#compareTo(Double) doubleOne.compareTo(doubleTwo)} is
     *  returned.
     */
    public static int compareDoubles(final Double doubleOne, final Double doubleTwo) {
        return compareDoubles(doubleOne, doubleTwo, true);
    }

    /**
     * Utility method to compare two Doubles.
     * <br>When nullIsHigh == true then Doubles are ordered as follows:
     * <ol>
     *   <li>&lt; 0</li>
     *   <li>0</li>
     *   <li>&gt; 0</li>
     *   <li>null</li>
     * </ol>
     * When nullIsHigh == false then Doubles are ordered as follows:
     * <ol>
     *   <li>null</li>
     *   <li>&lt; 0</li>
     *   <li>0</li>
     *   <li>&gt; 0</li>
     * </ol>
     * @param doubleOne The first long.
     * @param doubleTwo The second long.
     * @param nullIsHigh Determines whether a null value is higher than a
     *  non-null value (true) or not (false).
     * @return &lt; 0, 0, &gt; 0 (see the following table for details):
     *  <table>
     *  <tr><th>Pseudo expression</th><th>nullIsHigh</th><th>returns</th></tr>
     *  <tr>
     *    <td>doubleOne == null and doubleTwo != null</td>
     *    <td>true</td><td>-1</td>
     *  </tr>
     *  <tr><td>doubleOne &lt; doubleTwo</td><td>n/a</td><td>&lt; 0</td></tr>
     *  <tr>
     *    <td>doubleOne != null and doubleTwo == null</td>
     *    <td>true</td><td>-1</td>
     *  </tr>
     *  <tr><td>doubleOne == doubleTwo</td><td>n/a</td><td>0</td></tr>
     *  <tr>
     *    <td>doubleOne == null and doubleTwo != null</td>
     *    <td>true</td><td>1</td>
     *  </tr>
     *  <tr><td>doubleOne &gt; doubleTwo</td><td>n/a</td><td>&gt; 0</td></tr>
     *  <tr>
     *    <td>doubleOne != null and doubleTwo == null</td>
     *    <td>false</td><td>1</td>
     *  </tr>
     *  </table>
     *  Note: When both doubleOne and doubleTwo are non-null then the result of
     *  {@link Double#compareTo(Double) doubleOne.compareTo(doubleTwo)} is
     *  returned.
     */
    public static int compareDoubles(final Double doubleOne, final Double doubleTwo,final boolean nullIsHigh) {
        int result;

        if (doubleOne != null && doubleTwo != null) {
            result = doubleOne.compareTo(doubleTwo);
        } else {
            if (doubleOne == null && doubleTwo == null) {
                result = 0;
            } else {
                result = doubleOne == null
                    ? (nullIsHigh ? 1 : -1)
                    : (nullIsHigh ? -1 : 1);
            }
        }

        return result;
    }

    /**
     * Utility method to compare two Strings.
     * <br>Null values are ordered last.
     * @param stringOne The first string.
     * @param stringTwo The second string.
     * @return &lt; 0, 0, &gt; 0 (see the following table for details):
     *  <table>
     *  <tr><th>Pseudo expression</th><th>returns</th></tr>
     *  <tr><td>stringOne &lt; stringTwo</td><td>&lt; 0</td></tr>
     *  <tr><td>stringOne != null and stringTwo == null</td><td>-1</td></tr>
     *  <tr><td>stringOne == stringTwo</td><td>0</td></tr>
     *  <tr><td>stringOne == null and stringTwo != null</td><td>1</td></tr>
     *  <tr><td>stringOne &gt; stringTwo</td><td>&gt; 0</td></tr>
     *  </table>
     *  Note: When both stringOne and stringTwo are non-null then the result of
     *  {@link String#compareTo(String) stringOne.compareTo(stringTwo)} is
     *  returned.
     */
    public static int compareStrings(final String stringOne, final String stringTwo) {
        return compareStrings(stringOne, stringTwo, true);
    }

    /**
     * Utility method to compare two Strings.
     * <br>Null values are ordered last when nullIsHigh == true and null values
     * are ordered first when nullIsHigh == false.
     * @param stringOne The first string.
     * @param stringTwo The second string.
     * @param nullIsHigh Determines whether a null value is higher than a
     *  non-null value (true) or not (false).
     * @return &lt; 0, 0, &gt; 0 (see the following table for details):
     *  <table>
     *  <tr><th>Pseudo expression</th><th>nullIsHigh</th><th>returns</th></tr>
     *  <tr>
     *    <td>stringOne == null and stringTwo != null</td>
     *    <td>true</td><td>-1</td>
     *  </tr>
     *  <tr><td>stringOne &lt; stringTwo</td><td>n/a</td><td>&lt; 0</td></tr>
     *  <tr>
     *    <td>stringOne != null and stringTwo == null</td>
     *    <td>true</td><td>-1</td>
     *  </tr>
     *  <tr><td>stringOne == stringTwo</td><td>n/a</td><td>0</td></tr>
     *  <tr>
     *    <td>stringOne == null and stringTwo != null</td>
     *    <td>true</td><td>1</td>
     *  </tr>
     *  <tr><td>stringOne &gt; stringTwo</td><td>n/a</td><td>&gt; 0</td></tr>
     *  <tr>
     *    <td>stringOne != null and stringTwo == null</td>
     *    <td>false</td><td>1</td>
     *  </tr>
     *  </table>
     *  Note: When both stringOne and stringTwo are non-null then the result of
     *  {@link String#compareTo(String) stringOne.compareTo(stringTwo)} is
     *  returned.
     */
    public static int compareStrings(final String stringOne, final String stringTwo,final boolean nullIsHigh) {
        int result;

        if (stringOne != null && stringTwo != null) {
            result = stringOne.compareTo(stringTwo);
        } else {
            if (stringOne == null && stringTwo == null) {
                result = 0;
            } else {
                result = stringOne == null
                    ? (nullIsHigh ? 1 : -1)
                    : (nullIsHigh ? -1 : 1);
            }
        }

        return result;
    }

    /**
     * Utility method to compare two Dates.
     * <br>Null values are ordered last.
     * @param dateOne The first date.
     * @param dateTwo The second date.
     * @return &lt; 0, 0, &gt; 0 (see the following table for details):
     *  <table>
     *  <tr><th>Pseudo expression</th><th>returns</th></tr>
     *  <tr><td>dateOne &lt; dateTwo</td><td>&lt; 0</td></tr>
     *  <tr><td>dateOne != null and dateTwo == null</td><td>-1</td></tr>
     *  <tr><td>dateOne == dateTwo</td><td>0</td></tr>
     *  <tr><td>dateOne == null and dateTwo != null</td><td>1</td></tr>
     *  <tr><td>dateOne &gt; dateTwo</td><td>&gt; 0</td></tr>
     *  </table>
     *  Note: When both dateOne and dateTwo are non-null then the result of
     *  {@link Date#compareTo(Date) dateOne.compareTo(dateTwo)} is returned.
     */
    public static int compareDates(final Date dateOne, final Date dateTwo) {
        return compareDates(dateOne, dateTwo, true);
    }

    /**
     * Utility method to compare two Dates.
     * <br>Null values are ordered last when nullIsHigh == true and null values
     * are ordered first when nullIsHigh == false.
     * @param dateOne The first date.
     * @param dateTwo The second date.
     * @param nullIsHigh Determines whether a null value is higher than a
     *  non-null value (true) or not (false).
     * @return &lt; 0, 0, &gt; 0 (see the following table for details):
     *  <table>
     *  <tr><th>Pseudo expression</th><th>nullIsHigh</th><th>returns</th></tr>
     *  <tr>
     *    <td>dateOne == null and dateTwo != null</td>
     *    <td>true</td><td>-1</td>
     *  </tr>
     *  <tr><td>dateOne &lt; dateTwo</td><td>n/a</td><td>&lt; 0</td></tr>
     *  <tr>
     *    <td>dateOne != null and dateTwo == null</td>
     *    <td>true</td><td>-1</td>
     *  </tr>
     *  <tr><td>dateOne == dateTwo</td><td>n/a</td><td>0</td></tr>
     *  <tr>
     *    <td>dateOne == null and dateTwo != null</td>
     *    <td>true</td><td>1</td>
     *  </tr>
     *  <tr><td>dateOne &gt; dateTwo</td><td>n/a</td><td>&gt; 0</td></tr>
     *  <tr>
     *    <td>dateOne != null and dateTwo == null</td>
     *    <td>false</td><td>1</td>
     *  </tr>
     *  </table>
     *  Note: When both dateOne and dateTwo are non-null then the result of
     *  {@link Date#compareTo(Date) dateOne.compareTo(dateTwo)} is returned.
     */
    public static int compareDates(final Date dateOne, final Date dateTwo,final boolean nullIsHigh) {
        int result;

        if (dateOne != null && dateTwo != null) {
            result = dateOne.compareTo(dateTwo);
        } else {
            if (dateOne == null && dateTwo == null) {
                result = 0;
            } else {
                result = dateOne == null
                    ? (nullIsHigh ? 1 : -1)
                    : (nullIsHigh ? -1 : 1);
            }
        }

        return result;
    }

    /**
     * Gets an OrderByField by its ID.
     * @param id ID of the OrderByField to get.
     * @return The allowed OrderByField with the supplied id.
     *  <br>When the supplied id corresponds to a field that is currently used
     *  in the comparison than this field is returned. Otherwise a field is
     *  returned out of the array of allowed order-by-fields.
     * @throws IllegalArgumentException When no OrderByField with the supplied
     *  id exists.
     */
    public OrderByField getOrderByField(final String id) {
        // Check order-by-fields currently used in this comparator
        for (int i = 0; i < orderByFields.length; ++i) {
            if (orderByFields[i].id.equals(id)) {
                return orderByFields[i];
            }
        }

        // Check the array of allowed order-by-fields.
        for (int i = 0; i < allowedOrderByFields.length; ++i) {
            if (allowedOrderByFields[i].id.equals(id)) {
                return allowedOrderByFields[i];
            }
        }

        throw new IllegalArgumentException(
            "No OrderByField with id=" + id + ", exists!");
    }

    /**
     * Gets all the fields used in the comparison.
     * @return The order-by-fields used in the comparison.
     *  <br>This method will never return null.
     */
    public OrderByField [] getOrderByFields() {
        return orderByFields;
    }

    /**
     * Sets all the fields used in the comparison.
     * @param orderByFields Fields used to order the collection.
     * @throws NullPointerException When the supplied orderByFields is null.
     * @throws IllegalArgumentException When the supplied orderByFields array is
     *  empty.
     */
    public void setOrderByFields(final OrderByField [] orderByFields) {
        CDebug.checkParameterNotNull(orderByFields, "orderByFields");
        CDebug.checkParameterTrue(orderByFields.length > 0, "Can't have an empty array of orderByFields!");

        this.orderByFields = cloneOrderByFields(orderByFields);
    }

    /**
     * Sets a single field to be used in the comparison.
     * @param orderByField Field used to order the collection.
     * @throws NullPointerException When the supplied orderByField is null.
     */
    public void setOrderByField(final OrderByField orderByField) {
        CDebug.checkParameterNotNull(orderByField, "orderByField");

        this.orderByFields = new OrderByField [] {new OrderByField(orderByField)};
    }

    /**
     * Convenience method to get the locale.
     * <br>The locale can be used in concrete subclasses to aid in comparing
     * localized descriptions for constants for example.
     * @return The locale or null when the locale has not been set.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Convenience method to set the locale.
     * <br>The locale can be used in concrete subclasses to aid in comparing
     * localized descriptions for constants for example.
     * @param locale The locale to use in concrete subclasses.
     */
    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    /**
     * Creates a String representation of this comparator.
     * @return String representation of this comparator.
     */
    public String toString() {
        final StringBuffer result = new StringBuffer("Sort on:");
        result.append(CString.arrayToString(orderByFields));

        return result.toString();
    }

    /**
     * Creates a clone of the supplied orderByFields array.
     * @param orderByFields Array of OrderByField objects.
     * @return Clone of the supplied orderByFields.
     */
    private OrderByField [] cloneOrderByFields(final OrderByField [] orderByFields) {
        CDebug.checkParameterNotNull(orderByFields, "orderByFields");

        final OrderByField [] result = new OrderByField[orderByFields.length];
        for (int i = 0; i < orderByFields.length; ++i) {
            result[i] = new OrderByField(orderByFields[i]);
        }

        return result;
    }

    /**
     * A field that can be used in a comparison.
     * @author Kees Schotanus
     * 
     */
    public static class OrderByField {

        /**
         * ID of this order-by-field.
         */
        private String id;

        /**
         * Compare ascending (true) or descending (false).
         */
        private boolean ascending;


        /**
         * Copy constructor.
         * @param orderByField The object to copy information from.
         */
        public OrderByField(final OrderByField orderByField) {
            CDebug.checkParameterNotNull(orderByField, "orderByField");

            this.id = orderByField.id;
            this.ascending = orderByField.ascending;
        }

        /**
         * Constructs a single order-by-field that will be ordered ascending by
         * default.
         * @param id ID of this order-by-field.
         */
        public OrderByField(final String id) {
            this(id, true);
        }

        /**
         * Constructs a single order-by-field.
         * @param id ID of this order-by-field.
         * @param ascending True when ascending, false when descending.
         */
        public OrderByField(final String id, final boolean ascending) {
            this.id = id;
            this.ascending = ascending;
        }

        /**
         * Gets the ID of this order-by-field.
         * @return ID of this order-by-field.
         */
        public String getID() {
           return id;
        }

        /**
         * Gets the ordering for this field.
         * @return True when ascending, false when descending.
         */
        public boolean isAscending() {
            return ascending;
        }

        /**
         * Sets the ordering of this field to ascending or descending.
         * @param ascending True to order ascending, false to order descending.
         */
        public void setAscending(final boolean ascending) {
            this.ascending = ascending;
        }

        /**
         * Determines whether this object and the supplied object are the same.
         * <br>The two objects are the same if the supplied object is not null
         * and the supplied object is an OrderByField and the stored id in both
         * objects is the same.
         * @param object The OrderByField to compare with.
         * @return True if both objects are the same (contain the same id),
         *  false otherwise.
         */
        public boolean equals(final Object object) {
            return object instanceof OrderByField
                && id.equals(((OrderByField)object).id);
        }

        /**
         * Simplistic hashCode method.
         * @return A hash code value for this object.
         *  <br>Actually the hashCode of the stored id is returned.
         */
        public int hashCode() {
            return id.hashCode();
        }

        /**
         * Creates a String representation of this order-by-field.
         * @return String representation of this order-by-field.
         */
        public String toString() {
            return "OrderByField:ID=" + getID()
                + (ascending ? " (ascending)" : " (descending)");
        }
    }

}

