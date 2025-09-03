package commands

import (
	"github.com/bwmarrin/discordgo"
	"github.com/bxn4/QuickQuiz/internal/commands/server"
	quiz "github.com/bxn4/QuickQuiz/internal/commands/slash"
	"github.com/bxn4/QuickQuiz/internal/utils"
)

func HandleCommands(s *discordgo.Session, i *discordgo.InteractionCreate) {
	switch i.Type {
	case discordgo.InteractionApplicationCommand:
		handleSlashCommand(s, i)
	case discordgo.InteractionMessageComponent:
		handleButtonPress(s, i)

	case discordgo.InteractionModalSubmit:
		handleModals(s, i)
	}
	if i.Type != discordgo.InteractionApplicationCommand {
		return
	}

}

func handleSlashCommand(s *discordgo.Session, i *discordgo.InteractionCreate) {
	switch i.ApplicationCommandData().Name {
	case utils.Commands.QUIZ:
		quiz.HandleQuizCommand(s, i)
	}
}

func handleButtonPress(s *discordgo.Session, i *discordgo.InteractionCreate) {
	switch i.MessageComponentData().CustomID {
	case utils.IDs.BUTTON_CHANGE_SERVER_LANGUAGE:
		server.HandlesServerChangeLanguage(s, i)
	}
}

func handleModals(s *discordgo.Session, i *discordgo.InteractionCreate) {
	switch i.ModalSubmitData().CustomID {
	case utils.IDs.MODAL_CHANGE_SERVER_LANGUAGE:
		server.ChangeServerLanguage(s, i)
	}
}
