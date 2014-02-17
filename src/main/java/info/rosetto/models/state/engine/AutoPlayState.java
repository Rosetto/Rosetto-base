package info.rosetto.models.state.engine;

import java.io.Serializable;

/**
 * スキップやオートモード等の自動再生に関わる設定を保持する.
 * @author tohhy
 */
public class AutoPlayState implements Serializable {
    private static final long serialVersionUID = -5991457775751195853L;

    /**
     * 現在オートモードが適用されているかどうか.
     */
    private boolean isAutoMode = false;
    
    /**
     * オートモード時の待ちフレーム数.
     */
    private int autoModeWait = 50;
    
    /**
     * 現在のカウント秒数.これが一定秒数に達するとisWaitFinished==trueになる.
     */
    private int autoModeCount = 0;
    
    /**
     * 現在スキップモードが適用されているかどうか.
     */
    private boolean isSkipMode = false;
    
    /**
     * スキップ機能が使用可能かどうか.
     */
    private boolean isSkipAvailable = true;
    
    /**
     * クリックスキップ機能が使用可能かどうか.
     */
    private boolean isClickSkipAvailable = true;
    
    /**
     * カウントをリセットして0に戻す.
     */
    public void resetCount() {
        this.autoModeCount = 0;
    }
    
    /**
     * カウントを進める.
     */
    public void nextCount() {
        this.autoModeCount++;
    }

    /**
     * Waitが完了したかどうかを返す.
     * このハンドラがカウントした秒数がMessageStateで指定されたautoModeWait以上ならばtrueを返す.
     * このメソッドがtrueを返すまでnextCountを呼び出し、trueを返したらresetCountとページ送り処理を呼び出すようにする.
     * @param ms MessageStateのインスタンス
     * @return Waitが完了したかどうか
     */
    public boolean isWaitFinished() {
        return autoModeWait <= autoModeCount;
    }
    
    public int getCurrentAutoModeCount() {
        return autoModeCount;
    }

    public int getAutoModeWait() {
        return autoModeWait;
    }

    public void setAutoModeWait(int autoModeWait) {
        this.autoModeWait = autoModeWait;
    }

    public boolean isAutoMode() {
        return isAutoMode;
    }

    public void setAutoMode(boolean isAutoMode) {
        this.isAutoMode = isAutoMode;
    }

    public boolean isSkipMode() {
        return isSkipMode;
    }

    /**
     * isSkipAvailableがtrueの時のみskipModeを変更できる.
     * @param isSkipMode
     */
    public void setSkipMode(boolean isSkipMode) {
        if(!isSkipAvailable) return;
        this.isSkipMode = isSkipMode;
    }

    public boolean isSkipAvailable() {
        return isSkipAvailable;
    }

    public void setSkipAvailable(boolean isSkipAvailable) {
        this.isSkipAvailable = isSkipAvailable;
    }
    
    public boolean isClickSkipAvailable() {
        return isClickSkipAvailable;
    }

    public void setClickSkipAvailable(boolean isClickSkipEnabled) {
        this.isClickSkipAvailable = isClickSkipEnabled;
    }
    
}
