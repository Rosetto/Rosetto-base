package info.rosetto.models.base.values;

import info.rosetto.exceptions.NotConvertibleException;

/**
 * 
 * @author tohhy
 *
 */
public class StringValue implements RosettoValue {
    
    /**
     * このStringValueが保持する値.
     */
    private final String value;
    
    public StringValue(String value) {
        if(value == null)
            throw new IllegalArgumentException("value must not be null");
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.String;
    }

    @Override
    public Object getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return value;
    }

    /**
     * valueを全て小文字に変換したときに"true"である場合はtrue.<br>
     * valueを全て小文字に変換したときに"false"である場合はfalse.<br>
     * 長さ0の文字列の場合はfalse.<br>
     * それ以外の場合はNotConvertibleExceptionをスローする.
     */
    @Override
    public boolean toBool() throws NotConvertibleException {
        if(value.toLowerCase().equals("false")) return false;
        return value.length() > 0;
    }

    @Override
    public boolean toBool(boolean defaultValue) {
        return false;
    }

    @Override
    public int toInt() {
        return 0;
    }

    @Override
    public int toInt(int defaultValue) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double toDouble() {
        return 0;
    }

    @Override
    public double toDouble(double defaultValue) {
        // TODO Auto-generated method stub
        return 0;
    }
}
