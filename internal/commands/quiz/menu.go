package quiz

import (
	"fmt"

	"github.com/bwmarrin/discordgo"
	"github.com/bxn4/QuickQuiz/internal/utils"
)

func HandleQuizMenu(s *discordgo.Session, i *discordgo.InteractionCreate) {
	description := ""
	var embed *discordgo.MessageEmbed
	var components []discordgo.MessageComponent
	languageCode := "en"

	guildID := i.GuildID
	if guildID != "" {
		username := "bence:|NXM"
		level := 10
		levelProgress := 41.5
		levelNeededXP := 100
		correctAnswers := 40
		streak := 7
		bestStreak := 14

		titleStr := fmt.Sprintf(utils.GetTranslation(languageCode, utils.Translations.MENU_STATS), username)
		levelStr := fmt.Sprintf(utils.GetTranslation(languageCode, utils.Translations.MENU_LEVEL), level, int(levelProgress))
		progressStr := fmt.Sprintf(utils.GetTranslation(languageCode, utils.Translations.MENU_LEVELUP), levelNeededXP, level+1)
		correctAnswersStr := fmt.Sprintf(utils.GetTranslation(languageCode, utils.Translations.MENU_CORRECT_ANSWERS), correctAnswers)
		streakStr := fmt.Sprintf(utils.GetTranslation(languageCode, utils.Translations.MENU_STREAK), streak)
		bestStreakStr := fmt.Sprintf(utils.GetTranslation(languageCode, utils.Translations.MENU_BEST_STREAK), bestStreak)

		description = fmt.Sprintf(
			"%s %s\n\n %s %s\n %s %s\n\n %s %s\n %s %s\n %s %s",
			utils.Emojis.STATS, titleStr,
			utils.Emojis.LEVEL, levelStr,
			utils.Emojis.PROGRESS, progressStr,
			utils.Emojis.CORRECT_ANSWERS, correctAnswersStr,
			utils.Emojis.STREAK, streakStr,
			utils.Emojis.BEST_STREAK, bestStreakStr,
		)

		embed = &discordgo.MessageEmbed{
			Title:       utils.Titles.Default,
			Description: description,
			Color:       utils.Colors.PrimaryColor,
		}

		components = []discordgo.MessageComponent{
			discordgo.ActionsRow{
				Components: []discordgo.MessageComponent{
					discordgo.Button{
						Label:    fmt.Sprintf("%s %s", utils.Emojis.QUIZ_NEW, utils.GetTranslation(languageCode, utils.Translations.QUIZ_NEW)),
						Style:    discordgo.SecondaryButton,
						CustomID: utils.IDs.QUIZ_NEW,
					},

					discordgo.Button{
						Label:    fmt.Sprintf("%s %s", utils.Emojis.QUIZ_LIST, utils.GetTranslation(languageCode, utils.Translations.QUIZ_LIST)),
						Style:    discordgo.SecondaryButton,
						CustomID: utils.IDs.QUIZ_LIST,
					},
				},
			},
			discordgo.ActionsRow{
				Components: []discordgo.MessageComponent{
					discordgo.Button{
						Label:    fmt.Sprintf("%s %s", utils.Emojis.QUIZ_PACKS, utils.GetTranslation(languageCode, utils.Translations.QUIZ_PACKS)),
						Style:    discordgo.SecondaryButton,
						CustomID: utils.IDs.QUIZ_PACKS,
					},
					discordgo.Button{
						Label:    fmt.Sprintf("%s %s", utils.Emojis.QUIZ_TOPLIST, utils.GetTranslation(languageCode, utils.Translations.QUIZ_TOPLIST)),
						Style:    discordgo.SecondaryButton,
						CustomID: utils.Emojis.QUIZ_TOPLIST,
					},
				},
			},
			discordgo.ActionsRow{
				Components: []discordgo.MessageComponent{
					discordgo.Button{
						Label:    fmt.Sprintf("%s %s", utils.Emojis.QUIZ_SPONSOR, utils.GetTranslation(languageCode, utils.Translations.QUIZ_SPONSOR)),
						Style:    discordgo.PrimaryButton,
						CustomID: utils.IDs.QUIZ_SPONSOR,
					},
					discordgo.Button{
						Label:    fmt.Sprintf("%s %s", utils.Emojis.OWNER_SETTINGS, utils.GetTranslation(languageCode, utils.Translations.BOT_SETTINGS)),
						Style:    discordgo.SecondaryButton,
						CustomID: utils.IDs.BOT_SETTINGS,
					},
				},
			},
		}
	} else {
		description = "asd"

		embed = &discordgo.MessageEmbed{
			Title:       utils.Titles.Default,
			Description: description,
			Color:       utils.Colors.PrimaryColor,
		}

		components = []discordgo.MessageComponent{
			discordgo.ActionsRow{
				Components: []discordgo.MessageComponent{
					discordgo.Button{
						Label:    fmt.Sprintf("%s %s", utils.Emojis.QUIZ_NEW, utils.GetTranslation(languageCode, utils.Translations.QUIZ_NEW)),
						Style:    discordgo.SecondaryButton,
						CustomID: utils.IDs.QUIZ_NEW,
					},

					discordgo.Button{
						Label:    fmt.Sprintf("%s %s", utils.Emojis.QUIZ_LIST, utils.GetTranslation(languageCode, utils.Translations.QUIZ_LIST)),
						Style:    discordgo.SecondaryButton,
						CustomID: utils.IDs.QUIZ_LIST,
					},
				},
			},
			discordgo.ActionsRow{
				Components: []discordgo.MessageComponent{
					discordgo.Button{
						Label:    fmt.Sprintf("%s %s", utils.Emojis.QUIZ_PACKS, utils.GetTranslation(languageCode, utils.Translations.QUIZ_PACKS)),
						Style:    discordgo.SecondaryButton,
						CustomID: utils.IDs.QUIZ_PACKS,
					},
					discordgo.Button{
						Label:    fmt.Sprintf("%s %s", utils.Emojis.VIEW_MY_STATS, utils.GetTranslation(languageCode, "VIEW_MY_STATS")),
						Style:    discordgo.SecondaryButton,
						CustomID: utils.IDs.VIEW_MY_STATS,
					},
				},
			},
			discordgo.ActionsRow{
				Components: []discordgo.MessageComponent{
					discordgo.Button{
						Label:    fmt.Sprintf("%s %s", utils.Emojis.QUIZ_SPONSOR, utils.GetTranslation(languageCode, utils.Translations.QUIZ_SPONSOR)),
						Style:    discordgo.PrimaryButton,
						CustomID: utils.IDs.QUIZ_SPONSOR,
					},
					discordgo.Button{
						Label:    fmt.Sprintf("%s %s", utils.Emojis.OWNER_SETTINGS, utils.GetTranslation(languageCode, utils.Translations.BOT_SETTINGS)),
						Style:    discordgo.SecondaryButton,
						CustomID: utils.IDs.BOT_SETTINGS,
					},
				},
			},
		}
	}

	err := s.InteractionRespond(i.Interaction, &discordgo.InteractionResponse{
		Type: discordgo.InteractionResponseChannelMessageWithSource,
		Data: &discordgo.InteractionResponseData{
			Embeds:     []*discordgo.MessageEmbed{embed},
			Components: components,
			Flags:      discordgo.MessageFlagsEphemeral,
		},
	})

	if err != nil {
		fmt.Println(err)
	}
}
