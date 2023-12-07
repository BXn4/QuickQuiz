package com.bxn4.discord.QuickQuiz.commands.quiz;

import com.bxn4.discord.QuickQuiz.Database;
import com.bxn4.discord.QuickQuiz.Languages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class Toplist {

    public static void getToplist(ButtonInteractionEvent event, Integer OFFSET, String ORDER_BY) {
        Long serverID = event.getGuild().getIdLong();
        Long userID = event.getUser().getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);
        String serverName = event.getGuild().getName();

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle(serverName + " TOP 10");

        eb.setColor(new Color(236, 210, 89));

        List<List<Object>> userList = Database.getUsersToplist(serverID, OFFSET, ORDER_BY);

        StringBuilder userToplist = new StringBuilder();

        for (int i = 0; i < userList.size(); i++) {
            OFFSET++;
            Long toplistUserID = (Long) userList.get(i).get(1);
            String userName = (String) userList.get(i).get(0);
            User user = event.getJDA().getUserById(toplistUserID);

            if (user != null) {
                String userMention = user.getAsMention();
                userToplist.append("**" + OFFSET + ".** " + userMention + ": " + userList.get(i).get(2) + "XP\n");
            } else {
                userToplist.append("**" + OFFSET + ".** ** " + userName + "**: " + userList.get(i).get(2) + "XP\n");
            }
        }

        //System.out.println(userToplist);

        eb.setDescription(":trophy: " + Languages.getMessage(serverLanguage, "TOPLIST_USER_RANK") + "** " + Database.getUserRank(serverID, userID) +
                "**\n\n" + userToplist);

        int roundedOffset = (Math.round(OFFSET / 10.0f) * 10) - 10;

       /* if(ORDER_BY.equals("DESC")) {
            toplistActions = ActionRow.of(
                    Button.secondary("toplist_search", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_SEARCH"))).withEmoji(Emoji.fromUnicode("\uD83D\uDD0D")).withDisabled(true),
                    Button.secondary("toplist_orderby_" + roundedOffset + "_DESC", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_ORDER_BY_DESC"))).withEmoji(Emoji.fromUnicode("⬇️"))
            );
        }
        else {
            toplistActions = ActionRow.of(
                    Button.secondary("toplist_search", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_SEARCH"))).withEmoji(Emoji.fromUnicode("\uD83D\uDD0D")).withDisabled(true),
                    Button.secondary("toplist_orderby_" + roundedOffset + "_ASC", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_ORDER_BY_ASC"))).withEmoji(Emoji.fromUnicode("⬆️"))
            );
        }

        ActionRow toplistPages = null;
        Integer maxPages = Database.countToplistMaxPages(serverID);

        if (OFFSET == 10 && maxPages > 1) {
            toplistPages = ActionRow.of(
                    Button.secondary("toplist_page_0", Emoji.fromUnicode("⬅️")).withDisabled(true),
                    Button.secondary("pages", roundedOffset + "/" + maxPages).withDisabled(true),
                    Button.secondary("toplist_page_10_" + ORDER_BY, Emoji.fromUnicode("➡️")).withDisabled(false)
            );
        } else if (OFFSET == 10 && maxPages == 1) {
            toplistPages = ActionRow.of(
                    Button.secondary("toplist_page_0", Emoji.fromUnicode("⬅️")).withDisabled(true),
                    Button.secondary("pages", roundedOffset + "/" + maxPages).withDisabled(true),
                    Button.secondary("toplist_page_0", Emoji.fromUnicode("➡️")).withDisabled(true)
            );
        } else if (OFFSET / 10 != maxPages) {
            toplistPages = ActionRow.of(
                    Button.secondary("toplist_page_" + roundedOffset + "_" + ORDER_BY, Emoji.fromUnicode("⬅️")).withDisabled(false),
                    Button.secondary("pages", roundedOffset + "/" + maxPages).withDisabled(true),
                    Button.secondary("toplist_page_" + (roundedOffset + 10) + "_" + ORDER_BY, Emoji.fromUnicode("➡️")).withDisabled(false)
            );
        } else if (OFFSET / 10 == maxPages) {
            toplistPages = ActionRow.of(
                    Button.secondary("toplist_page_" + roundedOffset + "_" + ORDER_BY, Emoji.fromUnicode("⬅️")).withDisabled(false),
                    Button.secondary("pages", roundedOffset + "/" + maxPages).withDisabled(true),
                    Button.secondary("toplist_page_" + (roundedOffset + 10) + "_" + ORDER_BY, Emoji.fromUnicode("➡️")).withDisabled(true)
            );
        } */

        ActionRow toplistActions;

        toplistActions = ActionRow.of(
                Button.secondary("quiz_menu", Objects.requireNonNull(Languages.getMessage(serverLanguage, "BUTTON_MENU")))
                        .withEmoji(Emoji.fromCustom("menu", Long.parseLong("1166450383737069588"), false))
        );

        event.editMessageEmbeds(eb.build()).setComponents(toplistActions).queue();
    }

    public static void searchModal(ButtonInteractionEvent event) {
        Long serverID = event.getGuild().getIdLong();
        String serverLanguage = Database.serverGetLanguage(serverID);

        TextInput time = TextInput.create("name", Objects.requireNonNull(Languages.getMessage(serverLanguage, "TOPLIST_NAME")), TextInputStyle.SHORT)
                .setPlaceholder(Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_TOPLIST_NAME_PLACEHOLDER")))
                .setMinLength(0)
                .setMaxLength(120)
                .setRequired(true)
                .build();

        Modal modal = Modal.create("toplistSearch", Objects.requireNonNull(Languages.getMessage(serverLanguage, "MODAL_TOPLIST_NAME_TITLE")))
                .addComponents(ActionRow.of(time))
                .build();

        event.replyModal(modal).queue();
    }
}
