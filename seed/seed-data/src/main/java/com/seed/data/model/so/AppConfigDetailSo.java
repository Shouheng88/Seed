package com.seed.data.model.so;

import com.seed.base.model.query.SearchObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.stereotype.Repository;

/**
 * AppConfigDetailSo
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0 
 * @date 2020/11/06 08:36
 */
@Data
@Repository
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AppConfigDetailSo extends SearchObject {

    private static final long serialVersionUID = 1L;

    private Long appConfigId;

}
