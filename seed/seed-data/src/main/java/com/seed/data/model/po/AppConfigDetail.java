package com.seed.data.model.po;

import com.seed.base.annotation.ColumnInfo;
import com.seed.base.annotation.TableInfo;
import com.seed.base.model.AbstractPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * App config details.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 20:27
 */
@Data
@Table(name = "gt_app_config_detail")
@TableInfo(comment = "App Config Details")
@EqualsAndHashCode(callSuper = true)
public class AppConfigDetail extends AbstractPo {

    /** Id of {@link AppConfig} */
    @Column(name = "app_config_id", nullable = false)
    @ColumnInfo(comment = "Config id")
    private Long appConfigId;

    @Column(name = "value_name")
    @ColumnInfo(comment = "Config detail item name")
    private String valueName;

    @Column(name = "config_value", nullable = false)
    @ColumnInfo(comment = "Config detail item value")
    private String configValue;
}
