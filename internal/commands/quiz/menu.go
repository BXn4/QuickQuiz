package quiz

import (
	"fmt"

	"github.com/bwmarrin/discordgo"
	"github.com/bxn4/QuickQuiz/internal/commands/utils"
)

func HandleQuizMenu(s *discordgo.Session, i *discordgo.InteractionCreate) {
	languageCode := "en"
	username := "bence:|NXM"
	coins := 100
	level := 10
	levelProgress := 41.5
	levelNeededXP := 100
	correctAnswers := 40
	streak := 7
	bestStreak := 14

	titleStr := fmt.Sprintf(utils.GetTranslation(languageCode, "MENU_STATS"), username)
	coinsStr := fmt.Sprintf(utils.GetTranslation(languageCode, "MENU_COINS"), coins)
	levelStr := fmt.Sprintf(utils.GetTranslation(languageCode, "MENU_LEVEL"), level, int(levelProgress))
	progressStr := fmt.Sprintf(utils.GetTranslation(languageCode, "MENU_LEVELUP"), levelNeededXP, level+1)
	correctAnswersStr := fmt.Sprintf(utils.GetTranslation(languageCode, "MENU_CORRECT_ANSWERS"), correctAnswers)
	streakStr := fmt.Sprintf(utils.GetTranslation(languageCode, "MENU_STREAK"), streak)
	bestStreakStr := fmt.Sprintf(utils.GetTranslation(languageCode, "MENU_BEST_STREAK"), bestStreak)

	description := fmt.Sprintf(
		"%s %s\n\n %s %s\n %s %s\n %s %s\n\n %s %s\n %s %s\n %s %s",
		utils.Emojis.STATS, titleStr,
		utils.Emojis.COINS, coinsStr,
		utils.Emojis.LEVEL, levelStr,
		utils.Emojis.PROGRESS, progressStr,
		utils.Emojis.CORRECT_ANSWERS, correctAnswersStr,
		utils.Emojis.STREAK, streakStr,
		utils.Emojis.BEST_STREAK, bestStreakStr,
	)

	menu := &discordgo.MessageEmbed{
		Title:       "QuickQuiz",
		Description: description,
		Color:       utils.Colors.PrimaryColor,
	}

	buttons := []discordgo.MessageComponent{
		discordgo.ActionsRow{
			Components: []discordgo.MessageComponent{
				discordgo.Button{
					Label:    fmt.Sprintf("%s %s", utils.Emojis.QUIZ_NEW, utils.GetTranslation(languageCode, "QUIZ_NEW")),
					Style:    discordgo.SecondaryButton,
					CustomID: "quiz_new",
				},

				discordgo.Button{
					Label:    fmt.Sprintf("%s %s", utils.Emojis.QUIZ_LIST, utils.GetTranslation(languageCode, "QUIZ_LIST")),
					Style:    discordgo.SecondaryButton,
					CustomID: "quiz_list",
				},
			},
		},
		discordgo.ActionsRow{
			Components: []discordgo.MessageComponent{
				discordgo.Button{
					Label:    fmt.Sprintf("%s %s", utils.Emojis.QUIZ_PACKS, utils.GetTranslation(languageCode, "QUIZ_PACKS")),
					Style:    discordgo.SecondaryButton,
					CustomID: "quiz_packs",
				},
				discordgo.Button{
					Label:    fmt.Sprintf("%s %s", utils.Emojis.QUIZ_TOPLIST, utils.GetTranslation(languageCode, "QUIZ_TOPLIST")),
					Style:    discordgo.SecondaryButton,
					CustomID: "quiz_toplist",
				},
			},
		},
		discordgo.ActionsRow{
			Components: []discordgo.MessageComponent{

				discordgo.Button{
					Label:    fmt.Sprintf("%s %s", utils.Emojis.QUIZ_SHOP, utils.GetTranslation(languageCode, "QUIZ_SHOP")),
					Style:    discordgo.SecondaryButton,
					CustomID: "quiz_shop",
				},
				discordgo.Button{
					Label:    fmt.Sprintf("%s %s", utils.Emojis.QUIZ_SPONSOR, utils.GetTranslation(languageCode, "QUIZ_SPONSOR")),
					Style:    discordgo.PrimaryButton,
					CustomID: "quiz_sponsor",
				},
			},
		},
	}

	err := s.InteractionRespond(i.Interaction, &discordgo.InteractionResponse{
		Type: discordgo.InteractionResponseChannelMessageWithSource,
		Data: &discordgo.InteractionResponseData{
			Embeds:     []*discordgo.MessageEmbed{menu},
			Components: buttons,
			Flags:      discordgo.MessageFlagsEphemeral,
		},
	})

	if err != nil {
		fmt.Println(err)
	}
}
