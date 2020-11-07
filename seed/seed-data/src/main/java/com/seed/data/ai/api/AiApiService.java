package com.seed.data.ai.api;

import com.seed.data.ai.model.RecognizeResult;
import com.seed.data.ai.model.TokenResponse;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Baidu AI api service
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 0:02
 */
public interface AiApiService {

    /**
     * 获取百度文字识别接口的 Token
     *
     * @param clientId     注册应用的 API key
     * @param clientSecret 注册应用的 Secret Key
     * @return             请求响应
     */
    @GET(value = "oauth/2.0/token?grant_type=client_credentials")
    Call<TokenResponse> token(@Query("client_id") String clientId, @Query("client_secret") String clientSecret);

    /**
     * 通用的文字识别接口
     *
     * @param accessToken token
     * @param image 图像数据，base64编码后进行urlencode，要求base64编码和urlencode后大小不超过4M，最短边至少15px，
     *              最长边最大4096px,支持jjpg/jpeg/png/bmp格式，当image字段存在时url字段失效
     * @param url 图片完整URL，URL长度不超过1024字节，URL对应的图片base64编码后大小不超过4M，最短边至少15px，
     *            最长边最大4096px,支持jpg/jpeg/png/bmp格式，当image字段存在时url字段失效，不支持https的图片链接
     * @param languageType 识别语言类型，默认为CHN_ENG。可选值包括：
     * - CHN_ENG：中英文混合；
     * - ENG：英文；
     * - POR：葡萄牙语；
     * - FRE：法语；
     * - GER：德语；
     * - ITA：意大利语；
     * - SPA：西班牙语；
     * - RUS：俄语；
     * - JAP：日语；
     * - KOR：韩语
     * @param detectDirection 是否检测图像朝向，默认不检测，即：false。朝向是指输入图像是正常方向、逆时针旋转90/180/270度。可选值包括:
     * - true：检测朝向；
     * - false：不检测朝向。
     * @param detectLanguage 是否检测语言，默认不检测。当前支持（中文、英语、日语、韩语）
     * @param probability 是否返回识别结果中每一行的置信度
     * @return 识别结果
     */
    @FormUrlEncoded
    @POST(value = "rest/2.0/ocr/v1/general_basic")
    Call<RecognizeResult> generalBasic(
            @Query("access_token") String accessToken,
            @Field("image") String image,
            @Field("url") String url,
            @Field("language_type") String languageType,
            @Field("detect_direction") boolean detectDirection,
            @Field("detect_language") boolean detectLanguage,
            @Field("probability") boolean probability);

    /**
     * 通用的文字识别接口（含位置信息）
     *
     * @param accessToken token
     * @param image 图像数据，base64编码后进行urlencode，要求base64编码和urlencode后大小不超过4M，最短边至少15px，
     *              最长边最大4096px,支持jjpg/jpeg/png/bmp格式，当image字段存在时url字段失效
     * @param url 图片完整URL，URL长度不超过1024字节，URL对应的图片base64编码后大小不超过4M，最短边至少15px，
     *            最长边最大4096px,支持jpg/jpeg/png/bmp格式，当image字段存在时url字段失效，不支持https的图片链接
     * @param languageType 识别语言类型，默认为CHN_ENG。可选值包括：
     * - CHN_ENG：中英文混合；
     * - ENG：英文；
     * - POR：葡萄牙语；
     * - FRE：法语；
     * - GER：德语；
     * - ITA：意大利语；
     * - SPA：西班牙语；
     * - RUS：俄语；
     * - JAP：日语；
     * - KOR：韩语
     * @param detectDirection 是否检测图像朝向，默认不检测，即：false。朝向是指输入图像是正常方向、逆时针旋转90/180/270度。可选值包括:
     * - true：检测朝向；
     * - false：不检测朝向。
     * @param detectLanguage 是否检测语言，默认不检测。当前支持（中文、英语、日语、韩语）
     * @param vertexesLocation 是否返回文字外接多边形顶点位置，不支持单字位置。默认为false
     * @param probability 是否返回识别结果中每一行的置信度
     * @return 识别结果
     */
    @FormUrlEncoded
    @POST(value = "rest/2.0/ocr/v1/general")
    Call<RecognizeResult> general(
            @Query("access_token") String accessToken,
            @Field("image") String image,
            @Field("url") String url,
            @Field("language_type") String languageType,
            @Field("detect_direction") boolean detectDirection,
            @Field("detect_language") boolean detectLanguage,
            @Field("vertexes_location") boolean vertexesLocation,
            @Field("probability") boolean probability);

    /**
     * 通用文字识别（高精度版）
     *
     * @param accessToken token
     * @param image 图像数据，base64编码后进行urlencode，要求base64编码和urlencode后大小不超过4M，最短边至少15px，
     *              最长边最大4096px,支持jjpg/jpeg/png/bmp格式，当image字段存在时url字段失效
     * @param detectDirection 是否检测图像朝向，默认不检测，即：false。朝向是指输入图像是正常方向、逆时针旋转90/180/270度。可选值包括:
     * - true：检测朝向；
     * - false：不检测朝向。
     * @param probability 是否返回识别结果中每一行的置信度
     * @return 识别结果
     */
    @FormUrlEncoded
    @POST(value = "rest/2.0/ocr/v1/accurate_basic")
    Call<RecognizeResult> accurateBasic(
            @Query("access_token") String accessToken,
            @Field("image") String image,
            @Field("detect_direction") boolean detectDirection,
            @Field("probability") boolean probability);

    /**
     * 通用文字识别（高精度含位置版）
     *
     * @param accessToken token
     * @param image 图像数据，base64编码后进行urlencode，要求base64编码和urlencode后大小不超过4M，最短边至少15px，
     *              最长边最大4096px,支持jjpg/jpeg/png/bmp格式，当image字段存在时url字段失效
     * @param recognizeGranularity 是否定位单字符位置，big：不定位单字符位置，默认值；small：定位单字符位置
     * @param detectDirection 是否检测图像朝向，默认不检测，即：false。朝向是指输入图像是正常方向、逆时针旋转90/180/270度。可选值包括:
     * - true：检测朝向；
     * - false：不检测朝向。
     * @param vertexesLocation 是否返回文字外接多边形顶点位置，不支持单字位置。默认为false
     * @param probability 是否返回识别结果中每一行的置信度
     * @return 响应结果
     */
    @FormUrlEncoded
    @POST(value = "rest/2.0/ocr/v1/accurate")
    Call<RecognizeResult> accurate(
            @Query("access_token") String accessToken,
            @Field("image") String image,
            @Field("recognize_granularity") String recognizeGranularity,
            @Field("detect_direction") boolean detectDirection,
            @Field("vertexes_location") boolean vertexesLocation,
            @Field("probability") boolean probability);

}
