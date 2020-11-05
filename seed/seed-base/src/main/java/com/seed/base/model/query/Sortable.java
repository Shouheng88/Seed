package com.seed.base.model.query;

import java.util.List;

/**
 * Sortable definition for fields.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 14:12
 */
public interface Sortable {

    /** Get filed sorts definition. */
    List<Sort> getSorts();

    /** Add field sort definition. */
    void addSort(Sort sort);

    /** Add multiple filed sort definitions add one time. */
    void addSorts(List<Sort> sorts);
}
