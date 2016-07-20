package com.bol.team5f.lazybot;

import lombok.Value;
import org.jivesoftware.smackx.muc.MultiUserChat;
import com.bol.team5f.lazybot.api.HipChat;

import java.util.List;

@Value
public class RequestContext {
    MultiUserChat muc;
    HipChat api;
    Config config;
    List<Command> commands;
}
