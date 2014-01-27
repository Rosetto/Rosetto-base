package info.rosetto.contexts.base;

import info.rosetto.models.base.function.RosettoFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * 名前空間に取り込む関数をまとめたパッケージ.<br>
 * 
 * @author tohhy
 */
public class FunctionPackage {
    /**
     * このパッケージが保有する要素の一覧.
     */
    private final List<RosettoFunction> functions = new ArrayList<RosettoFunction>();
    
    private final String packageName;
    
    public FunctionPackage(String packageName, RosettoFunction...functions) {
        this.packageName = packageName;
        for(int i=0; i<functions.length; i++) {
            this.functions.add(functions[i]);
        }
    }
    
    public void addTo(NameSpace space) {
        for(RosettoFunction f : functions) {
            space.putAbsolute(packageName + "." + f.getName(), f);
        }
    }
    
}
