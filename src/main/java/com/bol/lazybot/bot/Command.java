package com.bol.lazybot.bot;

import com.bol.lazybot.hipchat.server.webhooks.dto.RoomMessage;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Command {
    private RoomMessage originalMessage;
    private String command;
    private List<String> args;
}
