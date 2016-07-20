package com.bol.team5f.lazybot;

import java.util.List;

public interface Command {
    String getName();
    String getDescription();
    boolean handle(List<String> args, RequestContext requestContext);
}
