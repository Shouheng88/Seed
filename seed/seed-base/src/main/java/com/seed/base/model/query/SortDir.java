package com.seed.base.model.query;

/**
 * The filed sort direction definition.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 14:15
 */
public enum SortDir {
    /** Ascend */
    ASC(""),
    /** Descend */
    DESC("DESC");

    public final String value;

    SortDir(String value) {
        this.value = value;
    }

    public static SortDir get(String sortDir) {
        if ("DESC".equalsIgnoreCase(sortDir)) {
            return SortDir.DESC;
        }
        return SortDir.ASC;
    }
}
