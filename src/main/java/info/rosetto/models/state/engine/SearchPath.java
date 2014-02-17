/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.models.state.engine;

import java.io.Serializable;

/**
 * ファイル探索の対象になるパスのモデル.
 * @author tohhy
 */
public class SearchPath implements Serializable {
    private static final long serialVersionUID = -3952170424526009874L;

    /**
     * ファイル探索の方式.
     * @author tohhy
     */
    public enum SearchType {
        FLAT, TREE, DIRECT
    }
    
    /**
     * 探索対象にするファイルタイプ.
     * @author tohhy
     */
    public enum FileType {
        SCENARIO, IMAGE, SOUND, MOVIE, FONT, 
        
        /**
         * SCENARIO, IMAGE, SOUND, MOVIE, FONT全ての探索対象にする
         */
        ALL,
        
        /**
         * IMAGE, SOUND, MOVIE, FONTの探索対象にする
         */
        RESOURCES,
    }
    
    /**
     * 探索対象のパス.
     */
    private final String path;
    
    /**
     * 探索対象のファイルタイプ.
     * @author tohhy
     */
    private final FileType fileType;
    
    /**
     * このパスでのファイル探索の方式.
     */
    private final SearchType searchType;
    
    
    /**
     * ファイル探索パスの定義を作成する.
     * @param path 探索パス
     * @param type 探索の方式
     * @param allowExt このパスで探索対象にする拡張子
     */
    public SearchPath(String path, SearchType type, FileType fileType) {
        this.path = path;
        this.fileType = fileType;
        this.searchType = type;
    }
    
    /**
     * このパスの文字列表現を取得する.
     * @return このパスの文字列表現
     */
    public String getPath() {
        return path;
    }
    
    /**
     * このパスでの探索方式を取得する.
     * @return このパスでの探索方式
     */
    public SearchType getSearchType() {
        return searchType;
    }

    /**
     * このパスでの探索で対象とするファイル種別を取得する.
     * @return このパスでの探索で対象とするファイル種別
     */
    public FileType getFileType() {
        return fileType;
    }
}
