package database

import "log"

func GetRegisteredServersCount() int {
	var count int
	err := DB.QueryRow("SELECT COUNT(id) FROM servers").Scan(&count)
	if err != nil {
		log.Fatal(err)
		return 0
	}
	return count
}

func CheckIfServerRegistered(serverID uint64) bool {
	var id int
	err := DB.QueryRow("SELECT id FROM servers WHERE id = $1", serverID).Scan(&id)
	if err != nil {
		return false
	}
	return true
}

func RegisterServer(serverID uint64, language string) {
	_, err := DB.Exec("INSERT INTO servers (id, language) VALUES ($1, $2)", serverID, language)
	if err != nil {
		log.Fatal(err)
	}
}

func GetServerLanguage(serverID uint64) string {
	var language string
	err := DB.QueryRow("SELECT language FROM servers WHERE id = $1", serverID).Scan(&language)
	if err != nil {
		return "en"
	}
	return language
}

func UpdateServerLanguage(serverID uint64, language string) {
	_, err := DB.Exec("UPDATE servers SET language = $1 WHERE id = $2", language, serverID)
	if err != nil {
		log.Fatal(err)
	}
}
