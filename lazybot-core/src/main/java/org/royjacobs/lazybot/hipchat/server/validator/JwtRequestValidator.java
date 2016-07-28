package org.royjacobs.lazybot.hipchat.server.validator;

import com.google.common.base.Throwables;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import ratpack.http.Request;

@Slf4j
public class JwtRequestValidator implements HipChatRequestValidator {
    @Override
    public void validate(Request request, String oauthSecret) {
        String jwtToken = request.getQueryParams().get("signed_request");
        if (jwtToken == null) {
            final String headerValue = request.getHeaders().get("Authorization");
            if (headerValue != null) jwtToken = headerValue.substring(4); // remove "JWT " from header value
        }
        if (jwtToken == null) throw new UnsupportedOperationException("Could not retrieve JWT token");

        try {
            Jwts.parser()
                    .setSigningKey(oauthSecret.getBytes())
                    .parse(jwtToken);
        } catch (Exception e) {
            log.warn("Could not validate JWT token. Ignoring message.", e);
            throw Throwables.propagate(e);
        }

    }
}
