package utils

import (
	"fmt"
	"os"

	"github.com/leonelquinteros/gotext"
)

func GetTranslation(languageCode, messageID string) string {
	po := gotext.NewPo()

	filePath := fmt.Sprintf("internal/data/locales/%s.po", languageCode)

	if _, err := os.Stat(filePath); err != nil {
		if os.IsNotExist(err) {
			// FALLBACK!!
			languageCode = Configs.Language
			filePath = fmt.Sprintf("internal/data/locales/%s.po", languageCode)
		} else {
			// FALLBACK!!
			return messageID
		}
	}

	po.ParseFile(filePath)

	return po.Get(messageID)
}

func CheckIfTranslationExist(languageCode string) bool {
	filePath := fmt.Sprintf("internal/data/locales/%s.po", languageCode)

	if _, err := os.Stat(filePath); err != nil {
		return false
	}

	return true
}

type Translation struct {
	MENU_STATS           string
	MENU_LEVEL           string
	MENU_LEVELUP         string
	MENU_CORRECT_ANSWERS string
	MENU_STREAK          string
	MENU_BEST_STREAK     string

	QUIZ_NEW        string
	QUIZ_LIST       string
	QUIZ_PACKS      string
	QUIZ_TOPLIST    string
	QUIZ_PREMIUM    string
	BOT_SETTINGS    string
	CHANGE_LANGUAGE string

	QUICK_QUIZ_NOT_SET     string
	WELCOME_USE_HELP       string
	WELCOME_TITLE_STEP_1   string
	WELCOME_TITLE_STEP_2   string
	WELCOME_TITLE_STEP_3   string
	WELCOME_TITLE_STEP_4   string
	WELCOME_TITLE_STEP_5   string
	WELCOME_TITLE_STEP_6   string
	WELCOME_GUIDE_STEP_1   string
	WELCOME_GUIDE_STEP_1_1 string
	WELCOME_GUIDE_STEP_1_2 string
	WELCOME_GUIDE_STEP_2   string
	WELCOME_GUIDE_STEP_2_1 string
	WELCOME_GUIDE_STEP_2_2 string
	WELCOME_GUIDE_STEP_3_1 string
	WELCOME_GUIDE_STEP_3_2 string
	WELCOME_GUIDE_STEP_4_1 string
	WELCOME_GUIDE_STEP_5   string
	EVERYONE_ROLE          string
	LANGUAGE_NOT_SUPPORTED string

	CHANGE_LANGUAGE_LABEL       string
	CHANGE_LANGUAGE_PLACEHOLDER string
	SERVER_LANGUAGE_CHANGED     string
}

var Translations = Translation{
	MENU_STATS:           "MENU_STATS",
	MENU_LEVEL:           "MENU_LEVEL",
	MENU_LEVELUP:         "MENU_LEVELUP",
	MENU_CORRECT_ANSWERS: "MENU_CORRECT_ANSWERS",
	MENU_STREAK:          "MENU_STREAK",
	MENU_BEST_STREAK:     "MENU_BEST_STREAK",

	QUIZ_NEW:        "QUIZ_NEW",
	QUIZ_LIST:       "QUIZ_LIST",
	QUIZ_PACKS:      "QUIZ_PACKS",
	QUIZ_TOPLIST:    "QUIZ_TOPLIST",
	QUIZ_PREMIUM:    "QUIZ_PREMIUM",
	BOT_SETTINGS:    "BOT_SETTINGS",
	CHANGE_LANGUAGE: "CHANGE_LANGUAGE",

	QUICK_QUIZ_NOT_SET:     "QUICK_QUIZ_NOT_SET",
	WELCOME_USE_HELP:       "WELCOME_USE_HELP",
	WELCOME_TITLE_STEP_1:   "WELCOME_TITLE_STEP_1",
	WELCOME_TITLE_STEP_2:   "WELCOME_TITLE_STEP_2",
	WELCOME_TITLE_STEP_3:   "WELCOME_TITLE_STEP_3",
	WELCOME_TITLE_STEP_4:   "WELCOME_TITLE_STEP_4",
	WELCOME_TITLE_STEP_5:   "WELCOME_TITLE_STEP_5",
	WELCOME_TITLE_STEP_6:   "WELCOME_TITLE_STEP_6",
	WELCOME_GUIDE_STEP_1:   "WELCOME_GUIDE_STEP_1",
	WELCOME_GUIDE_STEP_1_1: "WELCOME_GUIDE_STEP_1_1",
	WELCOME_GUIDE_STEP_1_2: "WELCOME_GUIDE_STEP_1_2",
	WELCOME_GUIDE_STEP_2:   "WELCOME_GUIDE_STEP_2",
	WELCOME_GUIDE_STEP_2_1: "WELCOME_GUIDE_STEP_2_1",
	WELCOME_GUIDE_STEP_2_2: "WELCOME_GUIDE_STEP_2_2",
	WELCOME_GUIDE_STEP_3_1: "WELCOME_GUIDE_STEP_3_1",
	WELCOME_GUIDE_STEP_3_2: "WELCOME_GUIDE_STEP_3_2",
	WELCOME_GUIDE_STEP_4_1: "WELCOME_GUIDE_STEP_4_1",
	WELCOME_GUIDE_STEP_5:   "WELCOME_GUIDE_STEP_5",
	EVERYONE_ROLE:          "EVERYONE_ROLE",

	CHANGE_LANGUAGE_LABEL:       "CHANGE_LANGUAGE_LABEL",
	CHANGE_LANGUAGE_PLACEHOLDER: "CHANGE_LANGUAGE_PLACEHOLDER",
	LANGUAGE_NOT_SUPPORTED:      "LANGUAGE_NOT_SUPPORTED",
	SERVER_LANGUAGE_CHANGED:     "SERVER_LANGUAGE_CHANGED",
}
