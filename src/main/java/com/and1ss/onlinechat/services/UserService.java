package com.and1ss.onlinechat.services;

import com.and1ss.onlinechat.services.model.LoginInfo;
import com.and1ss.onlinechat.services.model.AccessToken;
import com.and1ss.onlinechat.services.model.AccountInfo;
import com.and1ss.onlinechat.services.model.RegisterInfo;

import java.util.List;
import java.util.UUID;

public interface UserService {
        AccountInfo registerUser(RegisterInfo registerInfo);
        AccessToken loginUser(LoginInfo credentials);
        AccountInfo authorizeUserByAccessToken(String accessToken);
        AccountInfo findUserByLogin(String login);
        AccountInfo findUserById(UUID id);
        List<AccountInfo> findUsersByListOfIds(List<UUID> ids);
}