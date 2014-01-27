package info.rosetto.models.base.function;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * 関数の実行時に用いるキーワード引数のリスト.
 * インスタンス化のためには引数として与える先の関数が必要になる.
 * 関数の持つ引数リストと照らし合わせ、適切に引数を割り振ることで生成される.
 * @author tohhy
 */
public class ExpandedArguments {
    
    private final Map<String, String> kwargs;
    
    /**
     * 未評価の引数と関数を取り、実行時引数の実体を生成する.
     * @param args 未評価の引数
     * @param func 実行する関数
     */
    protected ExpandedArguments(RosettoArguments args, RosettoFunction func) {
        Map<String, String> kwargs = new TreeMap<String, String>();
        kwargs.putAll(args.parse(func));
        this.kwargs = Collections.unmodifiableMap(kwargs);
    }
    
    /**
     * 指定引数名にマッピングされた引数値を取得する.
     * 指定引数が存在しない場合はnull
     * @param key 引数名
     * @return 指定引数名にマッピングされた引数値
     */
    public String get(String key) {
        return kwargs.get(key);
    }
    
    /**
     * 指定キーがこの引数マップに含まれているかを取得する.
     * @param key 引数名
     * @return キーが存在するかどうか
     */
    public boolean containsKey(String key) {
        return kwargs.containsKey(key);
    }

    /**
     * キーワード引数のマップを取得する.読み込み専用.
     * @return
     */
    public Map<String, String> getMap() {
        return kwargs;
    }

}
