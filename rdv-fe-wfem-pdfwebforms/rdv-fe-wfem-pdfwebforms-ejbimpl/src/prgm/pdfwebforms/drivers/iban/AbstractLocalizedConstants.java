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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;


/**
 * Abstraction of a collection of constant values with localizable descriptions.
 * <br>A better alternative to usage of this class is the class
 * {@link AbstractLocalizedTypeSafeEnumeration}.
 * @author Martijn Baels
 * @author Kees Schotanus
 * 
 */
public abstract class AbstractLocalizedConstants {

    /**
     * Use this constant to specify the description style.
     * <br>Note: This constant uses a long constant description.
     */
    public static final int DESC_STYLE_LONG = 0;

    /**
     * Use this constant to specify the description style.
     * <br>Note: This constant uses a short constant description.
     */
    public static final int DESC_STYLE_SHORT = 1;

    /**
     * Use this constant to specify the description style.
     * <br>Note: This constant uses a combination of a long constant and a
     * short constant description.
     */
    public static final int DESC_STYLE_BOTH = 2;

    /**
     * Key to lookup: The description of this collection of constants.
     */
    public static final String KEY_DESCRIPTION = "description";

    /**
     * Key to lookup: The default constant value for this collection of
     * constants.
     */
    public static final String KEY_DEFAULT = "default";

    /**
     * Key to lookup: An invalid constant has been supplied.
     * <br>Arguments:
     * <ul>
     * <li>0 = The constant value</li>
     * </ul>
     */
    public static final String MSG_INVALID_CONSTANT = "invalidConstant";

    /**
     * Key postfix for short localized descriptions.
     */
    private static final String KEY_POSTFIX_SHORT = "_short";

    /**
     * Collection of related constants.
     */
    private Map constants = new HashMap();

    /**
     * Default constructor that creates an empty group of constants.
     */
    protected AbstractLocalizedConstants() {
    }

    /**
     * Adds the supplied constant to the group of constants.
     * <br>This method uses the toString() method of the added object to
     * generate a key. This key is used to map it to the supplied constant so
     * the constant can be retrieved by key later.
     * @param constant The constant to add to this collection of constants.
     * @throws IllegalArgumentException When the same constant is added more
     *  than once or when the toString() method on the supplied constant is
     *  one of the reserved keys: {@link #KEY_DEFAULT}, {@link #KEY_DESCRIPTION}
     *  or {@link #MSG_INVALID_CONSTANT}
     * @throws NullPointerException When the supplied constant is null or when
     *  the toString() method on the constant returns null.
     * @see #add(String, Object)
     */
    protected void add(final Object constant) {
        add(constant.toString(), constant);
    }

    /**
     * Adds the supplied constant to the group of constants.
     * @param key Key used to store and retrieve the supplied constant.
     * @param constant The constant to add to this collection of constants.
     * @throws IllegalArgumentException When the same constant is added more
     *  than once or when the supplied key equals one of the reserved keys:
     *  {@link #KEY_DEFAULT}, {@link #KEY_DESCRIPTION} or
     *  {@link #MSG_INVALID_CONSTANT} or when the supplied key is empty.
     * @throws NullPointerException When either the supplied key or constant is
     *  null.
     */
    protected void add(final String key, final Object constant) {
        CDebug.checkParameterNotEmpty(key, "key");
        CDebug.checkParameterNotNull(constant, "constant");

        if (key.equals(KEY_DEFAULT) || key.equals(KEY_DESCRIPTION)
                || key.equals(MSG_INVALID_CONSTANT)) {
            throw new IllegalArgumentException("Reserved key:" + key);
        }

        final Object previousConstant = constants.put(key, constant);
        if (previousConstant != null) {
            final StringBuffer message = new StringBuffer("Constant:");
            message.append(constant);
            message.append(" already exists!");
            throw new IllegalArgumentException(message.toString());
        }
    }

    /**
     * Fetches the description for this collection of constants.
     * @param locale Locale used to localize the description.
     * @return Localized description for this collection of constants.
     *  <br>When the key {@link #KEY_DESCRIPTION} could not be retrieved a non
     *  localized, fixed description is returned.
     */
    public String getDescription(final Locale locale) {
        return getString(KEY_DESCRIPTION, locale, "Constants");
    }

