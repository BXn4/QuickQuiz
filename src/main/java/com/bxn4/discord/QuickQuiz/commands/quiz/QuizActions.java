package com.bxn4.discord.QuickQuiz.commands.quiz;

import com.bxn4.discord.QuickQuiz.Database;
import com.bxn4.discord.QuickQuiz.Languages;
import com.bxn4.discord.QuickQuiz.QuickQuiz;
import com.bxn4.discord.QuickQuiz.Timer;
import com.bxn4.discord.QuickQuiz.objects.QuizObj;
import com.bxn4.discord.QuickQuiz.objects.UserObj;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.awt.*;
import java.time.Instant;
import java.util.*;
import java.util.List;

import static com.bxn4.discord.QuickQuiz.QuickQuiz.shardManager;

public class QuizActions {

    public static void startQuiz(ButtonInteractionEvent event, Long quizID) {
        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        if (!QuizObj.isActiveQuiz(quizID)) {
            Long channelID = Database.getQuizChannelID(serverID);
            EmbedBuilder eb = new EmbedBuilder();

            List<Object> quizDetails = new ArrayList<>(Database.getQuiz(serverID, quizID));

            Instant instant = Instant.now();
            Long timestampMillis = instant.toEpochMilli();

            String Title = (String) quizDetails.get(2);
            String Question = (String) quizDetails.get(3);
            String Answers = (String) quizDetails.get(5);
            String[] stringArray = Answers.split(",");
            List<String> answerList = Arrays.asList(stringArray);
            String Image = (String) quizDetails.get(6);
            Integer time = (Integer) quizDetails.get(7);

            Long validTime = (timestampMillis / 1000) + time * 60;

            eb.setTitle(Title);
            eb.addField(Objects.requireNonNull(Languages.getMessage(serverLanguage, "QUIZ_QUESTION")), Question, false);

            if (!QuizObj.isActiveQuiz(quizID)) {
                if (time != 0) {
                    eb.setDescription(Languages.getMessage(serverLanguage, "QUICK_QUIZ_ACTIVE_TIME") + " <t:" + validTime + ":T>");
                } else {
                    eb.setDescription(Languages.getMessage(serverLanguage, "QUICK_QUIZ_ACTIVE_TIME") + " " + Languages.getMessage(serverLanguage, "QUICK_QUIZ_ACTIVE"));
                }
            }
            if (!Image.equals("EMPTY")) {
                eb.setImage(Image);
            }

            eb.setColor(new Color(77, 126, 130));


            List<String> answers = new ArrayList<>();
            answers.addAll(answerList);

            List<List<String>> splitAnswers = new ArrayList<>();
            for (String answer_ : answers) {
                String[] parts = answer_.split("___");
                splitAnswers.add(Arrays.asList(parts));
            }

            List<Button> buttons = new ArrayList<>();
            List<Button> actions = new ArrayList<>();

            List<String> emojis = new ArrayList<>();
            emojis.add("\uD83D\uDCD8");
            emojis.add("\uD83D\uDCD9");
            emojis.add("\uD83D\uDCD5");
            emojis.add("\uD83D\uDCD7");

            Collections.shuffle(emojis);

            for (int i = 0; i < splitAnswers.size(); i++) {
                String answerSplit = splitAnswers.get(i).get(0);

                if (splitAnswers.get(i).get(1).equals("1")) {
                    Button button = Button.of(
                            ButtonStyle.SECONDARY,
                            "quiz_answer_" + quizID + "_1",
                            answerSplit).withEmoji(Emoji.fromUnicode(emojis.get(i)));

                    buttons.add(button);
                } else {
                    Button button = Button.of(
                            ButtonStyle.SECONDARY,
                            "quiz_answer_" + quizID + "_0_" + (i + 1),
                            answerSplit).withEmoji(Emoji.fromUnicode(emojis.get(i)));

                    buttons.add(button);
                }
            }

            Collections.shuffle(buttons);

            actions.add(0,
                    Button.secondary("quiz_total", Languages.getMessage
                            (serverLanguage, "QUICK_QUIZ_ANSWERS") + " 0").withDisabled(true)
            );

            try {
                TextChannel textChannel = event.getGuild().getTextChannelById(channelID);
                MessageCreateAction sendQuiz = textChannel.sendMessageEmbeds(eb.build()).setActionRow(buttons).addActionRow(actions);

                int pageNumber = Database.getNumberByQuiz(serverID, quizID);

                if (time != 0) {
                    Timer.quizValidTimeMap.put(quizID, validTime);
                }

                sendQuiz.queue(message -> {
                    Long messageID = message.getIdLong();
                    QuizObj.setQuizActive(quizID, validTime);
                    QuizObj.setQuizMessageID(quizID, messageID);
                    Database.setQuizActive(serverID, quizID, messageID, 1, validTime);
                    Quizzes.changePage(event, pageNumber);
                    QuizObj.setTheServerID(quizID, serverID);
                    QuizObj.setQuizAnswersTotal(quizID);
                });
            }
            catch (Exception e) {
                eb.clear();

                eb.setDescription(Languages.getMessage(serverLanguage, "CANT_START_QUIZ"));
                eb.setColor(new Color(168, 84, 84, 255));

                event.replyEmbeds(eb.build()).setEphemeral(true).queue();
            }
        } else {
            EmbedBuilder eb = new EmbedBuilder();

            eb.setDescription(Languages.getMessage(serverLanguage, "QUIZ_IS_ACTIVE"));
            eb.setColor(new Color(168, 84, 84, 255));

            event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();

            Quizzes.changePage(event, Database.getNumberByQuiz(serverID, quizID));
        }
    }

