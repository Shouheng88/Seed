package com.seed.data.model.po;

import com.seed.base.annotation.ColumnInfo;
import com.seed.base.annotation.TableInfo;
import com.seed.base.model.AbstractPo;
import com.seed.base.model.enums.DeviceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * App config object, used for App when first launch to check latest version and
 * decide whether to upgrade, or get other configurations defined by {@link AppConfigDetail}.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 20:23
 */
@Data
@Table(name = "gt_app_config")
@TableInfo(comment = "Basic App Config Item")
@EqualsAndHashCode(callSuper = true)
public class AppConfig extends AbstractPo {

    @Column(name = "app_id", nullable = false)
    @ColumnInfo(comment = "App id")
    private Long appId;

    @Column(name = "platform", nullable = false)
    @ColumnInfo(comment = "Target platform")
    private DeviceType platform;

    @Column(name = "target_version", nullable = false)
    @ColumnInfo(comment = "Target version")
    private String targetVersion;

    @Column(name = "target_channel", nullable = false)
    @ColumnInfo(comment = "Target channel")
    private String targetChannel;

    @Column(name = "latest_version", nullable = false)
    @ColumnInfo(comment = "Latest version")
    private String latestVersion;

    @Column(name = "force_upgrade", nullable = false)
    @ColumnInfo(comment = "Force upgrade")
    private Boolean forceUpgrade;

}