    /**
     * Fetches the description of a constant.
     * @param key Key to the constant for which the description should be
     *  returned.
     *  <br>This is the key used by either {@link #add(Object)} or
     *  {@link #add(String, Object)}.
     * @param locale Locale used to localize the description.
     *  <br>When null is supplied the default locale will be used.
     * @return Localized description for the supplied constant.
     * @throws InvalidArgumentException When the supplied key does not exist.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
    public String getDescription(final String key, final Locale locale)
            throws InvalidArgumentException {
        CDebug.checkParameterNotEmpty(key, "key");

        validateKey(key);
        return getString(
            key,
            locale == null ? Locale.getDefault() : locale,
            key);
    }

    /**
     * Fetches the short description of a constant.
     * @param key Key to the constant for which the description should be
     *  returned.
     *  <br>This is the key used by either {@link #add(Object)} or
     *  {@link #add(String, Object)}.
     * @param locale Locale used to localize the description.
     *  <br>When null is supplied the default locale will be used.
     * @return Short localized description for the supplied constant.
     * @throws InvalidArgumentException When the supplied key does not exist.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
    public String getShortDescription(final String key, final Locale locale)
            throws InvalidArgumentException {
        CDebug.checkParameterNotEmpty(key, "key");

        validateKey(key);
        return getString(
            key + KEY_POSTFIX_SHORT,
            locale == null ? Locale.getDefault() : locale,
            key + KEY_POSTFIX_SHORT);
    }

    /**
     * Gets the key to the default constant for this collection of constants,
     * for the supplied locale.
     * @param locale For which the default constant should be retrieved.
     *  <br>When null is supplied the default locale will be used.
     * @return Key to the default constant for this group of constants or null
     *  when no default constant has been specified in the resource bundle.
     */
    public String getDefaultKey(final Locale locale) {
        return getString(
            KEY_DEFAULT,
            locale == null ? Locale.getDefault() : locale,
            null);
    }

    /**
     * Gets the default constant for this collection of constants,
     * for the supplied locale.
     * @param locale For which the default constant should be retrieved.
     *  <br>When null is supplied the default locale will be used.
     * @return The default constant for this group of constants or null
     *  when no default constant has been specified in the resource bundle.
     */
    public Object getDefaultConstant(final Locale locale) {
        final String key = getDefaultKey(locale);
        return (key == null) ? null : getConstant(key);
    }

    /**
     * Finds the constant corresponding to the supplied key.
     * @param key Key corresponding to the constant that should be returned.
     *  <br>This is the key used by either {@link #add(Object)} or
     *  {@link #add(String, Object)}.
     * @return Constant or null when no constant exists with the supplied
     *  key.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
    public Object getConstant(final String key) {
        CDebug.checkParameterNotEmpty(key, "key");

        return constants.get(key);
    }

    /**
     * Gets the collection of all stored constants.
     * @return Collection of all stored constants.
     */
    public Collection getConstants() {
        return constants.values();
    }

    /**
     * Finds the key corresponding to the supplied constant.
     * @param constant Constant for which the corresponding key should be
     *  returned.
     * @return Key corresponding to the supplied constant or null when the
     *  supplied constant does not exist.
     * @throws NullPointerException When the supplied constant is null.
     */
    public String getKey(final Object constant) {
        CDebug.checkParameterNotNull(constant, "constant");

        for (final Iterator iterator = constants.entrySet().iterator();
                iterator.hasNext(); ) {
            final Map.Entry entry = (Map.Entry)iterator.next();
            if (entry.getValue().equals(constant)) {
                return (String)entry.getKey();
            }
        }

        return null;
    }

    /**
     * Creates and returns a collection of {@link CodeDescription} pairs for
     * all stored constants.
     * <br>The descriptions will be localized using the supplied locale.
     * @param locale The locale used to localize the descriptions.
     *  <br>When null is supplied the default locale will be used.
     * @return A collection of localized {@link CodeDescription} pairs for all
     *  stored constants.
     *  <br>The returned collection is ordered by localized description
     *  (ascending).
     * @see #getCodeDescriptionPairs(Locale, Comparator)
     */
    public Collection getCodeDescriptionPairs(final Locale locale) {
        return getCodeDescriptionPairs(locale, null);
    }

    /**
     * Creates and returns a collection of {@link CodeDescription} pairs for
     * all stored constants.
     * <br>The descriptions will be localized using the supplied locale.
     * @param locale The locale used to localize the descriptions.
     *  <br>When null is supplied the default locale will be used.
     * @param comparator CodeDescriptionComparator that sorts the code and
     *  (localized) description pairs.
     *  <br>When null is supplied a default comparator will be used that orders
     *  by localized description (ascending).
     * @return A sorted collection of localized {@link CodeDescription} pairs
     *  for all stored constants.
     * @see #getCodeDescriptionPairs(Locale, Comparator, int)
     */
    public Collection getCodeDescriptionPairs(
            final Locale locale, final Comparator comparator) {
        return getCodeDescriptionPairs(locale, comparator, DESC_STYLE_LONG);
    }

