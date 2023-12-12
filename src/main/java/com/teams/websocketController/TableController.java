package com.teams.websocketController;
// this is under poc
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
public class TableController {
    @MessageMapping("/test")
    @SendTo("/test")
    public String greeting() throws Exception {
        Thread.sleep(1000); // simulated delay
        return "This is test";
    }
}
