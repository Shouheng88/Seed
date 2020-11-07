package com.seed.data.api

/**
 * Get App Config
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 2020/11/07 02:39
 */
interface AppConfigService {

    /**
     * Api: Get App config
     * 
     * Desc: 
     * 
     * Parameter required: 
     * 1. apiParams.appId : current app id (Required): 
     * 2. apiParams.platform : current platform (Required): 
     * 3. apiParams.currentVersion : current version (Required): 
     * 4. apiParams.channel : current channel
     */
    @POST("/rest/config/getAppConfig")
    fun getAppConfigAsync(@Body request: BusinessRequest<AppConfigSo>): Deferred<BusinessResponse<AppConfigVo>>

}

