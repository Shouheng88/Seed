package com.seed.portal.service.impl;

import cn.hutool.core.util.StrUtil;
import com.seed.base.annotation.HideLog;
import com.seed.base.model.PackVo;
import com.seed.base.model.enums.ResultCode;
import com.seed.data.ai.api.AiApiService;
import com.seed.data.ai.model.RecognizeResult;
import com.seed.data.ai.model.TokenResponse;
import com.seed.data.config.Constants;
import com.seed.data.manager.RedisHelper;
import com.seed.portal.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 0:07
 */
@Slf4j
@Service("aiService")
@Transactional(rollbackFor = Exception.class)
public class AiServiceImpl implements AiService {

    /** Baidu OCR client id */
    @Value(value = "${baidu.api.client-id}")
    private String baiduClientId;

    /** Baidu OCR client secret */
    @Value(value = "${baidu.api.client-secret}")
    private String baiduClientSecret;

    private RedisHelper redisHelper;

    @Autowired
    public AiServiceImpl(RedisHelper redisHelper) {
        this.redisHelper = redisHelper;
    }

    @Override
    public PackVo<String> token()  {
        String token = redisHelper.getBaiduApiToken();
        if (StrUtil.isEmpty(token)) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.URL_BAIDU_API_BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
            AiApiService service = retrofit.create(AiApiService.class);
            Response<TokenResponse> response;
            try {
                response = service.token(baiduClientId, baiduClientSecret).execute();
                if (response.isSuccessful() && response.body() != null) {
                    redisHelper.saveBaiduApiToken(response.body().getAccess_token(), response.body().getExpires_in());
                    return PackVo.success(response.body().getAccess_token());
                } else {
                    log.error("Failed to request [{}] [{}]", response.code(), response.body());
                    return PackVo.fail(ResultCode.THIRD_PART_REQUEST_ERROR);
                }
            } catch (IOException e) {
                log.error("Failed to request due to IOException : ", e);
                return PackVo.fail(ResultCode.THIRD_PART_REQUEST_ERROR);
            }
        }
        return PackVo.success(token);
    }

    @Override
    public PackVo<RecognizeResult> generalBasic(@HideLog String image, String url, String languageType,
                                                boolean detectDirection, boolean detectLanguage, boolean probability) {
        PackVo<String> packToken = token();
        String token;
        if (packToken.isSuccess() && !StrUtil.isEmpty((token = packToken.getVo()))) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.URL_BAIDU_API_BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
            AiApiService service = retrofit.create(AiApiService.class);
            try {
                Response<RecognizeResult> response = service
                        .generalBasic(token, image, url, languageType, detectDirection, detectLanguage, probability)
                        .execute();
                if (response.isSuccessful() && response.body() != null) {
                    return PackVo.success(response.body());
                } else {
                    log.error("Failed to request [{}] [{}] [{}] [{}]", response.code(), response.body());
                    return PackVo.fail(ResultCode.THIRD_PART_REQUEST_ERROR);
                }
            } catch (IOException e) {
                log.error("Failed to request due to io exception :", e);
                return PackVo.fail(ResultCode.THIRD_PART_REQUEST_ERROR);
            }
        }
        return PackVo.fail(ResultCode.THIRD_PART_REQUEST_ERROR);
    }

    @Override
    public PackVo<RecognizeResult> general(@HideLog String image, String url, String languageType, boolean detectDirection,
                                           boolean detectLanguage, boolean vertexesLocation, boolean probability) {
        PackVo<String> packToken = token();
        String token;
        if (packToken.isSuccess() && !StrUtil.isEmpty((token = packToken.getVo()))) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.URL_BAIDU_API_BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
            AiApiService service = retrofit.create(AiApiService.class);
            try {
                Response<RecognizeResult> response = service
                        .general(token, image, url, languageType, detectDirection, detectLanguage, vertexesLocation, probability)
                        .execute();
                if (response.isSuccessful() && response.body() != null) {
                    return PackVo.success(response.body());
                } else {
                    log.error("Failed to request [{}] [{}]", response.code(), response.body());
                    return PackVo.fail(ResultCode.THIRD_PART_REQUEST_ERROR);
                }
            } catch (IOException e) {
                log.error("Failed to request due to io exception :", e);
                return PackVo.fail(ResultCode.THIRD_PART_REQUEST_ERROR);
            }
        }
        return PackVo.fail(ResultCode.THIRD_PART_REQUEST_ERROR);
    }

    @Override
    public PackVo<RecognizeResult> accurateBasic(@HideLog String image, boolean detectDirection, boolean probability) {
        PackVo<String> packToken = token();
        String token;
        if (packToken.isSuccess() && !StrUtil.isEmpty((token = packToken.getVo()))) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.URL_BAIDU_API_BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
            AiApiService service = retrofit.create(AiApiService.class);
            try {
                Response<RecognizeResult> response = service
                        .accurateBasic(token, image, detectDirection, probability)
                        .execute();
                if (response.isSuccessful() && response.body() != null) {
                    return PackVo.success(response.body());
                } else {
                    log.error("Failed to request [{}] [{}]", response.code(), response.body());
                    return PackVo.fail(ResultCode.THIRD_PART_REQUEST_ERROR);
                }
            } catch (IOException e) {
                log.error("Failed to request due to io exception :", e);
                return PackVo.fail(ResultCode.THIRD_PART_REQUEST_ERROR);
            }
        }
        return PackVo.fail(ResultCode.THIRD_PART_REQUEST_ERROR);
    }

    @Override
    public PackVo<RecognizeResult> accurate(@HideLog String image,
                                            String recognizeGranularity,
                                            boolean detectDirection,
                                            boolean vertexesLocation,
                                            boolean probability) {
        PackVo<String> packToken = token();
        String token;
        if (packToken.isSuccess() && !StrUtil.isEmpty((token = packToken.getVo()))) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.URL_BAIDU_API_BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
            AiApiService service = retrofit.create(AiApiService.class);
            try {
                Response<RecognizeResult> response = service
                        .accurate(token, image, recognizeGranularity, detectDirection, vertexesLocation, probability)
                        .execute();
                if (response.isSuccessful() && response.body() != null) {
                    return PackVo.success(response.body());
                } else {
                    log.error("Failed to request [{}] [{}]", response.code(), response.body());
                    return PackVo.fail(ResultCode.THIRD_PART_REQUEST_ERROR);
                }
            } catch (IOException e) {
                log.error("Failed to request due to io exception :", e);
                return PackVo.fail(ResultCode.THIRD_PART_REQUEST_ERROR);
            }
        }
        return PackVo.fail(ResultCode.THIRD_PART_REQUEST_ERROR);
    }

}
