package commands

import (
	"log"

	"github.com/bwmarrin/discordgo"
	"github.com/bxn4/QuickQuiz/internal/commands/quiz"
	"github.com/bxn4/QuickQuiz/internal/modules/database"
	"github.com/bxn4/QuickQuiz/internal/utils"
)

var (
	Commands = []*discordgo.ApplicationCommand{
		{
			Name:        "quiz",
			Description: "Show the Quiz menu",
		},
	}
)

func HandleCommands(s *discordgo.Session, i *discordgo.InteractionCreate) {
	if i.Type != discordgo.InteractionApplicationCommand {
		return
	}

	var userID uint64

	if i.User != nil {
		userID = utils.ToUint64(i.User.ID)
	} else {
		userID = utils.ToUint64(i.Member.User.ID)
	}

	isRegistered := database.CheckIfUserRegistered(userID)
	if !isRegistered {
		database.RegisterUser(userID)
	}

	guildID := i.GuildID
	if guildID != "" {
		guild, err := s.Guild(guildID)

		roles, err := utils.GetRolesFromIDs(s, guild.ID, i.Member.Roles)

		if err != nil {
			log.Fatal(err)
		}

		isRegistered := utils.CheckIfServerRegistered(utils.ToUint64(i.GuildID))
		isAdmin := utils.CheckIfAdmin(utils.ToUint64(i.Member.User.ID), utils.ToUint64(guild.OwnerID), roles)
		if !isRegistered && !isAdmin {
			embed := &discordgo.MessageEmbed{
				Title:       utils.Titles.Default,
				Description: utils.GetTranslation(string(*i.GuildLocale)[0:2], utils.Translations.QUICK_QUIZ_NOT_SET),
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
			log.Fatal("ADMIN ADMIN ADMIN COMMANDS.GO 72")
		}
	}

	switch i.ApplicationCommandData().Name {
	case "quiz":
		quiz.HandleQuizMenu(s, i)
	}
}
