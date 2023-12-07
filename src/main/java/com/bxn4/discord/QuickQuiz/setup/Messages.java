package com.bxn4.discord.QuickQuiz.setup;

import com.bxn4.discord.QuickQuiz.Database;
import com.bxn4.discord.QuickQuiz.Languages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericSelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Messages {

    public static void sendWelcomeMessage(GuildJoinEvent event) {
        try {
            Long serverID = event.getGuild().getIdLong();
            String serverLanguage = Database.serverGetLanguage(serverID);

            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle(Languages.getMessage(serverLanguage, "OWNER_WELCOME"));
            eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_GUIDE_1") +
                    "\n\n" + Languages.getMessage(serverLanguage, "OWNER_NEXT") +
                    "\n\n" + Languages.getMessage(serverLanguage, "OWNER_CHANGE_LANG") +
                    "\n\n" + Languages.getMessage(serverLanguage, "OWNER_HELP"));
            eb.setColor(new Color(77, 126, 130));

            ActionRow changeLangView = null;
            switch (serverLanguage) {
                case "EN" -> changeLangView = ActionRow.of(StringSelectMenu.create("owner_change_language_1")
                        .addOptions(SelectOption.of("EN", "EN").withEmoji(Emoji.fromUnicode("\uD83C\uDDFA\uD83C\uDDF8")).withDefault(true))
                        .addOptions(SelectOption.of("HU", "HU").withEmoji(Emoji.fromUnicode("\uD83C\uDDED\uD83C\uDDFA")))
                        .build());
                case "HU" -> changeLangView = ActionRow.of(StringSelectMenu.create("owner_change_language_1")
                        .addOptions(SelectOption.of("EN", "EN").withEmoji(Emoji.fromUnicode("\uD83C\uDDFA\uD83C\uDDF8")))
                        .addOptions(SelectOption.of("HU", "HU").withEmoji(Emoji.fromUnicode("\uD83C\uDDED\uD83C\uDDFA")).withDefault(true))
                        .build());
            }

            ActionRow nextButton = ActionRow.of(
                    Button.secondary("owner_change_page_2", Emoji.fromUnicode("➡️"))
            );

            List<ActionRow> actionRow = new ArrayList<>();
            actionRow.add(changeLangView);
            actionRow.add(nextButton);

            Objects.requireNonNull(event.getGuild().getDefaultChannel()).asTextChannel().sendMessageEmbeds(eb.build()).setComponents(actionRow).queue();
        }
        catch (Exception ignore) {

        }
    }
    public static void updateMessage(GenericSelectMenuInteractionEvent event, int pageID) {
        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        EmbedBuilder eb = new EmbedBuilder();

        ActionRow nextButton = null;

        switch (pageID) {
            case 1 -> {
                eb.setTitle(Languages.getMessage(serverLanguage, "OWNER_WELCOME"));
                eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_GUIDE_1") +
                        "\n\n" + Languages.getMessage(serverLanguage, "OWNER_NEXT") +
                        "\n\n" + Languages.getMessage(serverLanguage, "OWNER_CHANGE_LANG") +
                        "\n\n" + Languages.getMessage(serverLanguage, "OWNER_HELP"));
                eb.setColor(new Color(77, 126, 130));
                nextButton = ActionRow.of(
                        Button.secondary("owner_change_page_" + (pageID + 1), Emoji.fromUnicode("➡️"))
                );
            }

            case 2 -> {
                eb.setTitle(Languages.getMessage(serverLanguage, "OWNER_ROLE_TITLE"));
                eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_GUIDE_2") +
                        "\n\n" + Languages.getMessage(serverLanguage, "OWNER_ROLE") +
                        "\n\n" + Languages.getMessage(serverLanguage, "OWNER_HELP"));
                eb.setColor(new Color(77, 126, 130));
                nextButton = ActionRow.of(
                        Button.secondary("owner_change_page_" + (pageID + 1), Emoji.fromUnicode("➡️"))
                );
            }

            case 3 -> {
                if (Database.checkIfTheOwnerSetTheRole(serverID)) {
                    eb.setTitle(Languages.getMessage(serverLanguage, "OWNER_CHANNEL_TITLE"));
                    eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_CHANNEL") +
                            "\n\n" + Languages.getMessage(serverLanguage, "OWNER_HELP"));
                    eb.setColor(new Color(77, 126, 130));
                    nextButton = ActionRow.of(
                            Button.secondary("owner_change_page_" + (pageID + 1), Emoji.fromUnicode("➡️"))
                    );
                }
                else {
                    eb.setTitle(Languages.getMessage(serverLanguage, "OWNER_ROLE_TITLE"));
                    eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_GUIDE_2") +
                            "\n\n" + Languages.getMessage(serverLanguage, "OWNER_ROLE") +
                            "\n\n" + Languages.getMessage(serverLanguage, "OWNER_HELP"));
                    eb.setColor(new Color(77, 126, 130));
                    nextButton = ActionRow.of(
                            Button.secondary("owner_change_page_" + pageID, Emoji.fromUnicode("➡️"))
                    );
                }
            }

            case 4 -> {
                if (Database.checkIfTheOwnerSetTheChannel(serverID)) {
                    eb.setTitle(Languages.getMessage(serverLanguage, "OWNER_FINISHED_TITLE"));
                    eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_FINISHED") +
                            "\n\n" + Languages.getMessage(serverLanguage, "OWNER_HELP"));
                    eb.setColor(new Color(77, 126, 130));
                }
                else {
                    eb.setTitle(Languages.getMessage(serverLanguage, "OWNER_CHANNEL_TITLE"));
                    eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_CHANNEL") +
                            "\n\n" + Languages.getMessage(serverLanguage, "OWNER_HELP"));
                    eb.setColor(new Color(77, 126, 130));
                    nextButton = ActionRow.of(
                            Button.secondary("owner_change_page_" + pageID, Emoji.fromUnicode("➡️"))
                    );
                }
            }
        }

        ActionRow changeLangView = null;
        switch (serverLanguage) {
            case "EN" -> changeLangView = ActionRow.of(StringSelectMenu.create("owner_change_language_" + pageID)
                    .addOptions(SelectOption.of("EN", "EN").withEmoji(Emoji.fromUnicode("\uD83C\uDDFA\uD83C\uDDF8")).withDefault(true))
                    .addOptions(SelectOption.of("HU", "HU").withEmoji(Emoji.fromUnicode("\uD83C\uDDED\uD83C\uDDFA")))
                    .build());
            case "HU" -> changeLangView = ActionRow.of(StringSelectMenu.create("owner_change_language_" + pageID)
                    .addOptions(SelectOption.of("EN", "EN").withEmoji(Emoji.fromUnicode("\uD83C\uDDFA\uD83C\uDDF8")))
                    .addOptions(SelectOption.of("HU", "HU").withEmoji(Emoji.fromUnicode("\uD83C\uDDED\uD83C\uDDFA")).withDefault(true))
                    .build());
        }


        List<ActionRow> actionRow = new ArrayList<>();
        actionRow.add(changeLangView);
        if (nextButton != null) {
            actionRow.add(nextButton);
        }

        event.editMessageEmbeds(eb.build()).setComponents(actionRow).queue();
    }
    public static void changePageButton(ButtonInteractionEvent event, int pageID) {
        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        ActionRow nextButton = null;

        EmbedBuilder eb = new EmbedBuilder();

        switch (pageID) {
            case 1 -> {
                eb.setTitle(Languages.getMessage(serverLanguage, "OWNER_WELCOME"));
                eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_GUIDE_1") +
                        "\n\n" + Languages.getMessage(serverLanguage, "OWNER_NEXT") +
                        "\n\n" + Languages.getMessage(serverLanguage, "OWNER_CHANGE_LANG") +
                        "\n\n" + Languages.getMessage(serverLanguage, "OWNER_HELP"));
                eb.setColor(new Color(77, 126, 130));
                nextButton = ActionRow.of(
                        Button.secondary("owner_change_page_" + (pageID + 1), Emoji.fromUnicode("➡️"))
                );
            }

            case 2 -> {
                eb.setTitle(Languages.getMessage(serverLanguage, "OWNER_ROLE_TITLE"));
                eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_GUIDE_2") +
                        "\n\n" + Languages.getMessage(serverLanguage, "OWNER_ROLE") +
                        "\n\n" + Languages.getMessage(serverLanguage, "OWNER_HELP"));
                eb.setColor(new Color(77, 126, 130));
                nextButton = ActionRow.of(
                        Button.secondary("owner_change_page_" + (pageID + 1), Emoji.fromUnicode("➡️"))
                );
            }

            case 3 -> {
                if (Database.checkIfTheOwnerSetTheRole(serverID)) {
                    eb.setTitle(Languages.getMessage(serverLanguage, "OWNER_CHANNEL_TITLE"));
                    eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_CHANNEL") +
                            "\n\n" + Languages.getMessage(serverLanguage, "OWNER_HELP"));
                    eb.setColor(new Color(77, 126, 130));
                    nextButton = ActionRow.of(
                            Button.secondary("owner_change_page_" + (pageID + 1), Emoji.fromUnicode("➡️"))
                    );
                } else {
                    eb.setTitle(Languages.getMessage(serverLanguage, "OWNER_ROLE_TITLE"));
                    eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_GUIDE_2") +
                            "\n\n" + Languages.getMessage(serverLanguage, "OWNER_ROLE") +
                            "\n\n" + Languages.getMessage(serverLanguage, "OWNER_HELP"));
                    eb.setColor(new Color(77, 126, 130));
                    nextButton = ActionRow.of(
                            Button.secondary("owner_change_page_" + pageID, Emoji.fromUnicode("➡️"))
                    );

                    EmbedBuilder embedBuilder = new EmbedBuilder();

                    embedBuilder.setDescription(Languages.getMessage(serverLanguage, "OWNER_ROLE"));
                    embedBuilder.setColor(new Color(168, 84, 84, 255));

                    event.getHook().sendMessageEmbeds(embedBuilder.build()).setEphemeral(true).queue();
                }
            }

            case 4 -> {
                if (Database.checkIfTheOwnerSetTheChannel(serverID)) {
                    eb.setTitle(Languages.getMessage(serverLanguage, "OWNER_FINISHED_TITLE"));
                    eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_FINISHED") +
                            "\n\n" + Languages.getMessage(serverLanguage, "OWNER_HELP"));
                    eb.setColor(new Color(77, 126, 130));
                } else {
                    eb.setTitle(Languages.getMessage(serverLanguage, "OWNER_CHANNEL_TITLE"));
                    eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_CHANNEL") +
                            "\n\n" + Languages.getMessage(serverLanguage, "OWNER_HELP"));
                    eb.setColor(new Color(77, 126, 130));
                    nextButton = ActionRow.of(
                            Button.secondary("owner_change_page_" + pageID, Emoji.fromUnicode("➡️"))
                    );

                    EmbedBuilder embedBuilder = new EmbedBuilder();

                    embedBuilder.setDescription(Languages.getMessage(serverLanguage, "OWNER_CHANNEL"));
                    embedBuilder.setColor(new Color(168, 84, 84, 255));

                    event.getHook().sendMessageEmbeds(embedBuilder.build()).setEphemeral(true).queue();
                }
            }
        }

        ActionRow changeLangView = null;
        switch (serverLanguage) {
            case "EN" -> changeLangView = ActionRow.of(StringSelectMenu.create("owner_change_language_" + pageID)
                    .addOptions(SelectOption.of("EN", "EN").withEmoji(Emoji.fromUnicode("\uD83C\uDDFA\uD83C\uDDF8")).withDefault(true))
                    .addOptions(SelectOption.of("HU", "HU").withEmoji(Emoji.fromUnicode("\uD83C\uDDED\uD83C\uDDFA")))
                    .build());
            case "HU" -> changeLangView = ActionRow.of(StringSelectMenu.create("owner_change_language_" + pageID)
                    .addOptions(SelectOption.of("EN", "EN").withEmoji(Emoji.fromUnicode("\uD83C\uDDFA\uD83C\uDDF8")))
                    .addOptions(SelectOption.of("HU", "HU").withEmoji(Emoji.fromUnicode("\uD83C\uDDED\uD83C\uDDFA")).withDefault(true))
                    .build());
        }

        List<ActionRow> actionRow = new ArrayList<>();
        actionRow.add(changeLangView);
        if (nextButton != null) {
            actionRow.add(nextButton);
        }

        event.editMessageEmbeds(eb.build()).setComponents(actionRow).queue();
    }
    public static void updateRole(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        EmbedBuilder eb = new EmbedBuilder();

        Role role = Objects.requireNonNull(event.getOption("role")).getAsRole();

        if (!role.getName().equals("@everyone")) {
            Long roleID = role.getIdLong();
            Database.updateServerQuizRole(serverID, roleID);

            eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_ROLE_FINISHED_TITLE")
                    + "\n" + role.getAsMention());
            eb.setColor(new Color(158, 222, 52));

            event.getHook().sendMessageEmbeds(eb.build()).queue();
        }
        else {
            eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_ROLE_EVERYONE"));
            eb.setColor(new Color(168, 84, 84, 255));

            event.getHook().sendMessageEmbeds(eb.build()).queue();
        }
    }
    public static void updateChannel(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        EmbedBuilder eb = new EmbedBuilder();

        Channel channel = Objects.requireNonNull(event.getOption("channel")).getAsChannel();

        Long channelID = channel.getIdLong();
        Database.updateServerQuizChannel(serverID, channelID);

        eb.setDescription(Languages.getMessage(serverLanguage, "CHANNEL_SET")
                + "\n" + channel.getAsMention());
        eb.setColor(new Color(158, 222, 52));

        event.getHook().sendMessageEmbeds(eb.build()).queue();
    }
    public static void sendNotOwnerMessageSlash(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle(Languages.getMessage(serverLanguage, "USER_MESSAGES_SERVER_OWNER_ONLY_TITLE"));
        eb.setDescription(Languages.getMessage(serverLanguage, "USER_MESSAGES_SERVER_OWNER_ONLY"));
        eb.setColor(new Color(168, 84, 84, 255));

        event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
    }
    public static void sendNotOwnerMessageGeneric(GenericSelectMenuInteractionEvent event) {
        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle(Languages.getMessage(serverLanguage, "USER_MESSAGES_SERVER_OWNER_ONLY_TITLE"));
        eb.setDescription(Languages.getMessage(serverLanguage, "USER_MESSAGES_SERVER_OWNER_ONLY"));
        eb.setColor(new Color(168, 84, 84, 255));

        event.replyEmbeds(eb.build()).setEphemeral(true).queue();
    }
    public static void sendNotOwnerMessageButton(ButtonInteractionEvent event) {
        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle(Languages.getMessage(serverLanguage, "USER_MESSAGES_SERVER_OWNER_ONLY_TITLE"));
        eb.setDescription(Languages.getMessage(serverLanguage, "USER_MESSAGES_SERVER_OWNER_ONLY"));
        eb.setColor(new Color(168, 84, 84, 255));

        event.replyEmbeds(eb.build()).setEphemeral(true).queue();
    }

    public static void languageAutoComplete(CommandAutoCompleteInteractionEvent event) {
        String[] languages = new String[] {
                "English",
                "Hungarian"
        };

        List<Command.Choice> optionsLang = Stream.of(languages).filter(word -> word.startsWith(event.getFocusedOption().getValue()))
                .map(word -> new Command.Choice(word, word)).collect(Collectors.toList());
        event.replyChoices(optionsLang).queue();
    }

    public static void updateLanguage(SlashCommandInteractionEvent event) {
        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();
        String language = event.getOption("lang").getAsString();

        switch (language) {
            case "English" -> Database.updateServerLang(serverID, "EN");
            case "Hungarian" -> Database.updateServerLang(serverID, "HU");
        }

        String serverLanguage = Database.serverGetLanguage(serverID);

        if (language.equals("English") || language.equals("Hungarian")) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_LANGUAGE_UPDATE") + " " + language);
            eb.setColor(new Color(158, 222, 52));

            event.replyEmbeds(eb.build()).setEphemeral(true).queue();

        }
        else {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setDescription(Languages.getMessage(serverLanguage, "OWNER_LANGUAGE_NOT_EXIST"));
            eb.setColor(new Color(168, 84, 84, 255));

            event.replyEmbeds(eb.build()).setEphemeral(true).queue();
        }
    }
}
