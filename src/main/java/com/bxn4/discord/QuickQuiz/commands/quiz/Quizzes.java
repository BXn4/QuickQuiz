package com.bxn4.discord.QuickQuiz.commands.quiz;

import com.bxn4.discord.QuickQuiz.Database;
import com.bxn4.discord.QuickQuiz.Languages;
import com.bxn4.discord.QuickQuiz.objects.QuizObj;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.*;

public class Quizzes {

    public static void showQuizzes(ButtonInteractionEvent event) {
        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);
        Long quizID = Database.getQuizByNumber(serverID, 1);
        Integer serverQuizzes = Database.countServerQuizzes(serverID);
        EmbedBuilder eb = new EmbedBuilder();

        if (quizID != null) {
            List<Object> quizDetails = new ArrayList<>(Database.getQuiz(serverID, quizID));
            String Title = (String) quizDetails.get(2);
            String Question = (String) quizDetails.get(3);
            String Image = (String) quizDetails.get(6);

            eb.setTitle(Title);
            eb.addField(Objects.requireNonNull(Languages.getMessage(serverLanguage, "QUIZ_QUESTION")), Question, false);
            if (!Image.equals("EMPTY")) {
                eb.setThumbnail(Image);
            }

            eb.setColor(new Color(77, 126, 130));
            eb.setFooter(Languages.getMessage(serverLanguage, "QUIZ_ID") + quizID);

            ActionRow kvizMenu;

            if(QuizObj.isActiveQuiz(quizID)) {
                kvizMenu = ActionRow.of(
                        Button.success("quiz_start_" + quizID, Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_QUIZ_START"))).withDisabled(true),
                        Button.danger("quiz_stop_" + quizID,  Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_QUIZ_CANCEL"))).withDisabled(false),
                        Button.secondary("quiz_refresh_1",  Emoji.fromUnicode("\uD83D\uDD04")).withDisabled(false));
            }
            else {
                kvizMenu = ActionRow.of(
                        Button.success("quiz_start_" + quizID, Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_QUIZ_START"))).withDisabled(false),
                        Button.danger("quiz_stop_" + quizID,  Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_QUIZ_CANCEL"))).withDisabled(true),
                        Button.secondary("quiz_refresh_1",  Emoji.fromUnicode("\uD83D\uDD04")).withDisabled(false));
            }

            ActionRow kvizActions = ActionRow.of(
                    Button.secondary("quiz_editQ_" + quizID, Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_EDIT"))).withEmoji(Emoji.fromUnicode("\uD83D\uDCDD")),
                    Button.secondary("quiz_create", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_CREATE"))).withEmoji(Emoji.fromUnicode("\uD83D\uDD8A\uFE0F")));

            ActionRow kvizPages = null;

            if (serverQuizzes == 1) {
                kvizPages = ActionRow.of(
                        Button.secondary("quiz_set_page_0" , Emoji.fromUnicode("⬅️")).withDisabled(true),
                        Button.secondary("pages", "1/" + serverQuizzes).withDisabled(true),
                        Button.secondary("quiz_set_page_2", Emoji.fromUnicode("➡️")).withDisabled(true),
                        Button.secondary("quiz_menu", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_MENU"))).withEmoji(Emoji.fromCustom("menu", Long.parseLong("1166450383737069588"), false))
                );
            }

            else {
                kvizPages = ActionRow.of(
                        Button.secondary("quiz_set_page_0" , Emoji.fromUnicode("⬅️")).withDisabled(true),
                        Button.secondary("pages", "1/" + serverQuizzes).withDisabled(true),
                        Button.secondary("quiz_set_page_2", Emoji.fromUnicode("➡️")).withDisabled(false),
                        Button.secondary("quiz_menu", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_MENU"))).withEmoji(Emoji.fromCustom("menu", Long.parseLong("1166450383737069588"), false))
                );
            }

            event.editMessageEmbeds(eb.build()).setComponents(kvizMenu, kvizActions, kvizPages).queue();
        }
        else {
            eb.setTitle(Languages.getMessage(serverLanguage, "INFO_MESSAGES_OUT_OF_QUIZZES"));
            eb.setDescription(Languages.getMessage(serverLanguage, "INFO_MESSAGES_CREATE_NEW_QUIZ"));
            eb.setColor(new Color(77, 126, 130));

            ActionRow kvizActions = ActionRow.of(
                    Button.secondary("quiz_create", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_CREATE"))).withEmoji(Emoji.fromUnicode("\uD83D\uDD8A\uFE0F")),
                    Button.secondary("quiz_menu", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_MENU"))).withEmoji(Emoji.fromCustom("menu", Long.parseLong("1166450383737069588"), false)));

            event.editMessageEmbeds(eb.build()).setComponents(kvizActions).queue();
        }
    }

    public static void changePage(ButtonInteractionEvent event, int pageNumber) {
        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);
        Long quizID = null;
        if (pageNumber != 0) {
            quizID = Database.getQuizByNumber(serverID, pageNumber);
        }
        Integer serverQuizzes = Database.countServerQuizzes(serverID);
        EmbedBuilder eb = new EmbedBuilder();

        if (quizID != null) {
            List<Object> quizDetails = new ArrayList<>(Database.getQuiz(serverID, quizID));
            String Title = (String) quizDetails.get(2);
            String Question = (String) quizDetails.get(3);
            String Image = (String) quizDetails.get(6);

            eb.setTitle(Title);
            eb.addField(Objects.requireNonNull(Languages.getMessage(serverLanguage, "QUIZ_QUESTION")), Question, false);
            if (!Image.equals("EMPTY")) {
                eb.setThumbnail(Image);
            }

            eb.setColor(new Color(77, 126, 130));
            eb.setFooter(Languages.getMessage(serverLanguage, "QUIZ_ID") + quizID);

            ActionRow kvizMenu;

            if(QuizObj.isActiveQuiz(quizID)) {
                kvizMenu = ActionRow.of(
                        Button.success("quiz_start_" + quizID, Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_QUIZ_START"))).withDisabled(true),
                        Button.danger("quiz_stop_" + quizID,  Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_QUIZ_CANCEL"))).withDisabled(false),
                        Button.secondary("quiz_refresh_" + pageNumber,  Emoji.fromUnicode("\uD83D\uDD04")).withDisabled(false));
            }
            else {
                kvizMenu = ActionRow.of(
                        Button.success("quiz_start_" + quizID, Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_QUIZ_START"))).withDisabled(false),
                        Button.danger("quiz_stop_" + quizID,  Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_QUIZ_CANCEL"))).withDisabled(true),
                        Button.secondary("quiz_refresh_" + pageNumber,  Emoji.fromUnicode("\uD83D\uDD04")).withDisabled(false));
            }

            ActionRow kvizActions = ActionRow.of(
                    Button.secondary("quiz_editQ_" + quizID, Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_EDIT"))).withEmoji(Emoji.fromUnicode("\uD83D\uDCDD")),
                    Button.secondary("quiz_create", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_CREATE"))).withEmoji(Emoji.fromUnicode("\uD83D\uDD8A\uFE0F")));

            ActionRow kvizPages = null;

            if (pageNumber > 1 && pageNumber < serverQuizzes) {
                kvizPages = ActionRow.of(
                        Button.secondary("quiz_set_page_" + (pageNumber - 1), Emoji.fromUnicode("⬅️")).withDisabled(false),
                        Button.secondary("pages", + pageNumber + "/" + serverQuizzes).withDisabled(true),
                        Button.secondary("quiz_set_page_" + (pageNumber + 1), Emoji.fromUnicode("➡️")).withDisabled(false),
                        Button.secondary("quiz_menu", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_MENU")))
                                .withEmoji(Emoji.fromCustom("menu", Long.parseLong("1166450383737069588"), false))
                );
            }

          if (pageNumber == 1 || pageNumber == 0) {
              kvizPages = ActionRow.of(
                      Button.secondary("quiz_set_page_" + (pageNumber - 1), Emoji.fromUnicode("⬅️")).withDisabled(true),
                      Button.secondary("pages", + pageNumber + "/" + serverQuizzes).withDisabled(true),
                      Button.secondary("quiz_set_page_" + (pageNumber + 1), Emoji.fromUnicode("➡️")).withDisabled(false),
                      Button.secondary("quiz_menu", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_MENU")))
                              .withEmoji(Emoji.fromCustom("menu", Long.parseLong("1166450383737069588"), false))
              );
          }

          if (pageNumber == serverQuizzes && pageNumber != 1) {
              kvizPages = ActionRow.of(
                      Button.secondary("quiz_set_page_" + (pageNumber - 1), Emoji.fromUnicode("⬅️")).withDisabled(false),
                      Button.secondary("pages", + pageNumber + "/" + serverQuizzes).withDisabled(true),
                      Button.secondary("quiz_set_page_" + (pageNumber + 1), Emoji.fromUnicode("➡️")).withDisabled(true),
                      Button.secondary("quiz_menu", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_MENU")))
                              .withEmoji(Emoji.fromCustom("menu", Long.parseLong("1166450383737069588"), false))
              );
          }

            if (pageNumber == serverQuizzes && pageNumber == 1) {
                kvizPages = ActionRow.of(
                        Button.secondary("quiz_set_page_" + (pageNumber - 1), Emoji.fromUnicode("⬅️")).withDisabled(true),
                        Button.secondary("pages", + pageNumber + "/" + serverQuizzes).withDisabled(true),
                        Button.secondary("quiz_set_page_" + (pageNumber + 1), Emoji.fromUnicode("➡️")).withDisabled(true),
                        Button.secondary("quiz_menu", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_MENU")))
                                .withEmoji(Emoji.fromCustom("menu", Long.parseLong("1166450383737069588"), false))
                );
            }


            event.editMessageEmbeds(eb.build()).setComponents(kvizMenu, kvizActions, kvizPages).queue();
        }
        else {
            eb.setTitle(Languages.getMessage(serverLanguage, "INFO_MESSAGES_OUT_OF_QUIZZES"));
            eb.setDescription(Languages.getMessage(serverLanguage, "INFO_MESSAGES_CREATE_NEW_QUIZ"));
            eb.setColor(new Color(77, 126, 130));

            ActionRow kvizActions = ActionRow.of(
                    Button.secondary("quiz_create", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_CREATE"))).withEmoji(Emoji.fromUnicode("\uD83D\uDD8A\uFE0F")),
                    Button.secondary("quiz_menu", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_MENU"))).withEmoji(Emoji.fromCustom("menu", Long.parseLong("1166450383737069588"), false)));

            event.editMessageEmbeds(eb.build()).setComponents(kvizActions).queue();
        }
    }
}
