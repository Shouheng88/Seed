package com.seed.data.model.vo;

import com.seed.base.model.AbstractVo;
import com.seed.base.model.enums.DeviceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AppConfigVo
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0 
 * @date 2020/11/06 08:36
 */
@Data
@Repository
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AppConfigVo extends AbstractVo {

    private static final long serialVersionUID = 1L;

    private Long appId;

    private DeviceType platform;

    private String targetVersion;

    private String targetChannel;

    private String latestVersion;

    private Boolean forceUpgrade;

    /** App Config Details */
    private List<AppConfigDetailVo> configDetails;

}