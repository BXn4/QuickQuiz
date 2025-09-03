package database

import (
	"log"
)

func GetRegisteredUsersCount() int {
	var count int
	err := DB.QueryRow("SELECT COUNT(id) FROM users").Scan(&count)
	if err != nil {
		log.Fatal(err)
		return 0
	}
	return count
}

func CheckIfUserRegistered(userID uint64) bool {
	var id uint64
	err := DB.QueryRow("SELECT id FROM users WHERE id = $1", userID).Scan(&id)
	if err != nil {
		return false
	}

	return true
}

func RegisterUser(userID uint64, language string) {
	_, err := DB.Exec("INSERT INTO users (id, language) VALUES ($1, $2)", userID, language)
	if err != nil {
		log.Fatal(err)
	}
}

func GetUserLanguage(userID uint64) string {
	var language string
	err := DB.QueryRow("SELECT language FROM users WHERE id = $1", userID).Scan(&language)
	if err != nil {
		return "en"
	}
	return language
}
