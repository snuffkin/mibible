/*
 * SnmpRevision.java
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

package net.percederberg.mibble.snmp;

import net.percederberg.mibble.MibException;
import net.percederberg.mibble.MibLoaderLog;
import net.percederberg.mibble.MibValue;

/**
 * An SNMP module identity revision value.  This declaration is used
 * inside a module identity macro type.
 *
 * @see SnmpModuleIdentity
 *
 * @author   Per Cederberg, <per at percederberg dot net>
 * @version  2.8
 * @since    2.0
 */
public class SnmpRevision {

    /**
     * The revision number.
     */
    private MibValue value;

    /**
     * The revision description.
     */
    private String description;

    /**
     * Creates a new SNMP module identity revision.
     *
     * @param value          the revision number
     * @param description    the revision description
     */
    public SnmpRevision(MibValue value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * Initializes the MIB revision number. This will remove all
     * levels of indirection present. No information is lost by this
     * operation. This method may modify this object as a
     * side-effect, and will be called by the MIB loader.
     *
     * @param log            the MIB loader log
     *
     * @throws MibException if an error was encountered during the
     *             initialization
     */
    void initialize(MibLoaderLog log) throws MibException {
        value = value.initialize(log, null);
    }

    /**
     * Returns the revision number.
     *
     * @return the revision number
     */
    public MibValue getValue() {
        return value;
    }

    /**
     * Returns the revision description. Any unneeded indentation
     * will be removed from the description, and it also replaces
     * all tab characters with 8 spaces.
     *
     * @return the revision description
     *
     * @see #getUnformattedDescription()
     */
    public String getDescription() {
        return SnmpType.removeIndent(description);
    }

    /**
     * Returns the unformatted revision description. This method
     * returns the original MIB file description, without removing
     * unneeded indentation or similar.
     *
     * @return the revision description
     *
     * @see #getDescription()
     *
     * @since 2.6
     */
    public String getUnformattedDescription() {
        return description;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object
     */
    public String toString() {
        return value.toString() + " (" + description + ")";
    }
}
