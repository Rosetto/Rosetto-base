package info.rosetto.parsers.rosetto;

import info.rosetto.models.base.engine.AvailableExtensions;

/**
 * Rosettoが対応する拡張子の定義.
 * @author tohhy
 */
public class RosettoExt extends AvailableExtensions{
    private static final long serialVersionUID = -3088581624708934561L;
    
    public static final String[] SCENARIO_EXT = {"txt", "rs"};
    @Override
    public String[] getScenarioExt() {
        return SCENARIO_EXT;
    }
    
    public static final String[] IMAGE_EXT = {"png", "jpg", "gif"};
    @Override
    public String[] getImageExt() {
        return IMAGE_EXT;
    }

    public static final String[] SOUND_EXT = {"wav", "ogg", "mp3", "flac"};
    @Override
    public String[] getSoundExt() {
        return SOUND_EXT;
    }

    public static final String[] MOVIE_EXT = {};
    @Override
    public String[] getMovieExt() {
        return MOVIE_EXT;
    }

    public static final String[] FONT_EXT = {};
    @Override
    public String[] getFontExt() {
        return FONT_EXT;
    }
}
