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
		return messageID
	}

	po.ParseFile(filePath)

	return po.Get(messageID)
}

type Translation struct {
	MENU_STATS           string
	MENU_LEVEL           string
	MENU_LEVELUP         string
	MENU_CORRECT_ANSWERS string
	MENU_STREAK          string
	MENU_BEST_STREAK     string

	QUIZ_NEW     string
	QUIZ_LIST    string
	QUIZ_PACKS   string
	QUIZ_TOPLIST string
	QUIZ_SPONSOR string
	BOT_SETTINGS string

	QUICK_QUIZ_NOT_SET string
}

var Translations = Translation{
	MENU_STATS:           "MENU_STATS",
	MENU_LEVEL:           "MENU_LEVEL",
	MENU_LEVELUP:         "MENU_LEVELUP",
	MENU_CORRECT_ANSWERS: "MENU_CORRECT_ANSWERS",
	MENU_STREAK:          "MENU_STREAK",
	MENU_BEST_STREAK:     "MENU_BEST_STREAK",

	QUIZ_NEW:     "QUIZ_NEW",
	QUIZ_LIST:    "QUIZ_LIST",
	QUIZ_PACKS:   "QUIZ_PACKS",
	QUIZ_TOPLIST: "QUIZ_TOPLIST",
	QUIZ_SPONSOR: "QUIZ_SPONSOR",
	BOT_SETTINGS: "BOT_SETTINGS",

	QUICK_QUIZ_NOT_SET: "QUICK_QUIZ_NOT_SET",
}
