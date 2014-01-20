package info.rosetto.models.base.function;

import javax.annotation.concurrent.Immutable;

import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.utils.base.TextUtils;

/**
 * TODO 未実装
 * 単一の引数を受け取って適切な型をdetectするstatic RosettoArgument parse(String arg)を実装
 * @author tohhy
 */
@Immutable
public class RosettoArgument {
    private final String key;
    private final RosettoValue value;
    
    public RosettoArgument(String arg) {
        int equalPosition = arg.indexOf("=");
        if(equalPosition == -1) {
            this.key = null;
            this.value = new RosettoValue(arg);
        } else {
            this.key = arg.substring(0, equalPosition);
            this.value = new RosettoValue(
                    TextUtils.removeDoubleQuote(arg.substring(equalPosition + 1)));
        }
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
    
    public RosettoArgument(String key, Object value) {
        this.key = key;
        this.value = new RosettoValue(value);
    }
    
    public RosettoValue getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }
}
