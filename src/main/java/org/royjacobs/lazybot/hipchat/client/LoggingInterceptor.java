package org.royjacobs.lazybot.hipchat.client;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

@Slf4j
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        log.debug(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        log.debug(String.format("Received response (status %d) for %s in %.1fms%n%s", response.code(), response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        if (response.code() >= 300) {
            log.debug(response.body().string());
        }

        return response;
    }
}
