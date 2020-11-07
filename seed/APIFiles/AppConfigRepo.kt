package com.seed.data.repo

/**
 * Get App Config
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 2020/11/07 02:39
 */
class AppConfigRepo private constructor() {

    /**
     * Get App config
     */
    fun getAppConfig(appId: String, platform: String, currentVersion: String, channel: String, listener: OnResultListener?) {
        GlobalScope.launch(Dispatchers.Main) {
            val res = withContext(Dispatchers.IO) {
                Net.connect(Server.get(AppConfigService::class.java)
                    .getAppConfigAsync(BusinessRequest.get(AppConfigSo().apply {
                        this.appId = appId
                        this.platform = platform
                        this.currentVersion = currentVersion
                        this.channel = channel
                    })))
            }
            if (res.success) {
                listener?.onSuccess(res.data!!)
            } else {
                listener?.onFailed(res.code!!, res.message!!)
            }
        }
    }

    companion object {
        val instance: AppConfigRepo by lazy { AppConfigRepo() }
    }
}
