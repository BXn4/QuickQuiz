package com.bxn4.discord.QuickQuiz;

import com.bxn4.discord.QuickQuiz.objects.QuizObj;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Database {
    static Connection conn = null;

    public static void connectToDatabase() {
        Connection conn = connect();
    }

    public static Connection connect() {
        String databaseUrl = "jdbc:" + "mariadb" + "://" + "127.0.0.1" + ":" + "3306" + "/" + "quick_quiz";
        try {
            conn = DriverManager.getConnection(databaseUrl, "", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public static void addNewQuiz(
            Long SERVER_ID,
            Long QUIZ_ID,
            String TITLE,
            String QUESTION,
            String INFO,
            List<String> ANSWERS,
            String IMAGE,
            Integer TIME,
            Integer FAVS,
            Integer ACTIVE
    ) {
        try {
            PreparedStatement stmt;

            String insertQuery = "INSERT INTO QUIZZES (SERVER_ID, QUIZ_ID, TITLE, QUESTION, INFO, ANSWERS, IMAGE, TIME, FAVS, ACTIVE, ACTIVE_TIME) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stmt = conn.prepareStatement(insertQuery);

            stmt.setLong(1, SERVER_ID);
            stmt.setLong(2, QUIZ_ID);
            stmt.setString(3, TITLE);
            stmt.setString(4, QUESTION);
            stmt.setString(5, INFO);
            stmt.setString(6, String.join(",", ANSWERS));
            stmt.setString(7, IMAGE);
            stmt.setInt(8, TIME);
            stmt.setInt(9, FAVS);
            stmt.setInt(10, ACTIVE);
            stmt.setLong(11, -1);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteQuiz(Long SERVER_ID, Long QUIZ_ID) {
        try {
            PreparedStatement stmt;

            String deleteQuery = "DELETE FROM QUIZZES WHERE SERVER_ID = ? AND QUIZ_ID = ?";

            stmt = conn.prepareStatement(deleteQuery);

            stmt.setLong(1, SERVER_ID);
            stmt.setLong(2, QUIZ_ID);

            stmt.executeUpdate();

            int deleted = stmt.executeUpdate();
            Integer LIMIT = getServerMaxQuizzes(SERVER_ID);
            LIMIT += 1;

            updateServeQuizLimit(SERVER_ID, LIMIT);

            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Object> getQuiz(Long SERVER_ID, Long QUIZ_ID) {
        List<Object> quizDetails = new ArrayList<>();

        try {
            PreparedStatement stmt;

            String selectQuery = "SELECT SERVER_ID, QUIZ_ID, TITLE, QUESTION, INFO, ANSWERS, IMAGE, TIME, FAVS, ACTIVE " +
                    "FROM QUIZZES WHERE SERVER_ID = ? AND QUIZ_ID = ?";

            stmt = conn.prepareStatement(selectQuery);

            stmt.setLong(1, SERVER_ID);
            stmt.setLong(2, QUIZ_ID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Long serverId = rs.getLong("SERVER_ID");
                Long quizId = rs.getLong("QUIZ_ID");
                String title = rs.getString("TITLE");
                String question = rs.getString("QUESTION");
                String info = rs.getString("INFO");
                String answers = rs.getString("ANSWERS");
                String image = rs.getString("IMAGE");
                Integer time = rs.getInt("TIME");
                Integer favs = rs.getInt("FAVS");
                Integer active = rs.getInt("ACTIVE");

                quizDetails.add(serverId);
                quizDetails.add(quizId);
                quizDetails.add(title);
                quizDetails.add(question);
                quizDetails.add(info);
                quizDetails.add(answers);
                quizDetails.add(image);
                quizDetails.add(time);
                quizDetails.add(favs);
                quizDetails.add(active);
            }

            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return quizDetails;
    }

    public static List<Object> getServerQuizzesID(Long SERVER_ID) {
        List<Object> quizDetails = new ArrayList<>();

        try {
            PreparedStatement stmt;

            String selectQuery = "SELECT QUIZ_ID " +
                    "FROM QUIZZES WHERE SERVER_ID = ?";

            stmt = conn.prepareStatement(selectQuery);

            stmt.setLong(1, SERVER_ID);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long quizId = rs.getLong("QUIZ_ID");

                quizDetails.add(quizId);
            }

            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return quizDetails;
    }

    public static Long getQuizChannelID(Long SERVER_ID) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT CHANNEL_ID " +
                    "FROM SERVERS WHERE SERVER_ID = ?");

            stmt.setLong(1, SERVER_ID);

            ResultSet rs = stmt.executeQuery();

            Long CHANNEL_ID = null;
            if (rs.next()) {
                CHANNEL_ID = rs.getLong("CHANNEL_ID");
            }

            stmt.close();

            return CHANNEL_ID;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long getQuizIDS() {
        try {
            PreparedStatement stmt;

            stmt = conn.prepareStatement("SELECT QUIZ_ID FROM QUIZZES ORDER BY QUIZ_ID DESC LIMIT 1");

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Long QUIZ_ID = rs.getLong(1);
                stmt.close();
                return QUIZ_ID;
            }

            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0L;
    }

    public static Boolean checkIfTheQuizIsActive(Long SERVER_ID, Long QUIZ_ID) {
        try {
            PreparedStatement stmt;

            stmt = conn.prepareStatement("SELECT ACTIVE FROM QUIZZES WHERE SERVER_ID = ? AND QUIZ_ID = ?");
            stmt.setLong(1, SERVER_ID);
            stmt.setLong(2, QUIZ_ID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int active = rs.getInt("ACTIVE");

                stmt.close();
                stmt.close();

                return (active == 1);
            }
            stmt.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static void updateQuiz(Long SERVER_ID, Long QUIZ_ID, String TITLE, String QUESTION, String INFO) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE QUIZZES " +
                    "SET TITLE = ?, QUESTION = ?, INFO = ? " +
                    "WHERE SERVER_ID = ? AND QUIZ_ID = ?");

            stmt.setString(1, TITLE);
            stmt.setString(2, QUESTION);
            stmt.setString(3, INFO);
            stmt.setLong(4, SERVER_ID);
            stmt.setLong(5, QUIZ_ID);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setQuizActive(Long SERVER_ID, Long QUIZ_ID, Long MESSAGE_ID, Integer ACTIVE, Long ACTIVE_TIME) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE QUIZZES " +
                    "SET MESSAGE_ID  = ?, ACTIVE = ?, ACTIVE_TIME = ? " +
                    "WHERE SERVER_ID = ? AND QUIZ_ID = ?");

            stmt.setLong(1, MESSAGE_ID);
            stmt.setInt(2, ACTIVE);
            stmt.setLong(3, ACTIVE_TIME);
            stmt.setLong(4, SERVER_ID);
            stmt.setLong(5, QUIZ_ID);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateQuizImage(Long SERVER_ID, Long QUIZ_ID, String IMAGE) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE QUIZZES " +
                    "SET IMAGE = ? WHERE SERVER_ID = ? AND QUIZ_ID = ?");

            stmt.setString(1, IMAGE);
            stmt.setLong(2, SERVER_ID);
            stmt.setLong(3, QUIZ_ID);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateQuizTime(Long SERVER_ID, Long QUIZ_ID, Integer TIME) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE QUIZZES " +
                    "SET TIME = ? WHERE SERVER_ID = ? AND QUIZ_ID = ?");

            stmt.setInt(1, TIME);
            stmt.setLong(2, SERVER_ID);
            stmt.setLong(3, QUIZ_ID);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateQuizAnswers(Long SERVER_ID, Long QUIZ_ID, List<String> ANSWERS) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE QUIZZES " +
                    "SET ANSWERS = ? WHERE SERVER_ID = ? AND QUIZ_ID = ?");

            stmt.setString(1, String.join(",", ANSWERS));
            stmt.setLong(2, SERVER_ID);
            stmt.setLong(3, QUIZ_ID);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateServeQuizLimit(Long SERVER_ID, int LIMIT) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE SERVERS " +
                    "SET MAX_QUIZZES = ? WHERE SERVER_ID = ?");

            stmt.setInt(1, LIMIT);
            stmt.setLong(2, SERVER_ID);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer getServerMaxQuizzes(Long SERVER_ID) {
        try {
            PreparedStatement stmt;

            stmt = conn.prepareStatement("SELECT MAX_QUIZZES " +
                    "FROM SERVERS WHERE SERVER_ID = ?");

            stmt.setLong(1, SERVER_ID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Integer MAX_QUIZZES = rs.getInt("MAX_QUIZZES");
                stmt.close();

                return MAX_QUIZZES;
            }

            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 25;
    }

    public static String serverGetLanguage(Long SERVER_ID) {
        PreparedStatement stmt;
        try {
            if (checkIfServerExist(SERVER_ID)) {
                stmt = conn.prepareStatement("SELECT LANGUAGE FROM SERVERS WHERE SERVER_ID = ?");
                stmt.setLong(1, SERVER_ID);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String SERVER_LANGUAGE = rs.getString("LANGUAGE");
                    stmt.close();
                    return SERVER_LANGUAGE;
                }
            } else {
                stmt = conn.prepareStatement("INSERT INTO SERVERS " +
                        "(SERVER_ID, ROLE_ID, CHANNEL_ID, LANGUAGE, MAX_QUIZZES)" +
                        "VALUES (?, ?, ?, ?, ?)");

                stmt.setLong(1, SERVER_ID);
                stmt.setLong(2, -1);
                stmt.setLong(3, -1);
                stmt.setString(4, "EN");
                stmt.setInt(5, 25);

                stmt.executeUpdate();
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return "EN";
    }

    public static Integer countServerQuizzes(Long SERVER_ID) {
        try {
            PreparedStatement stmt;

            stmt = conn.prepareStatement("SELECT COUNT(*) FROM QUIZZES WHERE SERVER_ID = ?");

            stmt.setLong(1, SERVER_ID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Integer count = rs.getInt(1);
                stmt.close();
                return count;
            }

            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    public static Long serverGetQuizRole(Long SERVER_ID) {
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("SELECT ROLE_ID FROM SERVERS WHERE SERVER_ID = ?");
            stmt.setLong(1, SERVER_ID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Long ROLE_ID = rs.getLong("ROLE_ID");
                stmt.close();

                return ROLE_ID;
            }

            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static Boolean checkIfServerExist(Long SERVER_ID) {
        try {
            PreparedStatement stmt;

            stmt = conn.prepareStatement("SELECT SERVER_ID " +
                    "FROM SERVERS WHERE SERVER_ID = ?");
            stmt.setLong(1, SERVER_ID);

            ResultSet rs = stmt.executeQuery();

            boolean serverExist = rs.next();

            stmt.close();

            return serverExist;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateServerLang(Long SERVER_ID, String LANGUAGE) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE SERVERS " +
                    "SET LANGUAGE = ? WHERE SERVER_ID = ?");

            stmt.setString(1, LANGUAGE);
            stmt.setLong(2, SERVER_ID);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean checkIfTheOwnerSetTheRole(Long SERVER_ID) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT ROLE_ID " +
                    "FROM SERVERS WHERE SERVER_ID = ?");

            stmt.setLong(1, SERVER_ID);

            ResultSet rs = stmt.executeQuery();

            Long ROLE_ID = null;
            if (rs.next()) {
                ROLE_ID = rs.getLong("ROLE_ID");
            }

            stmt.close();

            return ROLE_ID != -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean checkIfTheOwnerSetTheChannel(Long SERVER_ID) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT CHANNEL_ID " +
                    "FROM SERVERS WHERE SERVER_ID = ?");

            stmt.setLong(1, SERVER_ID);

            ResultSet rs = stmt.executeQuery();

            Long CHANNEL_ID = null;
            if (rs.next()) {
                CHANNEL_ID = rs.getLong("CHANNEL_ID");
            }

            stmt.close();

            return CHANNEL_ID != -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateServerQuizRole(Long SERVER_ID, Long ROLE_ID) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE SERVERS " +
                    "SET ROLE_ID = ? WHERE SERVER_ID = ?");

            stmt.setLong(1, ROLE_ID);
            stmt.setLong(2, SERVER_ID);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateServerQuizChannel(Long SERVER_ID, Long CHANNEL_ID) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE SERVERS " +
                    "SET CHANNEL_ID = ? WHERE SERVER_ID = ?");

            stmt.setLong(1, CHANNEL_ID);
            stmt.setLong(2, SERVER_ID);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean checkIfAQuizIsActive(Long SERVER_ID) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT ACTIVE " +
                    "FROM QUIZZES WHERE SERVER_ID = ?");

            stmt.setLong(1, SERVER_ID);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int ACTIVE = rs.getInt("ACTIVE");
                if (ACTIVE == 1) {
                    stmt.close();
                    return true;
                }
            }

            stmt.close();

            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long getQuizByNumber(Long SERVER_ID, int NUMBER) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT QUIZ_ID " +
                    "FROM QUIZZES WHERE SERVER_ID = ?");

            stmt.setLong(1, SERVER_ID);

            ResultSet rs = stmt.executeQuery();

            for (int i = 0; i < NUMBER; i++) {
                if (!rs.next()) {
                    return null;
                }
            }

            Long quizId = rs.getLong("QUIZ_ID");

            stmt.close();

            return quizId;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getNumberByQuiz(Long SERVER_ID, Long QUIZ_ID) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT QUIZ_ID " +
                    "FROM QUIZZES WHERE SERVER_ID = ?");

            stmt.setLong(1, SERVER_ID);

            ResultSet rs = stmt.executeQuery();
            int i = 0;

            while (rs.next()) {
                i++;
                Long quizId = rs.getLong("QUIZ_ID");
                if (quizId.equals(QUIZ_ID)) {
                    stmt.close();
                    return i;
                }
            }

            stmt.close();

            return -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkIfUserExist(Long SERVER_ID, Long USER_ID, String NAME) {
        try {
            PreparedStatement stmt;

            stmt = conn.prepareStatement("SELECT * " +
                    "FROM USERS WHERE SERVER_ID = ? AND USER_ID = ?");

            stmt.setLong(1, SERVER_ID);
            stmt.setLong(2, USER_ID);

            ResultSet rs = stmt.executeQuery();

            boolean userExist = rs.next();

            stmt.close();

            if (!userExist) {
                stmt = conn.prepareStatement("INSERT INTO USERS " +
                        "(SERVER_ID, USER_ID, NAME, XP, FAVS, CORRECT_ANSWERS)" +
                        "VALUES (?, ?, ?, ?, ?, ?)");

                stmt.setLong(1, SERVER_ID);
                stmt.setLong(2, USER_ID);
                stmt.setString(3, NAME);
                stmt.setInt(4, 0);
                stmt.setInt(5, 0);
                stmt.setInt(6, 0);

                stmt.executeUpdate();
                stmt.close();
            }

            if (userExist) {
                String USERNAME = rs.getString("NAME");
                if (!USERNAME.equals(NAME)) {
                    stmt = conn.prepareStatement("UPDATE USERS SET NAME = ? " +
                            "WHERE SERVER_ID = ? AND USER_ID = ?");
                    stmt.setString(1, NAME);
                    stmt.setLong(2, SERVER_ID);
                    stmt.setLong(3, USER_ID);

                    stmt.executeUpdate();
                    stmt.close();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List getUser(Long SERVER_ID, Long USER_ID) {
        try {
            PreparedStatement stmt;

            stmt = conn.prepareStatement("SELECT * " +
                    "FROM USERS WHERE SERVER_ID = ? AND USER_ID = ?");

            stmt.setLong(1, SERVER_ID);
            stmt.setLong(2, USER_ID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Integer XP = rs.getInt("XP");
                Integer CORRECT_ANSWERS = rs.getInt("CORRECT_ANSWERS");
                stmt.close();

                List<Integer> userStats = new ArrayList<>();
                userStats.add(XP);
                userStats.add(CORRECT_ANSWERS);

                return userStats;
            }

            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static void updateUserXP(Long SERVER_ID, Long USER_ID, Integer XP) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE USERS " +
                    "SET XP = ? WHERE SERVER_ID = ? AND USER_ID = ?");

            stmt.setInt(1, XP);
            stmt.setLong(2, SERVER_ID);
            stmt.setLong(3, USER_ID);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateUserCorrects(Long SERVER_ID, Long USER_ID, Integer CORRECT_ANSWERS) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE USERS " +
                    "SET CORRECT_ANSWERS = ? WHERE SERVER_ID = ? AND USER_ID = ?");

            stmt.setInt(1, CORRECT_ANSWERS);
            stmt.setLong(2, SERVER_ID);
            stmt.setLong(3, USER_ID);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setQuizzes() {
        try {
            PreparedStatement stmt;

            stmt = conn.prepareStatement("SELECT * " +
                    "FROM QUIZZES WHERE ACTIVE = 1");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long SERVER_ID = rs.getLong("SERVER_ID");
                Long QUIZ_ID = rs.getLong("QUIZ_ID");
                Integer TIME = rs.getInt("TIME");
                Long MESSAGE_ID = rs.getLong("MESSAGE_ID");
                Long ACTIVE_TIME = rs.getLong("ACTIVE_TIME");

                if (MESSAGE_ID != -1L) {

                    if (!TIME.equals(0)) {
                        Timer.quizValidTimeMap.put(QUIZ_ID, ACTIVE_TIME);
                        //QuizActions.updateQuizTime(SERVER_ID, QUIZ_ID, MESSAGE_ID);
                    }

                    QuizObj.setQuizActive(QUIZ_ID, ACTIVE_TIME);
                    QuizObj.setQuizMessageID(QUIZ_ID, MESSAGE_ID);
                    Database.setQuizActive(SERVER_ID, QUIZ_ID, MESSAGE_ID, 1, ACTIVE_TIME);
                    QuizObj.setTheServerID(QUIZ_ID, SERVER_ID);
                    QuizObj.setQuizAnswersTotal(QUIZ_ID);
                }
                else {
                    Database.setQuizActive(SERVER_ID, QUIZ_ID, MESSAGE_ID, 0, -1L);
                }
            }

            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer countToplistMaxPages(Long SERVER_ID) {
        try {
            PreparedStatement stmt;

            stmt = conn.prepareStatement("SELECT COUNT(USER_ID) FROM USERS WHERE SERVER_ID = ?");

            stmt.setLong(1, SERVER_ID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Integer count = rs.getInt(1);
                stmt.close();

                double total = Math.round((double) count + 10.0) / 10.0;
                return (int) (total);
            }

            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 1;
    }

    public static List<List<Object>> getUsersToplist(Long SERVER_ID, Integer OFFSET, String ORDERBY) {
        try {
            PreparedStatement stmt;

            stmt = conn.prepareStatement("SELECT NAME, USER_ID, XP, CORRECT_ANSWERS " +
                    "FROM USERS WHERE SERVER_ID = ? ORDER BY XP " + ORDERBY + " LIMIT 10 OFFSET ?");

            stmt.setLong(1, SERVER_ID);
            stmt.setInt(2, OFFSET);

            ResultSet rs = stmt.executeQuery();

            List<List<Object>> userStats = new ArrayList<>();

            while (rs.next()) {
                String NAME = rs.getString("NAME");
                Long USER_ID = rs.getLong("USER_ID");
                Integer XP = rs.getInt("XP");
                Integer CORRECT_ANSWERS = rs.getInt("CORRECT_ANSWERS");

                List<Object> userData = new ArrayList<>();
                userData.add(NAME);
                userData.add(USER_ID);
                userData.add(XP);
                userData.add(CORRECT_ANSWERS);
                userStats.add(userData);
            }

            return userStats;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer getUserRank(Long SERVER_ID, Long USER_ID) {
        try {
            PreparedStatement stmt;

            stmt = conn.prepareStatement("SELECT XP FROM USERS WHERE SERVER_ID = ? AND USER_ID = ?");

            stmt.setLong(1, SERVER_ID);
            stmt.setLong(2, USER_ID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userXP = rs.getInt("XP");

                stmt = conn.prepareStatement("SELECT COUNT(*) AS RANK FROM USERS WHERE SERVER_ID = ? AND XP > ?");
                stmt.setLong(1, SERVER_ID);
                stmt.setInt(2, userXP);

                rs = stmt.executeQuery();

                if (rs.next()) {
                    return rs.getInt("RANK") + 1;
                }
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer countUsers() {
        try {
            PreparedStatement stmt;

            stmt = conn.prepareStatement("SELECT COUNT(USER_ID) FROM USERS");


            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Integer count = rs.getInt(1);
                stmt.close();

                return count;
            }

            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    public static Integer countQuizzes() {
        try {
            PreparedStatement stmt;

            stmt = conn.prepareStatement("SELECT COUNT(QUIZ_ID) FROM QUIZZES");


            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Integer count = rs.getInt(1);
                stmt.close();

                return count;
            }

            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }
}
