package com.seed.portal.service.impl;

import cn.hutool.core.util.StrUtil;
import com.seed.base.exception.DAOException;
import com.seed.base.model.PackVo;
import com.seed.base.model.enums.DeviceType;
import com.seed.base.model.query.Sort;
import com.seed.base.model.query.SortDir;
import com.seed.base.utils.DozerBeanUtil;
import com.seed.data.dao.AppConfigDAO;
import com.seed.data.model.po.AppConfig;
import com.seed.data.model.so.AppConfigDetailSo;
import com.seed.data.model.so.AppConfigSo;
import com.seed.data.model.vo.AppConfigDetailVo;
import com.seed.data.model.vo.AppConfigVo;
import com.seed.data.utils.AppConfigComparator;
import com.seed.portal.service.AppConfigDetailService;
import com.seed.portal.service.AppConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * AppConfigServiceImpl
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0 
 * @date 2020/11/06 08:36
 */
@Slf4j
@Service("appConfigService")
public class AppConfigServiceImpl implements AppConfigService {

    @Resource
    private AppConfigDAO appConfigDAO;
    @Resource
    private DozerBeanUtil dozerBeanUtil;
    @Resource
    private AppConfigDetailService appConfigDetailService;

    @Override
    public PackVo<AppConfigVo> createAppConfig(AppConfigVo vo) {
        PackVo<AppConfigVo> packVo = new PackVo<>();
        AppConfig entity = dozerBeanUtil.convert(vo, AppConfig.class);
        int ret = appConfigDAO.insert(entity);
        if (ret != 1) {
            throw new DAOException("Failed to create entity : " + vo);
        }
        packVo.setVo(dozerBeanUtil.convert(entity, AppConfigVo.class));
        return packVo;
    }

    @Override
    public PackVo<AppConfigVo> getAppConfig(Long primaryKey) {
        PackVo<AppConfigVo> packVo = new PackVo<>();
        packVo.setVo(appConfigDAO.selectVoByPrimaryKey(primaryKey));
        return packVo;
    }

    @Override
    public PackVo<AppConfigVo> updateAppConfig(AppConfigVo vo) {
        PackVo<AppConfigVo> packVo = new PackVo<>();
        AppConfig po = dozerBeanUtil.convert(vo, AppConfig.class);
        int ret = appConfigDAO.update(po);
        if (ret != 1) {
            throw new DAOException("Failed to update entity : " + vo);
        }
        packVo.setVo(appConfigDAO.selectVoByPrimaryKey(po.getId()));
        return packVo;
    }

    @Override
    public PackVo<AppConfigVo> deleteAppConfig(Long primaryKey) {
        PackVo<AppConfigVo> packVo = new PackVo<>();
        int ret = appConfigDAO.deleteByPrimaryKey(primaryKey);
        if (ret != 1) {
            throw new DAOException("Failed to delete entity with primary key : " + primaryKey);
        }
        return packVo;
    }

    @Override
    public PackVo<AppConfigVo> searchAppConfig(AppConfigSo so) {
        PackVo<AppConfigVo> packVo = new PackVo<>();
        AppConfigSo searchSo = new AppConfigSo();
        searchSo.setAppId(so.getAppId());
        searchSo.addSort(Sort.of("id", SortDir.ASC));
        // Get all app configurations
        List<AppConfigVo> voList = appConfigDAO.searchVosBySo(searchSo);
        voList = voList.stream().filter(appConfigVo -> {
            boolean platformMatch = appConfigVo.getPlatform() == so.getPlatform()
                    || appConfigVo.getPlatform() == DeviceType.UNKNOWN;
            boolean channelMatch = StrUtil.equalsIgnoreCase(appConfigVo.getTargetChannel(), "all")
                    || StrUtil.equalsIgnoreCase(appConfigVo.getTargetChannel(), so.getChannel());
            boolean versionMatch = StrUtil.equalsIgnoreCase(appConfigVo.getTargetVersion(), "all")
                    || StrUtil.equalsIgnoreCase(appConfigVo.getTargetVersion(), so.getCurrentVersion());
            return channelMatch && versionMatch && platformMatch;
        }).collect(Collectors.toList());
        // Sort app configurations
        Optional<AppConfigVo> optional = voList.stream().max(
                new AppConfigComparator(so.getPlatform(), so.getChannel(), so.getCurrentVersion()));
        // Get app config details
        optional.ifPresent(appConfigVo -> {
            AppConfigDetailSo appConfigDetailSo = new AppConfigDetailSo();
            appConfigDetailSo.setAppConfigId(appConfigVo.getId());
            PackVo<AppConfigDetailVo> detailVoPackVo = appConfigDetailService.searchAppConfigDetail(appConfigDetailSo);
            appConfigVo.setConfigDetails(detailVoPackVo.getVoList());
            packVo.setVo(appConfigVo);
        });
        return packVo;
    }

    @Override
    public PackVo<AppConfigVo> searchAppConfigCount(AppConfigSo so) {
        PackVo<AppConfigVo> packVo = new PackVo<>();
        Long count = appConfigDAO.searchCountBySo(so);
        packVo.setUdf1(count);
        return packVo;
    }

}
