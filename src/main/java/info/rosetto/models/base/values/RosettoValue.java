package info.rosetto.models.base.values;

import info.rosetto.models.base.function.FunctionCall;

import javax.annotation.concurrent.Immutable;

/**
 * 引数に渡される値や関数実行時の返り値をラップするクラス.
 * enumの型情報を保持している.ジェネリクスにすると返り値の型を動的に判別できないため.
 * @author tohhy
 */
@Immutable
public class RosettoValue {
    /**
     * 返り値を返さない関数の戻り値を表す.シングルトン.
     */
    public static final RosettoValue VOID = new RosettoValue();
    
    /**
     * このクラスが保持する値.typeに対応した型が保証される.
     */
    private final Object value;
    
    /**
     * このクラスが保持する値の型.
     */
    private final ValueType type;
    
    /**
     * Voidの返り値を表すRosettoValueを初期化する.
     * RosettoValue.VOIDの生成のみで使用.
     */
    private RosettoValue() {
        this.type = ValueType.Void;
        this.value = new VoidValue();
    }
    
    /**
     * 
     * @param value
     */
    public RosettoValue(Object value) {
        this.type = detectType(value);
        this.value = value;
    }
    
    public RosettoValue(String value) {
        this.type = ValueType.String;
        this.value = value;
    }
    
    public RosettoValue(FunctionCall value) {
        this.type = ValueType.FuncCall;
        this.value = value;
    }
    
    @Override
    public String toString() {
        if(value instanceof FunctionCall) return "()";//TODO S式を返す
        return value.toString();
    }
    
    public ValueType getType() {
        return type;
    }
    
    public Object getValue() {
        return value;
    }

    private ValueType detectType(Object value) {
        if(value == null)
            throw new IllegalArgumentException("value must not be null");
        if(value instanceof String) {
            return ValueType.String;
        } else if(value instanceof FunctionCall) {
            return ValueType.FuncCall;
        }
        throw new IllegalArgumentException("Illegal type value received: " + value.getClass());
    }

    /**
     * Voidの場合の返り値.
     * @author tohhy
     */
    public static class VoidValue {
        private VoidValue() {}
        @Override
        public String toString() {
            return "void";
        }
    }
}
