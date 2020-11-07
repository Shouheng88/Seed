package com.seed.portal.service.impl;

import com.seed.base.model.PackVo;
import com.seed.base.model.enums.ResultCode;
import com.seed.portal.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.File;
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
    private JavaMailSender mailSender;
    @Resource
    private TemplateEngine templateEngine;
    @Value("${mail.from.address}")
    private String emailAddress;

    @Override
    public PackVo sendTextMail(String toAddr, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailAddress);
        message.setTo(toAddr);
        message.setSubject(title);
        message.setText(content);

        try {
            mailSender.send(message);
            log.info("Email. Sent text email: [{}] [{}].", toAddr, title);
            return PackVo.success();
        } catch (Exception e) {
            log.error("Email. Failed to send text email:", e);
            return PackVo.fail(ResultCode.FAILED_TO_SEND_EMAIL);
        }
    }

    @Override
    public PackVo sendHtmlMail(String toAddr, String title, String content) {
        long timeBegin = System.currentTimeMillis();
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(emailAddress);
            helper.setTo(toAddr);
            helper.setSubject(title);
            helper.setText(content, true);

            mailSender.send(message);
            log.info("Email. Sent html email [{}] [{}] in [{}ms].", toAddr, title, System.currentTimeMillis() - timeBegin);
            return PackVo.success();
        } catch (Exception e) {
            log.error("Email. Failed to send html email:", e);
            return PackVo.fail(ResultCode.FAILED_TO_SEND_EMAIL);
        }
    }

    @Override
    public PackVo sendAttachmentsMail(String toAddr, String title, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(emailAddress);
            helper.setTo(toAddr);
            helper.setSubject(title);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);

            mailSender.send(message);
            log.info("Email. Sent email with attachments [{}] [{}].", toAddr, title);
            return PackVo.success();
        } catch (Exception e) {
            log.error("Email. Failed to send email with attachment:", e);
            return PackVo.fail(ResultCode.FAILED_TO_SEND_EMAIL);
        }
    }

    @Override
    public PackVo sendInlineResourceMail(String toAddr, String title, String content, String rscPath, String rscId) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(emailAddress);
            helper.setTo(toAddr);
            helper.setSubject(title);

            helper.setText(content, true);

            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res);

            mailSender.send(message);
            log.info("Email. Sent email with inline resource [{}] [{}].", toAddr, title);
            return PackVo.success();
        } catch (Exception e) {
            log.error("Email. Failed to send email with inline resources:", e);
            return PackVo.fail(ResultCode.FAILED_TO_SEND_EMAIL);
        }
    }

    @Override
    public PackVo sendTemplateHtmlMail(String toAddr, String title, String template, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        String emailContent = templateEngine.process(template, context);
        return sendHtmlMail(toAddr, title, emailContent);
    }

}
