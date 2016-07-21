package com.bol.lazybot.config;

import lombok.Data;

import java.util.Set;

@Data
public class HipChatConfig {
    private Set<String> scopes;
}
