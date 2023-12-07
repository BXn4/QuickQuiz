package com.bxn4.discord.QuickQuiz.commands.quiz;

import com.bxn4.discord.QuickQuiz.Database;
import com.bxn4.discord.QuickQuiz.Languages;
import com.bxn4.discord.QuickQuiz.objects.UserObj;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Menu {

    public static void quizMainButton(ButtonInteractionEvent event) {
        Long serverID = event.getGuild().getIdLong();

        if (Database.checkIfTheOwnerSetTheRole(serverID) && Database.checkIfTheOwnerSetTheChannel(serverID)) {
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
                creatorMenu(event);
            } else {
                userMenu(event);
            }
        }
    }

    private static void creatorMenu(ButtonInteractionEvent event) {
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

        eb.setTitle(Languages.getMessage(serverLanguage, "STATS"));

        eb.setDescription(Languages.getMessage(serverLanguage, "LEVEL") + " **" + userLevel + "** (" + userLevelProgress + "%)\n**" +
                (needXp - XP) + "**" + Languages.getMessage(serverLanguage, "NEEDED_XP_1") + " " + nextLvl + Languages.getMessage(serverLanguage, "NEEDED_XP_2") +
                "\n" + Languages.getMessage(serverLanguage, "CORRECT_ANSWERS") + " " + Corrects);

        eb.setColor(new Color(77, 126, 130));

        ActionRow kvizMenu = ActionRow.of(
               Button.secondary("quiz_create", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_CREATE"))).withEmoji(Emoji.fromUnicode("\uD83D\uDD8A\uFE0F")),
                Button.secondary("quiz_quizzes", Languages.getMessage(serverLanguage, "BUTTON_QUIZZES") + ": " + quizzes).withEmoji(Emoji.fromUnicode("\uD83D\uDDD2\uFE0F")));

        ActionRow kvizMenu2 = ActionRow.of(
               Button.secondary("quiz_toplist_0_DESC", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_TOPLIST"))).withEmoji(Emoji.fromUnicode("\uD83C\uDFC6")));
               //Button.secondary("quiz_mystats", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_MY_STATS"))).withEmoji(Emoji.fromUnicode("\uD83D\uDCC8")));

        ActionRow kvizMenu3 = ActionRow.of(
                Button.primary("quiz_publics", Languages.getMessage(serverLanguage, "BUTTON_PUBLIC_QUIZZES") + ": 0").withEmoji(Emoji.fromUnicode("\uD83C\uDF0E"))).withDisabled(true);


        event.editMessageEmbeds(eb.build()).setComponents(kvizMenu, kvizMenu2, kvizMenu3).queue();

    }

    private static void userMenu(ButtonInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();

        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();
        Long userID = event.getUser().getIdLong();

        String serverLanguage = Database.serverGetLanguage(serverID);

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
                Button.secondary("quiz_toplist_0_DESC", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_TOPLIST"))).withEmoji(Emoji.fromUnicode("\uD83C\uDFC6")));


        event.editMessageEmbeds(eb.build()).setComponents(kvizMenu).queue();
    }
}
