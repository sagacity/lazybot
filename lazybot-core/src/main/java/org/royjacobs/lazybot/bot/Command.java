package org.royjacobs.lazybot.bot;

import org.royjacobs.lazybot.hipchat.server.webhooks.dto.RoomMessage;
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