    public static void stopQuiz(ButtonInteractionEvent event, Long quizID) {
        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();

        if (QuizObj.isActiveQuiz(quizID)) {
            Long channelID = Database.getQuizChannelID(serverID);
            String serverLanguage = Database.serverGetLanguage(serverID);

            List<Object> quizDetails = new ArrayList<>(Database.getQuiz(serverID, quizID));

            Instant instant = Instant.now();
            long timestampMillis = instant.toEpochMilli();
            long time = (timestampMillis / 1000);

            String Title = (String) quizDetails.get(2);
            String Question = (String) quizDetails.get(3);
            String Image = (String) quizDetails.get(6);

            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle(Title);
            eb.addField(Objects.requireNonNull(Languages.getMessage(serverLanguage, "QUIZ_QUESTION")), Question, false);

            if (!Image.equals("EMPTY")) {
                eb.setImage(Image);
            }

            eb.setDescription(Languages.getMessage(serverLanguage, "QUIZ_TIME_ENDED") + " <t:" + time + ":T>");

            eb.setColor(new Color(49, 51, 56));

            Long messageID = QuizObj.getQuizMessageID(quizID);

            try {
                TextChannel textChannel = event.getGuild().getTextChannelById(channelID);
                try {
                    Message message = textChannel.retrieveMessageById(messageID).complete();

                    List<ActionRow> currentComponents = message.getActionRows();
                    ActionRow buttons = currentComponents.get(0);
                    ActionRow answers = currentComponents.get(1);

                    ActionRow newButtons = buttons.withDisabled(true);

                    ActionRow actions = ActionRow.of(Button.secondary("quiz_total", Languages.getMessage(serverLanguage, "QUICK_QUIZ_ANSWERS") + " " + QuizObj.getQuizAnswersTotal(quizID)).withDisabled(true)
                    );

                    List<ActionRow> newComponents = new ArrayList<>(currentComponents);
                    newComponents.remove(buttons);
                    newComponents.remove(answers);
                    newComponents.add(newButtons);
                    newComponents.add(actions);

                    message.editMessageEmbeds(eb.build()).setComponents(newComponents).queue();

                    QuizObj.stopQuiz(quizID);

                    Database.setQuizActive(serverID, quizID, messageID, 0, -1L);

                    Timer.quizValidTimeMap.remove(quizID);

                    int pageNumber = Database.getNumberByQuiz(serverID, quizID);

                    Quizzes.changePage(event, pageNumber);
                } catch (ErrorResponseException e) {
                    QuizObj.stopQuiz(quizID);

                    Database.setQuizActive(serverID, quizID, messageID, 0, -1L);

                    Timer.quizValidTimeMap.remove(quizID);

                    int pageNumber = Database.getNumberByQuiz(serverID, quizID);
                    Quizzes.changePage(event, pageNumber);
                }
            }
            catch (Exception e) {
                eb.clear();

                eb.setDescription(Languages.getMessage(serverLanguage, "CANT_STOP_QUIZ"));
                eb.setColor(new Color(168, 84, 84, 255));

                event.replyEmbeds(eb.build()).setEphemeral(true).queue();
            }
        } else {
            int pageNumber = Database.getNumberByQuiz(serverID, quizID);
            Quizzes.changePage(event, pageNumber);
        }
    }

