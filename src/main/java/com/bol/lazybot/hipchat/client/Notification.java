package com.bol.lazybot.hipchat.client;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Notification {
    private String from;
    private String message_format;
    private String message;
}
