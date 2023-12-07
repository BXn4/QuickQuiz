package com.bxn4.discord.QuickQuiz;

import com.bxn4.discord.QuickQuiz.commands.Commands;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QuickQuiz extends ListenerAdapter {

    public static ShardManager shardManager;

    public QuickQuiz() throws InvalidTokenException {
        String token = "";

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("/help, /quiz"));

        shardManager = builder.build();

        shardManager.addEventListener(this, this, new Commands());

    }


    public static ShardManager getShardManager() {
        return shardManager;
    }

    public static void main(String[] args) {
        try {
            new Languages();
            Class.forName("org.mariadb.jdbc.Driver");
            Database.connectToDatabase();
            Database.setQuizzes();

            QuickQuiz bot = new QuickQuiz();
        } catch (InvalidTokenException e) {
            System.out.println("[ERROR]: Invalid token!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
