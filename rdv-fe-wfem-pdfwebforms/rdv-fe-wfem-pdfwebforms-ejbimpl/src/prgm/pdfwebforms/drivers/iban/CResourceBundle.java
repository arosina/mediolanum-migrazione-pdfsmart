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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * Utility resource bundle class.
 * <br>In addition to Sun's {@link ResourceBundle} class this class adds the
 * capability of searching multiple resource bundle files, in order,
 * transparently. This allows you to store common resources and specific
 * resources in separate bundles without adding complexity to code to retrieve
 * resources.<br>
 * Use this class for example in JSP beans. Each JSP bean in the inheritance
 * tree can add a ResourceBundle to this bundle. Now when the JSP bean lowest
 * in the hierarchy needs a translation, it can do so by calling just one
 * get...(...) method on this class.<br>
 * The last resource file added will be the first in line to be checked for
 * resources, this allows you to override common translations with more
 * specific translations.<br>
 * Note: This class has not been made thread-safe! (and it doesn't need to be).
 * @author Kees Schotanus
 * 
 */
public class CResourceBundle implements Serializable {

    /**
     * Logger for this class.
     */
    //private static final Logger log = Logger.getLogger(CResourceBundle.class);

    /**
     * Stores all the resource bundles ordered from 'added first' to
     * 'added last'.
     * <br>This member should be synchronized with resourceBundleNames!
     */
    private List resourceBundles = new ArrayList();

    /**
     * Stores all the resource bundle names ordered from first to last one
     * added.<br>
     * <br>This member should be synchronized with the resourceBundles list!
     * The name is necessary to give proper messages to the user for example
     * when a required resource is missing. In this case it is good to show a
     * list of all the resource bundles that were searched. Note that in JDK's
     * up to and including 1.3 a resourceBundle.toString() only shows the class
     * name and a hash code, not the name of the properties file!
     */
    private List resourceBundleNames = new ArrayList();

    /**
     * Lock object to synchronize the two lists above (resourceBundles and
     * resourceBundleNames).
     */
    private Object lockLists = new Object();

    /**
     * Locale to be used for all resource bundles that are part of this bundle.
     * <br>The locale will be null when this object was created using a
     * ResourceBundle (instead of a resource bundle name.
     */
    private Locale locale;


    /**
     * Constructs an object using the supplied {@link ResourceBundle} name
     * and the default locale.
     * <br>All subsequent bundles added using {@link #addResourceBundle(String)}
     * will use the same locale.<br>
     * Note: Only use this constructor when the properties can be loaded by
     * the same classloader that loads jxpfw.
     * @param resourceBundleName Name of the resource bundle file. Supplying
     *  "org.jxpfw.util.UtilMessages" would use the file
     *   org/jxpfw/util/UtilMessages.properties.
     * @throws MissingResourceException When the resource bundle could not be
     *  located.
     * @throws NullPointerException When the supplied resourceBundleName is
     *  null.
     * @throws IllegalArgumentException When the supplied resourceBundleName is
     *  empty.
     */
    public CResourceBundle(final String resourceBundleName) {
        this(resourceBundleName, Locale.getDefault());
    }

    /**
     * Constructs an object using the supplied resourceBundle and
     * resourceBundleName.
     * @param resourceBundle The resource bundle.
     * @param resourceBundleName Name of the resource bundle file.
     *  <br>This name is only used for error messages.
     * @throws NullPointerException When either the supplied resourceBundle or
     *  resourceBundleName is null.
     * @throws IllegalArgumentException When the supplied resourceBundleName is
     *  empty.
     */
    public CResourceBundle(final ResourceBundle resourceBundle,
            final String resourceBundleName) {
        CDebug.checkParameterNotNull(resourceBundle, "resourceBundle");
        CDebug.checkParameterNotEmpty(resourceBundleName, "resourceBundleName");

        resourceBundles.add(resourceBundle);
        resourceBundleNames.add(resourceBundleName);
    }

    /**
     * Constructs an object using the supplied {@link ResourceBundle} name
     * and the supplied locale.
     * <br>All subsequent bundles added using {@link #addResourceBundle(String)}
     * will use the same locale.<br>
     * Note: Only use this constructor when the properties can be loaded by
     * the same classloader that loads jxpfw.
     * @param resourceBundleName Name of the resource bundle file. Supplying
     *  "org.jxpfw.util.UtilMessages" would use the file
     *   org/jxpfw/util/UtilMessages.properties.
     * @param locale Locale used to translate messages.
     * @throws MissingResourceException When the resource bundle could not be
     *  located.
     * @throws IllegalArgumentException When the supplied resourceBundleName is
     *  empty.
     * @throws NullPointerException When either the supplied resourceBundleName
     *  or the supplied locale is null.
     */
    public CResourceBundle(final String resourceBundleName,
            final Locale locale) {
        CDebug.checkParameterNotEmpty(resourceBundleName, "resourceBundleName");
        CDebug.checkParameterNotNull(locale, "locale");

        this.locale = locale;
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(
            resourceBundleName, locale);
        resourceBundles.add(resourceBundle);
        resourceBundleNames.add(resourceBundleName);
    }

    /**
     * Adds a resource bundle file to be searched.
     * <br>The resource bundle file that has been added last will be used first
     * to locate a resource.
     * @param resourceBundle The resource bundle to add.
     * @param resourceBundleName Name of the resource bundle file.
     *  <br>This name is only used for error messages.
     * @throws NullPointerException When either the supplied resourceBundle or
     *  resourceBundleName is null.
     * @throws IllegalArgumentException When the supplied resourceBundleName is
     *  empty.
     */
    public void addResourceBundle(final ResourceBundle resourceBundle,
            final String resourceBundleName) {
        CDebug.checkParameterNotNull(resourceBundle, "resourceBundle");
        CDebug.checkParameterNotEmpty(resourceBundleName, "resourceBundleName");

        synchronized(lockLists) {
            resourceBundles.add(resourceBundle);
            resourceBundleNames.add(resourceBundle.toString());
        }
    }

    /**
     * Adds a resource bundle file to be searched.
     * <br>The resource bundle file that has been added last will be used first
     * to locate a resource. The locale used for this resource bundle is
     * determined by the used constructor.
     * @param resourceBundleName Name of the resource bundle file. Supplying
     *  "org.jxpfw.util.UtilMessages" would use the file
     *   org/jxpfw/util/UtilMessages.properties.
     * @throws MissingResourceException When the resource bundle could not be
     *  located.
     * @throws NullPointerException When the supplied resourceBundleName is
     *  null.
     * @throws IllegalArgumentException When the supplied resourceBundleName is
     *  empty.
     * @throws UnsupportedOperationException When this object has been created
     *  by {@link #CResourceBundle(ResourceBundle, String)}.
     *  <br>The reason is that in this case we do not know what locale to use.
     *  Solve this by using the other add method named
     *  {@link #addResourceBundle(ResourceBundle, String)}.
     */
    public void addResourceBundle(final String resourceBundleName) {
        CDebug.checkParameterNotEmpty(resourceBundleName, "resourceBundleName");

        if (locale == null) {
           throw new UnsupportedOperationException(
               "addResourceBundle(String) is not supported! "
                   + "Use: addResourceBundle(ResourceBundle, String), "
                   + "or create this the bundle with:"
                   +  "CResourceBundle(ResourceBundle, String).");
        }

        /*
         * Previously this method took the locale from the first bundle but the
         * problem was that the method getLocale() on a ResourceBundle will give
         * you the actual locale of the bundle (could be a fallback), not the
         * requested locale.
         */
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(
            resourceBundleName, locale);

        synchronized(lockLists) {
            resourceBundles.add(resourceBundle);
            resourceBundleNames.add(resourceBundleName);
        }
    }

    /**
     * Gets a boolean resource from the list of resource bundles.
     * @param key Key to the boolean resource.
     * @return Boolean resource or false when the key could not be located or
     *  when the key could be located but the resource is not a boolean.
     *  <br>In the latter case a warning will be logged.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
     public boolean getBoolean(final String key) {
        CDebug.checkParameterNotEmpty(key, "key");

        boolean resource;
        final String stringResource = getString(key);
        if (stringResource == null) {
            resource = false;
        } else {
            try {
                resource = CBoolean.parseBoolean(stringResource);
            } catch (final IllegalArgumentException exception) {
                //log.warn(createNotABooleanMessage(key, stringResource));
                resource = false;
            }
        }
        return resource;
    }

    /**
     * Gets a boolean resource from the list of resource bundles.
     * @param key Key to the boolean resource.
     * @param defaultValue Default value in case the key is missing or when the
     *  located resource is not a boolean.
     * @return Boolean resource or the supplied default value when the
     *  supplied key does not exist or when the key does exist but the resource
     *  is not a boolean.
     *  <br>In the latter case a warning will be logged.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
     public boolean getBoolean(final String key, final boolean defaultValue) {
        CDebug.checkParameterNotEmpty(key, "key");

        boolean resource;

        final String stringResource = getString(key);
        if (stringResource == null) {
            resource = defaultValue;
        } else {
            try {
                resource = CBoolean.parseBoolean(stringResource);
            } catch (final IllegalArgumentException exception) {
                //log.warn(createNotABooleanMessage(key, stringResource));
                resource = defaultValue;
            }
        }

        return resource;
    }

    /**
     * Gets a required boolean resource from the list of resource bundles.
     * <br>When the key is missing or when the key does exist but the resource
     * is not a boolean,an error will be logged.<br>
     * @param key Key to the boolean resource.
     * @return Boolean resource.
     * @throws MissingResourceException When the key does not exist.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty or when
     *  the retrieved resource could not be converted to a boolean value.
     */
    public boolean getRequiredBoolean(final String key) {
        CDebug.checkParameterNotEmpty(key, "key");

        final String stringResource = getRequiredString(key, null);
        try {
            return CBoolean.parseBoolean(stringResource);
        } catch (final IllegalArgumentException exception) {
            //log.error(createNotABooleanMessage(key, stringResource));
            throw new IllegalArgumentException(
                createNotABooleanMessage(key, stringResource));
        }
    }

    /**
     * Gets an integer resource from the list of resource bundles.
     * @param key Key to the integer resource.
     * @return Integer resource or null when the key could not be located or
     *  when the key could be located but the resource is not an integer.
     *  <br>In the latter case a warning will be logged.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
     public Integer getInteger(final String key) {
        CDebug.checkParameterNotEmpty(key, "key");

        Integer resource;

        final String stringResource = getString(key);
        if (stringResource == null) {
            resource = null;
        } else {
            try {
                resource = Integer.valueOf(stringResource);
            } catch (final NumberFormatException exception) {
                //log.warn(createNotAnIntegerMessage(key, stringResource));
                resource = null;
            }
        }
        return resource;
    }

    /**
     * Gets an integer resource from the list of resource bundles.
     * @param key Key to the integer resource.
     * @param defaultValue Default value in case the key is missing or when the
     *  located resource is not an integer.
     * @return Integer resource or the supplied default value when the
     *  supplied key does not exist or when the key does exist but the resource
     *  is not an integer.
     *  <br>In the latter case a warning will be logged.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
     public Integer getInteger(final String key, final Integer defaultValue) {
        CDebug.checkParameterNotEmpty(key, "key");

        Integer resource;

        final String stringResource = getString(key);
        if (stringResource == null) {
            resource = defaultValue;
        } else {
            try {
                resource = Integer.valueOf(stringResource);
            } catch (final NumberFormatException exception) {
                //log.warn(createNotAnIntegerMessage(key, stringResource));
                resource = defaultValue;
            }
        }

        return resource;
    }

    /**
     * Gets a required integer resource from the list of resource bundles.
     * <br>When the key is missing or when the key does exist but the resource
     * is not an integer,an error will be logged.<br>
     * @param key Key to the integer resource.
     * @return Integer resource.
     * @throws MissingResourceException When the key does not exist.
     * @throws NumberFormatException When the key exists but is not an integer.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
    public Integer getRequiredInteger(final String key) {
        CDebug.checkParameterNotEmpty(key, "key");

        final String stringResource = getRequiredString(key, null);
        try {
            return Integer.valueOf(stringResource);
        } catch (final NumberFormatException exception) {
            //log.error(createNotAnIntegerMessage(key, stringResource));
            throw exception;
        }

    }

    /**
     * Gets a long resource from the list of resource bundles.
     * @param key Key to the long resource.
     * @return Long resource or null when the key could not be located or when
     *  the key could be located but the resource is not a long.
     *  <br>In the latter case a warning will be logged.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
     public Long getLong(final String key) {
        CDebug.checkParameterNotEmpty(key, "key");

        Long resource;

        final String stringResource = getString(key);
        if (stringResource == null) {
            resource = null;
        } else {
            try {
                resource = Long.valueOf(stringResource);
            } catch (final NumberFormatException exception) {
                //log.warn(createNotALongMessage(key, stringResource));
                resource = null;
            }
        }
        return resource;
    }

    /**
     * Gets a long resource from the list of resource bundles.
     * @param key Key to the integer resource.
     * @param defaultValue Default value in case the key is missing or when the
     *  located resource is not a long integer.
     * @return Long resource or the supplied default value when the supplied
     *  key does not exist or when the key does exist but the resource is not a
     *  long.
     *  <br>In the latter case a warning will be logged.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
     public Long getLong(final String key, final Long defaultValue) {
        CDebug.checkParameterNotEmpty(key, "key");

        Long resource;

        final String stringResource = getString(key);
        if (stringResource == null) {
            resource = defaultValue;
        } else {
            try {
                resource = Long.valueOf(stringResource);
            } catch (final NumberFormatException exception) {
                //log.warn(createNotALongMessage(key, stringResource));
                resource = defaultValue;
            }
        }

        return resource;
    }

    /**
     * Gets a required long resource from the list of resource bundles.
     * <br>When the key is missing or when the key does exist but the resource
     * is not a long, an error will be logged.<br>
     * @param key Key to the long integer resource.
     * @return Long resource.
     * @throws MissingResourceException When the key does not exist.
     * @throws NumberFormatException When the key exists but is not a long.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
    public Long getRequiredLong(final String key) {
        CDebug.checkParameterNotEmpty(key, "key");

        final String stringResource = getRequiredString(key, null);
        try {
            return Long.valueOf(stringResource);
        } catch (final NumberFormatException exception) {
            //log.error(createNotALongMessage(key, stringResource));
            throw exception;
        }

    }

    /**
     * Gets a double resource from the list of resource bundles.
     * @param key Key to the double resource.
     * @return Double resource or null when the key could not be located or
     *  when the key could be located but the resource is not a double.
     *  <br>In the latter case a warning will be logged.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
     public Double getDouble(final String key) {
        CDebug.checkParameterNotEmpty(key, "key");

        Double resource;

        final String stringResource = getString(key);
        if (stringResource == null) {
            resource = null;
        } else {
            try {
                resource = Double.valueOf(stringResource);
            } catch (final NumberFormatException exception) {
                //log.warn(createNotADoubleMessage(key, stringResource));
                resource = null;
            }
        }
        return resource;
    }

    /**
     * Gets a double resource from the list of resource bundles.
     * @param key Key to the integer resource.
     * @param defaultValue Default value in case the key is missing or when the
     *  located resource is not a double.
     * @return Double resource or the supplied default value when the
     *  supplied key does not exist or when the key does exist but the resource
     *  is not a double.
     *  <br>In the latter case a warning will be logged.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
     public Double getDouble(final String key, final Double defaultValue) {
        CDebug.checkParameterNotEmpty(key, "key");

        Double resource;

        final String stringResource = getString(key);
        if (stringResource == null || stringResource.length() == 0) {
            resource = defaultValue;
        } else {
            try {
                resource = Double.valueOf(stringResource);
            } catch (final NumberFormatException exception) {
                //log.warn(createNotADoubleMessage(key, stringResource));
                resource = defaultValue;
            }
        }

        return resource;
    }

    /**
     * Gets a required double resource from the list of resource bundles.
     * <br>When the key is missing or when the key does exist but the resource
     * is not a double, an error will be logged.<br>
     * @param key Key to the double resource.
     * @return Double resource.
     * @throws MissingResourceException When the key does not exist.
     * @throws NumberFormatException When the key exists but is not a double.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
    public Double getRequiredDouble(final String key) {
        CDebug.checkParameterNotEmpty(key, "key");

        final String stringResource = getRequiredString(key, null);
        try {
            return Double.valueOf(stringResource);
        } catch (final NumberFormatException exception) {
            //log.error(createNotADoubleMessage(key, stringResource));
            throw exception;
        }

    }

    /**
     * Gets a String resource from the list of resource bundles.
     * @param key Key to the String resource.
     * @return String resource or null when the resource could not be located.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
    public String getString(final String key) {
        CDebug.checkParameterNotEmpty(key, "key");

        // This is the only method that directly accesses the resource bundles

        // Search from right to left so last file added is used first
        for (int i = resourceBundles.size() - 1; i >= 0; --i) {
            try {
                final ResourceBundle resourceBundle =
                    (ResourceBundle)resourceBundles.get(i);
                return resourceBundle.getString(key);
            } catch (final MissingResourceException ignore) {
                ; // Just try another bundle
            }
        }

        return null;
    }

    /**
     * Gets a String resource from the list of resource bundles.
     * @param key Key to the String resource.
     * @param arguments Message arguments that will be substituted in the
     *  retrieved string resource.
     *  <br>May be null, in which case no message arguments will be substituted.
     * @return String resource or null when the resource could not be located.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
    public String getString(final String key, final Object [] arguments) {
        CDebug.checkParameterNotEmpty(key, "key");

        String resource = getString(key);
        if (CString.isNotEmpty(resource)) {
            resource = format(resource, arguments);
        }

        return resource;
    }

    /**
     * Gets a String resource from the list of resource bundles.
     * @param key Key to the String resource.
     * @param defaultValue Default value in case the key does not exist.
     * @param arguments Message arguments that will be substituted in the
     *  retrieved string resource.
     *  <br>May be null, in which case no message arguments will be substituted.
     * @return String resource or the supplied default value when the supplied
     *  key does not exist.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
    public String getString(final String key, final String defaultValue,
            final Object [] arguments) {
        CDebug.checkParameterNotEmpty(key, "key");

        String resource = getString(key, arguments);
        if (resource == null && CString.isNotEmpty(defaultValue)) {
            resource = format(defaultValue, arguments);
        }
        return resource;
    }

    /**
     * Gets a required String resource from the list of resource bundles.
     * <br>When the key is missing an error will be logged.
     * @param key Key to the String resource.
     * @param arguments Message arguments that will be substituted in the
     *  retrieved string resource.
     *  <br>May be null, in which case no message arguments will be substituted.
     * @return String resource.
     * @throws MissingResourceException When the key does not exist.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
    public String getRequiredString(
            final String key, final Object [] arguments) {
         CDebug.checkParameterNotEmpty(key, "key");

        final String resource = getString(key, arguments);
        if (resource == null) {
            final StringBuffer message = new StringBuffer();
            message.append("Could not locate key:").append(key);
            message.append(" in bundles:").append(toString())     ;
            //log.error(message);
            throw new MissingResourceException(
                message.toString(), getClass().getName(), key);
        }

        return resource;
    }

    /**
     * Constructs a compound message based on the message pattern and the
     * supplied arguments.
     * @param messagePattern The message pattern.
     *  <br>For example: Processing record {0} of {1}.
     * @param messageArguments The arguments that must be inserted into the
     *  message pattern.
     * @return The localized compound message or simply the supplied
     *  messagePattern when the supplied messageArguments is null or empty.
     * @throws IllegalArgumentException When messagePattern is empty.
     * @throws NullPointerException When the supplied messagePattern is null.
     */
    public String format(final String messagePattern,
            final Object [] messageArguments) {
        CDebug.assertNotEmpty(messagePattern, "messagePattern");

        if (messageArguments != null && messageArguments.length > 0) {
            final MessageFormat messageFormat =
                new MessageFormat(messagePattern);
            messageFormat.setLocale(locale);
            return messageFormat.format(messageArguments);
        } else {
            return messagePattern;
        }
    }

    /**
     * Creates a String representation of this bundle of resource bundles.
     * @return String representation of this bundle of resource bundles.
     */
    public String toString() {
        return resourceBundleNames.toString();
    }

    /**
     * Creates a message specifying that the supplied value corresponding to
     * the supplied key does not represent a valid boolean.
     * @param key Key to the boolean resource.
     * @param value String representation of the boolean resource.
     * @return Message specifying that the located resource is not a valid
     *  boolean.
     */
    private String createNotABooleanMessage(
            final String key, final String value) {
        final StringBuffer message = new StringBuffer();

        message.append("key=").append(key);
        message.append(":value=").append(value);
        message.append(":Not a boolean:bundles=");
        message.append(toString());

        return message.toString();
   }

    /**
     * Creates a message specifying that the supplied value corresponding to
     * the supplied key does not represent a valid integer.
     * @param key Key to the integer resource.
     * @param value String representation of the integer resource.
     * @return Message specifying that the located resource is not a valid
     *  integer.
     */
    private String createNotAnIntegerMessage(
            final String key, final String value) {
        final StringBuffer message = new StringBuffer();

        message.append("key=").append(key);
        message.append(":value=").append(value);
        message.append(":Not an integer:bundles=");
        message.append(toString());

        return message.toString();
   }

    /**
     * Creates a message specifying that the supplied value corresponding to
     * the supplied key does not represent a valid long.
     * @param key Key to the long resource.
     * @param value String representation of the long resource.
     * @return Message specifying that the located resource is not a valid long.
     */
    private String createNotALongMessage(
            final String key, final String value) {
        final StringBuffer message = new StringBuffer();

        message.append("key=").append(key);
        message.append(":value=").append(value);
        message.append(":Not a long:bundles=");
        message.append(toString());

        return message.toString();
   }

    /**
     * Creates a message specifying that the supplied value corresponding to
     * the supplied key does not represent a valid double.
     * @param key Key to the double resource.
     * @param value String representation of the double resource.
     * @return Message specifying that the located resource is not a valid
     *  double.
     */
    private String createNotADoubleMessage(
            final String key, final String value) {
        final StringBuffer message = new StringBuffer();

        message.append("key=").append(key);
        message.append(":value=").append(value);
        message.append(":Not a double:bundles=");
        message.append(toString());

        return message.toString();
   }

}
