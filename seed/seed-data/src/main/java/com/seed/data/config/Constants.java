package com.seed.data.config;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 0:12
 */
public interface Constants {

    /** Baidu OCR request timeout in seconds */
    int THIRD_PART_API_TIMEOUT_IN_SECONDS       = 10;
    /** Baidu OCR request base url */
    String URL_BAIDU_API_BASE_URL               = "https://aip.baidubce.com/";

    /** If you visit given url 5 times in one seconds, then you will be limited */
    int NORMAL_MAX_REQUEST_COUNT_IN_SECOND      = 5;
    /** The request limit in seconds, you can't visit again if limited in 30 minutes */
    int NORMAL_REQUEST_LIMIT_IN_SECONDS         = 1800;

}
