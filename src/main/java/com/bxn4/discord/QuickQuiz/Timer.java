package com.bxn4.discord.QuickQuiz;

import com.bxn4.discord.QuickQuiz.commands.quiz.QuizActions;
import com.bxn4.discord.QuickQuiz.objects.QuizObj;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Timer {
    public static ConcurrentHashMap<Long, Long> quizValidTimeMap = new ConcurrentHashMap<>(); // LONG -> QUIZ ID, LONG -> VALID TIMESTAMPS

    public static void validTimer() {
        Instant instant = Instant.now();
        long currentTimestamp = instant.getEpochSecond();

        System.out.println(quizValidTimeMap);

        for (Map.Entry<Long, Long> entry : quizValidTimeMap.entrySet()) {
            Long quizID = entry.getKey();
            Long validTime = entry.getValue();

            if (QuizObj.isActiveQuiz(quizID)) {
                if (currentTimestamp >= validTime) {
                    System.out.println("LEJÁRT: " + quizID);

                    Long messageID = QuizObj.getQuizMessageID(quizID);

                    QuizActions.endedQuiz(quizID, messageID);

                    //quizValidTimeMap.remove(quizID);
                }
            }
            /*if (!QuizObj.isActiveQuiz(quizID)) {
                System.out.println(QuizObj.isActiveQuiz(quizID));
                quizValidTimeMap.remove(quizID, validTime);
            }*/
        }
    }
}
