package com.teams.constant;


import org.springframework.http.HttpStatus;

public class Response {
    HttpStatus statusCode;
    String response;
    Object content;

    public Response(HttpStatus statusCode, String response) {
        this.statusCode = statusCode;
        this.response = response;
    }
}
