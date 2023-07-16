package com.exam.service.impl;

import com.exam.exception.ExamPortalException;
import com.exam.exception.ResourceNotFoundException;
import com.exam.model.User;
import com.exam.model.VerificationToken;
import com.exam.repository.VerificationTokenRepository;
import com.exam.service.VerificationTokenService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import static com.exam.constants.ExamPortalConstant.EXCEPTION_FIELD;
import static com.exam.constants.ExamPortalConstant.USER_EXCEPTION;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR= KeyGenerators.secureRandom(64);

    @Override
    public VerificationToken getVerificationTokenByToken(String token) {
        return verificationTokenRepository.findByToken(token).orElseThrow(()-> new ExamPortalException("Token is either expired or invalid"));
    }

    @Override
    public VerificationToken saveToken(VerificationToken verificationToken) {
        return verificationTokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken createVerificationToken() {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey())));
        return verificationToken;
    }

    @Override
    public void removeVerificationToken(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }

    @Override
    public VerificationToken getVerificationTokenByUser(User user) {
        return verificationTokenRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD , user.getUserId()));
    }
}
