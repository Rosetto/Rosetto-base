package info.rosetto.models.text.style;

import java.io.Serializable;

/**
 * 行の範囲情報を保持する.
 * 行の高さや幅の固定サイズを保持でき、行の内容にかかわらず表示上の大きさを決められる.
 * イミュータブル.
 * @author tohhy
 */
public class LineBounds implements Serializable {
    /**
     * 0.1.0 (2013/09/08)
     */
    private static final long serialVersionUID = -7301442235876270589L;

    /**
     * この行が占める幅.
     */
    private final int width;
    
    /**
     * この行が占める高さ.
     */
    private final int height;
    
    /**
     * 高さも幅も固定しないLineBounds.
     */
    public static final LineBounds FREE = new LineBounds(-1, -1);

    /**
     * 固定する幅と高さを指定してLineBoundsを生成する.
     * 負の値を指定した場合は固定されない.負の値は-1に丸められる.
     * @param width 固定する幅
     * @param height 固定する高さ
     */
    public LineBounds(int width, int height) {
        this.width = (width < -1) ? -1 : width;
        this.height = (height < -1) ? -1 : height;
    }
    
    /**
     * 幅を固定せず、高さのみ指定値で固定したLineBoundsを生成する.
     * 負の値を指定した場合高さも固定されない.負の値は-1に丸められる.
     * @param height 固定する高さ
     */
    public LineBounds(int height) {
    	this(-1, height);
    }
    
    /**
     * 行の固定幅を返す.
     * 固定されていない場合は-1.
     * @return 行の固定幅
     */
    public int getWidth() {
        return width;
    }

    /**
     * 行の固定された高さを返す.
     * 固定されていない場合は-1.
     * @return 行の固定された高さ
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * 行の幅が固定されているかどうかを返す.
     * @return 行の幅が固定されているかどうか
     */
    public boolean isWidthFixed() {
        return (width >= 0);
    }
    
    /**
     * 行の高さが固定されているかどうかを返す.
     * @return 行の高さが固定されているかどうか
     */
    public boolean isHeightFixed() {
        return (height >= 0);
    }
}
