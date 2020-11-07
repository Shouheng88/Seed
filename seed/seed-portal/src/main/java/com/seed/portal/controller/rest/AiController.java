package com.seed.portal.controller.rest;

import com.seed.base.annotation.ApiInfo;
import com.seed.base.annotation.Limiting;
import com.seed.base.model.PackVo;
import com.seed.base.model.business.BusinessRequest;
import com.seed.base.model.business.BusinessResponse;
import com.seed.base.model.enums.ResultCode;
import com.seed.data.ai.model.RecognizeResult;
import com.seed.data.config.Constants;
import com.seed.data.model.so.AiTextSo;
import com.seed.portal.service.AiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 0:18
 */
@Slf4j
@RestController
@Api(tags = "Baidu OCR api")
@RequestMapping(path = "/rest/ai")
public class AiController {

    private AiService aiService;

    @Autowired
    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @ApiOperation("General basic OCR")
    @PostMapping(value = "/text/generalBasic")
    @ApiInfo(auth = false, limiting = @Limiting(limit = 50, timeUnit = TimeUnit.DAYS))
    public WebAsyncTask<BusinessResponse<RecognizeResult>> generalBasic(@RequestBody BusinessRequest<AiTextSo> request) {
        return getWebAsyncTask(() -> {
            PackVo<RecognizeResult> packVo = aiService.generalBasic(
                    request.getApiParams().getImage(),
                    request.getApiParams().getUrl(),
                    request.getApiParams().getLanguageType(),
                    request.getApiParams().isDetectDirection(),
                    request.getApiParams().isDetectLanguage(),
                    request.getApiParams().isProbability());
            return packVo.toResponse();
        });
    }

    @ApiOperation("General OCR with location")
    @PostMapping(value = "/text/general")
    @ApiInfo(limiting = @Limiting(limit = 50, timeUnit = TimeUnit.DAYS))
    public WebAsyncTask<BusinessResponse<RecognizeResult>> general(@RequestBody BusinessRequest<AiTextSo> request) {
        return getWebAsyncTask(() -> {
            PackVo<RecognizeResult> packVo = aiService.general(
                    request.getApiParams().getImage(),
                    request.getApiParams().getUrl(),
                    request.getApiParams().getLanguageType(),
                    request.getApiParams().isDetectDirection(),
                    request.getApiParams().isDetectLanguage(),
                    request.getApiParams().isVertexesLocation(),
                    request.getApiParams().isProbability());
            return packVo.toResponse();
        });
    }

    @ApiOperation("General OCR (accurate)")
    @PostMapping(value = "/text/accurateBasic")
    @ApiInfo(limiting = @Limiting(limit = 50, timeUnit = TimeUnit.DAYS))
    public WebAsyncTask<BusinessResponse<RecognizeResult>> accurateBasic(@RequestBody BusinessRequest<AiTextSo> request) {
        return getWebAsyncTask(() -> {
            PackVo<RecognizeResult> packVo = aiService.accurateBasic(
                    request.getApiParams().getImage(),
                    request.getApiParams().isDetectDirection(),
                    request.getApiParams().isProbability());
            return packVo.toResponse();
        });
    }

    @ApiOperation("General OCR (accurate) with location")
    @PostMapping(value = "/text/accurate")
    @ApiInfo(limiting = @Limiting(limit = 50, timeUnit = TimeUnit.DAYS))
    public WebAsyncTask<BusinessResponse<RecognizeResult>> accurate(@RequestBody BusinessRequest<AiTextSo> request) {
        return getWebAsyncTask(() -> {
            PackVo<RecognizeResult> packVo = aiService.accurate(
                    request.getApiParams().getImage(),
                    request.getApiParams().getRecognizeGranularity(),
                    request.getApiParams().isDetectDirection(),
                    request.getApiParams().isVertexesLocation(),
                    request.getApiParams().isProbability());
            return packVo.toResponse();
        });
    }

    private WebAsyncTask<BusinessResponse<RecognizeResult>> getWebAsyncTask(Callable<BusinessResponse<RecognizeResult>> callable) {
        WebAsyncTask<BusinessResponse<RecognizeResult>> webAsyncTask = new WebAsyncTask<>(Constants.THIRD_PART_API_TIMEOUT_IN_SECONDS*1000, callable);
        webAsyncTask.onCompletion(() -> log.info("Completed method."));
        webAsyncTask.onTimeout(() -> {
            log.warn("Calling method timeout.");
            return BusinessResponse.fail(ResultCode.THIRD_PART_REQUEST_TIMEOUT);
        });
        webAsyncTask.onError(() -> {
            log.error("Error calling method.");
            return BusinessResponse.fail(ResultCode.THIRD_PART_REQUEST_ERROR);
        });
        return webAsyncTask;
    }

}
