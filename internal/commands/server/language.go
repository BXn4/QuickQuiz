package server

import (
	"log"
	"strings"

	"github.com/bwmarrin/discordgo"
	"github.com/bxn4/QuickQuiz/internal/modules/database"
	"github.com/bxn4/QuickQuiz/internal/utils"
)

func HandlesServerChangeLanguage(s *discordgo.Session, i *discordgo.InteractionCreate) {
	userID := utils.GetUserID(i)

	language := utils.Configs.Language
	guildLocale := string(*i.GuildLocale)[0:2]

	if utils.CheckIfTranslationExist(guildLocale) {
		language = guildLocale
	}

	if database.CheckIfUserRegistered(userID) {
		language = database.GetUserLanguage(userID)
	}

	err := s.InteractionRespond(i.Interaction, &discordgo.InteractionResponse{
		Type: discordgo.InteractionResponseModal,
		Data: &discordgo.InteractionResponseData{
			CustomID: utils.IDs.MODAL_CHANGE_SERVER_LANGUAGE,
			Title:    utils.GetTranslation(language, utils.Translations.CHANGE_LANGUAGE),
			Components: []discordgo.MessageComponent{
				discordgo.ActionsRow{
					Components: []discordgo.MessageComponent{
						discordgo.TextInput{
							CustomID:    utils.IDs.CHANGE_SERVER_LANGUAGE,
							Label:       utils.GetTranslation(language, utils.Translations.CHANGE_LANGUAGE_LABEL),
							Style:       discordgo.TextInputShort,
							Placeholder: utils.GetTranslation(language, utils.Translations.CHANGE_LANGUAGE_PLACEHOLDER),
							Required:    true,
						},
					},
				},
			},
		},
	})
	if err != nil {
		log.Fatal(err)
	}
}

func ChangeServerLanguage(s *discordgo.Session, i *discordgo.InteractionCreate) {
	modalData := i.ModalSubmitData()
	if len(modalData.Components) == 0 {
		return
	}

	// userID := utils.GetUserID(i)

	guildIDSTR := i.GuildID

	guild, err := s.Guild(guildIDSTR)

	if err != nil {
		log.Fatal(err)
	}

	guildID := utils.ToUint64(guild.ID)

	language := utils.Configs.Language
	guildLocale := string(*i.GuildLocale)[0:2]

	if utils.CheckIfTranslationExist(guildLocale) {
		language = guildLocale
	}

	if database.CheckIfServerRegistered(guildID) {
		language = database.GetServerLanguage(guildID)
	}

	if !utils.CheckIfTranslationExist(language) {
		// UPDATES TO DEFAULT, FALLBACK!!
		language = utils.Configs.Language
		database.UpdateServerLanguage(guildID, language)
	}

	/* if database.CheckIfUserRegistered(userID) {
	language = database.GetUserLanguage(userID)
	} */

	var newLanguage string

	for _, component := range modalData.Components {
		if actionRow, ok := component.(*discordgo.ActionsRow); ok {
			for _, innerComponent := range actionRow.Components {
				if textInput, ok := innerComponent.(*discordgo.TextInput); ok {
					if len(textInput.Value) < 2 ||
						strings.ToLower(textInput.Value) == language ||
						!utils.CheckIfTranslationExist(strings.ToLower(textInput.Value)[0:2]) {
						errorEmbed := &discordgo.MessageEmbed{
							Title:       utils.GetTranslation(language, utils.Titles.Error),
							Description: utils.GetTranslation(language, utils.Translations.LANGUAGE_NOT_SUPPORTED),
							Color:       utils.Colors.WarningColor,
						}

						s.InteractionRespond(i.Interaction, &discordgo.InteractionResponse{
							Type: discordgo.InteractionResponseChannelMessageWithSource,
							Data: &discordgo.InteractionResponseData{
								Embeds: []*discordgo.MessageEmbed{errorEmbed},
								Flags:  discordgo.MessageFlagsEphemeral,
							},
						})

						return
					}

					newLanguage = strings.ToLower(textInput.Value)[0:2]
				}
				break
			}
		}
	}

	database.UpdateServerLanguage(guildID, newLanguage)

	s.InteractionRespond(i.Interaction, &discordgo.InteractionResponse{
		Type: discordgo.InteractionResponseChannelMessageWithSource,
		Data: &discordgo.InteractionResponseData{
			Content: utils.GetTranslation(newLanguage, utils.Translations.SERVER_LANGUAGE_CHANGED),
		},
	})
}
