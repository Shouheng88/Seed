package com.seed.portal.service;

import com.seed.base.model.PackVo;
import com.seed.base.model.business.BusinessRequest;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 15:34
 */
public interface CheckService {

    /**
     * Check if given request is legal.
     *
     * @param request request
     * @return       is the request legal
     */
    PackVo checkRequest(BusinessRequest request);

    /**
     * Check certificate available from request, for example, check the token and key.
     *
     * @param request request
     * @return       the result
     */
    PackVo checkCertificate(BusinessRequest request);

}
