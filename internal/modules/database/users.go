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

func RegisterUser(userID uint64) {
	_, err := DB.Exec("INSERT INTO users (id) VALUES ($1)", userID)
	if err != nil {
		log.Fatal(err)
	}
}
