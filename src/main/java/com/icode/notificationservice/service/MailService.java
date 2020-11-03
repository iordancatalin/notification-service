package com.icode.notificationservice.service;

import com.icode.notificationservice.model.EmailConfirmationModel;
import com.icode.notificationservice.model.ResetPasswordModel;
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

    @Value("${email-confirmation.subject}")
    private String emailConfirmationSubject;

    @Value("${email-confirmation.feEndpoint}")
    private String emailConfirmationEndpoint;

    @Value("${reset-password.subject}")
    private String resetPasswordSubject;

    @Value("${reset-password.feEndpoint}")
    private String resetPasswordEndpoint;

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public MailService(JavaMailSender mailSender,
                       TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendConfirmationEmail(EmailConfirmationModel emailConfirmationModel) {
        final var email = emailConfirmationModel.getEmail();
        final var content = getEmailConfirmationContent(emailConfirmationModel.getConfirmationToken());
        final var messagePreparator = prepareHTMLMail(email, emailConfirmationSubject, content);

        sendMail(messagePreparator);
    }

    public void sendResetPasswordMail(ResetPasswordModel resetPasswordModel) {
        final var email = resetPasswordModel.getEmail();
        final var content = getResetPasswordContent(resetPasswordModel.getResetToken());
        final var messagePreparator = prepareHTMLMail(email, resetPasswordSubject, content);

        sendMail(messagePreparator);
    }

    private void sendMail(MimeMessagePreparator messagePreparator) {
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            log.error(e);
        }
    }

    private MimeMessagePreparator prepareHTMLMail(String email, String subject, String content) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(from);
            messageHelper.setTo(email);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
        };
    }

    private String getEmailConfirmationContent(String confirmationToken) {
        final var confirmationLink = emailConfirmationEndpoint + confirmationToken;
        final var context = new Context();
        context.setVariable("confirmationLink", confirmationLink);

        return templateEngine.process("confirm_email", context);
    }

    private String getResetPasswordContent(String resetToken) {
        final var resetLink = resetPasswordEndpoint + resetToken;
        final var context = new Context();
        context.setVariable("resetLink", resetLink);

        return templateEngine.process("reset_password", context);
    }
}
