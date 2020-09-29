package com.and1ss.onlinechat.services.user.impl.password_hasher;

import java.security.NoSuchAlgorithmException;

public interface PasswordHasher {
    String hashPassword(String password) throws NoSuchAlgorithmException;
}