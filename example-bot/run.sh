#/bin/sh

# Perform a build, if needed
if [ ! -f example-bot.jar ]; then
  mvn clean install
fi

# Run the bot with an example configuration file and customlogging
java -jar -Dlogback.configurationFile=./logback.xml example-bot.jar example-config.json
