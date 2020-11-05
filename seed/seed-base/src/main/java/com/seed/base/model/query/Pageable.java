package com.seed.base.model.query;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 14:20
 */
public interface Pageable {

    /** Get current page number. */
    Integer getPage();

    /** Set page number. */
    void setPage(Integer page);

    /** Get per page size/ */
    Integer getPageSize();

    /** Set per page size. */
    void setPageSize(Integer pageSize);

}
