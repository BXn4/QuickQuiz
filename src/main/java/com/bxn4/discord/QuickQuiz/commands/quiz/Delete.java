package com.bxn4.discord.QuickQuiz.commands.quiz;

import com.bxn4.discord.QuickQuiz.Database;
import com.bxn4.discord.QuickQuiz.Languages;
import com.bxn4.discord.QuickQuiz.objects.QuizObj;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.awt.*;
import java.util.Objects;

public class Delete {

    public static void deleteQuiz(ButtonInteractionEvent event, long quizID) {
        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();
        Integer pageNumber = Database.getNumberByQuiz(serverID, quizID);
        Integer serverQuizzes = Database.countServerQuizzes(serverID);
        String serverLanguage = Database.serverGetLanguage(serverID);

        if (!QuizObj.isActiveQuiz(quizID)) {

            Database.deleteQuiz(serverID, quizID);

            if (serverQuizzes > pageNumber) {
                Quizzes.changePage(event, pageNumber);
            } else {
                Quizzes.changePage(event, pageNumber - 1);
            }

        }
        else {
            EmbedBuilder eb = new EmbedBuilder();

            eb.setDescription(Languages.getMessage(serverLanguage, "QUIZ_DELETE_IS_ACTIVE"));
            eb.setColor(new Color(168, 84, 84, 255));

            event.replyEmbeds(eb.build()).setEphemeral(true).queue();
        }
    }
}
