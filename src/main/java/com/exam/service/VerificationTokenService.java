package com.exam.service;

import com.exam.model.User;
import com.exam.model.VerificationToken;

public interface VerificationTokenService {

    public VerificationToken getVerificationTokenByToken(String token);

    public VerificationToken saveToken(VerificationToken verificationToken);

    public VerificationToken createVerificationToken();

    public void removeVerificationToken(VerificationToken verificationToken);

    public VerificationToken getVerificationTokenByUser(User user);

}
