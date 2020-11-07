package com.seed.portal.service;

import com.seed.base.model.PackVo;

import java.util.Map;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 15:00
 */
public interface MailService {

    PackVo sendTextMail(String toAddr, String title, String content);

    PackVo sendHtmlMail(String toAddr, String title, String content);

    PackVo sendAttachmentsMail(String toAddr, String title, String content, String filePath);

    PackVo sendInlineResourceMail(String toAddr, String title, String content, String rscPath, String rscId);

    PackVo sendTemplateHtmlMail(String toAddr, String title, String template, Map<String, Object> variables);

}
