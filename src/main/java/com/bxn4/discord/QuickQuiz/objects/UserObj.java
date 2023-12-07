package com.bxn4.discord.QuickQuiz.objects;

public class UserObj {
    public static int getLevel(Integer XP) {
        return (int) Math.floor(Math.pow((double) XP / 2, 1 / 2.6));
    }

    public static int getXpForLevel(int level) {
        return (int) Math.floor(Math.pow(level, 2.6) + 0.99) * 2;
    }

    public static double getLevelProgress(Integer XP) {
        int currentXp = XP;
        int level = getLevel(XP);
        int startxp = getXpForLevel(level);
        int nextxp = getXpForLevel(level + 1);

        double progress = 0.0;

        if (nextxp - startxp != 0 && currentXp >= startxp) {
            progress = ((double) (currentXp - startxp) / (nextxp - startxp)) * 100;
            progress = Math.min(100.0, progress);
        }

        return progress;
    }
}
