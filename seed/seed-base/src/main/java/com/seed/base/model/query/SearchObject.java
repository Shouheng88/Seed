package com.seed.base.model.query;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * The search object definition.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 14:21
 */
public class SearchObject implements Pageable, Sortable, Serializable {

    private static final long serialVersionUID = 7739709965769082011L;

    /** Current offset used for limit in SQL. */
    private Integer offset;

    /** Current page number. */
    private Integer page;

    /** Per page size. */
    private Integer pageSize;

    /** Fields sort definitions. */
    private List<Sort> sorts = new LinkedList<>();

    @Override
    public Integer getPage() {
        return page;
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public List<Sort> getSorts() {
        return sorts;
    }

    @Override
    public void addSort(Sort sort) {
        this.sorts.add(sort);
    }

    @Override
    public void addSorts(List<Sort> sorts) {
        this.sorts.addAll(sorts);
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "SearchObject{" +
                "offset=" + offset +
                ", page=" + page +
                ", pageSize=" + pageSize +
                ", sorts=" + sorts +
                '}';
    }
}
