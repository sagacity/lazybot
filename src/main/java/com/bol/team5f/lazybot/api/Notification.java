package com.bol.team5f.lazybot.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Notification {
    private String from;
    private String message_format;
    private String message;
}
