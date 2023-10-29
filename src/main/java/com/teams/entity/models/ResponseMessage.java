package com.teams.entity.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {
    String ResponseMessage;
    Object data;
    public ResponseMessage(String responseMessage) {
        this.ResponseMessage = responseMessage;
    }
}
