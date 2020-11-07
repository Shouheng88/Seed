package com.seed.portal;

import com.seed.base.model.PackVo;
import com.seed.portal.base.SpringBaseTest;
import com.seed.portal.service.MailService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 15:42
 */
public class MailServiceTest extends SpringBaseTest {

    private String receiver = "xxxxxxxxx@163.com";

    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testSimpleMail() {
        PackVo packVo = mailService.sendTextMail(receiver,"email title","hello there!");
        Assert.assertTrue(packVo.isSuccess());
    }

    @Test
    public void testHtmlMail() {
        String content = "<html>\n" +
                "<body>\n" +
                "    <h3>Hello world!</h3>\n" +
                "</body>\n" +
                "</html>";
        PackVo packVo = mailService.sendHtmlMail(receiver,"test simple mail", content);
        Assert.assertTrue(packVo.isSuccess());
    }

    @Test
    public void sendAttachmentsMail() {
        String imgPath = new File("src/test/resources/static/img.jpg").getAbsolutePath();
        PackVo packVo = mailService.sendAttachmentsMail(receiver, "email with attachment", "please check the attachment", imgPath);
        Assert.assertTrue(packVo.isSuccess());
    }

    @Test
    public void sendInlineResourceMail() {
        String rscId = "img";
        String content="<html><body>email with image<img src=\'cid:" + rscId + "\' ></body></html>";
        String imgPath = new File("src/test/resources/static/img.jpg").getAbsolutePath();
        PackVo packVo = mailService.sendInlineResourceMail(receiver, "email with image", content, imgPath, rscId);
        Assert.assertTrue(packVo.isSuccess());
    }

    @Test
    public void sendTemplateMail() {
        Context context = new Context();
        context.setVariable("option", "account");
        context.setVariable("code", "123456");
        String emailContent = templateEngine.process("VerifyCode", context);
        PackVo packVo = mailService.sendHtmlMail(receiver,"email of template",emailContent);
        Assert.assertTrue(packVo.isSuccess());
    }

}
