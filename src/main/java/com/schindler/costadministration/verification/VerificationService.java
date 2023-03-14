package com.schindler.costadministration.verification;

import com.schindler.costadministration.entities.VerificationCode;
import com.schindler.costadministration.model.VerificationCodeModel;
import com.schindler.costadministration.repository.VerificationRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@Transactional
public class VerificationService {

    private final VerificationRepository verificationRepository;
    private final  EmailService emailService;

    public VerificationService(VerificationRepository verificationRepository, EmailService emailService) {
        this.verificationRepository = verificationRepository;
        this.emailService = emailService;
    }

    public void saveVerificationCode(VerificationCodeModel model) {
        this.verificationRepository.save(new VerificationCode(model));
    }

    public void sendVerificationEmail(VerificationCodeModel model) {
        try {
            this.emailService.sendVerificationEmail(model);
        } catch (MessagingException | UnsupportedEncodingException exception) {
            // TODO
        }
    }

    public VerificationCode getVerificationCode(String code) {
        return this.verificationRepository.findVerificationCodeByCode(code);
    }
}
