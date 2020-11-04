package com.and1ss.onlinechat.services;

import com.and1ss.onlinechat.services.model.PrivateChat;
import com.and1ss.onlinechat.services.model.PrivateMessage;
import com.and1ss.onlinechat.services.model.AccountInfo;

import java.util.List;
import java.util.UUID;

public interface PrivateChatMessageService {
    List<PrivateMessage> getAllMessages(PrivateChat privateChat, AccountInfo author);
    PrivateMessage addMessage(PrivateChat privateChat, PrivateMessage message, AccountInfo author);
    PrivateMessage patchMessage(PrivateChat privateChat, PrivateMessage message, AccountInfo author);
    PrivateMessage getMessageById(UUID id);
    void deleteMessage(PrivateChat privateChat, PrivateMessage message, AccountInfo author);
}