    public static void endedQuiz(Long quizID, Long messageID) {
        ShardManager shardManager = QuickQuiz.getShardManager();
        Long serverID = QuizObj.getServerID(quizID);
        Long channelID = Database.getQuizChannelID(serverID);
        String serverLanguage = Database.serverGetLanguage(serverID);

        try {
            TextChannel textChannel = shardManager.getGuildById(serverID).getTextChannelById(channelID);
            try {
                Message message = textChannel.retrieveMessageById(messageID).complete();

                List<MessageEmbed> embeds = message.getEmbeds();
                MessageEmbed quizEmbed = embeds.get(0);
                EmbedBuilder eb = new EmbedBuilder(quizEmbed);

                List<Object> quizDetails = new ArrayList<>(Database.getQuiz(serverID, quizID));
                String Info = (String) quizDetails.get(4);

                if (!Info.equals("EMPTY")) {
                    eb.addField(Objects.requireNonNull(Languages.getMessage(serverLanguage, "QUIZ_QUIZ_DESC")), Info, false);
                }

                Instant instant = Instant.now();
                long timestampMillis = instant.toEpochMilli();
                long time = (timestampMillis / 1000);

                eb.setDescription(Languages.getMessage(serverLanguage, "QUIZ_TIME_ENDED") + " <t:" + time + ":T>");

                eb.setColor(new Color(49, 51, 56));

                String Answers = (String) quizDetails.get(5);
                String[] stringArray = Answers.split(",");
                List<String> answerList = Arrays.asList(stringArray);

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
                        ).withEmoji(Emoji.fromUnicode(emojiUnicode)).withDisabled(true);

                        buttons.add(button);
                    } else {
                        Button button = Button.of(
                                ButtonStyle.SECONDARY,
                                "quiz_edit_answers_" + quizID + "_" + (i + 1),
                                answer
                        ).withEmoji(Emoji.fromUnicode(emojiUnicode)).withDisabled(true);

                        buttons.add(button);
                    }
                }

                ActionRow kvizAnswers = ActionRow.of(buttons);

                ActionRow actions = ActionRow.of(Button.secondary("quiz_total", Languages.getMessage(serverLanguage, "QUICK_QUIZ_ANSWERS") + " " + QuizObj.getQuizAnswersTotal(quizID)).withDisabled(true)
                );

                textChannel.editMessageEmbedsById(messageID, eb.build()).setComponents(kvizAnswers, actions).queue();

                QuizObj.stopQuiz(quizID);

                Database.setQuizActive(serverID, quizID, messageID, 0, -1L);

