/*
 * NumberValue.java
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import net.percederberg.mibble.MibLoaderLog;
import net.percederberg.mibble.MibType;
import net.percederberg.mibble.MibValue;
import net.percederberg.mibble.type.CompoundConstraint;
import net.percederberg.mibble.type.Constraint;
import net.percederberg.mibble.type.IntegerType;
import net.percederberg.mibble.type.SizeConstraint;
import net.percederberg.mibble.type.StringType;
import net.percederberg.mibble.type.ValueConstraint;
import net.percederberg.mibble.type.ValueRangeConstraint;

/**
 * A numeric MIB value.
 *
 * @author   Per Cederberg, <per at percederberg dot net>
 * @version  2.8
 * @since    2.0
 */
public class NumberValue extends MibValue {

    /**
     * The number value.
     */
    private Number value;

    /**
     * Creates a new number value.
     *
     * @param value          the number value
     */
    public NumberValue(Number value) {
        super("Number");
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
        return new NumberValue(value);
    }

    /**
     * Compares this object with the specified object for order. This
     * method will attempt to compare by numerical value, but will
     * use a string comparison as the default comparison operation.
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
        if (obj instanceof NumberValue) {
            return compareToNumber(((NumberValue) obj).value);
        } else if (obj instanceof Number) {
            return compareToNumber((Number) obj);
        } else {
            return toString().compareTo(obj.toString());
        }
    }

    /**
     * Compares this object with the specified number for order.
     *
     * @param num            the number to compare to
     *
     * @return less than zero if this number is less than the specified,
     *         zero if the numbers are equal, or
     *         greater than zero otherwise
     */
    private int compareToNumber(Number num) {
        BigDecimal  num1;
        BigDecimal  num2;

        if (value instanceof Integer && num instanceof Integer) {
            return ((Integer) value).compareTo((Integer) num);
        } else if (value instanceof Long && num instanceof Long) {
            return ((Long) value).compareTo((Long) num);
        } else if (value instanceof BigInteger
                && num instanceof BigInteger) {

            return ((BigInteger) value).compareTo((BigInteger) num);
        } else {
            num1 = new BigDecimal(value.toString());
            num2 = new BigDecimal(num.toString());
            return num1.compareTo(num2);
        }
    }

    /**
     * Checks if this object equals another object. This method will
     * compare the string representations for equality.
     *
     * @param obj            the object to compare with
     *
     * @return true if the objects are equal, or
     *         false otherwise
     */
    public boolean equals(Object obj) {
        return compareTo(obj) == 0;
    }

    /**
     * Returns a hash code for this object.
     *
     * @return a hash code for this object
     */
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Returns a Java Number representation of this value.
     *
     * @return a Java Number representation of this value
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
        return value.toString();
    }

    /**
     * Returns the minimum number of characters for the ASCII representation
     * of the number value.
     *
     * @param type           the MIB value type
     * @param byteLength     the length of a printed byte
     *
     * @return the minimum number of characters required
     */
    protected int getMinimumLength(MibType type, int byteLength) {
        Constraint  c = null;
        int         minLength;

        if (type instanceof IntegerType) {
            c = ((IntegerType) type).getConstraint();
        } else if (type instanceof StringType) {
            c = ((StringType) type).getConstraint();
        }
        minLength = getByteSize(c) * byteLength;
        if (minLength < 0) {
            minLength = 1;
        }
        return minLength;
    }

    /**
     * Returns the minimum size in bytes required by the specified constraint.
     *
     * @param c              the constraint
     *
     * @return the minimum number of bytes required, or
     *         -1 if not possible to determine
     */
    private int getByteSize(Constraint c) {
        ArrayList  list;
        MibValue   value;
        int        size;

        if (c instanceof CompoundConstraint) {
            list = ((CompoundConstraint) c).getConstraintList();
            for (int i = 0; i < list.size(); i++) {
                size = getByteSize((Constraint) list.get(i));
                if (size >= 0) {
                    return size;
                }
            }
        } else if (c instanceof SizeConstraint) {
            c = (Constraint) ((SizeConstraint) c).getValues().get(0);
            value = null;
            if (c instanceof ValueConstraint) {
                value = ((ValueConstraint) c).getValue();
            } else if (c instanceof ValueRangeConstraint) {
                value = ((ValueRangeConstraint) c).getLowerBound();
            }
            if (value != null && value.toObject() instanceof Number) {
                return ((Number) value.toObject()).intValue();
            }
        }
        return -1;
    }
}
