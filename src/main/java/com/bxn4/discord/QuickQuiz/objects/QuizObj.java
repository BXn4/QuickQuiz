package com.bxn4.discord.QuickQuiz.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizObj {
    private static HashMap<Long, Long> activeQuizzes = new HashMap<>(); // LONG -> quizID  LONG -> VALIDTIMESTAMP
    private static HashMap<Long, Integer> quizAnswersTotal = new HashMap<>(); // LONG -> quizID INT -> ANSWERS
    private static HashMap<Long, Long> quizMessageIds = new HashMap<>(); // LONG -> quizID LONG -> quizMessageID
    private static HashMap<Long, Long> serverIDMAP = new HashMap<>(); // LONG -> quizID LONG ->ServerID
    private static HashMap<Long, List<Long>> quizUserAnswers = new HashMap<>(); // LONG -> quizID LIST -> USERS


    public static boolean isActiveQuiz(Long quizID) {
        if (activeQuizzes.containsKey(quizID)) {
            return true;
        }
        return false;
    }

    public static void setQuizActive(Long quizID, Long validTime) {
        activeQuizzes.put(quizID, validTime);
    }

    public static void stopQuiz(Long quizID) {
        activeQuizzes.remove(quizID);
        quizMessageIds.remove(quizID);
        quizAnswersTotal.remove(quizID);
        serverIDMAP.remove(quizID);
        quizUserAnswers.remove(quizID);
    }

    public static void setQuizMessageID(Long quizID, Long messageID) {
        quizMessageIds.put(quizID, messageID);
    }


    public static Long getQuizMessageID(Long quizID) {
        return quizMessageIds.get(quizID);
    }

    public static void setTheServerID(Long quizID, Long serverID) {
        serverIDMAP.put(quizID, serverID);
    }
    public static Long getServerID(Long quizID) {
        return serverIDMAP.get(quizID);
    }

    public static void setQuizAnswersTotal(Long quizID) {
        if (!quizAnswersTotal.containsKey(quizID)) {
            quizAnswersTotal.put(quizID, 0);
        }
        else {
            quizAnswersTotal.replace(quizID, getQuizAnswersTotal(quizID) + 1);
        }
    }

    public static Integer getQuizAnswersTotal(Long quizID) {
        return quizAnswersTotal.get(quizID);
    }

    public static Boolean isUserAnsweredToQuiz(Long quizID, Long userID) {
        List<Long> users = quizUserAnswers.get(quizID);
        if (users == null) {
            return false;
        }
        return users.contains(userID);
    }

    public static void userAnswerToQuiz(Long quizID, Long userID) {
        List<Long> users = quizUserAnswers.get(quizID);
        if (users == null) {
            users = new ArrayList<>();
            users.add(userID);
            quizUserAnswers.put(quizID, users);
        } else {
            users.add(userID);
            quizUserAnswers.replace(quizID, users);
        }
        setQuizAnswersTotal(quizID);
    }


}
