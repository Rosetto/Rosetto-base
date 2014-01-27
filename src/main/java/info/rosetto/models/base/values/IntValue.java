package info.rosetto.models.base.values;

import info.rosetto.exceptions.NotConvertibleException;

public class IntValue implements RosettoValue {
    private static final long serialVersionUID = -6660103213801013944L;
    
    private final long value;
    
    public IntValue(int value) {
        this.value = value;
    }
    
    public IntValue(long value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RosettoValue) {
            try {
                return (value == ((RosettoValue)obj).asInt());
            } catch (NotConvertibleException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public ValueType getType() {
        return ValueType.INTEGER;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String asString() {
        return String.valueOf(value);
    }
    
    @Override
    public String asString(String defaultValue) {
        return String.valueOf(value);
    }

    @Override
    public boolean asBool() throws NotConvertibleException {
        throw new NotConvertibleException();
    }

    @Override
    public boolean asBool(boolean defaultValue) {
        return defaultValue;
    }

    @Override
    public int asInt() throws NotConvertibleException {
        if(value > Integer.MAX_VALUE || value < Integer.MIN_VALUE)
            throw new NotConvertibleException("value out of int range");
        return (int)value;
    }

    @Override
    public int asInt(int defaultValue) {
        if(value > Integer.MAX_VALUE || value < Integer.MIN_VALUE)
            return defaultValue;
        return (int)value;
    }

    @Override
    public long asLong() throws NotConvertibleException {
        return value;
    }

    @Override
    public long asLong(long defaultValue) {
        return value;
    }

    @Override
    public double asDouble() throws NotConvertibleException {
        return value;
    }

    @Override
    public double asDouble(double defaultValue) {
        return value;
    }

}
