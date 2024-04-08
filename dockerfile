FROM maven:latest as builder
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mvn package
FROM openjdk:latest



# Token du bot
ENV token=""

# [Optional] Id du channel de là où le bot enverra le menu chaque jour, et heure de l'envoi sous le format HH:MM
ENV channelId=""
ENV dailyTime=""



COPY --from=builder /usr/src/app/target/RuMedrevilleBot-5.0.jar /app/RuMedrevilleBot.jar
WORKDIR /app
CMD ["java", "-Duser.timezone=Europe/Paris", "-jar", "RuMedrevilleBot.jar"]