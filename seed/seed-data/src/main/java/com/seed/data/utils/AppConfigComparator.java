package com.seed.data.utils;

import cn.hutool.core.util.StrUtil;
import com.seed.base.model.enums.DeviceType;
import com.seed.data.model.vo.AppConfigVo;

import java.util.Comparator;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 11:12
 */
public class AppConfigComparator implements Comparator<AppConfigVo> {

    private DeviceType platform;

    private String channel;

    private String version;

    public AppConfigComparator(DeviceType platform, String channel, String version) {
        this.platform = platform;
        this.channel = channel;
        this.version = version;
    }

    @Override
    public int compare(AppConfigVo o1, AppConfigVo o2) {
        int o1Cnt = (platform == o1.getPlatform() ? 1 : 0)
                + (StrUtil.equalsIgnoreCase(channel, o1.getTargetChannel()) ? 1 : 0)
                + (StrUtil.equalsIgnoreCase(version, o1.getTargetVersion()) ? 1 : 0);
        int o2Cnt = (platform == o2.getPlatform() ? 1 : 0)
                + (StrUtil.equalsIgnoreCase(channel, o2.getTargetChannel()) ? 1 : 0)
                + (StrUtil.equalsIgnoreCase(version, o2.getTargetVersion()) ? 1 : 0);
        return o1Cnt - o2Cnt;
    }
}

