package utils

import (
	"log"
	"os"
	"strconv"

	"github.com/bwmarrin/discordgo"
	"github.com/joho/godotenv"
)

func init() {
	err := godotenv.Load(".env")
	if err != nil {
		log.Fatal(err)
	}

	Configs = Config{
		Language: getFromEnv("DEFAULT_LANGUAGE"),
	}
}

type Command struct {
	QUIZ string
}

type Config struct {
	Language string
}

type Title struct {
	Default string
	Setup_1 string
	Setup_2 string
	Setup_3 string
	Setup_4 string
	Setup_5 string
	Setup_6 string

	Error string
}

type Color struct {
	PrimaryColor   int
	SecondaryColor int
	SuccessColor   int
	ErrorColor     int
	InfoColor      int
	WarningColor   int
}

type Emoji struct {
	STATS           string
	COINS           string
	LEVEL           string
	PROGRESS        string
	XP              string
	CORRECT_ANSWERS string
	STREAK          string
	BEST_STREAK     string

	QUIZ_NEW        string
	QUIZ_LIST       string
	QUIZ_TOPLIST    string
	QUIZ_PACKS      string
	QUIZ_SHOP       string
	QUIZ_PREMIUM    string
	OWNER_SETTINGS  string
	VIEW_MY_STATS   string
	GO_BACK         string
	GO_NEXT         string
	CHANGE_LANGUAGE string
}

type ID struct {
	QUIZ_NEW        string
	QUIZ_LIST       string
	QUIZ_PACKS      string
	QUIZ_TOPLIST    string
	QUIZ_PREMIUM    string
	BOT_SETTINGS    string
	VIEW_MY_STATS   string
	GO_BACK_SETUP_0 string
	GO_NEXT_SETUP_1 string

	BUTTON_CHANGE_SERVER_LANGUAGE string

	MODAL_CHANGE_SERVER_LANGUAGE string
	CHANGE_SERVER_LANGUAGE       string
}

var Colors = Color{
	PrimaryColor:   0x4d7e82,
	SecondaryColor: 0xff5733,
	SuccessColor:   0x28a745,
	ErrorColor:     0xdc3545,
	InfoColor:      0x17a2b8,
	WarningColor:   0xffc107,
}

var Emojis = Emoji{
	STATS:           ":star2:",
	COINS:           ":coin:",
	LEVEL:           ":level_slider:",
	PROGRESS:        ":bar_chart: ",
	XP:              ":test_tube:",
	CORRECT_ANSWERS: ":trophy:",
	STREAK:          ":fire:",
	BEST_STREAK:     ":medal:",

	QUIZ_NEW:        "🖊️",
	QUIZ_LIST:       "🗒️",
	QUIZ_TOPLIST:    "🏆",
	QUIZ_PACKS:      "🃏",
	QUIZ_SHOP:       "🛒",
	QUIZ_PREMIUM:    "❤️",
	OWNER_SETTINGS:  "⚙️",
	VIEW_MY_STATS:   "📈",
	GO_BACK:         "⬅️",
	GO_NEXT:         "➡️",
	CHANGE_LANGUAGE: "🌍",
}

var Titles = Title{
	Default: "QuickQuiz",
	Setup_1: "WELCOME_TITLE_STEP_1",
	Setup_2: "WELCOME_TITLE_STEP_2",
	Setup_3: "WELCOME_TITLE_STEP_3",
	Setup_4: "WELCOME_TITLE_STEP_4",
	Setup_5: "WELCOME_TITLE_STEP_5",
	Setup_6: "WELCOME_TITLE_STEP_6",

	Error: "ERROR_TITLE",
}

var IDs = ID{
	QUIZ_NEW:      "QUIZ_NEW",
	QUIZ_LIST:     "QUIZ_LIST",
	QUIZ_TOPLIST:  "QUIZ_TOPLIST",
	QUIZ_PACKS:    "QUIZ_PACKS",
	QUIZ_PREMIUM:  "QUIZ_PREMIUM",
	BOT_SETTINGS:  "BOT_SETTINGS",
	VIEW_MY_STATS: "VIEW_MY_STATS",

	GO_BACK_SETUP_0:               "GO_BACK_SETUP_0",
	GO_NEXT_SETUP_1:               "GO_NEXT_SETUP_1",
	BUTTON_CHANGE_SERVER_LANGUAGE: "CHANGE_SERVER_ANGUAGE",

	MODAL_CHANGE_SERVER_LANGUAGE: "MODAL_CHANGE_SERVER_LANGUAGE",
	CHANGE_SERVER_LANGUAGE:       "CHANGE_SERVER_LANGUAGE",
}

var Commands = Command{
	QUIZ: "quiz",
}

var Configs Config

func CheckIfAdmin(userID uint64, ownerID uint64, roles []*discordgo.Role) bool {
	if userID == ownerID {
		return true
	}

	for _, role := range roles {
		if role.Permissions&discordgo.PermissionAdministrator != 0 {
			return true
		}
	}

	return false
}

func GetRolesFromIDs(s *discordgo.Session, guildID string, roleIDs []string) ([]*discordgo.Role, error) {
	guild, err := s.State.Guild(guildID)
	if err != nil {
		log.Fatal(err)
	}

	roleMap := make(map[string]*discordgo.Role)
	for _, role := range guild.Roles {
		roleMap[role.ID] = role
	}

	var roles []*discordgo.Role
	for _, roleID := range roleIDs {
		if role, exists := roleMap[roleID]; exists {
			roles = append(roles, role)
		} else {
			log.Fatal(err)
		}
	}

	return roles, nil
}

func ToUint64(s string) uint64 {
	uid, err := strconv.ParseUint(s, 10, 64)
	if err != nil {
		log.Fatal(err)
	}

	return uid
}

func getFromEnv(key string) string {
	value := os.Getenv(key)
	return value
}

func GetUserID(i *discordgo.InteractionCreate) uint64 {
	if i.User != nil {
		return ToUint64(i.User.ID)
	}
	return ToUint64(i.Member.User.ID)
}
