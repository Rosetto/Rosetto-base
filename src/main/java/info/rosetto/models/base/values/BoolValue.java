package info.rosetto.models.base.values;

import info.rosetto.exceptions.NotConvertibleException;

public class BoolValue implements RosettoValue {
    private static final long serialVersionUID = 1419103880664446476L;
    
    private final boolean value;
    
    public BoolValue(boolean value) {
        this.value = value;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RosettoValue) {
            try {
                return (value == ((RosettoValue)obj).asBool());
            } catch (NotConvertibleException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public ValueType getType() {
        return ValueType.BOOLEAN;
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
        return value;
    }

    @Override
    public boolean asBool(boolean defaultValue) {
        return value;
    }

    @Override
    public int asInt() throws NotConvertibleException {
        return value ? 1 : 0;
    }

    @Override
    public int asInt(int defaultValue) {
        return value ? 1 : 0;
    }

    @Override
    public double asDouble() throws NotConvertibleException {
        throw new NotConvertibleException();
    }

    @Override
    public double asDouble(double defaultValue) {
        return defaultValue;
    }
    @Override
    public long asLong() throws NotConvertibleException {
        throw new NotConvertibleException();
    }
    @Override
    public long asLong(long defaultValue) {
        return defaultValue;
    }

}
