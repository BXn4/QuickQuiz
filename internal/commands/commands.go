package commands

import (
	"github.com/bwmarrin/discordgo"
	"github.com/bxn4/QuickQuiz/internal/commands/quiz"
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

	switch i.ApplicationCommandData().Name {
	case "quiz":
		quiz.HandleQuizMenu(s, i)
	}
}
