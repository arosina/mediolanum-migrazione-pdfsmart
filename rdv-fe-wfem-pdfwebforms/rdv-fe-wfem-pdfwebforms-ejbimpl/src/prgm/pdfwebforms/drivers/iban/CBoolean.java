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

import java.io.Serializable;


/**
 * This class wraps a primitive boolean value.
 * <br>The main difference between this class and class
 * {@link java.lang.Boolean} is that this class is mutable. No attempt is made
 * to add functionality to this class that is already present in class
 * {@link java.lang.Boolean} unless this functionality was absolutely
 * necessary.<br>
 * For performance reasons I decided to not make this class thread-safe! If you
 * want this functionality feel free to extend this class and add
 * synchronization.
 * @author Kees Schotanus
 * 
 */
public class CBoolean implements Comparable, Serializable {

    /**
     * The wrapped boolean value.
     * @serial
     */
    private boolean value;

    /**
     * Serial version ID.
     */
    private static final long serialVersionUID = 4307566374129995668L;

    /**
     * Constructs a new object with an initial value of false.
     */
    public CBoolean() {
        value = false;
    }

    /**
     * Constructs a new object from the supplied primitive boolean.
     * @param value Primitive boolean value.
     */
    public CBoolean(final boolean value) {
        this.value = value;
    }

    /**
     * Constructs a new object from the supplied Boolean.
     * @param object Boolean from which the boolean value is taken to initialize
     *  this object.
     * @throws NullPointerException When the supplied object is null.
     */
    public CBoolean(final Boolean object) {
        CDebug.checkParameterNotNull(object, "object");

        this.value = object.booleanValue();
    }

    /**
     * Copy constructor, creates a new object from the supplied object.
     * @param object CBoolean object which value will be copied into this
     *  object.
     * @throws NullPointerException When the supplied object is null.
     */
    public CBoolean(final CBoolean object) {
        CDebug.checkParameterNotNull(object, "object");

        this.value = object.value;
    }

    /**
     * Retrieves the boolean value of this object.
     * @return Primitive boolean value this object stores.
     */
    public boolean getValue() {
        return value;
    }

    /**
     * Sets the value of the stored boolean to the supplied value.
     * @param value Value that must be stored.
     */
    public void setValue(final boolean value) {
        this.value = value;
    }

    /**
     * Computes the hashcode value for this object.
     * @return 1963 when the stored boolean is true, -1961 when the stored
     *  boolean is false.
     */
    public int hashCode() {
        return value ? 1963 : -1961;
    }

    /**
     * Determines whether this object and the supplied object are the same.
     * <br>The two objects are the same if the supplied object is not null and
     * the supplied object is a CBoolean and the stored value in both objects
     * is the same.
     * @param object The CBoolean object to compare with.
     * @return True if both objects are the same, false otherwise.
     */
    public boolean equals(final Object object) {
        return object instanceof CBoolean && value == ((CBoolean)object).value;
    }

    /**
     * Determines whether this object and the supplied object are the same.
     * <br>The two objects are the same if the supplied object is not null and
     * the value in both objects is the same.
     * @param object The CBoolean object to compare with.
     * @return True if both objects are the same, false otherwise.
     */
    public boolean equals(final CBoolean object) {
        return object != null && value == object.value;
    }

    /**
     * Compares two CBoolean objects numerically.
     * @param object The CBoolean object to compare with this object.
     * @return -1 when this value is false and the other object's value is true,
     *  0 when this value equals the other object's value,
     *  1 when this value is true and the other object's value is false.
     * @throws ClassCastException When the supplied object is not of type
     *  CBoolean.
     * @throws NullPointerException When the supplied object is null.
     * @see Comparable
     */
    public int compareTo(final Object object) {
        CDebug.checkParameterNotNull(object, "object");

        return compareTo((CBoolean)object);
    }

    /**
     * Compares two CBoolean objects numerically.
     * @param object CBoolean object to compare with this object.
     * @return -1 when this value is false and the other object's value is true,
     *  0 when this value equals the other object's value,
     *  1 when this value is true and the other object's value is false.
     * @throws NullPointerException When the supplied object is null.
     * @see Comparable
     */
    public int compareTo(final CBoolean object) {
        CDebug.checkParameterNotNull(object, "object");

        return value == object.value ? 0 : value ? 1 : -1;
    }

    /**
     * Parses the supplied String value as a boolean.
     * @param value String value to parse as a boolean.
     * @return True when the supplied value is "true" (case insensitive) or "1",
     *  false when the supplied value is "false" (case insensitive) or "0".
     * @throws IllegalArgumentException When the supplied value is null, empty
     *  or not in the set [0, 1, false, true].
     */
    public static boolean parseBoolean(final String value) {
        if (value == null) {
            throw new IllegalArgumentException(
                "Can't convert null to a boolean");
        } else if (value.trim().length() == 0) {
            throw new IllegalArgumentException(
                "Can't convert empty String to a boolean");
        }

        final String valueLower = value.trim().toLowerCase();
        if ("true".equals(valueLower) || "1".equals(valueLower)) {
            return true;
        } else if (valueLower.equals("false") || valueLower.equals("0")) {
            return false;
        } else {
            final StringBuffer message = new StringBuffer();
            message.append("Invalid boolean value:").append(value);
            message.append(", should be one of [0, false, 1, true].");
            throw new IllegalArgumentException(message.toString());
        }
    }

    /**
     * Converts the stored primitive boolean value to a String.
     * @return String representation of the stored value.
     */
    public String toString() {
        return String.valueOf(value);
    }

}

