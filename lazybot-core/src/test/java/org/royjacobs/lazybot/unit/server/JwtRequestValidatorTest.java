package org.royjacobs.lazybot.unit.server;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.junit.Test;
import org.royjacobs.lazybot.hipchat.server.validator.JwtRequestValidator;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.test.handling.RequestFixture;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class JwtRequestValidatorTest {
    private final JwtRequestValidator validator = new JwtRequestValidator();
    private final String key = "key!key!key!key!key!key!key!key!";
    private final String token = Jwts.builder().setSubject("Foo").signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.encode(key)).compact();

    private Exception caughtException;

    private class TestHandler implements Handler {
        @Override
        public void handle(Context ctx) throws Exception {
            caughtException = null;
            try {
                validator.validate(ctx.getRequest(), key);
                ctx.render("OK");
            } catch (Exception e) {
                caughtException = e;
                ctx.error(e);
            }
        }
    }

    @Test
    public void canValidateValidTokenFromQueryParam() {
        RequestFixture.requestFixture().uri("http://test?signed_request=" + token).handle(new TestHandler());
        assertThat(caughtException, is(nullValue()));
    }

    @Test
    public void canValidateValidTokenFromHeader() {
        RequestFixture.requestFixture().header("Authorization", "JWT " + token).handle(new TestHandler());
        assertThat(caughtException, is(nullValue()));
    }

    @Test
    public void cannotValidateEmptyToken() {
        RequestFixture.requestFixture().handle(new TestHandler());
        assertThat(caughtException instanceof UnsupportedOperationException, is(true));
    }

    @Test
    public void cannotValidateInvalidToken() {
        RequestFixture.requestFixture().header("Authorization", "JWT invalid").handle(new TestHandler());
        assertThat(caughtException, is(not(nullValue())));
    }
}
