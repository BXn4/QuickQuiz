package com.bxn4.discord.QuickQuiz;

import java.util.HashMap;
import java.util.Map;

public class Languages {

    private static Map<String, Map<String, String>> languageMap = new HashMap<>();

    public Languages() {
        Map<String, String> EN = new HashMap<>();
        Map<String, String> HU = new HashMap<>();

        //region EN
        //region [OWNER MESSAGES]
        EN.put("OWNER_WELCOME", "Welcome to QuickQuiz!");
        EN.put("OWNER_GUIDE_1", "The following steps will guide you through the QuickQuiz setup process.");
        EN.put("OWNER_NEXT", "Continue by pressing the right-pointing arrow button.");
        EN.put("OWNER_CHANGE_LANG", "You can change the bot default language pressing the change language.");
        EN.put("OWNER_HELP", "Need help? Use the `/help` command.");
        EN.put("OWNER_ROLE_TITLE", "Let's make a quiz creator role!");
        EN.put("OWNER_GUIDE_2", "With this role, the server members allowed to create, delete, edit, start or stop the quiz at anytime.");
        EN.put("OWNER_ROLE", "Please specify the quiz creator role with the `/role` command!");
        EN.put("OWNER_ROLE_EVERYONE", "Really everyone? Not a good idea...");
        EN.put("OWNER_ROLE_FINISHED_TITLE", "Role set!");
        EN.put("OWNER_ROLE_FINISHED", "Continue by pressing the right-pointing arrow button");
        EN.put("OWNER_CHANNEL_TITLE", "Create a quiz channel!");
        EN.put("OWNER_CHANNEL", "Please specify the channel with the `/channel` command, where the active quizzes will be sent!");
        EN.put("CHANNEL_SET", "Channel set!");
        EN.put("OWNER_FINISHED_TITLE", "That's all!");
        EN.put("OWNER_FINISHED", "Your members are now free to use the bot with the /quiz command!");
        EN.put("OWNER_LANGUAGE_UPDATE", "Language updated to:");
        EN.put("OWNER_LANGUAGE_NOT_EXIST", "This language is not yet supported!");

        //endregion
        //region [QUIZ MESSAGES]
        EN.put("QUIZ_MESSAGE_DEFAULT", "Here, you can manage the quizzes on the server.");
        EN.put("QUIZ_ID", "Quiz ID: ");
        EN.put("QUIZ_NEW_QUIZ_TITLE", "New quiz");
        EN.put("QUIZ_QUESTION", "Question:");
        EN.put("QUIZ_NEW_QUESTION", "Enter the question...");
        EN.put("QUIZ_QUIZ_DESC", "Description:");
        EN.put("QUIZ_NEW_DESC", "Enter the answer description...");
        EN.put("QUIZ_ANSWERS", "Answer");
        EN.put("QUICK_QUIZ_MAX_TITLE", "Out of quiz spaces!");
        EN.put("QUICK_QUIZ_MAX", "If you would like to create more quizzes on this server, please contact me, to unlock more space!\n50 quiz spaces = 0$!");
        EN.put("QUICK_QUIZ_ACTIVE_TIME", "Active until:");
        EN.put("QUICK_QUIZ_ACTIVE", "Until cancellation");
        EN.put("QUICK_QUIZ_ANSWERS", "Answers:");
        EN.put("QUIZ_TIME_ENDED", "Ended:");

        //endregion
        //region [BUTTONS]
        EN.put("DELETE_MESSAGE", "Delete message");
        EN.put("BUTTON_CREATE", "New quiz");
        EN.put("BUTTON_QUIZZES", "Quizzes");
        EN.put("BUTTON_TOPLIST", "Toplist");
        EN.put("BUTTON_FAVOURITES", "Favourites");
        EN.put("BUTTON_PUBLIC_QUIZZES", "Public quizzes");
        EN.put("BUTTON_MENU", "Menu");
        EN.put("BUTTON_EDIT_QUIZ", "Edit quiz");
        EN.put("BUTTON_EDIT", "Edit");
        EN.put("BUTTON_EDIT_IMAGE", "Change image");
        EN.put("BUTTON_MINUTES", "minutes");
        EN.put("BUTTON_DELETE", "Delete");
        EN.put("BUTTON_QUIZ_START", "Start");
        EN.put("BUTTON_QUIZ_CANCEL", "Cancel");
        EN.put("BUTTON_MY_STATS", "My stats");
        EN.put("BUTTON_SEARCH", "Search");
        EN.put("BUTTON_ORDER_BY_DESC", "Order: DESC");
        EN.put("BUTTON_ORDER_BY_ASC", "Order: ASC");

        //endregion
        //region [INFO MESSAGES]
        EN.put("INFO_MESSAGES_OUT_OF_QUIZZES", "Out of quizzes...");
        EN.put("INFO_MESSAGES_CREATE_NEW_QUIZ", "Create a new quiz!");
        EN.put("INFO_MESSAGES_NO_FAVOURITES", "No favourite quizzes...");
        EN.put("INFO_MESSAGES_ADD_FAVS", "Click the heart button at the quiz to mark it as a favourite!");
        EN.put("QUIZ_NOT_FOUND", "The quiz was deleted!");
        EN.put("NOT_VALID_IMAGE_URL", "Please provide a valid link that points to an image or attachment!");
        EN.put("NOT_NUMBER", "Please enter the time in number!");
        EN.put("QUIZ_IS_ACTIVE", "This quiz is active!");
        EN.put("QUIZ_DELETE_IS_ACTIVE", "Can't delete this quiz, because it's active!");
        EN.put("CANT_START_QUIZ", "Cannot start the quiz, because missing the permission to the channel, or the channel was deleted.\n\nThe channel can be modified with the `/channel` command if you have administrator privileges for the server!");
        EN.put("CANT_STOP_QUIZ", "Cannot stop the quiz, because missing the permission to the channel, or the channel was deleted.\n\nThe channel can be modified with the `/channel` command if you have administrator privileges for the server!");


        //endregion
        //region [USER MESSAGES]
        EN.put("USER_MESSAGES_SERVER_OWNER_ONLY", "You can't use this!");
        EN.put("USER_MESSAGES_SERVER_ONLY", "The bot can be used on servers only");
        EN.put("QUICK_QUIZ_NOT_SET", "QuickQuiz is not yet configured :( \nIf you want to use it, please contact the server Owner or an Admin!! \nThe configuration only takes 10 seconds!");
        EN.put("ANSWERS_IS_CORRECT", "Correct!");
        EN.put("ANSWERS_IS_INCORRECT", "Incorrect!");
        EN.put("LEVEL", "Level:");
        EN.put("NEEDED_XP_1", "XP need to achieve level");
        EN.put("NEEDED_XP_2", ".");
        EN.put("QUIZ_ANSWERED", "You already answered to this quiz!");
        EN.put("STATS", "Stats");
        EN.put("CORRECT_ANSWERS", "Correct answers:");
        //endregion

        //region [MODALS]
        EN.put("MODAL_QUIZ_SUBJECT", "Quiz name:");
        EN.put("MODAL_QUIZ_SUBJECT_PLACEHOLDER", "Example: A cool quiz..");
        EN.put("MODAL_QUIZ_EDIT_QUIZ_TITLE", "Edit quiz");
        EN.put("MODAL_QUIZ_QUESTION", "Edit question:");
        EN.put("MODAL_QUIZ_QUESTION_PLACEHOLDER", "What is the question?");
        EN.put("MODAL_QUIZ_QUIZ_DESC", "Description:");
        EN.put("MODAL_QUIZ_QUIZ_DESC_PLACEHOLDER", "Why is this the correct answer?");
        EN.put("MODAL_IMAGE_SUBJECT", "IMAGE URL:");
        EN.put("MODAL_IMAGE_EDIT_QUIZ_TITLE", "Change image");
        EN.put("MODAL_TIME_SUBJECT", "Time:");
        EN.put("MODAL_TIME_SUBJECT_PLACEHOLDER", "(0-999) minutes");
        EN.put("MODAL_TIME_SUBJECT_TITLE", "Edit time");
        EN.put("MODAL_ANSWER_SUBJECT", "Answer:");
        EN.put("MODAL_ANSWER_SUBJECT_PLACEHOLDER", "What is the answer?");
        EN.put("MODAL_ANSWER_SUBJECT_TITLE", "Edit answer");
        EN.put("MODAL_ANSWER_IS_CORRECT", "Is correct?");
        EN.put("MODAL_ANSWER_IS_CORRECT_PLACEHOLDER", "Y/N");
        EN.put("MODAL_TOPLIST_NAME_PLACEHOLDER", "Enter the username...");
        EN.put("MODAL_TOPLIST_NAME_TITLE", "Search");
        //endregion

        EN.put("SERVER_TOPLIST", "'s toplist");
        EN.put("TOPLIST_USER_RANK", "Rank:");
        EN.put("TOPLIST_RANK", "Rank");
        EN.put("TOPLIST_NAME", "Name");
        EN.put("TOPLIST_LVL", "Level");
        EN.put("TOPLIST_CORRECT_ANSWERS", "Correct answers");

        EN.put("BOT_HELP", "Hello! How I can help you?");
        EN.put("BOT_HELP_MENU_SETUP", "Setup");
        EN.put("BOT_HELP_MENU_QUIZZES", "Quizzes");
        EN.put("BOT_HELP_SETUP", "To use the bot, you must first set a role and a channel.\n\n" +
                "The role is required so that only people who have this role, can start quizzes. Those without this rank will not be able to start quizzes, " +
                "so the server will not be spammed with quizzes\n\n" +
                "The quiz channel is necessary to keep quizzes separate so that no one will be disturbed, and everyone can see which quiz is active.\n\n" +
                "**Set the role:**\nTo set the role, use the `/role` command, then select the role you want to set from the menu that appears.\n\n" +
                "**Set the channel:**\nTo set the quiz channel, use the `/channel` command, then in the menu that appears, select the text channel where the active quizzes will be send.\n**If the channel is private, add the bot to the channel!**\n\n" +
                "**After these:**\nOnce these two things are set up, everyone can use the bot on the server with the `/quiz` command.\n\n" +
                "**Important:**\nDon't forget to assign the role to the members you want to allow to start quizzes on the server. They can start and create quizzes at any time, but they cannot change the role and channel.\n\n**If you want to create quizzes, assign the role to yourself!**");
        EN.put("BOT_HELP_QUIZZES", "Quizzes are easy to manage and edit.\n\n" +
                "**Getting started:**\nFirst, use the `/quiz` command to access the server quiz menu, then click on the New quiz button!\n" +
                "This will bring up the quiz editor menu.\n\n" +
                "**Editing the quiz:**\nThe Edit quiz button allows you to edit the quiz title, the question, and the description of the answer, what the correct answer is, and why.\n" +
                "If you leave the quiz description blank, it will be removed from the quiz. The title and question will not be removed, so you can leave them blank if you don't want to edit them.\n" +
                "\n**Replacing the quiz image:**\nYou can use the replace image button to add an image to the quiz. If the image doesn't appear, it's because you need a link that points to an image. \nEg: https://something.com/cat.png. \nIf you leave this blank, the image will be removed.\n" +
                "\n**Change the valid time:**\nYou can use the minute button to set the validity time for the quiz. So if you set it to 5 minutes, and once 5 minutes have passed, the quiz will become inactive and no one will be able to answer the quiz, and the correct answer will be revealed. If you want it to never expire, then set it to 0 minutes!\n" +
                "\n**Edit the answers:**\nBelow these you will find the answer buttons. Click on one of the buttons, then in the menu that appears, type in the answer and in the text area below it, indicate that the answer is correct (Is it correct? Y/N). If you leave it blank, the answer will not be overwritten. You can edit every answers.\n" +
                "\n**Delete quiz:**\nYou can use the delete button to remove the quiz from the server\n\n" +
                "When you are done, press the back arrow and click the Start button and the quiz will be sent to the quiz channel!\n" +
                "You can cancel the quiz at any time, in which case the answers will not be revealed.");
        //endregion

        //region HU
        //region [OWNER MESSAGES]
        HU.put("OWNER_WELCOME", "Üdv a QuickQuiz-ben!");
        HU.put("OWNER_GUIDE_1", "A következő lépések végigvezetik a QuickQuiz beállítási folyamatán.");
        HU.put("OWNER_NEXT", "Nyomd meg a jobbra mutató nyíl gombot a következő lépéshez.");
        HU.put("OWNER_CHANGE_LANG", "Megváltoztatható a bot alapértelmezett nyelve a nyelvváltoztatás menüvel.");
        HU.put("OWNER_HELP", "Elakadtál? Használd a `/help` parancsot!");
        HU.put("OWNER_ROLE_TITLE", "Készítsünk egy kvíz készítő rangot!");
        HU.put("OWNER_GUIDE_2", "Ezzel a ranggal a szerver tagjai bármikor létrehozhatnak, törölhetik, szerkeszthetik, elindíthatják vagy leállíthatják a kvízt.");
        HU.put("OWNER_ROLE", "Kérlek add meg a kvíz készítő rangot a `/role` paranccsal!");
        HU.put("OWNER_ROLE_EVERYONE", "Komolyan everyone? Nem jó ötlet...");
        HU.put("OWNER_ROLE_FINISHED_TITLE", "Rang beállítva!");
        HU.put("OWNER_ROLE_FINISHED", "Nyomd meg a jobbra mutató nyíl gombot a következő lépéshez");
        HU.put("OWNER_CHANNEL_TITLE", "Csináljunk egy kvíz csatornát is!");
        HU.put("OWNER_CHANNEL", "Kérlek add meg a `/channel` paranccsal azt a csatornát, ahol majd az aktív kvízek megjelennek.");
        HU.put("CHANNEL_SET", "Csatorna beállítva!");
        HU.put("OWNER_FINISHED_TITLE", "Végeztünk!");
        HU.put("OWNER_FINISHED", "Mostmár a szerveren bárki tudja használni a botot a /quiz paranccsal!");
        HU.put("OWNER_LANGUAGE_UPDATE", "Nyelv beállítva a következőre:");
        HU.put("OWNER_LANGUAGE_NOT_EXIST", "Ez a nyelv még nem támogatott!");


        //endregion
        //region [QUIZ MESSAGES]
        HU.put("QUIZ_MESSAGE_DEFAULT", "Itt tudod a szerver kvízeit kezelni.");
        HU.put("QUIZ_ID", "Kvíz ID: ");
        HU.put("QUIZ_NEW_QUIZ_TITLE", "Új kvíz");
        HU.put("QUIZ_QUESTION", "Kérdés:");
        HU.put("QUIZ_NEW_QUESTION", "Add meg a kérdést...");
        HU.put("QUIZ_QUIZ_DESC", "A válasz ismertetése:");
        HU.put("QUIZ_NEW_DESC", "Add meg a válasz ismertetését...");
        HU.put("QUIZ_ANSWERS", "Válasz");
        HU.put("QUICK_QUIZ_MAX_TITLE", "Elfogyott a kvíz tárhely!");
        HU.put("QUICK_QUIZ_MAX", "Ha szeretnél több kvízt létrehozni ezen a szerveren, akkor kérlek keress fel engem!\n50 db kvíz tárhely = 0$!");
        HU.put("QUICK_QUIZ_ACTIVE_TIME", "Aktív eddig:");
        HU.put("QUICK_QUIZ_ACTIVE", "Megszakításig");
        HU.put("QUICK_QUIZ_ANSWERS", "Válaszok:");
        HU.put("QUIZ_TIME_ENDED", "Véget ért:");


        //endregion
        //region [BUTTONS]
        HU.put("DELETE_MESSAGE", "Üzenet törlése");
        HU.put("BUTTON_CREATE", "Új kvíz");
        HU.put("BUTTON_QUIZZES", "Kvízek");
        HU.put("BUTTON_TOPLIST", "Toplista");
        HU.put("BUTTON_FAVOURITES", "Kedvencek");
        HU.put("BUTTON_PUBLIC_QUIZZES", "Publikus kvízek");
        HU.put("BUTTON_MENU", "Menü");
        HU.put("BUTTON_EDIT_QUIZ", "Kvíz szerkesztése");
        HU.put("BUTTON_EDIT", "Szerkesztés");
        HU.put("BUTTON_EDIT_IMAGE", "Kép lecserélése");
        HU.put("BUTTON_MINUTES", "perc");
        HU.put("BUTTON_DELETE", "Törlés");
        HU.put("BUTTON_QUIZ_START", "Indítás");
        HU.put("BUTTON_QUIZ_CANCEL", "Megszakítás");
        HU.put("BUTTON_MY_STATS", "Statisztikáim");
        HU.put("BUTTON_SEARCH", "Keresés");
        HU.put("BUTTON_ORDER_BY_DESC", "Rendezés: CSÖKK.");
        HU.put("BUTTON_ORDER_BY_ASC", "Rendezés: NÖV.");

        //endregion
        //region [INFO MESSAGES]
        HU.put("INFO_MESSAGES_OUT_OF_QUIZZES", "Elfogytak a kvízek..");
        HU.put("INFO_MESSAGES_CREATE_NEW_QUIZ", "Hozz létre egy új kvízt!");
        HU.put("INFO_MESSAGES_NO_FAVOURITES", "Nincsenek kedvenc kvízek..");
        HU.put("INFO_MESSAGES_ADD_FAVS", "Kattints a kvíznél a szív gombra, hogy kedvencnek jelöld!");
        HU.put("QUIZ_NOT_FOUND", "A kvíz törölve lett!");
        HU.put("NOT_VALID_IMAGE_URL", "Kérlek olyan érvényes hivatkozást adj meg, ami egy képre, vagy csatolmányra mutat!");
        HU.put("NOT_NUMBER", "Kérlek az időt szám formátumban add meg!");
        HU.put("QUIZ_IS_ACTIVE", "Ez a kvíz már aktív!");
        HU.put("QUIZ_DELETE_IS_ACTIVE", "Ez a kvíz nem törölhető, mert aktív!");
        HU.put("CANT_START_QUIZ", "Nem lehet a kvízt elindítani, mert a csatornához nincs jogosultság, vagy a beállított csatorna törlésre került.\n\nA csatornát a `/channel` paranccsal lehet modósítani, ha a szerverhez van rendszergazda jogosultságod!");
        HU.put("CANT_STOP_QUIZ", "Nem lehet a kvízt megszakítani, mert a csatornához nincs jogosultság, vagy a beállított csatorna törlésre került.\n\nA csatornát a `/channel` paranccsal lehet modósítani, ha a szerverhez van rendszergazda jogosultságod!");


        //endregion
        //region [USER MESSAGES]
        HU.put("USER_MESSAGES_SERVER_ONLY", "A bot csak szerveren használható!");
        HU.put("USER_MESSAGES_SERVER_OWNER_ONLY", "Ezt nem használhatod!");
        HU.put("QUICK_QUIZ_NOT_SET", "A QuickQuiz még nem lett konfigurálva :( \nHa szeretnéd használni, akkor keressd fel a szerver Tulajdonost vagy egy Admint! \nA konfigurálás mindössze 10 másodpercet vesz igénybe!");
        HU.put("ANSWERS_IS_CORRECT", "Helyes!");
        HU.put("ANSWERS_IS_INCORRECT", "Helytelen!");
        HU.put("LEVEL", "Szint:");
        HU.put("NEEDED_XP_1", "XP kell a");
        HU.put("NEEDED_XP_2", ". szint eléréséhez.");
        HU.put("QUIZ_ANSWERED", "Már válaszoltál erre a kvízre!");
        HU.put("STATS", "Statisztikák");
        HU.put("CORRECT_ANSWERS", "Helyes válaszok:");
        //endregion

        //region [MODALS]
        HU.put("MODAL_QUIZ_SUBJECT", "Kvíz neve:");
        HU.put("MODAL_QUIZ_SUBJECT_PLACEHOLDER", "Például: Egy menő kvíz...");
        HU.put("MODAL_QUIZ_EDIT_QUIZ_TITLE", "Kvíz szerkesztése");
        HU.put("MODAL_QUIZ_QUESTION", "Kérdés szerkesztése:");
        HU.put("MODAL_QUIZ_QUESTION_PLACEHOLDER", "Mi legyen a kérdés?");
        HU.put("MODAL_QUIZ_QUIZ_DESC", "A válasz ismertetése:");
        HU.put("MODAL_QUIZ_QUIZ_DESC_PLACEHOLDER", "Miért ez a helyes válasz?");
        HU.put("MODAL_IMAGE_SUBJECT", "KÉP URL:");
        HU.put("MODAL_IMAGE_EDIT_QUIZ_TITLE", "Kép megváltoztatása");
        HU.put("MODAL_TIME_SUBJECT", "Idő:");
        HU.put("MODAL_TIME_SUBJECT_PLACEHOLDER", "(0-999) perc");
        HU.put("MODAL_TIME_SUBJECT_TITLE", "Idő szerkesztése");
        HU.put("MODAL_ANSWER_SUBJECT", "Válasz:");
        HU.put("MODAL_ANSWER_SUBJECT_PLACEHOLDER", "Mi legyen a válasz?");
        HU.put("MODAL_ANSWER_SUBJECT_TITLE", "Válasz szerkesztése");
        HU.put("MODAL_ANSWER_IS_CORRECT", "Helyes?");
        HU.put("MODAL_ANSWER_IS_CORRECT_PLACEHOLDER", "I/N");
        HU.put("MODAL_TOPLIST_NAME_PLACEHOLDER", "Add meg a felhasználó nevét...");
        HU.put("MODAL_TOPLIST_NAME_TITLE", "Keresés");
        //endregion

        HU.put("SERVER_TOPLIST", " toplistája");
        HU.put("TOPLIST_USER_RANK", "Helyezésed:");
        HU.put("TOPLIST_RANK", "Helyezés");
        HU.put("TOPLIST_NAME", "Név");
        HU.put("TOPLIST_LVL", "Szint");
        HU.put("TOPLIST_CORRECT_ANSWERS", "Helyes válaszok");

        HU.put("BOT_HELP", "Helló! Miben segíthetek?");
        HU.put("BOT_HELP_MENU_SETUP", "Beállítás");
        HU.put("BOT_HELP_MENU_QUIZZES", "Kvízek");
        HU.put("BOT_HELP_SETUP", "A bot használatához először be kell állítani egy rangot, és egy csatornát.\n\n" +
                "A rang azért szükséges, hogy csak olyan személyek tudjanak kvízeket indítani, akiknek joguk van hozzá. Azok, akiken nincs ez a rang, nem lesznek képesek kvízeket indítani, " +
                "így a szerver nem lesz elárasztva kvízzekkel\n\n" +
                "A kvízcsatorna azért szükséges, hogy elkülönítve legyenek a kvízek, így senkit sem fog zavarni, plussz mindenki láthatja, hogy éppen milyen kvíz is aktív.\n\n" +
                "**A rang beállítása:**\nA rang beállításához használd a `/role` parancsot, majd a megjelent menüben válaszd ki azt a rangot, amit beszeretél állítani.\n\n" +
                "**A csatorna beállítása:**\nHogy beállítsd a kvízes csatornát, használd a `/channel` parancsot, majd a megjelent menüben válaszd ki azt a szöveges csatornát, ahová majd az aktív kvízek kerülni fognak.\n**Ha a csatorna privát, add hozzá a botot!**\n\n" +
                "**Ezek után:**\nMiután ez a két dolog beállításra került, akkor a szerveren használható lesz a bot a `/quiz` paranccsal.\n\n" +
                "**Fontos:**\nNe felejtsd el hozzárendelni a rangot azokhoz a tagokhoz, akiknek engedélyezni szeretnéd a kvízek indítását a szerveren. Ők bármikor indíthatnak, létrehozhatnak kvízeket, de nem tudják módosítani a beállított rangot és csatornát.\n\n**Ha te is szeretnél kvízeket indítani, akkor rendeld magadhoz a rangot!**");

        HU.put("BOT_HELP_QUIZZES", "A kvízek kezelése, szerkesztése egyszerű.\n\n" +
                "**Első lépés:**\nElsőnek használd a `/quiz` parancsot, hogy elérd a szerver kvíz menüjét, majd kattins az Új kvíz gombra!\n" +
                "Ekkor megjelenik a kvíz szerkesztő felület.\n\n" +
                "**A kvíz szerkesztése:**\nA kvíz szerkesztése gombbal tudod szerkeszteni a kvíz címét, a kérdést, illetve a válasz leírását, hogy mi a helyes válasz, illetve miért.\n" +
                "Ha a kvíz ismertetését üresen hagyod, akkor az eltávolításra kerül a kvízből. A cím és a kérdés nem kerül eltávolításra, szóval üresen hagyhatod, ha nem szeretnéd ezeket szerkeszteni.\n" +
                "\n**A kvíz kép lecserélése:**\nA kép lecserélése gombbal tudsz képet beállítani a kvíznek. Ha a kép nem jelenik meg, az azért van, mert egy olyan hivatkozás kell, ami egy képre mutat. \nPl: https://valami.hu/cica.png. \nHa ezt üresen hagyod, akkor a kép eltávolításra kerül.\n" +
                "\n**Az idő beállítása:**\nA perc gombbal tudsz beállítani a kvíznek érvényesség időt. Szóval ha beállítod 5 percre, és amint eltelik 5 perc, akkor a kvíz inaktív lesz, és senki se tud válaszolni a kvízre, és a helyes válasz fel lesz fedve. Ha azt szeretnéd, hogy sohase járjon le, akkor állítsd be 0 percre!\n" +
                "\n**A válaszok szerkesztése:**\nEzek alatt megtalálhatóak a válasz gombok. Kattins az egyik gombra, majd a megjelent menüben írd be a választ, és az alatta lévő szöveges sávba add meg, hogy ez a válasz helyes-e (Helyes a válasz? I/N). Ha üresen hagyod, akkor nem kerül a válasz felülírásra. Mindegyik választ szerkesztheted.\n" +
                "\n**Kvíz törlése:**\nA törlés gombbal tudod törölni a kvízt a szerverről.\n\n" +
                "Ha mindennel végeztél, akkor nyomj a vissza nyílra, és kattints az Indítás gombra, és a kvíz elküldésre kerül a kvízes csatornába!\n" +
                "A kvízeket bármikor megszakíthatod, ekkor a válaszok nem lesznek felfedve");
        //endregion

        languageMap.put("EN", EN);
        languageMap.put("HU", HU);
    }

    public static String getMessage(String language, String key) {
        Map<String, String> languageMessages = languageMap.get(language);
        if (languageMessages != null) {
            String message = languageMessages.get(key);
            if (message != null) {
                return message;
            }
        }

        return null;
    }
}
