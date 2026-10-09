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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * Class that aids in debugging.
 * <br>Originally this class provided the capability of assertions but as of
 * JDK 1.4 the assert statement is part of Java. The current implementation
 * assumes JDK 1.4 will be used in the near future, but for now JDK 1.3 is used
 * until webservers and applicationservers support the new JDK 1.4.<br>
 * Three groups of methods exist in this class. The first group of methods all
 * start with either assert...(), pre...(), post...() or inv...(). These methods
 * should be used until projects are using JDK 1.4. Here is an example of how to
 * use a method from this group: <br><pre><code>
 * public void method(final String value) {
 *     CDebug.assertNotNull(value);
 *     ...
 * } </code></pre>
 * The method above checks that the supplied parameter is non null.<br><br>
 * The second group of methods consists of methods starting with check...().
 * These methods are different from assert methods because they throw standard
 * runtime exceptions as sugested by Joshua Bloch, Effective Java, Chapter 8,
 * item 40, instead of an Error like the assert statements. Here is an example
 * of how to use a method from this group:<br><pre><code>
 * public void method(final String value) {
 *     CDebug.checkParameterNotEmpty(value, "value");
 *     ...
 * } </code></pre>
 * The method above checks that the supplied parameter is neither null nor
 * empty.<br><br>
 * Remaining methods are part of the third, miscellaneous group.<br>
 * Note: Some expressions may take a lot of time to evaluate. To prevent this
 * use the following construction:<pre><code>
 *     if (CDebug.isDebugEnabled()) {
 *         CDebug.checkParameterTrue(expensive_expression(), "message");
 *     }
 * </code></pre>
 * This construction prevents the expensive_expression() from being evaluated
 * when debug mode is disabled. To disable jxpfw debug mode you can either call
 * the {@link #setDebug(boolean)} method with a value of false or supply a value
 * of false for the debug key in CDebug.properties.
 *
 * @author Kees Schotanus
 * 
 */
public class CDebug {
    /*
     * Design considerations:
     * This class is written with the necessity of speed in mind. If you want to
     * enhance the code in any way, think if your enhancements come at the cost
     * of speed.
     */

     /*
      * Technical considerations:
      * This class has been tested against JDK 1.4. All occurrences of assert
      * have been replaced with new names in order to avoid conflicts when
      * moving to JDK 1.4
      *
      * After switching to JDK 1.4 all CDebug.method(...) should be checked and
      * replaced with the standard assert mechanism. This does require changes
      * in this class. assert...() methods can probably be removed et cetera.
      * This could be the class to add debug messages that can be used after the
      * colon in the assert statement.
      * E.g. assert expr==true : CDebug.getAssertNotTrueMsg();
      */

    /**
     * Logger for this class.
     */
    //private static final Logger log = Logger.getLogger(CDebug.class);
    	
    /**
     * Determines whether jxpfw debug mode is enabled (true) or disabled
     * (false).
     */
    private static boolean isDebugEnabled = false;

    /**
     * Gets the default debug mode from the CDebug.properties file.
     * In case of an error the debug mode will be disabled.
     */
    static {
        try {
            final ResourceBundle resourceBundle = ResourceBundle.getBundle(
                CDebug.class.getName());
            isDebugEnabled =
                CBoolean.parseBoolean(resourceBundle.getString("debug"));
        } catch (final MissingResourceException exception) {
            isDebugEnabled = false;
        } catch (final IllegalArgumentException exception) {
            isDebugEnabled = false;
        }
    }

    /**
     * Private constructor prevents construction of this utility class outside
     * this class.
     */
    private CDebug() {
    }

    /**
     * Determines whether debug mode is enabled (true) or diabled (false).
     * <br>Use this method in your code to skip the evaluation of time consuming
     * expressions.
     * @return True when jxpfw debug mode is enabled, false when disabled.
     */
    public static boolean isDebugEnabled() {
         return isDebugEnabled;
    }

    /**
     * Enables or disables the jxpfw debug mode.
     * <br>The jxpfw debug mode can be configured in CDebug.properties but this
     * method overrides the value from the properties file.<br>
     * Note: This method does <b>not</b> change the behavior of any method in
     * this class, it only changes the behavior of your methods that use the
     * {@link #isDebugEnabled()} method.
     * @param debug Supply true to enable jxpfw debug mode, supply false to
     *  disable the jxpfw debug mode.
     */
    public static void setDebug(final boolean debug) {
        CDebug.isDebugEnabled = debug;
    }

    /**
     * Asserts that the supplied boolean value (expression) is true.
     * <br>Example: CDebug.assertTrue(x >= 0);
     * @param expression Boolean expression to assert.
     */
    public static void assertTrue(final boolean expression) {
        if (!expression) {
            assertionFailed("Assertion failed!");
        }
    }

    /**
     * Asserts that the supplied boolean value (expression) is true.
     * <br>Example: CDebug.assertTrue(x >= 0, "x should be positive!");
     * @param expression Boolean expression to assert.
     * @param message Message to write when the assertion fails.
     */
    public static void assertTrue(final boolean expression,
            final String message) {
        if (!expression) {
            final StringBuffer errorMessage =
                new StringBuffer("Assertion failed");
            if (message != null) {
                errorMessage.append(":");
                errorMessage.append(message);
            }
            errorMessage.append("!");
            assertionFailed(errorMessage.toString());
        }
    }


    /**
     * Asserts that the supplied reference is not null.
     * <br>Example: CDebug.assertNotNull(object);
     * @param reference Object reference to assert that it is not null.
     */
    public static void assertNotNull(final Object reference) {
        if (reference == null) {
            assertionFailed("Assertion failed:Object reference is null!");
        }
    }

    /**
     * Asserts that the supplied reference is not null.
     * <br>Example: CDebug.assertNotNull(object, "object");
     * @param reference Object reference to assert that it is not null.
     * @param referenceName Name of the object reference.
     */
    public static void assertNotNull(final Object reference,
            final String referenceName) {
        if (reference == null) {
            final StringBuffer message = new StringBuffer("Assertion failed:");
            if (referenceName == null) {
                message.append("Object reference is null!");
            } else {
                message.append("Object reference [");
                message.append(referenceName);
                message.append("] is null!");
            }
            assertionFailed(message.toString());
        }
    }

    /**
     * Asserts that the supplied String is not empty.
     * <br>Example: CDebug.assertNotEmpty(string, "string");
     * @param reference String reference to assert that it is not empty.
     * @param referenceName Name of the String reference.
     */
    public static void assertNotEmpty(
            final String reference, final String referenceName) {
        if (reference == null) {
            final StringBuffer message = new StringBuffer("Assertion failed:");
            if (referenceName == null) {
                message.append("String reference is null!");
            } else {
                message.append("String reference [");
                message.append(referenceName);
                message.append("] is null!");
            }
            assertionFailed(message.toString());
        } else if (reference.trim().length() == 0) {
            final StringBuffer message = new StringBuffer("Assertion failed:");
            if (referenceName == null) {
                message.append("String reference is empty!");
            } else {
                message.append("String reference [");
                message.append(referenceName);
                message.append("] is empty!");
            }
            assertionFailed(message.toString());
        }
    }

    /**
     * Asserts that the supplied boolean pre condition is true.
     * <br>Example: CDebug.pre(x >= 0);
     * @param expression Boolean expression to assert.
     */
    public static void pre(final boolean expression) {
        if (!expression) {
            assertionFailed("Pre condition failed!");
        }
    }

    /**
     * Asserts that the supplied boolean pre condition is true.
     * <br>Example: CDebug.pre(x >= 0, "x should be positive!");
     * @param expression Boolean expression to assert.
     * @param message Message to write when the assertion fails.
     */
    public static void pre(final boolean expression, final String message) {
        if (!expression) {
            final StringBuffer errorMessage =
                new StringBuffer("Pre condition failed");
            if (message != null) {
                errorMessage.append(":");
                errorMessage.append(message);
            }
            errorMessage.append("!");
            assertionFailed(errorMessage.toString());
        }
    }

    /**
     * Asserts that the supplied reference is not null.
     * <br>Example: CDebug.pre(reference, "reference");
     * @param reference Object reference to assert that it is not null.
     * @param referenceName Name of the object reference.
     */
    public static void pre(final Object reference, final String referenceName) {
        if (reference == null) {
            final StringBuffer message =
                new StringBuffer("Pre condition failed:");
            if (referenceName == null) {
                message.append("Object reference is null!");
            } else {
                message.append("Object reference [");
                message.append(referenceName);
                message.append("] is null!");
            }
            assertionFailed(message.toString());
        }
    }

    /**
     * Asserts that the supplied String is not empty.
     * <br>Example: CDebug.preNotEmpty(string, "string");
     * @param reference String reference to assert that it is not empty.
     * @param referenceName Name of the String reference.
     */
    public static void preNotEmpty(
            final String reference, final String referenceName) {
        if (reference == null) {
            final StringBuffer message =
                new StringBuffer("Pre condition failed:");
            if (referenceName == null) {
                message.append("String reference is null!");
            } else {
                message.append("String reference [");
                message.append(referenceName);
                message.append("] is null!");
            }
            assertionFailed(message.toString());
        } else if (reference.trim().length() == 0) {
            final StringBuffer message =
                new StringBuffer("Pre condition failed:");
            if (referenceName == null) {
                message.append("String reference is empty!");
            } else {
                message.append("String reference [");
                message.append(referenceName);
                message.append("] is empty!");
            }
            assertionFailed(message.toString());
        }
    }

    /**
     * Asserts that the supplied boolean invariant (still) holds.
     * <br>Example: CDebug.inv(total > 0);
     * @param expression Boolean invariant condition to assert.
     */
    public static void inv(final boolean expression) {
        if (!expression) {
            assertionFailed("Invariant broken");
        }
    }

    /**
     * Asserts that the supplied boolean invariant (still) holds.
     * <br>Example: CDebug.inv(total > 0, "total must always be positive!");
     * @param expression Boolean invariant condition to assert.
     * @param message Message to write when the invariant is broken.
     */
    public static void inv(final boolean expression, final String message) {
        if (!expression) {
            final StringBuffer errorMessage =
                new StringBuffer("Invariant broken");
            if (message != null) {
                errorMessage.append(":");
                errorMessage.append(message);
            }
            errorMessage.append("!");
            assertionFailed(errorMessage.toString());
        }
    }

    /**
     * Called when one of the called assert(...), pre(...), inv(...) or
     * post(...) methods failed.
     * <br>Logs a fatal message with an Exception so a stacktrace will be logged
     * as well.
     * @param message Message that will be logged.
     * @throws Error Always!
     */
    private static void assertionFailed(final String message) {
        /*
         * Design considerations:
         * I decided against creating a special AssertionError or
         * AssertionException class since this currently wouldn't add any
         * functionality. I decided to throw a standard Error object and not a
         * standard Exception since a developer should normally not recover from
         * an assertion failed condition. JDK 1.4 defines a new AssertionError
         * class!
         */

        final Error assertionError = new Error(message);
        //log.error(message, assertionError);
        throw assertionError;
    }

    /**
     * Checks that the supplied boolean value (expression) is true.
     * <br>Example: CDebug.checkParameterTrue(text.length() &gt; 3,
     * "Input text is too short!");
     * @param expression Boolean expression to check.
     * @param message Message to write when the check fails.
     * @throws IllegalArgumentException When the supplied boolean expression is
     *  false.
     */
    public static void checkParameterTrue(final boolean expression,
            final String message) {
        if (!expression) {
            final StringBuffer errorMessage =
                new StringBuffer("checkParameterTrue failed");
            if (message != null) {
                errorMessage.append(":");
                errorMessage.append(message);
            }
            errorMessage.append("!");
            final IllegalArgumentException exception =
                new IllegalArgumentException(errorMessage.toString());
            //log.fatal(errorMessage.toString(), exception);
            throw exception;
        }
    }

    /**
     * Checks that the supplied parameter reference is not null.
     * <br>Example: CDebug.checkParameterNotNull(reference, "reference");
     * @param parameter Parameter reference to check that it is not null.
     * @param parameterName Name of the parameter.
     * @throws NullPointerException When the supplied parameter is null.
     */
    public static void checkParameterNotNull(final Object parameter,
            final String parameterName) {
        if (parameter == null) {
            if (parameterName == null) {
                final String message = "Parameter is null!";
                final NullPointerException exception =
                    new NullPointerException(message);
                //log.fatal(message, exception);
                throw exception;
            } else {
                final StringBuffer message = new StringBuffer("Parameter [");
                message.append(parameterName);
                message.append("] is null!");
                final NullPointerException exception =
                    new NullPointerException(message.toString());
                //log.fatal(message.toString(), exception);
                throw exception;
            }
        }
    }

    /**
     * Checks that the supplied String parameter reference is not empty.
     * <br>Example: CDebug.checkParameterNotEmpty(string, "string");
     * @param parameter String parameter reference to check that it is not
     *  empty.
     * @param parameterName Name of the String reference.
     * @throws NullPointerException When the supplied parameter is null.
     * @throws IllegalArgumentException When the supplied parameter is empty.
     */
    public static void checkParameterNotEmpty(
            final String parameter, final String parameterName) {
        if (parameter == null) {
            if (parameterName == null) {
                final String message = "Parameter is null!";
                final NullPointerException exception =
                    new NullPointerException(message);
                //log.fatal(message, exception);
                throw exception;
            } else {
                final StringBuffer message =
                    new StringBuffer("String parameter [");
                message.append(parameterName);
                message.append("] is null!");
                final NullPointerException exception =
                    new NullPointerException(message.toString());
                //log.fatal(message.toString(), exception);
                throw exception;
            }
        } else if (parameter.trim().length() == 0) {
            if (parameterName == null) {
                String message = "String parameter is empty!";
                final IllegalArgumentException exception=
                    new IllegalArgumentException(message);
                //log.fatal(message, exception);
                throw exception;
            } else {
                StringBuffer message = new StringBuffer("String parameter [");
                message.append(parameterName);
                message.append("] is empty!");
                final IllegalArgumentException exception =
                    new IllegalArgumentException(message.toString());
                //log.fatal(message.toString(), exception);
                throw exception;
            }
        }
    }

    /**
     * Checks that the supplied arrayParameter reference is not empty.
     * <br>Example: CDebug.checkParameterNotEmpty(countries, "countries");
     * @param arrayParameter Array parameter reference to check that it is not
     *  empty.
     * @param parameterName Name of the String reference.
     * @throws NullPointerException When the supplied arrayParameter is null.
     * @throws IllegalArgumentException When the supplied arrayParameter is
     *  empty.
     */
    public static void checkParameterNotEmpty(
            final Object [] arrayParameter, final String parameterName) {
        if (arrayParameter == null) {
            if (parameterName == null) {
                String message = "Array parameter is null!";
                final NullPointerException exception = new
                    NullPointerException(message);
                //log.fatal(message, exception);
                throw exception;
            } else {
                StringBuffer message = new StringBuffer("Array parameter [");
                message.append(parameterName);
                message.append("] is null!");
                final NullPointerException exception =
                    new NullPointerException(message.toString());
                //log.fatal(message.toString(), exception);
                throw exception;
            }
        } else if (arrayParameter.length == 0) {
            if (parameterName == null) {
                final String message = "Array parameter is empty!";
                IllegalArgumentException exception =
                    new IllegalArgumentException(message);
                //log.fatal(message, exception);
                throw exception;
            } else {
                final StringBuffer message =
                    new StringBuffer("Array parameter [");
                message.append(parameterName);
                message.append("] is empty!");
                IllegalArgumentException exception =
                  new IllegalArgumentException(message.toString());
                //log.fatal(message.toString(), exception);
                throw exception;
            }
        }
    }

    /**
     * Gets the stack trace from the supplied throwable.
     * @param throwable The throwable containing the stack trace.
     * @return Stack trace of the supplied throwable.
     */
    public static String getStackTrace(final Throwable throwable) {
        CDebug.checkParameterNotNull(throwable, "throwable");

        try {
            final StringWriter stringWriter = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(stringWriter);
            throwable.printStackTrace(printWriter);
            printWriter.close();
            stringWriter.close();

            return stringWriter.toString();
        } catch (final IOException exception) {
            return "Stack trace is unavailable";
        }
    }

}
