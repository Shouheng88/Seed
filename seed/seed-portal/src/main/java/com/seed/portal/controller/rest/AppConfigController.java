package com.seed.portal.controller.rest;

import com.seed.base.annotation.ApiInfo;
import com.seed.base.model.PackVo;
import com.seed.base.model.business.BusinessRequest;
import com.seed.base.model.business.BusinessResponse;
import com.seed.data.model.so.AppConfigSo;
import com.seed.data.model.vo.AppConfigVo;
import com.seed.portal.service.AppConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 11:06
 */
@Slf4j
@RestController
@RequestMapping(path = "/rest/config")
@Api(tags = "Get App Config")
public class AppConfigController {

    @Resource
    private AppConfigService appConfigService;

    @ApiInfo(auth = false)
    @ApiOperation("Get App config")
    @PostMapping(value = "/getAppConfig")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "apiParams.appId", value = "current app id", required = true),
            @ApiImplicitParam(name = "apiParams.platform", value = "current platform", required = true),
            @ApiImplicitParam(name = "apiParams.currentVersion", value = "current version", required = true),
            @ApiImplicitParam(name = "apiParams.channel", value = "current channel")})
    public BusinessResponse<AppConfigVo> getAppConfig(@RequestBody BusinessRequest<AppConfigSo> request) {
        PackVo<AppConfigVo> packVo = appConfigService.searchAppConfig(request.getApiParams());
        return packVo.toResponse();
    }

}
