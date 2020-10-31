package com.icode.notificationservice.service;

import com.icode.notificationservice.model.EmailConfirmationModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Log4j2
@Service
public class MailService {

    @Value("${email.confirmation.subject}")
    private String emailConfirmationSubject;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${email.confirmation.feEndpoint}")
    private String emailConfirmationEndpoint;

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public MailService(JavaMailSender mailSender,
                       TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendConfirmationEmail(EmailConfirmationModel emailConfirmationModel) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(from);
            messageHelper.setTo(emailConfirmationModel.getEmail());
            messageHelper.setSubject(emailConfirmationSubject);

            final var mailContent = getMailContent(emailConfirmationModel.getConfirmationId());
            messageHelper.setText(mailContent, true);
        };
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            log.error(e);
        }
    }

    private String getMailContent(String confirmationId) {
        final var confirmationLink = emailConfirmationEndpoint + confirmationId;
        final var context = new Context();
        context.setVariable("confirmationLink", confirmationLink);

        return templateEngine.process("confirm_email", context);
    }

}
