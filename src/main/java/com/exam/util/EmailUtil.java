package com.exam.util;

import com.exam.exception.ExamPortalException;
import com.exam.model.User;
import com.exam.model.VerificationToken;
import com.exam.service.VerificationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import static com.exam.constants.ExamPortalConstant.*;

@Component
@Slf4j
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(String toEmail, String body, String subject) {
        log.info("sendEmail method invoking");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        javaMailSender.send(message);
        log.info("sendEmail method called");
    }

    public void sendEmailWithAttachment(User user) throws UnsupportedEncodingException, MessagingException{
        log.info("sendEmailWithAttachment method invoking");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
        String senderName = "Exam Portal";
        String subject = "Please verify your Email";
        VerificationToken verificationToken = verificationTokenService.createVerificationToken();
        verificationToken.setUser(user);
        log.info("Verification token saved");
        verificationTokenService.saveToken(verificationToken);
        String content = getContent(user,LOCAL_BASE_PATH,verificationToken.getToken());
        log.info(content);
        messageHelper.setFrom(fromEmail, senderName);
        messageHelper.setTo(user.getUserName());
        messageHelper.setSubject(subject);
        messageHelper.setText(content,true);
        try {
            log.info("attachment fetching");
            FileSystemResource fileSystemResource = new FileSystemResource(ResourceUtils.getFile("classpath:logo/logo.png"));
            messageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
            log.info("sending mail");
            javaMailSender.send(mimeMessage);
        } catch (FileNotFoundException e) {
            log.error("File not found in given location"+e.getMessage());
            throw new ExamPortalException("File not found in given location");
        }
        log.info("sendEmailWithAttachment method called");
    }

    private String getContent(User user, String siteUrl, String verificationToken) {
        log.info("getContent method invoking");
        StringBuffer content = new StringBuffer();
        content.append("<p>Dear ");
        content.append(user.getName());
        content.append(",<br>");
        content.append("Please click the link below to verify your registration:<br>");
        content.append("<div>");
        content.append("<a href='" );
        content.append(siteUrl);
        content.append(USER_BASE_PATH);
        content.append(VERIFY_USER);
        content.append("/");
        content.append(verificationToken);
        content.append("' target='_blank' style='margin-right: 20px;'>");
        content.append("<div style='height: 20px; width: 100px; padding: 10px 15px; ");
        content.append("background-color: lightgreen; border-radius: 5px; margin-right: 20px; ");
        content.append("display: inline-block; line-height: 20px; text-align: center; ");
        content.append("cursor: pointer; margin-right: 20px;'>");
        content.append("<b>VERIFY</b>");
        content.append("</div>");
        content.append("</a>" );
        content.append("</div> <br>");
        content.append("<p>This Link will expire in 15 minutes.</p><br>");
        content.append("Thank you,<br>");
        content.append("Exam Portal </p>");
        log.info("getContent method called");
        return content.toString();
    }
}
