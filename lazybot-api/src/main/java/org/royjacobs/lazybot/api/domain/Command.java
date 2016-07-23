package org.royjacobs.lazybot.api.domain;

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
