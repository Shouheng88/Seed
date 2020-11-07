package com.seed.data.model.so;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.seed.base.annotation.HideLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 0:21
 */
@Data
@Repository
@EqualsAndHashCode
@JsonFilter("logFilter")
@ToString(callSuper = true, exclude = {"image"})
@Api(value = "OCR request object")
public class AiTextSo {

    @HideLog
    @ApiModelProperty(value = "图像数据，base64 编码后进行 urlencode，要求 base64 编码和 urlencode 后大小不超过4M，最短边至少 15px，" +
            "最长边最大 4096px,支持 jjpg/jpeg/png/bmp 格式，当 image 字段存在时url字段失效。")
    private String image;

    @ApiModelProperty(value = "图片完整 URL，URL 长度不超过 1024 字节，URL 对应的图片 base64 编码后大小不超过 4M，最短边至少 15px，" +
            "最长边最大 4096px,支持 jpg/jpeg/png/bmp 格式，当 image 字段存在时 url 字段失效，不支持 https 的图片链接")
    private String url;

    @ApiModelProperty(value = "识别语言类型，默认为 CHN_ENG。可选值包括：\n" +
            "- CHN_ENG：中英文混合；\n" +
            "- ENG：英文；\n" +
            "- POR：葡萄牙语；\n" +
            "- FRE：法语；\n" +
            "- GER：德语；\n" +
            "- ITA：意大利语；\n" +
            "- SPA：西班牙语；\n" +
            "- RUS：俄语；\n" +
            "- JAP：日语；\n" +
            "- KOR：韩语")
    private String languageType;

    @ApiModelProperty(value = "是否定位单字符位置，big：不定位单字符位置，默认值；small：定位单字符位置")
    private String recognizeGranularity;

    @ApiModelProperty(value = "是否检测图像朝向，默认不检测，即：false。朝向是指输入图像是正常方向、逆时针旋转 90/180/270 度。可选值包括:" +
            "- true：检测朝向；" +
            "- false：不检测朝向。")
    private boolean detectDirection;

    @ApiModelProperty(value = "否检测语言，默认不检测。当前支持（中文、英语、日语、韩语）")
    private boolean detectLanguage;

    @ApiModelProperty(value = "是否返回文字外接多边形顶点位置，不支持单字位置。默认为 false")
    private boolean vertexesLocation;

    @ApiModelProperty(value = "是否返回识别结果中每一行的置信度")
    private boolean probability;

}
