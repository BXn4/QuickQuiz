package com.bxn4.discord.QuickQuiz.commands;

import com.bxn4.discord.QuickQuiz.Database;
import com.bxn4.discord.QuickQuiz.Help;
import com.bxn4.discord.QuickQuiz.Languages;
import com.bxn4.discord.QuickQuiz.Timer;
import com.bxn4.discord.QuickQuiz.commands.quiz.*;
import com.bxn4.discord.QuickQuiz.commands.quiz.Menu;
import com.bxn4.discord.QuickQuiz.objects.QuizObj;
import com.bxn4.discord.QuickQuiz.setup.Messages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericSelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static net.dv8tion.jda.api.interactions.commands.build.Commands.slash;

public class Commands extends ListenerAdapter {
    private static final ScheduledExecutorService quizTimingService = Executors.newScheduledThreadPool(4);
    private static final ScheduledExecutorService connectDBService = Executors.newScheduledThreadPool(1);

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        List<CommandData> commandDataList = new ArrayList<>();
        commandDataList.add(slash("quiz", "Show the quiz menu"));
        commandDataList.add(slash("role", "Set the quiz creator role")
                .addOption(OptionType.ROLE, "role", "Specify the quiz creator role", true));
        commandDataList.add(slash("language", "Change the bot language").addOption(OptionType.STRING, "lang", "Select a language", true, true));
        commandDataList.add(slash("about", "Info about the bot"));
        commandDataList.add(slash("help", "Show the help menu"));

        OptionData channel = new OptionData(OptionType.CHANNEL, "channel", "Specify the quiz channel", true).setChannelTypes(ChannelType.TEXT);
        commandDataList.add(slash("channel", "Set the quiz channel").addOptions(channel));
        event.getJDA().updateCommands().addCommands(commandDataList).queue();

