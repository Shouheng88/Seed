package com.seed.base.utils;

import com.seed.base.model.PackVo;
import com.seed.base.model.enums.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 23:01
 */
@Slf4j
public final class MailUtils {

    private TemplateEngine templateEngine;

    private JavaMailSender mailSender;

    private String fromAddress;

    public MailUtils(JavaMailSender mailSender, TemplateEngine templateEngine, String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
        this.templateEngine = templateEngine;
    }

    public PackVo sendTextMail(String receiver, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(receiver);
        message.setSubject(title);
        message.setText(content);

        try {
            mailSender.send(message);
            log.info("Email. Sent text email: [{}] [{}].", receiver, title);
            return PackVo.success();
        } catch (Exception e) {
            log.error("Email. Failed to send text email:", e);
            return PackVo.fail(ResultCode.FAILED_TO_SEND_EMAIL);
        }
    }

    public PackVo sendHtmlMail(String receiver, String title, String content) {
        long timeBegin = System.currentTimeMillis();
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAddress);
            helper.setTo(receiver);
            helper.setSubject(title);
            helper.setText(content, true);

            mailSender.send(message);
            log.info("Email. Sent html email [{}] [{}] in [{}ms].", receiver, title, System.currentTimeMillis() - timeBegin);
            return PackVo.success();
        } catch (Exception e) {
            log.error("Email. Failed to send html email:", e);
            return PackVo.fail(ResultCode.FAILED_TO_SEND_EMAIL);
        }
    }

    public PackVo sendAttachmentsMail(String receiver, String title, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAddress);
            helper.setTo(receiver);
            helper.setSubject(title);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);

            mailSender.send(message);
            log.info("Email. Sent email with attachments [{}] [{}].", receiver, title);
            return PackVo.success();
        } catch (Exception e) {
            log.error("Email. Failed to send email with attachment:", e);
            return PackVo.fail(ResultCode.FAILED_TO_SEND_EMAIL);
        }
    }

    public PackVo sendInlineResourceMail(String receiver, String title, String content, String rscPath, String rscId) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAddress);
            helper.setTo(receiver);
            helper.setSubject(title);

            helper.setText(content, true);

            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res);

            mailSender.send(message);
            log.info("Email. Sent email with inline resource [{}] [{}].", receiver, title);
            return PackVo.success();
        } catch (Exception e) {
            log.error("Email. Failed to send email with inline resources:", e);
            return PackVo.fail(ResultCode.FAILED_TO_SEND_EMAIL);
        }
    }

    public PackVo sendTemplateHtmlMail(String receiver, String title, String template, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        String emailContent = templateEngine.process(template, context);
        return sendHtmlMail(receiver, title, emailContent);
    }
}
