package com.seed.portal.service.impl;

import com.seed.base.exception.DAOException;
import com.seed.base.model.PackVo;
import com.seed.base.utils.DozerBeanUtil;
import com.seed.data.dao.AppConfigDetailDAO;
import com.seed.data.model.po.AppConfigDetail;
import com.seed.data.model.so.AppConfigDetailSo;
import com.seed.data.model.vo.AppConfigDetailVo;
import com.seed.portal.service.AppConfigDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AppConfigDetailServiceImpl
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0 
 * @date 2020/11/06 08:36
 */
@Slf4j
@Service("appConfigDetailService")
public class AppConfigDetailServiceImpl implements AppConfigDetailService {

    private AppConfigDetailDAO appConfigDetailDAO;

    private DozerBeanUtil dozerBeanUtil;

    @Autowired
    public AppConfigDetailServiceImpl(AppConfigDetailDAO appConfigDetailDAO, DozerBeanUtil dozerBeanUtil) {
        this.appConfigDetailDAO = appConfigDetailDAO;
        this.dozerBeanUtil = dozerBeanUtil;
    }

    @Override
    public PackVo<AppConfigDetailVo> createAppConfigDetail(AppConfigDetailVo vo) {
        PackVo<AppConfigDetailVo> packVo = new PackVo<>();
        AppConfigDetail entity = dozerBeanUtil.convert(vo, AppConfigDetail.class);
        int ret = appConfigDetailDAO.insert(entity);
        if (ret != 1) {
            throw new DAOException("Failed to create entity : " + vo);
        }
        packVo.setVo(dozerBeanUtil.convert(entity, AppConfigDetailVo.class));
        return packVo;
    }

    @Override
    public PackVo<AppConfigDetailVo> getAppConfigDetail(Long primaryKey) {
        PackVo<AppConfigDetailVo> packVo = new PackVo<>();
        packVo.setVo(appConfigDetailDAO.selectVoByPrimaryKey(primaryKey));
        return packVo;
    }

    @Override
    public PackVo<AppConfigDetailVo> updateAppConfigDetail(AppConfigDetailVo vo) {
        PackVo<AppConfigDetailVo> packVo = new PackVo<>();
        AppConfigDetail po = dozerBeanUtil.convert(vo, AppConfigDetail.class);
        int ret = appConfigDetailDAO.update(po);
        if (ret != 1) {
            throw new DAOException("Failed to update entity : " + vo);
        }
        packVo.setVo(appConfigDetailDAO.selectVoByPrimaryKey(po.getId()));
        return packVo;
    }

    @Override
    public PackVo<AppConfigDetailVo> deleteAppConfigDetail(Long primaryKey) {
        PackVo<AppConfigDetailVo> packVo = new PackVo<>();
        int ret = appConfigDetailDAO.deleteByPrimaryKey(primaryKey);
        if (ret != 1) {
            throw new DAOException("Failed to delete entity with primary key : " + primaryKey);
        }
        return packVo;
    }

    @Override
    public PackVo<AppConfigDetailVo> searchAppConfigDetail(AppConfigDetailSo so) {
        PackVo<AppConfigDetailVo> packVo = new PackVo<>();
        List<AppConfigDetailVo> voList = appConfigDetailDAO.searchVosBySo(so);
        packVo.setVoList(voList);
        return packVo;
    }

    @Override
    public PackVo<AppConfigDetailVo> searchAppConfigDetailCount(AppConfigDetailSo so) {
        PackVo<AppConfigDetailVo> packVo = new PackVo<>();
        Long count = appConfigDetailDAO.searchCountBySo(so);
        packVo.setUdf1(count);
        return packVo;
    }

}
