package org.royjacobs.lazybot.hipchat.server.validator;

import ratpack.http.Request;

public interface HipChatRequestValidator {
    void validate(Request request, String oauthSecret);
}
