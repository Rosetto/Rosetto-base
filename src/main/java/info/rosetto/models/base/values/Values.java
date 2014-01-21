package info.rosetto.models.base.values;

public class Values {
    public static final VoidValue VOID = VoidValue.INSTANCE;
    
    public static StringValue create(String value) {
        return new StringValue(value);
    }

}
