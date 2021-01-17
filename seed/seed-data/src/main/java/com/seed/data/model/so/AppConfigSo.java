package com.seed.data.model.so;

import com.seed.base.model.enums.DeviceType;
import com.seed.base.model.query.SearchObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * AppConfigSo
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0 
 * @date 2020/11/06 08:36
 */
@Data
@Repository
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AppConfigSo extends SearchObject {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "App id")
    private Long appId;

    @ApiModelProperty(value = "Current platform, Android, iOS etc.")
    private DeviceType platform;

    @ApiModelProperty(value = "Current version")
    private String currentVersion;

    @ApiModelProperty(value = "Current channel, Android channel")
    private String channel;

}
