package com.seed.portal.service;

import com.seed.base.model.PackVo;
import com.seed.data.model.so.AppConfigDetailSo;
import com.seed.data.model.vo.AppConfigDetailVo;

/**
 * AppConfigDetailService
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0 
 * @date 2020/11/06 08:36
 */
public interface AppConfigDetailService {

    PackVo<AppConfigDetailVo> createAppConfigDetail(AppConfigDetailVo vo);

    PackVo<AppConfigDetailVo> getAppConfigDetail(Long primaryKey);

    PackVo<AppConfigDetailVo> updateAppConfigDetail(AppConfigDetailVo vo);

    PackVo<AppConfigDetailVo> deleteAppConfigDetail(Long primaryKey);

    PackVo<AppConfigDetailVo> searchAppConfigDetail(AppConfigDetailSo so);

    PackVo<AppConfigDetailVo> searchAppConfigDetailCount(AppConfigDetailSo so);

}
