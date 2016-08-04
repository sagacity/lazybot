# LazyBot
[![Build Status](https://travis-ci.org/RoyJacobs/lazybot.svg?branch=master)](https://travis-ci.org/RoyJacobs/lazybot)
[![Code Coverage](http://codecov.io/github/RoyJacobs/lazybot/coverage.svg?branch=master)](http://codecov.io/github/RoyJacobs/lazybot?branch=master)
[![License](http://img.shields.io/:license-mit-brightgreen.svg)](https://raw.githubusercontent.com/RoyJacobs/lazybot/master/LICENSE.md)

### Description

**LazyBot** is a framework for building HipChat bots on the JVM. It comes with a bunch of (example) plugins that should give you a good idea on how to build your own plugins.

### Quickstart
To quickly start playing with a bot, I suggest you use [ngrok](https://ngrok.com/). This allows you to expose your locally running bot to the internet with full support of HTTPS, which is what HipChat requires.

1. Start ngrok and make it listen to port 5050, which is where LazyBot will run:
   ```
   ngrok http 5050
   ```
2. Edit `example-bot/example-config.json` and set the publicAddress to whatever ngrok has provided you with.

3. Build and run the bot!
   ```
   cd example-bot
   ./run.sh
   ```

4. On the HipChat integrations page, choose "Install an integration from a descriptor URL"

5. Enter the following url:
   ```
   https://<address of your server>
   ```

6. Profit!