                Timer.quizValidTimeMap.remove(quizID);

            } catch (ErrorResponseException e) {
                QuizObj.stopQuiz(quizID);

                Database.setQuizActive(serverID, quizID, messageID, 0, -1L);

                Timer.quizValidTimeMap.remove(quizID);
            }
        }
        catch (Exception e) {
            QuizObj.stopQuiz(quizID);

            Database.setQuizActive(serverID, quizID, messageID, 0, -1L);

            Timer.quizValidTimeMap.remove(quizID);
        }
    }

    public static void updateQuizTime(Long serverID, Long quizID, Long messageID) {
        Long channelID = Database.getQuizChannelID(serverID);
        String serverLanguage = Database.serverGetLanguage(serverID);

        ShardManager shardManager = QuickQuiz.getShardManager();
        TextChannel textChannel = Objects.requireNonNull(shardManager.getGuildById(serverID)).getTextChannelById(channelID);
        assert textChannel != null;
        Message message = textChannel.retrieveMessageById(messageID).complete();

        List<MessageEmbed> embeds = message.getEmbeds();
        MessageEmbed quizEmbed = embeds.get(0);
        EmbedBuilder eb = new EmbedBuilder(quizEmbed);

        Instant instant = Instant.now();
        long timestampMillis = instant.toEpochMilli();
        long time = (timestampMillis / 1000);

        eb.setDescription(Languages.getMessage(serverLanguage, "QUIZ_TIME_ENDED") + " <t:" + time + ":T>");

        message.editMessageEmbeds(eb.build()).queue();
    }


    public static void answerToQuiz(ButtonInteractionEvent event, Long quizID, int isCorrect) {
        try {
            Long serverID = QuizObj.getServerID(quizID);
            String serverLanguage = Database.serverGetLanguage(serverID);
            Long userID = event.getUser().getIdLong();
            Long channelID = Database.getQuizChannelID(serverID);
            Long messageID = QuizObj.getQuizMessageID(quizID);

            if (!QuizObj.isUserAnsweredToQuiz(quizID, userID)) {
                TextChannel textChannel = Objects.requireNonNull(shardManager.getGuildById(serverID)).getTextChannelById(channelID);
                assert textChannel != null;

                Message message = textChannel.retrieveMessageById(messageID).complete();

                QuizObj.userAnswerToQuiz(quizID, userID);

                List<ActionRow> currentComponents = message.getActionRows();

                ActionRow actions = null;

                actions = currentComponents.get(currentComponents.size() - 1);

                List<ActionRow> newComponents = new ArrayList<>(currentComponents);
                try {
                    newComponents.remove(actions);
                } catch (Exception ignored) {

                }

                ActionRow answers = ActionRow.of(Button.secondary("quiz_total", Languages.getMessage(serverLanguage, "QUICK_QUIZ_ANSWERS") + " " + QuizObj.getQuizAnswersTotal(quizID)).withDisabled(true)
                );

                newComponents.add(answers);

                int rewardXP = 1;

                String userName = event.getUser().getName();

                Database.checkIfUserExist(serverID, userID, userName);

                List<Object> quizDetails = new ArrayList<>(Database.getQuiz(serverID, quizID));
                String Info = (String) quizDetails.get(4);

                if (isCorrect == 1) {
                    Random random = new Random();
                    rewardXP = random.nextInt(4, 12);

                    List<Object> userDetails = new ArrayList<>(Database.getUser(serverID, userID));
                    int XP = (Integer) userDetails.get(0) + rewardXP;
                    int Corrects = (Integer) userDetails.get(1);

                    int userLevel = UserObj.getLevel(XP);
                    int needXp = UserObj.getXpForLevel(userLevel + 1) - 1;
                    int nextLvl = userLevel + 1;

                    String userLevelProgress = String.format("%.2f", UserObj.getLevelProgress(XP + rewardXP));

                    EmbedBuilder eb = new EmbedBuilder();

                    eb.setTitle(Languages.getMessage(serverLanguage, "ANSWERS_IS_CORRECT"));

                    if (!Info.equals("EMPTY")) {
                        eb.setDescription(Info + "\n\n" + Languages.getMessage(serverLanguage, "LEVEL") + " **" + userLevel + "** (" + userLevelProgress + "%) " + "+" + rewardXP + "XP\n**" +
                                ((needXp - rewardXP) - XP) + "**" + Languages.getMessage(serverLanguage, "NEEDED_XP_1") + " " + nextLvl + Languages.getMessage(serverLanguage, "NEEDED_XP_2"));
                    } else {
                        eb.setDescription(Languages.getMessage(serverLanguage, "LEVEL") + " **" + userLevel + "** (" + userLevelProgress + "%) " + "+" + rewardXP + "XP\n**" +
                                ((needXp - rewardXP) - XP) + "**" + Languages.getMessage(serverLanguage, "NEEDED_XP_1") + " " + nextLvl + Languages.getMessage(serverLanguage, "NEEDED_XP_2"));
                    }

                    eb.setColor(new Color(158, 222, 52));

                    Database.updateUserXP(serverID, userID, XP);
                    Database.updateUserCorrects(serverID, userID, Corrects + 1);

                    event.replyEmbeds(eb.build()).setEphemeral(true).queue();

                } else {
                    EmbedBuilder eb = new EmbedBuilder();

                    List<Object> userDetails = new ArrayList<>(Database.getUser(serverID, userID));
                    int XP = (Integer) userDetails.get(0) + rewardXP;

                    int userLevel = UserObj.getLevel(XP);
                    int needXp = UserObj.getXpForLevel(userLevel + 1) - 1;
                    int nextLvl = userLevel + 1;

                    String userLevelProgress = String.format("%.2f", UserObj.getLevelProgress(XP + rewardXP));

                    eb.setTitle(Languages.getMessage(serverLanguage, "ANSWERS_IS_INCORRECT"));

                    if (!Info.equals("EMPTY")) {
                        eb.setDescription(Info + "\n\n" + Languages.getMessage(serverLanguage, "LEVEL") + " **" + userLevel + "** (" + userLevelProgress + "%) " + "+" + rewardXP + "XP\n**" +
                                ((needXp - rewardXP) - XP) + "**" + Languages.getMessage(serverLanguage, "NEEDED_XP_1") + " " + nextLvl + Languages.getMessage(serverLanguage, "NEEDED_XP_2"));
                    } else {
                        eb.setDescription(Languages.getMessage(serverLanguage, "LEVEL") + " **" + userLevel + "** (" + userLevelProgress + "%) " + "+" + rewardXP + "XP\n**" +
                                ((needXp - rewardXP) - XP) + "**" + Languages.getMessage(serverLanguage, "NEEDED_XP_1") + " " + nextLvl + Languages.getMessage(serverLanguage, "NEEDED_XP_2"));
                    }

                    eb.setColor(new Color(168, 84, 84, 255));

                    Database.updateUserXP(serverID, userID, XP);

                    event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                }


                message.editMessageComponents(newComponents).queue();
            } else {
                EmbedBuilder eb = new EmbedBuilder();

                eb.setDescription(Languages.getMessage(serverLanguage, "QUIZ_ANSWERED"));
                eb.setColor(new Color(168, 84, 84, 255));

                event.replyEmbeds(eb.build()).setEphemeral(true).queue();
            }
        } catch (Exception e) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.clear();

            eb.setDescription("Cannot answer to this quiz, because this quiz is not active, or deleted!\nIf the quiz is active, please report to me! DC: @bxn4 \n\n" + "**RAW Error message:** \n" + e + "\n(Known, not going to be fixed!)");
            eb.setColor(new Color(168, 84, 84, 255));

            event.replyEmbeds(eb.build()).setEphemeral(true).queue();
        }
    }
}