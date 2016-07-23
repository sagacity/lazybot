### Description

**LazyBot** is a framework for building HipChat bots on the JVM. It comes with a bunch of (example) plugins that should give you a good idea on how to build your own plugins.

### Quickstart
To quickly start playing with a bot, I suggest you use [ngrok](https://ngrok.com/). This allows you to expose your locally running bot to the internet with full support of HTTPS, which is what HipChat requires.

1. Start ngrok and make it listen to port 5050, which is where LazyBot will run:
   ```
   ngrok http 5050
   ```
2. Edit `dist/example-config.json` and set the publicAddress to whatever ngrok has provided you with.

3. Build and run the bot!
   ```
   cd dist
   ./run.sh
   ```

4. Build something cool :)
