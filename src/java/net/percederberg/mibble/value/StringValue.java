/*
 * StringValue.java
 *
 * This work is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This work is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 *
 * Copyright (c) 2004-2006 Per Cederberg. All rights reserved.
 */

package net.percederberg.mibble.value;

import net.percederberg.mibble.MibLoaderLog;
import net.percederberg.mibble.MibType;
import net.percederberg.mibble.MibValue;

/**
 * A string MIB value.
 *
 * @author   Per Cederberg, <per at percederberg dot net>
 * @version  2.8
 * @since    2.0
 */
public class StringValue extends MibValue {

    /**
     * The string value.
     */
    private String value;

    /**
     * Creates a new string MIB value.
     *
     * @param value          the string value
     */
    public StringValue(String value) {
        super("OCTET STRING");
        this.value = value;
    }

    /**
     * Initializes the MIB value. This will remove all levels of
     * indirection present, such as references to other values. No
     * value information is lost by this operation. This method may
     * modify this object as a side-effect, and will return the basic
     * value.<p>
     *
     * <strong>NOTE:</strong> This is an internal method that should
     * only be called by the MIB loader.
     *
     * @param log            the MIB loader log
     * @param type           the value type
     *
     * @return the basic MIB value
     */
    public MibValue initialize(MibLoaderLog log, MibType type) {
        return this;
    }

    /**
     * Creates a value reference to this value. The value reference
     * is normally an identical value. Only certain values support
     * being referenced, and the default implementation of this
     * method throws an exception.<p>
     *
     * <strong>NOTE:</strong> This is an internal method that should
     * only be called by the MIB loader.
     *
     * @return the MIB value reference
     *
     * @since 2.2
     */
    public MibValue createReference() {
        return new StringValue(value);
    }

    /**
     * Compares this object with the specified object for order. This
     * method will only compare the string representations with each
     * other.
     *
     * @param obj            the object to compare to
     *
     * @return less than zero if this object is less than the specified,
     *         zero if the objects are equal, or
     *         greater than zero otherwise
     *
     * @since 2.6
     */
    public int compareTo(Object obj) {
        return toString().compareTo(obj.toString());
    }

    /**
     * Checks if this object equals another object. This method will
     * compare the string representations for equality.
     *
     * @param obj            the object to compare with
     *
     * @return true if the objects are equal, or
     *         false otherwise
     *
     * @since 2.6
     */
    public boolean equals(Object obj) {
        return value.equals(obj.toString());
    }

    /**
     * Returns a hash code for this object.
     *
     * @return a hash code for this object
     *
     * @since 2.6
     */
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Returns a Java String representation of this value.
     *
     * @return a Java String representation of this value
     */
    public Object toObject() {
        return value;
    }

    /**
     * Returns a string representation of this value.
     *
     * @return a string representation of this value
     */
    public String toString() {
        return value;
    }
}
