package com.seed.portal;

import com.seed.data.model.so.AppConfigDetailSo;
import com.seed.data.model.vo.AppConfigDetailVo;
import com.seed.portal.base.MockTestUtil;
import com.seed.portal.base.SpringBaseTest;
import com.seed.portal.service.AppConfigDetailService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * AppConfigDetailServiceTest
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0 
 * @date 2020/11/06 08:36
 */
public class AppConfigDetailServiceTest extends SpringBaseTest {

    @Autowired
    private AppConfigDetailService appConfigDetailService;

    public AppConfigDetailVo create() {
        AppConfigDetailVo vo = MockTestUtil.getJavaBean(AppConfigDetailVo.class);
        return appConfigDetailService.createAppConfigDetail(vo).getVo();
    }

    @Test
    public void testCreate() {
        AppConfigDetailVo vo = this.create();
        Assert.assertTrue(vo!= null);
    }

    @Test
    public void testSearch() {
        AppConfigDetailVo vo = this.create();
        AppConfigDetailSo so = new AppConfigDetailSo();
        List<AppConfigDetailVo> voList = appConfigDetailService.searchAppConfigDetail(so).getVoList();
        Assert.assertTrue(voList != null && voList.size() > 0);
    }

    @Test
    public void testSearchCount() {
        AppConfigDetailVo vo = this.create();
        AppConfigDetailSo so = new AppConfigDetailSo();
        long count = appConfigDetailService.searchAppConfigDetailCount(so).getUdf1();
        Assert.assertTrue(count > 0);
    }

    @Test
    public void testUpdate() {
        AppConfigDetailVo vo = this.create();
        AppConfigDetailVo voTemp = appConfigDetailService.getAppConfigDetail(vo.getId()).getVo();
        voTemp.setRemark("remark was updated");
        AppConfigDetailVo updateRes = appConfigDetailService.updateAppConfigDetail(voTemp).getVo();
        Assert.assertTrue(updateRes !=null && "remark was updated".equals(updateRes.getRemark()));
    }

}
