package info.rosetto.models.base.values;

import info.rosetto.exceptions.NotConvertibleException;

public class DoubleValue implements RosettoValue {
    private static final long serialVersionUID = -3880822918826025412L;
    
    private final double value;
    
    public DoubleValue(double value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RosettoValue) {
            try {
                return (value == ((RosettoValue)obj).asDouble());
            } catch (NotConvertibleException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public ValueType getType() {
        return ValueType.DOUBLE;
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
        return (int)value;
    }

    @Override
    public int asInt(int defaultValue) {
        return (int)value;
    }

    @Override
    public double asDouble() throws NotConvertibleException {
        return value;
    }

    @Override
    public double asDouble(double defaultValue) {
        return value;
    }

    @Override
    public long asLong() throws NotConvertibleException {
        return (long)value;
    }

    @Override
    public long asLong(long defaultValue) {
        return (long)value;
    }

}
