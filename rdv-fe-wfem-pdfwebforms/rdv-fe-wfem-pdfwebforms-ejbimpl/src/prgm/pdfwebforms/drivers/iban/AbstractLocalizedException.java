/* 
 * Copyright (C) 2002 Martijn Baels
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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

//import org.apache.log4j.Logger;


/**
 * Ultimate base class for exceptions that are capable of showing localized
 * messages.
 * <br>Extend this class to add localizable messages to your own exceptions.
 * <br>Note: This class is a copy of {@link AbstractLocalizedRuntimeException}
 * except that this class extends Exception.
 * @author Martijn Baels
 * @author Kees Schotanus
 * 
 */
public abstract class AbstractLocalizedException extends Exception {

    /**
     * Logger for this class.
     */
    //private static final Logger log = Logger.getLogger(AbstractLocalizedException.class);

    /**
     * Name of the ResourceBundle that contains the localized messages.
     * <br>When this name is null than no localized message can be retrieved.
     */
    private String resourceBundleName;

    /**
     * Resource bundle key to retrieve a localized message.
     * <br>A null key indicates that no localized message can be retrieved.
     */
    private String messageKey;

    /**
     * Optional message arguments that will be inserted into the localized
     * message.
     * <br>Even though the arguments are optional, this array will never be
     * null!
     */
    private Object [] messageArguments;

    /**
     * The exception that caused this exception.
     * <br>May be null when this exception is not caused by another exception.
     */
    private Throwable cause;


    /**
     * Default constructor that creates an exception without a message.
     */
    public AbstractLocalizedException() {
        super();
    }

    /**
     * Constructs an exception using the supplied message.
     * <br>The message may be localized but in this case it is up to the
     * programmer to supply the correct localized message.
     * @param message Message indicating why the exception occurred.
     */
    public AbstractLocalizedException(final String message) {
        this(message, (Throwable)null);
    }

    /**
     * Constructs an exception using the supplied message.
     * <br>The message may be localized but in this case it is up to the
     * programmer to supply the correct localized message.
     * @param message Message indicating why the exception occurred.
     * @param cause Exception that caused this exception.
     *  <br>May be null in which case this exception is to be considered a stand
     *  alone exception that was not caused by another exception.
     */
    public AbstractLocalizedException(final String message,
            final Throwable cause) {
        super(message);
        this.cause = cause;
    }

    /**
     * Constructs a localized exception from the supplied {@link ResourceBundle}
     * name and the supplied message key.
     * @param resourceBundleName The name of the {@link ResourceBundle}
     *  that contains the localized messages.
     * @param messageKey Key to the resource bundle to retrieve the localized
     *  message.
     * @throws NullPointerException When the supplied resourceBundleName or
     *  messageKey is null.
     * @throws IllegalArgumentException When the supplied resourceBundleName or
     *  messageKey is empty.
     */
    public AbstractLocalizedException(
            final String resourceBundleName, final String messageKey) {
        this(resourceBundleName, messageKey, null);
    }

    /**
     * Constructs a localized exception from the supplied {@link ResourceBundle}
     * name and the supplied message key and optional arguments.
     * <br>Use this constructor when your message contains one or more
     * arguments.
     * @param resourceBundleName The name of the {@link ResourceBundle}
     *  that contains the localized messages.
     * @param messageKey Key to the resource bundle to retrieve the localized
     *  message.
     * @param messageArguments The message arguments that will be inserted into
     *  the localized message.
     * @throws NullPointerException When the supplied resourceBundleName or
     *  messageKey is null.
     * @throws IllegalArgumentException When the supplied resourceBundleName or
     *  messageKey is empty.
     */
    public AbstractLocalizedException(
            final String resourceBundleName,
            final String messageKey,
            final Object [] messageArguments) {
        this(resourceBundleName, messageKey, messageArguments, null);
    }

