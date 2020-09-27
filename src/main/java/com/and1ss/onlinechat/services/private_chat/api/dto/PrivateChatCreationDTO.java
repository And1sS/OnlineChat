package com.and1ss.onlinechat.services.private_chat.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivateChatCreationDTO {
    @NonNull
    @JsonProperty(value = "user_id")
    private UUID userId;
}