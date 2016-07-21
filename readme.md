### Description

It's lazy! It's a bot! It's **LazyBot**!

### Configuration
When starting you should provide a base-url for the bot using the Java system property `lazybot.server.publicAddress`.

Example: `-Dlazybot.server.publicAddress=https://41d2b126.ngrok.io`

Server port and many other things can also be configured like this, or through a configuration file or environment variables that are prefixed with `LAZYBOT_`.

### Storage
LazyBot currently stores all information about installations and plugin data in `target/`. This will be changed and made configurable, too.