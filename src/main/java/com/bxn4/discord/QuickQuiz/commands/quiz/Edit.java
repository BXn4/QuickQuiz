package com.bxn4.discord.QuickQuiz.commands.quiz;

import com.bxn4.discord.QuickQuiz.Database;
import com.bxn4.discord.QuickQuiz.Languages;
import com.bxn4.discord.QuickQuiz.objects.QuizObj;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Edit {

    public static void editQuiz(ButtonInteractionEvent event, long quizID) {
        Long serverID = event.getGuild().getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        EmbedBuilder eb = new EmbedBuilder();

        List<Object> quizDetails = new ArrayList<>(Database.getQuiz(serverID, quizID));

        if (!quizDetails.isEmpty()) {
            Long QuizID = (Long) quizDetails.get(1);
            String Title = (String) quizDetails.get(2);
            String Question = (String) quizDetails.get(3);
            String Info = (String) quizDetails.get(4);
            String Answers = (String) quizDetails.get(5);
            String[] stringArray = Answers.split(",");
            List<String> answerList = Arrays.asList(stringArray);
            String Image = (String) quizDetails.get(6);
            Integer Time = (Integer) quizDetails.get(7);


            eb.setTitle(Title);
            eb.addField(Objects.requireNonNull(Languages.getMessage(serverLanguage, "QUIZ_QUESTION")), Question, false);

            if (!Info.equals("EMPTY")) {
                    eb.addField(Objects.requireNonNull(Languages.getMessage(serverLanguage, "QUIZ_QUIZ_DESC")), Info, false);
                }

            if (!Image.equals("EMPTY")) {
                eb.setImage(Image);
            }

            String textTime;

            if (Time == 0) {
                textTime = "∞";
            } else {
                textTime = String.valueOf(Time);
            }

            eb.setFooter(Languages.getMessage(serverLanguage, "QUIZ_ID") + QuizID);
            eb.setColor(new Color(77, 126, 130));

            ActionRow kvizMenu = ActionRow.of(
                    Button.secondary("quiz_edit_quiz_" + quizID, Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_EDIT_QUIZ"))).withEmoji(Emoji.fromUnicode("\uD83D\uDCDD")),
                    Button.secondary("quiz_edit_image_" + quizID, Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_EDIT_IMAGE"))).withEmoji(Emoji.fromUnicode("\uD83D\uDDBC\uFE0F")),
                    Button.primary("quiz_edit_time_" + quizID, textTime + " " + Languages.getMessage(serverLanguage, "BUTTON_MINUTES")).withEmoji(Emoji.fromUnicode("\uD83D\uDD52")));

            List<List<String>> splitAnswers = new ArrayList<>();
            for (String answer : answerList) {
                String[] parts = answer.split("___");
                splitAnswers.add(Arrays.asList(parts));
            }

            List<Button> buttons = new ArrayList<>();

            for (int i = 0; i < splitAnswers.size(); i++) {
                String answer = splitAnswers.get(i).get(0);
                String emojiUnicode = splitAnswers.get(i).get(1).equals("1") ? "✅" : "❌";

                if (splitAnswers.get(i).get(1).equals("1")) {
                    Button button = Button.of(
                            ButtonStyle.SUCCESS,
                            "quiz_edit_answers_" + quizID + "_" + (i + 1),
                            answer
                    ).withEmoji(Emoji.fromUnicode(emojiUnicode));

                    buttons.add(button);
                } else {
                    Button button = Button.of(
                            ButtonStyle.SECONDARY,
                            "quiz_edit_answers_" + quizID + "_" + (i + 1),
                            answer
                    ).withEmoji(Emoji.fromUnicode(emojiUnicode));

                    buttons.add(button);
                }
            }

            ActionRow kvizAnswers = ActionRow.of(buttons);

            ActionRow kvizActions;

            if (QuizObj.isActiveQuiz(QuizID)) {
                kvizActions = ActionRow.of(
                        Button.secondary("quiz_edit_back_" + Database.getNumberByQuiz(serverID, QuizID), Emoji.fromUnicode("⬅️")),
                        Button.danger("quiz_delete_" + quizID, Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_DELETE"))).withEmoji(Emoji.fromUnicode("\uD83D\uDDD1\uFE0F")).withDisabled(true)
                );
            } else {
                kvizActions = ActionRow.of(
                        Button.secondary("quiz_edit_back_" + Database.getNumberByQuiz(serverID, QuizID), Emoji.fromUnicode("⬅️")),
                        Button.danger("quiz_delete_" + quizID, Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_DELETE"))).withEmoji(Emoji.fromUnicode("\uD83D\uDDD1\uFE0F"))

                );
            }

            event.editMessageEmbeds(eb.build()).setComponents(kvizMenu, kvizAnswers, kvizActions).queue();
        }
        else {
            eb.setDescription(Languages.getMessage(serverLanguage, "QUIZ_NOT_FOUND"));
            eb.setColor(new Color(168, 84, 84, 255));

            event.replyEmbeds(eb.build()).setEphemeral(true).queue();
        }

    }

    public static void editQuizModal(ButtonInteractionEvent event, Long quizID) {
        Long serverID = event.getGuild().getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        TextInput subject = TextInput.create("subject", Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_QUIZ_SUBJECT")), TextInputStyle.SHORT)
                .setPlaceholder(Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_QUIZ_SUBJECT_PLACEHOLDER")))
                .setMaxLength(30)
                .setRequired(false)
                .build();

        TextInput body = TextInput.create("body", Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_QUIZ_QUESTION")), TextInputStyle.PARAGRAPH)
                .setPlaceholder(Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_QUIZ_QUESTION_PLACEHOLDER")))
                .setMaxLength(210)
                .setRequired(false)
                .build();

        TextInput info = TextInput.create("info",  Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_QUIZ_QUIZ_DESC")), TextInputStyle.PARAGRAPH)
                .setPlaceholder(Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_QUIZ_QUIZ_DESC_PLACEHOLDER")))
                .setMaxLength(260)
                .setRequired(false)
                .build();

        Modal modal = Modal.create("quizEditModmailText_" + quizID, Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_QUIZ_EDIT_QUIZ_TITLE")))
                .addComponents(ActionRow.of(subject), ActionRow.of(body), ActionRow.of(info))
                .build();

        event.replyModal(modal).queue();
    }
    public static void changeQuizImage(ButtonInteractionEvent event, Long quizID) {
        Long serverID = event.getGuild().getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        TextInput subject = TextInput.create("imageUrl", Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_IMAGE_SUBJECT")), TextInputStyle.SHORT)
                .setPlaceholder("https://i.gifer.com/4KI.gif")
                .setMaxLength(320)
                .setRequired(false)
                .build();

        Modal modal = Modal.create("quizEditModmailImage_" + quizID, Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_IMAGE_EDIT_QUIZ_TITLE")))
                .addComponents(ActionRow.of(subject))
                .build();

        event.replyModal(modal).queue();
    }
    public static void changeQuizTime(ButtonInteractionEvent event, Long quizID) {
        Long serverID = event.getGuild().getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        TextInput time = TextInput.create("time", Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_TIME_SUBJECT")), TextInputStyle.SHORT)
                .setPlaceholder(Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_TIME_SUBJECT_PLACEHOLDER")))
                .setMinLength(0)
                .setMaxLength(3)
                .setRequired(false)
                .build();

        Modal modal = Modal.create("quizEditModmailTime_" + quizID, Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_TIME_SUBJECT_TITLE")))
                .addComponents(ActionRow.of(time))
                .build();

        event.replyModal(modal).queue();
    }
    public static void editQuizAnswers(ButtonInteractionEvent event, Long quizID, int answerID) {
        Long serverID = event.getGuild().getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        TextInput answer = TextInput.create("answer", Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_ANSWER_SUBJECT")), TextInputStyle.SHORT)
                .setPlaceholder(Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_ANSWER_SUBJECT_PLACEHOLDER")))
                .setMaxLength(16)
                .setRequired(false)
                .build();

        TextInput isCorrect = TextInput.create("isCorrect",  Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_ANSWER_IS_CORRECT")), TextInputStyle.SHORT)
                .setPlaceholder(Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_ANSWER_IS_CORRECT_PLACEHOLDER")))
                .setMaxLength(1)
                .setRequired(false)
                .build();

        Modal modal = Modal.create("quizEditModmailAnswer_" + quizID + "_" + answerID, Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_ANSWER_SUBJECT_TITLE")))
                .addComponents(ActionRow.of(answer), ActionRow.of(isCorrect))
                .build();

        event.replyModal(modal).queue();
    }
}