    /**
     * Constructs a localized exception from the supplied {@link ResourceBundle}
     * name, the supplied message key, optional arguments and optional cause.
     * <br>Use this constructor when your message contains one or more
     * arguments or when this exception is caused by another exception.
     * @param resourceBundleName The name of the {@link ResourceBundle}
     *  that contains the localized messages.
     * @param messageKey Key to the resource bundle to retrieve the localized
     *  message.
     * @param messageArguments The message arguments that will be inserted into
     *  the localized message.
     * @param cause Exception that caused this exception.
     *  <br>May be null in which case this exception is to be considered a stand
     *  alone exception that was not caused by another exception.
     * @throws NullPointerException When the supplied resourceBundleName or
     *  messageKey is null.
     * @throws IllegalArgumentException When the supplied resourceBundleName or
     *  messageKey is empty.
     */
    public AbstractLocalizedException(
            final String resourceBundleName,
            final String messageKey,
            final Object [] messageArguments,
            final Throwable cause) {
        CDebug.checkParameterNotEmpty(resourceBundleName, "resourceBundleName");
        CDebug.checkParameterNotEmpty(messageKey, "messageKey");

        this.resourceBundleName = resourceBundleName;
        this.messageKey = messageKey;

        if (messageArguments != null) {
            this.messageArguments = messageArguments;
        } else {
            this.messageArguments = new Object [] {};
        }

        this.cause = cause;
    }

    /**
     * Gets the resource bundle name containing the localized messages.
     * @return Resource bundle name containing the localized messages or null
     *  when no resource bundle has been supplied at construction time.
     */
    public String getResourceBundleName() {
        return resourceBundleName;
    }

    /**
     * Gets the key to the resource bundle to retrieve a localized message.
     * @return The key to the resource bundle to retrieve a localized message.
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * Gets the optional message arguments.
     * @return The optional message arguments.
     *  <br>In the absence of message arguments an empty array will be returned,
     *  hence this method <b>never</b> returns null.
     */
    public Object [] getMessageArguments() {
        return messageArguments;
    }

    /**
     * Gets the exception that caused this exception.
     * @return The exception that caused this exception or null when this
     *  exception was not caused by another exception.
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * Gets the localized message for the default locale.
     * @return The localized message for the default locale.
     */
    public String getMessage() {
        return getMessage(Locale.getDefault());
    }

    /**
     * Gets the localized message for the supplied locale.
     * <br>Post condition: A log message will be written when a message could
     * not be retrieved.
     * @param locale The locale.
     * @return The localized message for the supplied local.
     *  <br>When the localized message could not be retrieved from the resource
     *  bundle a standard message containing the message key is returned hence
     *  this method never throws a {@link MissingResourceException}.
     * @throws NullPointerException When the supplied locale is null.
     */
    public String getMessage(final Locale locale) {
        CDebug.checkParameterNotNull(locale, "locale");

        if (messageKey == null) {
            return super.getMessage();
        } else {
            try {
                String message = ResourceBundle.getBundle(
                    resourceBundleName, locale).getString(messageKey);
                if (messageArguments.length > 0) {
                    message = format(message, messageArguments, locale);
                }
                return message;
            } catch (final MissingResourceException exception) {
                final StringBuffer message = new StringBuffer();
                message.append("Could not retrieve exception message:key=");
                message.append(messageKey);
                message.append(":resourceBundle=");
                message.append(resourceBundleName);
                //log.warn(message, exception);

                return messageKey;
            }
        }
    }

    /**
     * Constructs a compound message based on the message pattern and the
     * supplied arguments.
     * @param messagePattern The message pattern.
     * @param messageArguments The arguments that must be inserted into the
     *  message pattern.
     * @param locale The locale.
     * @return The localized compound message.
     * @throws IllegalArgumentException When messagePattern is empty.
     * @throws NullPointerException When either messagePattern, messageArguments
     *  or locale is null.
     */
    private String format(
            final String messagePattern,
            final Object [] messageArguments,
            final Locale locale) {
        CDebug.assertNotEmpty(messagePattern, "messagePattern");
        CDebug.assertNotNull(messageArguments, "messageArguments");
        CDebug.assertNotNull(locale, "locale");

        final MessageFormat messageFormat = new MessageFormat(messagePattern);
        messageFormat.setLocale(locale);
        return messageFormat.format(messageArguments);
    }

    /**
     * Converts this exception to a String.
     * @return String representation of this exception.
     */
    public String toString() {
        return getMessage();
    }

}