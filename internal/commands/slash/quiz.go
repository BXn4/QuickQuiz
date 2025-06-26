package quiz

import (
	"fmt"
	"log"

	"github.com/bwmarrin/discordgo"
	"github.com/bxn4/QuickQuiz/internal/commands/quiz"
	"github.com/bxn4/QuickQuiz/internal/modules/database"
	"github.com/bxn4/QuickQuiz/internal/utils"
)

func HandleQuizCommand(s *discordgo.Session, i *discordgo.InteractionCreate) {
	guildIDSTR := i.GuildID
	userID := utils.GetUserID(i)

	if guildIDSTR != "" {
		guild, err := s.Guild(guildIDSTR)

		guildID := utils.ToUint64(guild.ID)

		roles, err := utils.GetRolesFromIDs(s, guild.ID, i.Member.Roles)

		if err != nil {
			log.Fatal(err)
		}

		language := utils.Configs.Language
		guildLocale := string(*i.GuildLocale)[0:2]

		if utils.CheckIfTranslationExist(guildLocale) {
			language = guildLocale
		}

		if database.CheckIfServerRegistered(guildID) {
			language = database.GetServerLanguage(guildID)
		}

		if !utils.CheckIfTranslationExist(language) {
			language = utils.Configs.Language
			database.UpdateServerLanguage(guildID, language)
			// FALLBACK!!
		}

		/* if database.CheckIfUserRegistered(userID) {
		language = database.GetUserLanguage(userID)
		} */

		isServerRegistered := database.CheckIfServerRegistered(guildID)
		isAdmin := utils.CheckIfAdmin(utils.ToUint64(i.Member.User.ID), utils.ToUint64(guild.OwnerID), roles)

		if !isServerRegistered && !isAdmin {
			embed := &discordgo.MessageEmbed{
				Title:       utils.Titles.Default,
				Description: utils.GetTranslation(language, utils.Translations.QUICK_QUIZ_NOT_SET),
				Color:       utils.Colors.ErrorColor,
			}

			err := s.InteractionRespond(i.Interaction, &discordgo.InteractionResponse{
				Type: discordgo.InteractionResponseChannelMessageWithSource,
				Data: &discordgo.InteractionResponseData{
					Embeds: []*discordgo.MessageEmbed{embed},
					Flags:  discordgo.MessageFlagsEphemeral,
				},
			})

			if err != nil {
				log.Fatal(err)
			}

			return
		} else if isAdmin {
			language := utils.Configs.Language
			guildLocale := string(*i.GuildLocale)[0:2]

			if utils.CheckIfTranslationExist(guildLocale) {
				language = guildLocale
			}

			if database.CheckIfServerRegistered(guildID) {
				language = database.GetServerLanguage(guildID)
			}

			/* if database.CheckIfUserRegistered(userID) {
			language = database.GetUserLanguage(userID)
			} */

			embed := &discordgo.MessageEmbed{
				Title: utils.GetTranslation(language, utils.Titles.Setup_1),
				Description: utils.GetTranslation(language, utils.Translations.WELCOME_GUIDE_STEP_1) + "\n\n" +
					fmt.Sprintf(utils.GetTranslation(language, utils.Translations.WELCOME_GUIDE_STEP_1_1), language),
				Color: utils.Colors.InfoColor,
				Footer: &discordgo.MessageEmbedFooter{
					Text: utils.GetTranslation(language, utils.Translations.WELCOME_GUIDE_STEP_1_2)},
			}

			components := []discordgo.MessageComponent{
				discordgo.ActionsRow{
					Components: []discordgo.MessageComponent{
						discordgo.Button{
							Label:    utils.Emojis.GO_BACK,
							Style:    discordgo.SecondaryButton,
							CustomID: utils.IDs.GO_BACK_SETUP_0,
							Disabled: true,
						},
						discordgo.Button{
							Label:    utils.Emojis.GO_NEXT,
							Style:    discordgo.SecondaryButton,
							CustomID: utils.IDs.GO_NEXT_SETUP_1,
						},

						discordgo.Button{
							Label:    fmt.Sprintf("%s %s", utils.Emojis.CHANGE_LANGUAGE, utils.GetTranslation(language, utils.Translations.CHANGE_LANGUAGE)),
							Style:    discordgo.PrimaryButton,
							CustomID: utils.IDs.BUTTON_CHANGE_SERVER_LANGUAGE,
						},
					},
				},
			}

			err = s.InteractionRespond(i.Interaction, &discordgo.InteractionResponse{
				Type: discordgo.InteractionResponseChannelMessageWithSource,
				Data: &discordgo.InteractionResponseData{
					Embeds:     []*discordgo.MessageEmbed{embed},
					Components: components,
					Flags:      discordgo.MessageFlagsEphemeral,
				},
			})

			if err != nil {
				log.Fatal(err)
			}

			if !database.CheckIfServerRegistered(guildID) {
				database.RegisterServer(guildID, language)
			}

			return
		}
	} else {
		if !database.CheckIfUserRegistered(userID) {
			database.RegisterUser(userID, utils.Configs.Language)
		}
	}

	quiz.HandleQuizMenu(s, i)
}
