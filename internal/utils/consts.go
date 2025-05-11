package utils

import (
	"log"
	"strconv"

	"github.com/bwmarrin/discordgo"
)

type Title struct {
	Default string
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

	QUIZ_NEW       string
	QUIZ_LIST      string
	QUIZ_TOPLIST   string
	QUIZ_PACKS     string
	QUIZ_SHOP      string
	QUIZ_SPONSOR   string
	OWNER_SETTINGS string
	VIEW_MY_STATS  string
}

type ID struct {
	QUIZ_NEW      string
	QUIZ_LIST     string
	QUIZ_PACKS    string
	QUIZ_TOPLIST  string
	QUIZ_SPONSOR  string
	BOT_SETTINGS  string
	VIEW_MY_STATS string
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

	QUIZ_NEW:       "🖊️",
	QUIZ_LIST:      "🗒️",
	QUIZ_TOPLIST:   "🏆",
	QUIZ_PACKS:     "🃏",
	QUIZ_SHOP:      "🛒",
	QUIZ_SPONSOR:   "❤️",
	OWNER_SETTINGS: "⚙️",
	VIEW_MY_STATS:  "📈",
}

var Titles = Title{
	Default: "QuickQuiz",
}

var IDs = ID{
	QUIZ_NEW:      "QUIZ_NEW",
	QUIZ_LIST:     "QUIZ_LIST",
	QUIZ_TOPLIST:  "QUIZ_TOPLIST",
	QUIZ_PACKS:    "QUIZ_PACKS",
	QUIZ_SPONSOR:  "QUIZ_SPONSOR",
	BOT_SETTINGS:  "BOT_SETTINGS",
	VIEW_MY_STATS: "VIEW_MY_STATS",
}

var registeredServers = make(map[uint64]bool)

func CheckIfServerRegistered(serverID uint64) bool {
	if registeredServers[serverID] {
		return true
	} else {
		return false
	}

}

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
