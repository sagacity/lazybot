package org.royjacobs.lazybot.api.domain;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value
@Builder
public class Command {
    private RoomMessage originalMessage;
    private String command;
    private List<String> args;

    public static Command of(final String message) {
        final RoomMessage roomMessage = new RoomMessage();
        final RoomMessageItem roomMessageItem = new RoomMessageItem();
        final RoomMessageItemData roomMessageItemData = new RoomMessageItemData();
        roomMessageItemData.setMessage(message);
        roomMessageItem.setMessage(roomMessageItemData);
        roomMessage.setItem(roomMessageItem);

        return of(roomMessage);
    }

    public static Command of(final RoomMessage originalMessage) {
        final List<String> cmdLine = Splitter.on(CharMatcher.WHITESPACE)
                .trimResults()
                .omitEmptyStrings()
                .splitToList(originalMessage.getItem().getMessage().getMessage());

        return Command.builder()
                .originalMessage(originalMessage)
                .command(cmdLine.size() > 1 ? cmdLine.get(1) : "help")
                .args(cmdLine.size() > 2 ? cmdLine.subList(2, cmdLine.size()) : Collections.emptyList())
                .build();
    }
}
