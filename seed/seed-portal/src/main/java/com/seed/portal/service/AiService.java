package com.seed.portal.service;

import com.seed.base.model.PackVo;
import com.seed.data.ai.model.RecognizeResult;

/**
 * Baidu OCR by OKHttp+Retrofit. See Baidu API document at
 * <a href="https://cloud.baidu.com/doc/OCR/index.html">https://cloud.baidu.com/doc/OCR/index.html</a>
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 0:03
 */
public interface AiService {

    /**
     * 获取百度文字识别接口的 Token
     *
     * @return 请求响应
     */
    PackVo<String> token();

    /**
     * 通用的文字识别接口
     *
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
    PackVo<RecognizeResult> generalBasic(String image, String url, String languageType,
                                         boolean detectDirection, boolean detectLanguage, boolean probability);

    /**
     * 通用的文字识别接口（含位置信息）
     *
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
    PackVo<RecognizeResult> general(String image, String url, String languageType, boolean detectDirection,
                                    boolean detectLanguage, boolean vertexesLocation, boolean probability);

    /**
     * 通用文字识别（高精度版）
     *
     * @param image 图像数据，base64编码后进行urlencode，要求base64编码和urlencode后大小不超过4M，最短边至少15px，
     *              最长边最大4096px,支持jjpg/jpeg/png/bmp格式，当image字段存在时url字段失效
     * @param detectDirection 是否检测图像朝向，默认不检测，即：false。朝向是指输入图像是正常方向、逆时针旋转90/180/270度。可选值包括:
     * - true：检测朝向；
     * - false：不检测朝向。
     * @param probability 是否返回识别结果中每一行的置信度
     * @return 识别结果
     */
    PackVo<RecognizeResult> accurateBasic(String image, boolean detectDirection, boolean probability);

    /**
     * 通用文字识别（高精度含位置版）
     *
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
    PackVo<RecognizeResult> accurate(String image, String recognizeGranularity, boolean detectDirection,
                                     boolean vertexesLocation, boolean probability);

}
