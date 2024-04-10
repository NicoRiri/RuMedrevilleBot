# 👾🍝 RuMédrevilleBot

## 👨‍🎓 Auteur
- Nicolas Bernardet

## 📋 Preview

<img src="https://github.com/NicoRiri/RuMedrevilleBot/blob/main/assets/preview.png?raw=true" alt="preview" width="500" align="middle">

## 🍮 Fonctionnalités

- ``/menu``: Affiche le menu du midi du Resto'U de Médreville
- Peut (si configuré) envoyer automatiquement tous les jours à X heures et Y minutes le menu dans un channel

---

## 🤖 Obtenir un bot discord

1. Aller sur <a href="https://discord.com/developers/applications">le portail des développeurs Discord</a>
2. Créer une nouvelle application via le bouton "New Application"
3. Descendre dans la section "Bot" du menu
4. Cliquer sur "Reset Token" afin de le générer pour la première fois et stocker ce token

## 🙋‍♂️ Inviter le bot sur son serveur discord

1. Aller sur <a href="https://discord.com/developers/applications">le portail des développeurs Discord</a>
2. Cliquer sur l'application à inviter
3. Descendre dans la section OAuth2
4. Dans "OAuth2 URL Generator", cocher "bot"
5. Dans "BOT PERMISSIONS", il y aura juste besoin de séletionner "Send Messages"
6. Copier / Coller la "GENERATED URL" sur un browser
7. Inviter le bot sur le serveur voulu

---

## 💻 Déploiement

Modifier dans le dockerfile le token du bot précédemment stocké
````dockerfile
# Token du bot
ENV token="Le_Token_Ici"
````

[Optional] Définir un channel ainsi qu'une heure pour que le bot y envoie automatiquement le menu tous les jours du lundi au vendredi
````dockerfile
# [Optional] Id du channel de là où le bot enverra le menu chaque jour, et heure de l'envoi sous le format HH:MM
ENV channelId="L'Id_Ici"
ENV dailyTime="11:25"
````

Avec docker, construire l'image
````shell
docker build -t rumedrevillebot .
````

Une fois construite, lancer un container avec l'image
````shell
docker run rumedrevillebot
````
