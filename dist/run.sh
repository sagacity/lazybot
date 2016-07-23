#/bin/sh

# Perform a build, if needed
if [ ! -f lazybot.jar ]; then
  mvn clean install -f ..
fi

# Run the bot with an example configuration file and customlogging
java -jar -Dlogback.configurationFile=./logback.xml lazybot.jar example-config.json
