package info.rosetto.models.base.values;

import info.rosetto.exceptions.NotConvertibleException;


public class NullValue implements RosettoValue {
    private static final long serialVersionUID = -5537257042851037526L;
    
    public static final NullValue INSTANCE = new NullValue();
    
    private NullValue() {}
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NullValue) {
            return true;
        }
        return false;
    }

    @Override
    public ValueType getType() {
        return ValueType.NULL;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public String asString() {
        return "NULL";
    }

    @Override
    public boolean asBool() throws NotConvertibleException {
        return false;
    }

    @Override
    public boolean asBool(boolean defaultValue) {
        return false;
    }

    @Override
    public int asInt() throws NotConvertibleException {
        throw new NotConvertibleException();
    }

    @Override
    public int asInt(int defaultValue) {
        return defaultValue;
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
