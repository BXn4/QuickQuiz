package com.bxn4.discord.QuickQuiz.commands.quiz;

import com.bxn4.discord.QuickQuiz.Database;
import com.bxn4.discord.QuickQuiz.Languages;
import com.bxn4.discord.QuickQuiz.objects.UserObj;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Quiz {
    public static void quizMain(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        Long serverID = event.getGuild().getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        if (Database.checkIfTheOwnerSetTheRole(serverID) && Database.checkIfTheOwnerSetTheChannel(serverID)) {
            try {
                Long serverQuizRole = Database.serverGetQuizRole(serverID);
                Member member = event.getGuild().retrieveMemberById(event.getUser().getIdLong()).complete();

                boolean quizCreator = false;

                if (!member.getRoles().isEmpty()) {
                    for (Role role : member.getRoles()) {
                        if (role.getIdLong() == serverQuizRole) {
                            quizCreator = true;
                            break;
                        }
                    }
                }

                if (quizCreator) {
                    creator(event);
                } else {
                    user(event);
                }

            } catch (NullPointerException e) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setDescription("A bot csak szervereken használható!");
                eb.setColor(new Color(168, 84, 84, 255));

                event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
            }
        } else {
            EmbedBuilder eb = new EmbedBuilder();
            Member member = event.getGuild().retrieveMemberById(event.getUser().getIdLong()).complete();

            boolean Admin = false;

            if (!member.getRoles().isEmpty()) {
                for (Role role : member.getRoles()) {
                    if (role.getPermissions().contains(Permission.ADMINISTRATOR)) {
                        Admin = true;
                        break;
                    }
                }
            }

            Long userID = event.getUser().getIdLong();
            Long ownerID = Objects.requireNonNull(event.getGuild()).getOwnerIdLong();

            if (userID.equals(ownerID)) {
                Admin = true;
            }

            if (Admin) {
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

                event.getHook().sendMessageEmbeds(eb.build()).setComponents(actionRow).queue();
            } else {
                eb.setDescription(Languages.getMessage(serverLanguage, "QUICK_QUIZ_NOT_SET"));
                eb.setColor(new Color(168, 84, 84, 255));

                event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
            }
        }
    }

    private static void creator(SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();

        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();
        Long userID = event.getUser().getIdLong();

        String serverLanguage = Database.serverGetLanguage(serverID);

        String userName = event.getUser().getName();

        Database.checkIfUserExist(serverID, userID, userName);

        Integer quizzes = Database.countServerQuizzes(serverID);

        List<Object> userDetails = new ArrayList<>(Database.getUser(serverID, userID));

        Integer XP = (Integer) userDetails.get(0);
        Integer Corrects = (Integer) userDetails.get(1);

        int userLevel = UserObj.getLevel(XP);
        Integer needXp = UserObj.getXpForLevel(userLevel + 1) - 1;
        int nextLvl = userLevel + 1;

        String userLevelProgress = String.format("%.2f", UserObj.getLevelProgress(XP));

        eb.setDescription(Languages.getMessage(serverLanguage, "QUIZ_MESSAGE_DEFAULT") + "\n\n" + Languages.getMessage(serverLanguage, "LEVEL") + " **" + userLevel + "** (" + userLevelProgress + "%)\n**" +
                (needXp - XP) + "**" + Languages.getMessage(serverLanguage, "NEEDED_XP_1") + " " + nextLvl + Languages.getMessage(serverLanguage, "NEEDED_XP_2") +
                "\n" + Languages.getMessage(serverLanguage, "CORRECT_ANSWERS") + " " + Corrects);

        eb.setColor(new Color(77, 126, 130));

        ActionRow kvizMenu = ActionRow.of(
                Button.secondary("quiz_create", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_CREATE"))).withEmoji(Emoji.fromUnicode("\uD83D\uDD8A\uFE0F")),
                Button.secondary("quiz_quizzes", Languages.getMessage(serverLanguage, "BUTTON_QUIZZES") + ": " + quizzes).withEmoji(Emoji.fromUnicode("\uD83D\uDDD2\uFE0F")));

        ActionRow kvizMenu2 = ActionRow.of(
                Button.secondary("quiz_toplist_0_DESC", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_TOPLIST"))).withEmoji(Emoji.fromUnicode("\uD83C\uDFC6")));

        ActionRow kvizMenu3 = ActionRow.of(
                Button.primary("quiz_publics", Languages.getMessage(serverLanguage, "BUTTON_PUBLIC_QUIZZES") + ": 0").withEmoji(Emoji.fromUnicode("\uD83C\uDF0E"))).withDisabled(true);


        event.getHook().sendMessageEmbeds(eb.build()).setComponents(kvizMenu, kvizMenu2, kvizMenu3).setEphemeral(true).queue();
    }

    private static void user(SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();

        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();
        Long userID = event.getUser().getIdLong();

        String serverLanguage = Database.serverGetLanguage(serverID);

        String userName = event.getUser().getName();

        Database.checkIfUserExist(serverID, userID, userName);

        List<Object> userDetails = new ArrayList<>(Database.getUser(serverID, userID));
        Integer XP = (Integer) userDetails.get(0);
        Integer Corrects = (Integer) userDetails.get(1);

        int userLevel = UserObj.getLevel(XP);
        Integer needXp = UserObj.getXpForLevel(userLevel + 1) - 1;
        int nextLvl = userLevel + 1;

        String userLevelProgress = String.format("%.2f", UserObj.getLevelProgress(XP));

        eb.setTitle(Languages.getMessage(serverLanguage, "STATS"));
        eb.setDescription(Languages.getMessage(serverLanguage, "LEVEL") + " **" + userLevel + "** (" + userLevelProgress + "%)\n**" +
                (needXp - XP) + "**" + Languages.getMessage(serverLanguage, "NEEDED_XP_1") + " " + nextLvl + Languages.getMessage(serverLanguage, "NEEDED_XP_2") +
                "\n" + Languages.getMessage(serverLanguage, "CORRECT_ANSWERS") + " " + Corrects);

        eb.setColor(new Color(77, 126, 130));

        ActionRow kvizMenu = ActionRow.of(
                Button.secondary("quiz_toplist_0_DESC", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_TOPLIST"))).withEmoji(Emoji.fromUnicode("\uD83C\uDFC6"))
        );

        event.getHook().sendMessageEmbeds(eb.build()).setComponents(kvizMenu).setEphemeral(true).queue();
    }
}
