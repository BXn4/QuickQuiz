package quiz

import (
	"fmt"

	"github.com/bwmarrin/discordgo"
	"github.com/bxn4/QuickQuiz/internal/commands/utils"
)

func HandleQuizMenu(s *discordgo.Session, i *discordgo.InteractionCreate) {
	languageCode := "en"
	level := 10
	levelProgress := 41.5
	levelNeededXP := 100
	correctAnswers := 40
	streak := 7
	bestStreak := 14

	titleStr := utils.GetTranslation(languageCode, "MENU_STATS")
	levelStr := fmt.Sprintf(utils.GetTranslation(languageCode, "MENU_LEVEL"), level, int(levelProgress))
	progressStr := fmt.Sprintf(utils.GetTranslation(languageCode, "MENU_LEVELUP"), levelNeededXP, level+1)
	correctAnswersStr := fmt.Sprintf(utils.GetTranslation(languageCode, "MENU_CORRECT_ANSWERS"), correctAnswers)
	streakStr := fmt.Sprintf(utils.GetTranslation(languageCode, "MENU_STREAK"), streak)
	bestStreakStr := fmt.Sprintf(utils.GetTranslation(languageCode, "MENU_BEST_STREAK"), bestStreak)

	description := fmt.Sprintf(
		"%s %s\n\n %s %s\n %s %s\n\n %s %s\n %s %s\n %s %s",
		utils.Emojis.STATS, titleStr,
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

	err := s.InteractionRespond(i.Interaction, &discordgo.InteractionResponse{
		Type: discordgo.InteractionResponseChannelMessageWithSource,
		Data: &discordgo.InteractionResponseData{
			Embeds: []*discordgo.MessageEmbed{menu},
			Flags:  discordgo.MessageFlagsEphemeral,
		},
	})

	if err != nil {
		fmt.Println(err)
	}
}
