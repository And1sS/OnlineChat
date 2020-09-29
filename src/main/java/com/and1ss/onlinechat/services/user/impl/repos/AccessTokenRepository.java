package com.and1ss.onlinechat.services.user.impl.repos;

import com.and1ss.onlinechat.services.user.model.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("access_token")
public interface AccessTokenRepository extends JpaRepository<AccessToken, UUID> {
    AccessToken findAccessTokenByUserId(UUID userId);
    AccessToken findAccessTokenByToken(UUID token);
}