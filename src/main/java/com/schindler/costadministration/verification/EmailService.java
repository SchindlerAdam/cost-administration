package com.schindler.costadministration.verification;

import com.schindler.costadministration.model.VerificationCodeModel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private static final String TEMPLATE_NAME = "emailTemplate";
    private static final String COST_LOGO_IMAGE = "templates/images/cost.png";
    private static final String PNG_MIME = "image/png";
    private static final String MAIL_SUBJECT = "Registration Confirmation";
    private static final String FROM_NAME = "Team Cost App";
    private static final String VERIFICATION_URL = "http://localhost:8080/api/user/verification/"
;
    @Value("${spring.mail.username}")
    private String mailFrom;
    private final JavaMailSender mailSender;
    private final TemplateEngine htmlTemplateEngine;

    @Autowired
    public EmailService(JavaMailSender mailSender, TemplateEngine htmlTemplateEngine) {
        this.mailSender = mailSender;
        this.htmlTemplateEngine = htmlTemplateEngine;
    }

    @Async
    public void sendVerificationEmail(VerificationCodeModel model) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(model.getUser().getEmail());
        helper.setSubject(MAIL_SUBJECT);
        helper.setFrom(new InternetAddress(mailFrom, FROM_NAME));

        Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", model.getUser().getEmail());
        ctx.setVariable("name", model.getUser().getRealUserName());
        ctx.setVariable("costLogo", COST_LOGO_IMAGE);
        ctx.setVariable("url", VERIFICATION_URL + model.getCode());

        String htmlContent = this.htmlTemplateEngine.process(TEMPLATE_NAME, ctx);
        helper.setText(htmlContent, true);

        ClassPathResource classPathResource = new ClassPathResource(COST_LOGO_IMAGE);
        helper.addInline("costLogo", classPathResource, PNG_MIME);
        mailSender.send(message);
    }
}
