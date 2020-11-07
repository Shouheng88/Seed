package com.seed.portal;

import com.seed.data.model.so.AppConfigSo;
import com.seed.data.model.vo.AppConfigVo;
import com.seed.portal.base.MockTestUtil;
import com.seed.portal.base.SpringBaseTest;
import com.seed.portal.service.AppConfigService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * AppConfigServiceTest
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0 
 * @date 2020/11/06 08:36
 */
public class AppConfigServiceTest extends SpringBaseTest {

    @Autowired
    private AppConfigService appConfigService;

    public AppConfigVo create() {
        AppConfigVo vo = MockTestUtil.getJavaBean(AppConfigVo.class);
        return appConfigService.createAppConfig(vo).getVo();
    }

    @Test
    public void testCreate() {
        AppConfigVo vo = this.create();
        Assert.assertTrue(vo!= null);
    }

    @Test
    public void testSearch() {
        AppConfigVo vo = this.create();
        AppConfigSo so = new AppConfigSo();
        List<AppConfigVo> voList = appConfigService.searchAppConfig(so).getVoList();
        Assert.assertTrue(voList != null && voList.size() > 0);
    }

    @Test
    public void testSearchCount() {
        AppConfigVo vo = this.create();
        AppConfigSo so = new AppConfigSo();
        long count = appConfigService.searchAppConfigCount(so).getUdf1();
        Assert.assertTrue(count > 0);
    }

    @Test
    public void testUpdate() {
        AppConfigVo vo = this.create();
        AppConfigVo voTemp = appConfigService.getAppConfig(vo.getId()).getVo();
        voTemp.setRemark("remark was updated");
        AppConfigVo updateRes = appConfigService.updateAppConfig(voTemp).getVo();
        Assert.assertTrue(updateRes !=null && "remark was updated".equals(updateRes.getRemark()));
    }

}
