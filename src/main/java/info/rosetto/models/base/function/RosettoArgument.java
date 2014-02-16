package info.rosetto.models.base.function;

import info.rosetto.models.base.values.ActionCall;
import info.rosetto.utils.base.TextUtils;

import javax.annotation.concurrent.Immutable;

/**
 * TODO 未実装
 * 単一の引数を受け取って適切な型をdetectするstatic RosettoArgument parse(String arg)を実装
 * @author tohhy
 */
@Immutable
public class RosettoArgument {
    private final String key;
    private final String value;
    
    public RosettoArgument(String arg) {
        int equalPosition = arg.indexOf("=");
        if(equalPosition == -1) {
            this.key = null;
            this.value = arg;
        } else {
            this.key = arg.substring(0, equalPosition);
            this.value = TextUtils.removeDoubleQuote(arg.substring(equalPosition + 1));
        }
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
    
    public RosettoArgument(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public RosettoArgument(String key, ActionCall value) {
        this.key = key;
        this.value = value.asString();
    }
    
    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }
}
