package com.bxn4.discord.QuickQuiz.commands.quiz;

import com.bxn4.discord.QuickQuiz.Database;
import com.bxn4.discord.QuickQuiz.Languages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Create {

    public static void createQuiz(ButtonInteractionEvent event) {
        Long serverID = event.getGuild().getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        Long quizID = Database.getQuizIDS() + 1;

        Integer serverQuizLimit = Database.getServerMaxQuizzes(serverID);

        if (serverQuizLimit != 0) {
            Database.updateServeQuizLimit(serverID, serverQuizLimit - 1);

            String Title = Languages.getMessage(serverLanguage, "QUIZ_NEW_QUIZ_TITLE");
            String Question = Languages.getMessage(serverLanguage, "QUIZ_NEW_QUESTION");
            String Info = Languages.getMessage(serverLanguage, "QUIZ_NEW_DESC");
            String AnswerText = Languages.getMessage(serverLanguage, "QUIZ_ANSWERS");

            List<String> Answers = new ArrayList<>();
            Answers.add(AnswerText + "___0");
            Answers.add(AnswerText + "___1");
            Answers.add(AnswerText + "___0");
            Answers.add(AnswerText + "___0");

            String ImageUrl = "https://www.stx.ox.ac.uk/sites/default/files/stx/images/article/depositphotos_41197145-stock-photo-quiz.jpg";
            Integer Time = 10;
            Integer Favs = 0;
            Integer Active = 0;

            Database.addNewQuiz(serverID, quizID, Title, Question, Info, Answers, ImageUrl, Time, Favs, Active);

            Edit.editQuiz(event, quizID);

        } else {
            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle(Languages.getMessage(serverLanguage, "QUICK_QUIZ_MAX_TITLE"));
            eb.setDescription(Languages.getMessage(serverLanguage, "QUICK_QUIZ_MAX"));
            eb.setColor(new Color(0, 108, 147));

            event.replyEmbeds(eb.build()).setEphemeral(true).queue();
        }
    }

}
