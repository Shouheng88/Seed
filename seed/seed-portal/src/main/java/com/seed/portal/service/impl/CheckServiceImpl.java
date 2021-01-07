package com.seed.portal.service.impl;

import com.seed.base.model.PackVo;
import com.seed.base.model.business.BusinessRequest;
import com.seed.portal.service.CheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 15:36
 */
@Slf4j
@Service("checkService")
@Transactional(rollbackFor = Exception.class)
public class CheckServiceImpl implements CheckService {

    @Override
    public PackVo checkRequest(BusinessRequest request) {
        return new PackVo().setSuccess(true);
    }

    @Override
    public PackVo checkCertificate(BusinessRequest request) {
        return new PackVo().setSuccess(true);
    }
}
