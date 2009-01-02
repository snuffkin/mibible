/*
 * ValueConstraint.java
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

package net.percederberg.mibble.type;

import net.percederberg.mibble.FileLocation;
import net.percederberg.mibble.MibException;
import net.percederberg.mibble.MibLoaderLog;
import net.percederberg.mibble.MibType;
import net.percederberg.mibble.MibValue;
import net.percederberg.mibble.value.NumberValue;
import net.percederberg.mibble.value.StringValue;

/**
 * A MIB type value constraint. This class represents a single value
 * in a set of value constraints.
 *
 * @author   Per Cederberg, <per at percederberg dot net>
 * @version  2.8
 * @since    2.0
 */
public class ValueConstraint implements Constraint {

    /**
     * The constraint location. This value is reset to null once the
     * constraint has been initialized. 
     */
    private FileLocation location;

    /**
     * The constraint value.
     */
    private MibValue value;

    /**
     * Creates a new value constraint.
     *
     * @param location       the constraint location
     * @param value          the constraint value
     */
    public ValueConstraint(FileLocation location, MibValue value) {
        this.location = location;
        this.value = value;
    }

    /**
     * Initializes the constraint. This will remove all levels of
     * indirection present, such as references to types or values. No
     * constraint information is lost by this operation. This method
     * may modify this object as a side-effect, and will be called by
     * the MIB loader.
     *
     * @param type           the type to constrain
     * @param log            the MIB loader log
     *
     * @throws MibException if an error was encountered during the
     *             initialization
     */
    public void initialize(MibType type, MibLoaderLog log)
        throws MibException {

        String  message;

        value = value.initialize(log, type);
        if (location != null && !isCompatible(type)) {
            message = "Value constraint not compatible with this type";
            log.addWarning(location, message);
        }
        location = null;
    }

    /**
     * Checks if the specified type is compatible with this
     * constraint.
     *
     * @param type            the type to check
     *
     * @return true if the type is compatible, or
     *         false otherwise
     */
    public boolean isCompatible(MibType type) {
        return type == null || value == null || type.isCompatible(value);
    }

    /**
     * Checks if the specified value is compatible with this
     * constraint.
     *
     * @param value          the value to check
     *
     * @return true if the value is compatible, or
     *         false otherwise
     */
    public boolean isCompatible(MibValue value) {
        String  str1 = this.value.toString();
        String  str2 = value.toString();

        if (this.value instanceof NumberValue
         && value instanceof NumberValue) {

            return str1.equals(str2);
        } else if (this.value instanceof StringValue
                && value instanceof StringValue) {

            return str1.equals(str2);
        } else {
            return false;
        }
    }

    /**
     * Returns the constraint value.
     *
     * @return the constraint value
     */
    public MibValue getValue() {
        return value;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object
     */
    public String toString() {
        return value.toString();
    }
}
