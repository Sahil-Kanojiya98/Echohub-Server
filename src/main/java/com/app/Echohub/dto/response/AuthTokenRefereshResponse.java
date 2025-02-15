package com.app.Echohub.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthTokenRefereshResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refreshToken;

    private String accessToken;

}
