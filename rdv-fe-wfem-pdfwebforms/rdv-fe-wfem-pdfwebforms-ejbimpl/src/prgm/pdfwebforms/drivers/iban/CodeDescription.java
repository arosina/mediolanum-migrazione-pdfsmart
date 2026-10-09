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
import java.util.Comparator;


/**
 * Abstraction of an immutable code and description pair.
 * <br>A collection of these code and description pairs could be used to fill a
 * combo box for example.
 * @author Kees Schotanus.
 * 
 */
public class CodeDescription implements Serializable {

    /**
     * Code.
     */
    private final Object code;

    /**
     * Description field.
     */
    private final String description;

    /**
     * Constructs a code and description pair from the supplied code and
     * description.
     * @param code The code.
     * @param description The description.
     * @throws NullPointerException When the supplied description is null.
     */
    public CodeDescription(final Object code, final String description) {
        CDebug.checkParameterNotNull(description, "description");

        this.code = code;
        this.description = description;
    }

    /**
     * Gets the code.
     * @return Code.
     */
    public Object getCode() {
        return code;
    }

    /**
     * Gets the description.
     * @return Description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Creates a comparator that sorts on description, ascending.
     * @return Comparator that sorts on description, ascending.
     */
    public static CodeDescriptionComparator createComparator() {
        return new CodeDescriptionComparator(true, true);
    }

    /**
     * Creates a comparator that sorts on either code or description in
     * ascending order.
     * @param sortOnDescription True to sort on description, false to sort on
     *  code.
     * @return Comparator that sorts on either code or description in
     *  ascending order.
     */
    public static CodeDescriptionComparator createComparator(
            final boolean sortOnDescription) {
        return new CodeDescriptionComparator(sortOnDescription, true);
    }

    /**
     * Creates a comparator that sorts on either code or description, either
     * ascending or descending.
     * @param sortOnDescription True to sort on description, false to sort on
     *  code.
     * @param ascending True to sort ascending, false to sort descending.
     * @return Comparator that that sorts on either code or description, either
     *  ascending or descending.
     */
    public static CodeDescriptionComparator createComparator(
            final boolean sortOnDescription, final boolean ascending) {
        return new CodeDescriptionComparator(sortOnDescription, ascending);
    }

    /**
     * Creates a String representation of this code description pair.
     * <br>Since this method could be used from within a Swing component it
     * simply returns the description.
     * @return Description for this code description pair.
     */
    public String toString() {
        return description;
    }

    /**
     * Comparator for {@link CodeDescription} objects.
     * <br>This single comparator class allows for sorting on code and
     * description, either ascending or descending.
     * @author Martijn Baels.
     * @author Kees Schotanus.
     * 
     */
    private static class CodeDescriptionComparator implements Comparator {

        /**
         * Sorts on description (true) or on code (false).
         */
        private boolean sortOnDescription;

        /**
         * Sorts ascending (true) or descending (false).
         */
        private boolean ascending;

        /**
         * Default constructor that sorts {@link CodeDescription} objects on
         * description in ascending order.
         */
        CodeDescriptionComparator() {
            this(true, true);
        }

        /**
         * Constructor that sorts {@link CodeDescription} objects on code or
         * description in ascending order.
         * @param sortOnDescription True to sort on description, false to sort
         *  on code.
         */
        CodeDescriptionComparator(final boolean sortOnDescription) {
            this(sortOnDescription, true);
        }

        /**
         * Constructor that sorts {@link CodeDescription} objects on description
         * or code, either ascending or descending.
         * @param sortOnDescription True to sort on description, false to sort
         *  on code.
         * @param ascending True to sort ascending, false to sort descending.
         */
        CodeDescriptionComparator(
                final boolean sortOnDescription, final boolean ascending) {
            this.sortOnDescription = sortOnDescription;
            this.ascending = ascending;
        }

        /**
         * Compares the supplied {@link CodeDescription} objects.
         * @param objectOne The first {@link CodeDescription} object.
         * @param objectTwo The second {@link CodeDescription} object.
         * @return -1 if the first {@link CodeDescription} is before the second
         *  one, zero if both objects are equal and 1 if the first
         *  {@link CodeDescription} is after the second one.
         * @throws ClassCastException When one of the objects to compare is not
         *  of type {@link CodeDescription}.
         * @throws NullPointerException When either the supplied objectOne or
         *  objectTwo is null.
         */
        public int compare(final Object objectOne, final Object objectTwo) {
            CDebug.checkParameterNotNull(objectOne, "objectOne");
            CDebug.checkParameterNotNull(objectTwo, "objectTwo");

            CodeDescription codeDescriptionOne = (CodeDescription)objectOne;
            CodeDescription codeDescriptionTwo = (CodeDescription)objectTwo;

            int result;
            if (sortOnDescription) {
                result = codeDescriptionOne.getDescription().compareTo(
                    codeDescriptionTwo.getDescription());
            } else {
                final Object codeOne = codeDescriptionOne.getCode();
                final Object codeTwo = codeDescriptionTwo.getCode();
                if (codeOne instanceof Comparable
                        && codeTwo instanceof Comparable) {
                    result = ((Comparable)codeOne).compareTo(codeTwo);
                } else {
                    result = AbstractComparator.compareStrings(
                        codeOne == null ? null : codeOne.toString(),
                        codeTwo == null ? null : codeTwo.toString());
                }
            }
            return result = ascending ? result : -result;
        }

        /**
         * Creates a String representation of this comparator.
         * @return String representation of this comparator.
         */
        public String toString() {
            final StringBuffer result = new StringBuffer("Sort on:");
            if (sortOnDescription) {
                result.append("description");
            } else {
                result.append("code");
            }
            if (ascending) {
                result.append(" (ascending)");
            } else {
                result.append(" (descending)");
            }
            return result.toString();
        }
    }
}