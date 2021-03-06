package com.and1ss.onlinechat.services;

import com.and1ss.onlinechat.services.dto.AccountInfoRetrievalDTO;
import com.and1ss.onlinechat.services.dto.FriendRetrievalDTO;

import java.util.List;
import java.util.UUID;

public interface FriendsService {
    FriendRetrievalDTO createFriendRequest(UUID requestIssuerId, UUID requesteeId);

    List<FriendRetrievalDTO> getFriendsForUser(UUID userId);

    List<FriendRetrievalDTO> getAcceptedFriendsForUser(UUID userId);

    List<AccountInfoRetrievalDTO> getAcceptedFriendsWithoutPrivateChatsForUser(UUID userId);

    void acceptFriendRequest(UUID requestIssuerId, UUID requesteeId);

    void deleteFriends(UUID user1Id, UUID user2Id);

    boolean areUsersFriends(UUID user1Id, UUID user2Id);
}