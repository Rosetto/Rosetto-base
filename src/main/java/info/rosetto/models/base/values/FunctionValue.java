package info.rosetto.models.base.values;

import info.rosetto.exceptions.NotConvertibleException;
import info.rosetto.models.base.function.FunctionCall;

public class FunctionValue implements RosettoValue {
    
    private final FunctionCall value;
    
    public FunctionValue(FunctionCall value) {
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.FuncCall;
    }

    @Override
    public Object getValue() {
        return value;
    }
    
    private RosettoValue evaluate() {
        //TODO
        return null;
    }
    
    @Override
    public String toString() {
        return evaluate().toString();
    }

    @Override
    public boolean toBool() throws NotConvertibleException {
        return evaluate().toBool();
    }

    @Override
    public int toInt() throws NotConvertibleException {
        return evaluate().toInt();
    }

    @Override
    public double toDouble() throws NotConvertibleException {
        return evaluate().toDouble();
    }

    @Override
    public boolean toBool(boolean defaultValue) {
        return evaluate().toBool(defaultValue);
    }

    @Override
    public int toInt(int defaultValue) {
        return evaluate().toInt(defaultValue);
    }

    @Override
    public double toDouble(double defaultValue) {
        return evaluate().toDouble(defaultValue);
    }

}