    /**
     * Creates and returns a collection of {@link CodeDescription} pairs for
     * all stored constants.
     * <br>The descriptions will be localized using the supplied locale and they
     * will be sorted (ascending).
     * @param locale The locale used to localize the descriptions.
     *  <br>When null is supplied the default locale will be used.
     * @param comparator CodeDescriptionComparator that sorts the code and
     *  (localized) description pairs.
     *  <br>When null is supplied a default comparator will be used that orders
     *  by localized description (ascending).
     * @param descriptionStyle The style to be used when constructing constant
     *  descriptions.
     * @return A sorted collection of localized {@link CodeDescription} pairs
     *  for all stored constants.
     * @throws IllegalArgumentException When the supplied descriptionStyle is
     *  not in the list:
     *  <ul>
     *    <li>{@link #DESC_STYLE_LONG}</li>
     *    <li>{@link #DESC_STYLE_SHORT}</li>
     *    <li>{@link #DESC_STYLE_BOTH}</li>
     *  </ul>
     */
    public Collection getCodeDescriptionPairs(
            final Locale locale, final Comparator comparator,
            final int descriptionStyle) {
        CDebug.checkParameterTrue(
            descriptionStyle == DESC_STYLE_LONG
                || descriptionStyle == DESC_STYLE_SHORT
                || descriptionStyle == DESC_STYLE_BOTH,
            "Illegal description style!");

        final Locale useLocale = locale == null ? Locale.getDefault() : locale;
        final Comparator useComparator = comparator != null
            ? comparator : CodeDescription.createComparator(true, true);

        final ArrayList result = new ArrayList(constants.size());
        for (final Iterator iterator = constants.entrySet().iterator();
                iterator.hasNext(); ) {
            final Map.Entry entry = (Map.Entry)iterator.next();
            final String key = entry.getKey().toString();

            String description;
            switch (descriptionStyle) {
            case DESC_STYLE_LONG :
                description = getString(key, useLocale, key);
                break;
            case DESC_STYLE_SHORT :
                description = getString(key + KEY_POSTFIX_SHORT, useLocale,
                    key + KEY_POSTFIX_SHORT);
                break;
            case DESC_STYLE_BOTH :
                description = getString(key, useLocale, key)
                    + " ("
                    + getString(key + KEY_POSTFIX_SHORT,
                        useLocale, key + KEY_POSTFIX_SHORT)
                    + ")";
                break;
            default:
                description = getString(key, useLocale, key);
                break;
            }

            result.add(new CodeDescription(entry.getKey(), description));
        }

        Collections.sort(result, useComparator);
        return result;
    }

    /**
     * Validates that the supplied key exists.
     * @param key Key to check for existence.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     * @throws InvalidArgumentException When the supplied key does not exist.
     * @see #isValidKey(String)
     */
    public void validateKey(final String key) throws InvalidArgumentException {
        CDebug.checkParameterNotEmpty(key, "key");

        if (!constants.containsKey(key)) {
            throw new InvalidArgumentException(
                getResourceBundleName(), MSG_INVALID_CONSTANT,
                new Object [] {key});
        }
    }

    /**
     * Validates that the supplied key exists.
     * @param key Key to check for existence.
     * @return True when the supplied key exists, false when it does not exist.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
    public boolean isValidKey(final String key) {
        CDebug.checkParameterNotEmpty(key, "key");

        return constants.containsKey(key);
    }

    /**
     * Validates that the supplied constant exists.
     * @param constant Constant to check for existence.
     * @throws NullPointerException When the supplied constant is null.
     * @throws InvalidArgumentException When the supplied constant does not
     *  exist.
     */
    public void validateConstant(final Object constant)
            throws InvalidArgumentException {
        CDebug.checkParameterNotNull(constant, "constant");

        if (!constants.containsValue(constant)) {
            throw new InvalidArgumentException(
                getResourceBundleName(), MSG_INVALID_CONSTANT,
                new Object [] {constant});
        }
    }

    /**
     * Validates that the supplied constant exists.
     * @param constant Constant to check for existence.
     * @return True when the supplied constant exists, false when it does not
     *  exist.
     * @throws NullPointerException When the supplied constant is null.
     */
    public boolean isValidConstant(final Object constant) {
        CDebug.checkParameterNotNull(constant, "constant");

        return constants.containsValue(constant);
    }

    /**
     * Gets the resource bundle used by this collection of constants for
     * descriptions et cetera.
     * <br>This implementation always returns the full name of the class. If
     * your class is called: org.jxpfw.util.FileTypes then this is what will be
     * returned. Using the same name for both your property file and your class
     * file is handy when you want to keep them together but note that this is
     * only possible for JDK 1.3 and up. Feel free to override this method and
     * supply a different resource bundle.
     * @return Full name of the class used by you for your constants.
     */
    protected String getResourceBundleName() {
        return getClass().getName();
    }

    /**
     * Gets a String resource from the resource bundle returned by
     * {@link #getResourceBundleName()}.
     * @param key Key to the String resource.
     * @param locale Locale used to localize the String resource.
     *  <br>When null is supplied the default locale will be used.
     * @param defaultValue Value that is returned when the String resource
     *  could not be retrieved from the resource bundle.
     * @return String resource corresponding to the supplied key or the
     *  supplied defaultValue when the String resource could not be fetched from
     *  the resource bundle.
     * @throws NullPointerException When the supplied key is null.
     * @throws IllegalArgumentException When the supplied key is empty.
     */
    private String getString(
            final String key, final Locale locale, final String defaultValue) {
        CDebug.assertNotEmpty(key, "key");

        final CResourceBundle resourceBundle = new CResourceBundle(
            getResourceBundleName(),
            locale == null ? Locale.getDefault() : locale);

        return resourceBundle.getString(key, defaultValue, null);
    }

    /**
     * Creates and returns a String representation of the stored constants.
     * @return String representation of the stored constants.
     */
    public String toString() {
        final StringBuffer result = new StringBuffer(100);
        result.append(getDescription(null));
        result.append(constants.keySet().toString());

        return result.toString();
    }

}
