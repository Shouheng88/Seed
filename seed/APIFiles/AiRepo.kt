package com.seed.data.repo

/**
 * Baidu OCR api
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 2020/11/07 02:39
 */
class AiRepo private constructor() {

    /**
     * General basic OCR
     */
    fun generalBasic(listener: OnResultListener?) {
        GlobalScope.launch(Dispatchers.Main) {
            val res = withContext(Dispatchers.IO) {
                Net.connect(Server.get(AiService::class.java)
                    .generalBasicAsync(BusinessRequest.get(AiTextSo().apply {
                    })))
            }
            if (res.success) {
                listener?.onSuccess(res.data!!)
            } else {
                listener?.onFailed(res.code!!, res.message!!)
            }
        }
    }

    /**
     * General OCR (accurate)
     */
    fun accurateBasic(listener: OnResultListener?) {
        GlobalScope.launch(Dispatchers.Main) {
            val res = withContext(Dispatchers.IO) {
                Net.connect(Server.get(AiService::class.java)
                    .accurateBasicAsync(BusinessRequest.get(AiTextSo().apply {
                    })))
            }
            if (res.success) {
                listener?.onSuccess(res.data!!)
            } else {
                listener?.onFailed(res.code!!, res.message!!)
            }
        }
    }

    /**
     * General OCR (accurate) with location
     */
    fun accurate(listener: OnResultListener?) {
        GlobalScope.launch(Dispatchers.Main) {
            val res = withContext(Dispatchers.IO) {
                Net.connect(Server.get(AiService::class.java)
                    .accurateAsync(BusinessRequest.get(AiTextSo().apply {
                    })))
            }
            if (res.success) {
                listener?.onSuccess(res.data!!)
            } else {
                listener?.onFailed(res.code!!, res.message!!)
            }
        }
    }

    /**
     * General OCR with location
     */
    fun general(listener: OnResultListener?) {
        GlobalScope.launch(Dispatchers.Main) {
            val res = withContext(Dispatchers.IO) {
                Net.connect(Server.get(AiService::class.java)
                    .generalAsync(BusinessRequest.get(AiTextSo().apply {
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
        val instance: AiRepo by lazy { AiRepo() }
    }
}
