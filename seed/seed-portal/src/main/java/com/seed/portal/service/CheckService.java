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
     * Check certificate available from request.
     *
     * @param request request
     * @return       the result
     */
    PackVo<Boolean> checkCertificate(BusinessRequest request);

}
