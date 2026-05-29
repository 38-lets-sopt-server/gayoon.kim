package org.sopt.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleUserInfoResponse(
        String sub,
        String name,
        String email,

        @JsonProperty("email_verified")
        Boolean emailVerified,

        String picture
) {
}