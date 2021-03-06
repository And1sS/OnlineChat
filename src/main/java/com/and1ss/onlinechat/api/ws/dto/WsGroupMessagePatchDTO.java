package com.and1ss.onlinechat.api.ws.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WsGroupMessagePatchDTO {
    @NonNull
    private String contents;

    @JsonProperty("chat_id")
    private UUID chatId;

    @JsonProperty("message_id")
    private UUID messageId;
}