        connectDBService.scheduleAtFixedRate(Database::connectToDatabase, 0, 5, TimeUnit.MINUTES);
        quizTimingService.scheduleAtFixedRate(Timer::validTimer, 0, 1, TimeUnit.SECONDS);

    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        String command = event.getName();
        switch (command) {
            case "language" -> Messages.languageAutoComplete(event);
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("role") || command.equals("channel") || command.equals("language")) {
            Long userID = event.getUser().getIdLong();
            Long ownerID = Objects.requireNonNull(event.getGuild()).getOwnerIdLong();

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

            if (userID.equals(ownerID)) {
                Admin = true;
            }


            if (Admin) {
                if (command.equals("role")) {
                    Messages.updateRole(event);
                }
                if (command.equals("channel")) {
                    Messages.updateChannel(event);
                }
                if (command.equals("language")) {
                    Messages.updateLanguage(event);
                }
            } else {
                Messages.sendNotOwnerMessageSlash(event);
            }
        }
        switch (command) {
            case "quiz" -> Quiz.quizMain(event);
            case "about" -> About(event);
            case "help" -> Help(event);
        }
    }
    @Override
    public void onGenericSelectMenuInteraction(GenericSelectMenuInteractionEvent event) {
        String componentId = event.getComponentId();
        String componentValue = String.valueOf(event.getValues());

        Long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();

        String[] buttonAction = componentId.split("_");

        if (buttonAction[0].equals("owner")) {
            Long userID = event.getUser().getIdLong();
            Long ownerID = event.getGuild().getOwnerIdLong();

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

            if (userID.equals(ownerID)) {
                Admin = true;
            }


            if (Admin) {
                switch (buttonAction[1]) {
                    case "change" -> {
                        switch (buttonAction[2]) {
                            case "language" -> {
                                switch (componentValue) {
                                    case "[EN]" -> {
                                        Database.updateServerLang(serverID, "EN");
                                        Messages.updateMessage(event, Integer.parseInt(buttonAction[3]));
                                    }
                                    case "[HU]" -> {
                                        Database.updateServerLang(serverID, "HU");
                                        Messages.updateMessage(event, Integer.parseInt(buttonAction[3]));
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Messages.sendNotOwnerMessageGeneric(event);
            }
        }

        if(componentId.equals("view")) {
            switch (componentValue) {
                case "[setup]" -> Help.setup(event);
                case "[quizzes]" -> Help.quizzes(event);
            }
        }
    }
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String componentId = event.getComponentId();

        String[] buttonAction = componentId.split("_");
        switch (buttonAction[0]) {
            case "owner" -> {
                Long userID = event.getUser().getIdLong();
                Long ownerID = Objects.requireNonNull(event.getGuild()).getOwnerIdLong();

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

                if (userID.equals(ownerID)) {
                    Admin = true;
                }

                if (Admin) {
                    switch (buttonAction[1]) {
                        case "change" -> {
                            switch (buttonAction[2]) {
                                case "page" -> Messages.changePageButton(event, Integer.parseInt(buttonAction[3]));
                            }
                        }
                    }
                } else {
                    Messages.sendNotOwnerMessageButton(event);
                }
            }
            case "quiz" -> {
                switch (buttonAction[1]) {
                    case "create" -> Create.createQuiz(event);
                    case "delete" -> Delete.deleteQuiz(event, Long.valueOf(buttonAction[2]));
                    case "quizzes" -> Quizzes.showQuizzes(event);
                    case "editQ" -> Edit.editQuiz(event, Long.valueOf(buttonAction[2]));
                    case "menu" -> Menu.quizMainButton(event);
                    case "start" -> QuizActions.startQuiz(event, Long.valueOf(buttonAction[2]));
                    case "stop" -> QuizActions.stopQuiz(event, Long.valueOf(buttonAction[2]));
                    case "answer" -> QuizActions.answerToQuiz(event, Long.valueOf(buttonAction[2]), Integer.parseInt(buttonAction[3]));
                    case "toplist" -> Toplist.getToplist(event, Integer.parseInt(buttonAction[2]), buttonAction[3]);
                    case "refresh" -> Quizzes.changePage(event, Integer.parseInt(buttonAction[2]));

                    case "edit" -> {
                        switch (buttonAction[2]) {
                            case "quiz" -> Edit.editQuizModal(event, Long.valueOf(buttonAction[3]));
                            case "image" -> Edit.changeQuizImage(event, Long.valueOf(buttonAction[3]));
                            case "time" -> Edit.changeQuizTime(event, Long.valueOf(buttonAction[3]));
                            case "answers" -> Edit.editQuizAnswers(event, Long.valueOf(buttonAction[3]), Integer.parseInt(buttonAction[4]));
                            case "back" -> Quizzes.changePage(event, Integer.parseInt(buttonAction[3]));
                        }
                    }

                    case "set" -> {
                        switch (buttonAction[2]) {
                            case "page" -> Quizzes.changePage(event, Integer.parseInt(buttonAction[3]));
                        }
                    }
                }
            }

            case "toplist" -> {
                switch (buttonAction[1]) {
                    case "orderby" -> {
                        String order = "DESC";
                        switch (buttonAction[3]) {
                            case "DESC" -> order = "ASC";
                            case "ASC" -> order = "DESC";
                        }
                        Toplist.getToplist(event, Integer.parseInt(buttonAction[2]), order);
                    }
                    case "search" -> Toplist.searchModal(event);
                    case "page" -> Toplist.getToplist(event, Integer.valueOf(buttonAction[2]), buttonAction[3]);
                }
            }
        }
    }
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        String[] quiz = event.getModalId().split("_");

        Long quizID = Long.valueOf(quiz[1]);
        Long serverID = event.getGuild().getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        if (!Database.getQuiz(serverID, quizID).isEmpty()) {
            if (quiz[0].equals("quizEditModmailText")) {
                String subject = event.getValue("subject").getAsString();
                String body = event.getValue("body").getAsString();
                String info = event.getValue("info").getAsString();

                List<Object> quizDetails = new ArrayList<>(Database.getQuiz(serverID, quizID));
                String Title = (String) quizDetails.get(2);
                String Question = (String) quizDetails.get(3);
                String Info = (String) quizDetails.get(4);
                String Image = (String) quizDetails.get(6);

                EmbedBuilder eb = new EmbedBuilder();

                if (subject.isEmpty()) {
                    subject = Title;
                } else {
                    Title = subject;
                }
                eb.setTitle(subject);

                if (body.isEmpty()) {
                    body = Question;
                } else {
                    Question = body;
                }

                if (info.isEmpty()) {
                    eb.addField(Objects.requireNonNull(Languages.getMessage(serverLanguage, "QUIZ_QUESTION")), body, false);
                    Info = "EMPTY";
                } else {
                    if (!info.equals("EMTPY")) {
                        eb.addField(Objects.requireNonNull(Languages.getMessage(serverLanguage, "QUIZ_QUESTION")), body, false);
                        eb.addField(Objects.requireNonNull(Languages.getMessage(serverLanguage, "QUIZ_QUIZ_DESC")), info, false);
                        Info = info;
                    } else {
                        eb.addField(Objects.requireNonNull(Languages.getMessage(serverLanguage, "QUIZ_QUESTION")), body, false);
                    }
                }

                if (!Image.equals("EMPTY")) {
                    eb.setImage(Image);
                }


                eb.setColor(new Color(77, 126, 130));
                eb.setFooter(Languages.getMessage(serverLanguage, "QUIZ_ID") + " " + quizID);

                Database.updateQuiz(serverID, quizID, Title, Question, Info);

                event.editMessageEmbeds(eb.build()).queue();
            }

            if (quiz[0].equals("quizEditModmailImage")) {
                String imageUrl = event.getValue("imageUrl").getAsString();

                Message message = event.getMessage();
                List<MessageEmbed> embeds = message.getEmbeds();
                MessageEmbed quizEmbed = embeds.get(0);

                EmbedBuilder eb = new EmbedBuilder(quizEmbed);

                if (!imageUrl.isEmpty()) {
                    try {
                        eb.setImage(imageUrl);
                    } catch (IllegalArgumentException e) {
                        eb.clear();

                        eb.setDescription(Languages.getMessage(serverLanguage, "NOT_VALID_IMAGE_URL"));
                        eb.setColor(new Color(168, 84, 84, 255));
                        event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                    }
                } else {
                    imageUrl = "EMPTY";
                    eb.setImage("https://empty.com");
                }

                Database.updateQuizImage(serverID, quizID, imageUrl);

                event.editMessageEmbeds(eb.build()).queue();
            }

            if (quiz[0].equals("quizEditModmailTime")) {
                String Time = event.getValue("time").getAsString();
                Integer TimeInt = 0;
                Boolean numberEntered = true;

                if (!Time.isEmpty()) {
                    try {
                        TimeInt = Integer.parseInt(Time);
                        if (TimeInt < 0) {
                            TimeInt = TimeInt * -1;
                        }
                    } catch (NumberFormatException e) {
                        numberEntered = false;
                    }
                }
                else {
                    List<Object> quizDetails = new ArrayList<>(Database.getQuiz(serverID, quizID));
                    Integer quizTime = (Integer) quizDetails.get(7);
                    Time = String.valueOf(quizTime);
                    TimeInt = quizTime;

                }



                if (numberEntered) {
                    Message message = event.getMessage();
                    List<ActionRow> components = message.getActionRows();
                    ActionRow quizMenu = components.get(0);
                    ActionRow quizAnswers = components.get(1);
                    ActionRow quizActions = components.get(2);

                    String textTime;

                    if (Time.equals("0") || Time.equals("-0")) {
                        textTime = "∞";
                    } else {
                        textTime = String.valueOf(TimeInt);
                    }

                    quizMenu.updateComponent("quiz_edit_time_" + quizID,
                            Button.primary("quiz_edit_time_" + quizID, textTime + " " + Languages.getMessage(serverLanguage, "BUTTON_MINUTES")).withEmoji(Emoji.fromUnicode("\uD83D\uDD52")));

                    Database.updateQuizTime(serverID, quizID, TimeInt);

                    event.editComponents(quizMenu, quizAnswers, quizActions).queue();
                } else {
                    EmbedBuilder eb = new EmbedBuilder();

                    eb.setDescription(Languages.getMessage(serverLanguage, "NOT_NUMBER"));
                    eb.setColor(new Color(168, 84, 84, 255));

                    event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                }
            }

            if (quiz[0].equals("quizEditModmailAnswer")) {
                String Answer = event.getValue("answer").getAsString();
                String isCorrect = event.getValue("isCorrect").getAsString();

                Integer isCorrectInt = 0;

                Message message = event.getMessage();
                List<ActionRow> components = message.getActionRows();
                ActionRow quizMenu = components.get(0);
                ActionRow quizActions = components.get(2);

                List<Object> quizDetails = new ArrayList<>(Database.getQuiz(serverID, quizID));
                String Answers = (String) quizDetails.get(5);
                String[] stringArray = Answers.split(",");
                List<String> answerList = new ArrayList<>(List.of(stringArray));

                for (int i = 0; i < answerList.size(); i++) {
                    String answer_ = answerList.get(i);
                    String[] parts = answer_.split("___");
                    //System.out.println(parts[1]);
                    if (isCorrect.equals("i") || isCorrect.equals("I") || isCorrect.equals("y") || isCorrect.equals("Y")) {
                            answerList.set(i, parts[0] + "___" + "0"); // NEED ONLY 1 CORRECT ANSWER
                    }
                }

                List<List<String>> splitAnswers = new ArrayList<>();
                for (String answer_ : answerList) {
                    String[] parts = answer_.split("___");
                    splitAnswers.add(Arrays.asList(parts));
                }

                List<Button> buttons = new ArrayList<>();

                if (Answer.isEmpty()) {
                    Answer = splitAnswers.get(Integer.parseInt(quiz[2]) - 1).get(0);
                }

                if (isCorrect.isEmpty()) {
                    isCorrect = splitAnswers.get(Integer.parseInt(quiz[2]) - 1).get(1);
                    if (isCorrect.equals(1)) {
                        isCorrect = "I";
                    }
                }

                if (isCorrect.toUpperCase().equals("I") || isCorrect.toUpperCase().equals("Y")) {
                    isCorrectInt = 1;
                }

                String newAnswers = Answer + "___" + isCorrectInt;

                answerList.remove(Integer.parseInt(quiz[2]) - 1);
                answerList.add(Integer.parseInt(quiz[2]) - 1, newAnswers);

                List<List<String>> newsplitAnswers = new ArrayList<>();
                for (String answer_ : answerList) {
                    String[] parts = answer_.split("___");
                    newsplitAnswers.add(Arrays.asList(parts));
                }

                for (int i = 0; i < newsplitAnswers.size(); i++) {
                    String answerSplit = newsplitAnswers.get(i).get(0);
                    String emojiUnicode = newsplitAnswers.get(i).get(1).equals("1") ? "✅" : "❌";

                    if (newsplitAnswers.get(i).get(1).equals("1")) {
                        Button button = Button.of(
                                ButtonStyle.SUCCESS,
                                "quiz_edit_answers_" + quiz[1] + "_" + (i + 1),
                                answerSplit
                        ).withEmoji(Emoji.fromUnicode(emojiUnicode));

                        buttons.add(button);
                    } else {
                        Button button = Button.of(
                                ButtonStyle.SECONDARY,
                                "quiz_edit_answers_" + quiz[1] + "_" + (i + 1),
                                answerSplit
                        ).withEmoji(Emoji.fromUnicode(emojiUnicode));

                        buttons.add(button);
                    }
                }

                ActionRow quizAnswers = ActionRow.of(buttons);

                Database.updateQuizAnswers(serverID, quizID, answerList);

                event.editComponents(quizMenu, quizAnswers, quizActions).queue();
            }
        }
        else {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setDescription(Languages.getMessage(serverLanguage, "QUIZ_NOT_FOUND"));
            eb.setColor(new Color(168, 84, 84, 255));

            event.replyEmbeds(eb.build()).setEphemeral(true).queue();
        }
    }

    public void onGuildJoin(GuildJoinEvent event) {
        System.out.println("Joined server: " + event.getGuild().getName());

        Messages.sendWelcomeMessage(event);
    }

    public void onGuildLeave(GuildLeaveEvent event) {
        System.out.println("Left server: " + event.getGuild().getName());
        Long serverID = event.getGuild().getIdLong();

        List<Object> serverQuizzes = new ArrayList<>(Database.getServerQuizzesID(serverID));
        System.out.println(serverQuizzes);

        for (int i = 0; i < serverQuizzes.size(); i++) {
            Long quizID = (Long) serverQuizzes.get(i);
            QuizObj.stopQuiz(quizID);
            Timer.quizValidTimeMap.remove(quizID);

            Database.setQuizActive(serverID, quizID, -1L, 0, -1L);
        }
    }

    private void About(@NotNull SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("QuickQuiz");
        eb.setDescription("Very simple to use Quiz bot with XP system.\n[Top.gg](https://top.gg/bot/1166431418533040209)\n");
        eb.addField("Developed by: ", "BXn4 (Bence) :flag_hu:", true);
        eb.addField("Discord: ", "@bxn4", true);
        eb.addField("Stats:", "Total users: **" + Database.countUsers() + "**\nTotal quizzes: **" + Database.countQuizzes() + "**", false);
        eb.setColor(new Color(77, 126, 130));
        eb.setFooter("Version: 0.11 #BUGFIXES");
        event.replyEmbeds(eb.build()).setEphemeral(true).queue();
    }

    private void Help(@NotNull SlashCommandInteractionEvent event) {
        Long serverID = event.getGuild().getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("QuickQuiz");
        eb.setDescription(Languages.getMessage(serverLanguage, "BOT_HELP"));
        eb.setColor(new Color(77, 126, 130));

        ActionRow view = ActionRow.of(StringSelectMenu.create("view")
                .addOptions(SelectOption.of(Objects.requireNonNull(Languages.getMessage(serverLanguage, "BOT_HELP_MENU_SETUP")), "setup").withEmoji(Emoji.fromUnicode("\uD83E\uDD16")))
                .addOptions(SelectOption.of(Objects.requireNonNull(Languages.getMessage(serverLanguage, "BOT_HELP_MENU_QUIZZES")), "quizzes").withEmoji(Emoji.fromUnicode("\uD83D\uDCDD")))
                .build());

        event.replyEmbeds(eb.build()).setComponents(view).setEphemeral(true).queue();
    }
}
