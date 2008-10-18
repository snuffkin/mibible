/*
 * ElementType.java
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
 * Copyright (c) 2004 Per Cederberg. All rights reserved.
 */

package net.percederberg.mibble.type;

import net.percederberg.mibble.MibException;
import net.percederberg.mibble.MibLoaderLog;
import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibType;
import net.percederberg.mibble.MibValue;

/**
 * A compound element MIB type. This typs is used inside various
 * compound types, storing a reference to the type and an optional
 * name.
 *
 * @author   Per Cederberg, <per at percederberg dot net>
 * @version  2.2
 * @since    2.0
 */
public class ElementType extends MibType {

    /**
     * The optional element name.
     */
    private String name;

    /**
     * The element type.
     */
    private MibType type;

    /**
     * Creates a new element type.
     *
     * @param name           the optional element name
     * @param type           the element type
     */
    public ElementType(String name, MibType type) {
        super("", false);
        this.name = name;
        this.type = type;
    }

    /**
     * Initializes the MIB type. This will remove all levels of
     * indirection present, such as references to types or values. No
     * information is lost by this operation. This method may modify
     * this object as a side-effect, and will return the basic
     * type.<p>
     *
     * <strong>NOTE:</strong> This is an internal method that should
     * only be called by the MIB loader.
     *
     * @param symbol         the MIB symbol containing this type
     * @param log            the MIB loader log
     *
     * @return the basic MIB type
     *
     * @throws MibException if an error was encountered during the
     *             initialization
     *
     * @since 2.2
     */
    public MibType initialize(MibSymbol symbol, MibLoaderLog log)
        throws MibException {

        type = type.initialize(symbol, log);
        return this;
    }

    /**
     * Checks if the specified value is compatible with this type.
     * The value is considered compatible with this type, if it is
     * compatible with the underlying type.
     *
     * @param value          the value to check
     *
     * @return true if the value is compatible, or
     *         false otherwise
     */
    public boolean isCompatible(MibValue value) {
        return type.isCompatible(value);
    }

    /**
     * Returns the optional element name.
     *
     * @return the element name, or
     *         null if no name has been set
     *
     * @since 2.2
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the referenced MIB type.
     *
     * @return the referenced MIB type
     *
     * @since 2.2
     */
    public MibType getType() {
        return type;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object
     */
    public String toString() {
        StringBuffer  buffer = new StringBuffer();

        buffer.append(super.toString());
        if (name != null) {
            buffer.append(name);
            buffer.append(" ");
        }
        buffer.append(type.toString());
        return buffer.toString();
    }
}
