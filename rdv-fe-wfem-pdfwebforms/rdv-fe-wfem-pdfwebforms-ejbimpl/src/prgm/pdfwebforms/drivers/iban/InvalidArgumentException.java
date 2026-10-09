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


/**
 * Exception that signals that an argument passed to a method is not valid.
 * <br>Resembles {@link IllegalArgumentException} but this is a checked and
 * localizable exception.
 * @author Kees Schotanus
 * @author Martijn Baels
 * 
 */
public class InvalidArgumentException extends AbstractLocalizedException {

    /**
     * Default constructor creates an exception with a standard, non localized
     * message.
     */
    public InvalidArgumentException() {
        super("Invalid argument.");
    }

    /**
     * Constructs an exception using the supplied message.
     * <br>The message may be localized but in this case it is up to the
     * programmer to supply the correct localized message.
     * @param message Message with additional information about why the argument
     *  is invalid.
     */
    public InvalidArgumentException(final String message) {
        super(message);
    }

    /**
     * Constructs a localized exception from the supplied
     * {@link java.util.ResourceBundle} name and the supplied message key.
     * @param resourceBundleName The name of the
     *  {@link java.util.ResourceBundle} that contains the localized messages.
     * @param messageKey Key to the resource bundle to retrieve the localized
     *  message.
     * @throws NullPointerException When the supplied resourceBundleName or
     *  messageKey is null.
     * @throws IllegalArgumentException When the supplied resourceBundleName or
     *  messageKey is empty.
     */
    public InvalidArgumentException(
            final String resourceBundleName, final String messageKey) {

        super(resourceBundleName, messageKey);
    }

    /**
     * Constructs a localized exception from the supplied
     * {@link java.util.ResourceBundle} name and the supplied message key and
     * optional arguments.
     * <br>Use this constructor when your message contains one or more
     * arguments.
     * @param resourceBundleName The name of the
     *  {@link java.util.ResourceBundle} that contains the localized messages.
     * @param messageKey Key to the resource bundle to retrieve the localized
     *  message.
     * @param messageArguments The message arguments that will be inserted into
     *  the localized message.
     * @throws NullPointerException When the supplied resourceBundleName or
     *  messageKey is null.
     * @throws IllegalArgumentException When the supplied resourceBundleName or
     *  messageKey is empty.
     */
    public InvalidArgumentException(
            final String resourceBundleName,
            final String messageKey,
            final Object [] messageArguments) {

        super(resourceBundleName, messageKey, messageArguments);
    }

}