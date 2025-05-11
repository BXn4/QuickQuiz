package database

import "log"

func CheckIfServerRegistered(serverID int) bool {
	var id int
	err := DB.QueryRow("SELECT id FROM servers WHERE id = $1", serverID).Scan(&id)
	if err != nil {
		log.Fatal(err)
		return false
	}
	return true
}

func RegisterServer(serverID int, language string) {
	_, err := DB.Exec("INSERT INTO servers (id, language) VALUES ($1, $2)", serverID, language)
	if err != nil {
		log.Fatal(err)
	}
}
