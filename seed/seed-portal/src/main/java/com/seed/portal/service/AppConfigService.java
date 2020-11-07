package com.seed.portal.service;

import com.seed.base.model.PackVo;
import com.seed.data.model.so.AppConfigSo;
import com.seed.data.model.vo.AppConfigVo;

/**
 * AppConfigService
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0 
 * @date 2020/11/06 08:36
 */
public interface AppConfigService {

    PackVo<AppConfigVo> createAppConfig(AppConfigVo vo);

    PackVo<AppConfigVo> getAppConfig(Long primaryKey);

    PackVo<AppConfigVo> updateAppConfig(AppConfigVo vo);

    PackVo<AppConfigVo> deleteAppConfig(Long primaryKey);

    PackVo<AppConfigVo> searchAppConfig(AppConfigSo so);

    PackVo<AppConfigVo> searchAppConfigCount(AppConfigSo so);

}
