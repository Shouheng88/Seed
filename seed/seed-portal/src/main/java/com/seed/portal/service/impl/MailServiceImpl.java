package com.seed.portal.service.impl;

import com.seed.base.model.PackVo;
import com.seed.base.utils.MailUtils;
import com.seed.portal.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 15:01
 */
@Slf4j
@Service("mailService")
@Transactional(rollbackFor = Exception.class)
public class MailServiceImpl implements MailService {

    @Resource
    private MailUtils mailUtils;

    @Override
    public PackVo sendTextMail(String toAddr, String title, String content) {
        return mailUtils.sendTextMail(toAddr, title, content);
    }

    @Override
    public PackVo sendHtmlMail(String toAddr, String title, String content) {
        return mailUtils.sendHtmlMail(toAddr, title, content);
    }

    @Override
    public PackVo sendAttachmentsMail(String toAddr, String title, String content, String filePath) {
        return mailUtils.sendAttachmentsMail(toAddr, title, content, filePath);
    }

    @Override
    public PackVo sendInlineResourceMail(String toAddr, String title, String content, String rscPath, String rscId) {
        return mailUtils.sendInlineResourceMail(toAddr, title, content, rscPath, rscId);
    }

    @Override
    public PackVo sendTemplateHtmlMail(String toAddr, String title, String template, Map<String, Object> variables) {
        return mailUtils.sendTemplateHtmlMail(toAddr, title, template, variables);
    }

}
