package com.seed.base.model.query;

import java.io.Serializable;

/**
 * The sort filed definition.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 14:12
 */
public class Sort implements Serializable {

    private static final long serialVersionUID = 7739709965769082011L;

    private String sortKey;

    private String sortDir;

    public static Sort of(String sortKey) {
        return new Sort(sortKey);
    }

    public static Sort of(String sortKey, SortDir dir) {
        return new Sort(sortKey, dir);
    }

    private Sort(String sortKey) {
        this.sortKey = sortKey;
        this.sortDir = SortDir.ASC.value;
    }

    private Sort(String sortKey, SortDir sortDir) {
        this.sortKey = sortKey;
        this.sortDir = sortDir.value;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    @Override
    public String toString() {
        return "Sort{" +
                "sortKey='" + sortKey + '\'' +
                ", sortDir='" + sortDir + '\'' +
                '}';
    }
}
