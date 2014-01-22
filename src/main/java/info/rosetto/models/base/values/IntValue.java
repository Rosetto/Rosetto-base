package info.rosetto.models.base.values;

import info.rosetto.exceptions.NotConvertibleException;

public class IntValue implements RosettoValue {
    private static final long serialVersionUID = -6660103213801013944L;
    
    private final int value;
    
    public IntValue(int value) {
        this.value = value;
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
    public boolean asBool() throws NotConvertibleException {
        throw new NotConvertibleException();
    }

    @Override
    public boolean asBool(boolean defaultValue) {
        return defaultValue;
    }

    @Override
    public int asInt() throws NotConvertibleException {
        return value;
    }

    @Override
    public int asInt(int defaultValue) {
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
