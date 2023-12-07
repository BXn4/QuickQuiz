package com.bxn4.discord.QuickQuiz;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.GenericSelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;
import java.util.Objects;

public class Help {

    public static void setup(GenericSelectMenuInteractionEvent event) {
        Long serverID = event.getGuild().getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("QuickQuiz");
        eb.setDescription(Languages.getMessage(serverLanguage, "BOT_HELP_SETUP"));
        eb.setColor(new Color(77, 126, 130));

        ActionRow view = ActionRow.of(StringSelectMenu.create("view")
                .addOptions(SelectOption.of(Objects.requireNonNull(Languages.getMessage(serverLanguage, "BOT_HELP_MENU_SETUP")), "setup").withEmoji(Emoji.fromUnicode("\uD83E\uDD16")).withDefault(true))
                .addOptions(SelectOption.of(Objects.requireNonNull(Languages.getMessage(serverLanguage, "BOT_HELP_MENU_QUIZZES")), "quizzes").withEmoji(Emoji.fromUnicode("\uD83D\uDCDD")))
                .build());

        event.editMessageEmbeds(eb.build()).setComponents(view).queue();
    }

    public static void quizzes(GenericSelectMenuInteractionEvent event) {
        Long serverID = event.getGuild().getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("QuickQuiz");
        eb.setDescription(Languages.getMessage(serverLanguage, "BOT_HELP_QUIZZES"));
        eb.setColor(new Color(77, 126, 130));

        ActionRow view = ActionRow.of(StringSelectMenu.create("view")
                .addOptions(SelectOption.of(Objects.requireNonNull(Languages.getMessage(serverLanguage, "BOT_HELP_MENU_SETUP")), "setup").withEmoji(Emoji.fromUnicode("\uD83E\uDD16")))
                .addOptions(SelectOption.of(Objects.requireNonNull(Languages.getMessage(serverLanguage, "BOT_HELP_MENU_QUIZZES")), "quizzes").withEmoji(Emoji.fromUnicode("\uD83D\uDCDD")).withDefault(true))
                .build());

        event.editMessageEmbeds(eb.build()).setComponents(view).queue();
    }
}
