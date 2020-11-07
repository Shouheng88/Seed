package com.seed.data.api

/**
 * Baidu OCR api
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 2020/11/07 02:39
 */
interface AiService {

    /**
     * Api: General basic OCR
     * 
     * Desc: 
     * 
     * Parameter required: 
     */
    @POST("/rest/ai/text/generalBasic")
    fun generalBasicAsync(@Body request: BusinessRequest<AiTextSo>): Deferred<WebAsyncTask<BusinessResponse<RecognizeResult>>>

    /**
     * Api: General OCR (accurate)
     * 
     * Desc: 
     * - Auth required
     * 
     * Parameter required: 
     */
    @POST("/rest/ai/text/accurateBasic")
    fun accurateBasicAsync(@Body request: BusinessRequest<AiTextSo>): Deferred<WebAsyncTask<BusinessResponse<RecognizeResult>>>

    /**
     * Api: General OCR (accurate) with location
     * 
     * Desc: 
     * - Auth required
     * 
     * Parameter required: 
     */
    @POST("/rest/ai/text/accurate")
    fun accurateAsync(@Body request: BusinessRequest<AiTextSo>): Deferred<WebAsyncTask<BusinessResponse<RecognizeResult>>>

    /**
     * Api: General OCR with location
     * 
     * Desc: 
     * - Auth required
     * 
     * Parameter required: 
     */
    @POST("/rest/ai/text/general")
    fun generalAsync(@Body request: BusinessRequest<AiTextSo>): Deferred<WebAsyncTask<BusinessResponse<RecognizeResult>>>

}

