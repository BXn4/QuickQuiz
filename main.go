package main

import (
	"fmt"
	"log"
	"os"
	"os/signal"
	"syscall"

	"github.com/bxn4/QuickQuiz/internal/commands"

	"github.com/bwmarrin/discordgo"
	"github.com/joho/godotenv"
)

func main() {
	env, err := godotenv.Read(".env")
	if err != nil {
		log.Fatal("Cannot find .env file!")
	}

	token := env["TOKEN"]

	s, err := discordgo.New("Bot " + token)
	if err != nil {
		log.Fatal(err)
	}

	s.AddHandler(commands.HandleCommands)

	err = s.Open()
	if err != nil {
		log.Fatal(err)
	}
	defer s.Close()

	fmt.Printf(`
Watching %d servers.
Stats:
- Registered Users:   %d
- Total Quizzes:      %d
- Active Quizzes:     %d

`,
		len(s.State.Guilds),
		0, // Registered Users
		0, // Total Quizzes
		0, // Active Quizzes
	)

	sc := make(chan os.Signal, 1)
	signal.Notify(sc, syscall.SIGINT, syscall.SIGTERM, os.Interrupt)
	<-sc

	fmt.Println("Saving...")
}
