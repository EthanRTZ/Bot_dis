package com.example.bot_dis;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws LoginException {
        String token = "TON_TOKEN_ICI"; // Remplace par le token de ton bot

        JDABuilder builder = JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT);

        // Ajouter les activités et écouteurs
        builder.setActivity(Activity.playing("en Dev!"))
                .addEventListeners(new MessageListener(), new SlashCommandListener());

        // Construire le bot
        net.dv8tion.jda.api.JDA jda = builder.build();

        // Ajouter les commandes slash au démarrage
        jda.updateCommands().addCommands(
                Commands.slash("ping", "Renvoie Pong avec la latence"),
                Commands.slash("gifdrole", "Envoie un GIF drôle")
        ).queue();
    }
}

class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // On ignore les messages envoyés par le bot lui-même
        if (event.getAuthor().isBot()) return;

        // Récupérer le contenu du message et l'auteur
        Message message = event.getMessage();
        String content = message.getContentRaw();
        String authorName = event.getAuthor().getName(); // Nom de l'utilisateur

        // Vérifier si le message contient le mot-clé
        String motCle = "bonjour";
        if (content.equalsIgnoreCase(motCle)) {
            event.getChannel().sendMessage("Bonjour, " + authorName + " ! Comment puis-je t'aider ?").queue();
        }
    }
}

class SlashCommandListener extends ListenerAdapter {

    // Tableau contenant des GIFs drôles
    private final String[] funnyGifs = {
            "https://media.giphy.com/media/3o7TKpBq9iKYeBOAmE/giphy.gif", // Exemple GIF 1
            "https://media.giphy.com/media/l0HlBO7eyXzSZkJri/giphy.gif", // Exemple GIF 2
            "https://media.giphy.com/media/3o6ZsW8oW7Ow8YzZ6g/giphy.gif", // Exemple GIF 3
            "https://media.giphy.com/media/l41lFw057lAJQMwg0/giphy.gif", // Exemple GIF 4
            "https://media.giphy.com/media/26AHONQ79FdWZhAI0/giphy.gif"  // Exemple GIF 5
    };

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // Vérifier si la commande est "/ping"
        if (event.getName().equals("ping")) {
            long time = System.currentTimeMillis();
            // Répondre avec Pong visible par tout le monde
            event.reply("Pong!").queue(response -> {
                long latency = System.currentTimeMillis() - time;
                response.editOriginal("Pong! Latence : " + latency + "ms").queue();
            });
        }

        // Vérifier si la commande est "/gifdrole"
        if (event.getName().equals("gifdrole")) {
            Random random = new Random();
            String gifUrl = funnyGifs[random.nextInt(funnyGifs.length)]; // Sélectionne un GIF aléatoire

            // Répondre avec un GIF drôle
            event.reply("Voici un GIF drôle pour toi ! 🎉\n" + gifUrl).queue();
        }
    }
}
