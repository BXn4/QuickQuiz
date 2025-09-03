package database

import (
	"database/sql"
	"fmt"
	"log"

	_ "github.com/lib/pq"
)

var DB *sql.DB

func ConnectToDB(username, password, dbname string) {
	log.Println("Connecting to target database...")

	dsn := fmt.Sprintf("user=%s password=%s dbname=%s sslmode=disable", username, password, dbname)
	db, err := sql.Open("postgres", dsn)
	if err != nil {
		log.Fatalf("Failed to open DB: %v", err)
	}

	if err := db.Ping(); err != nil {
		log.Printf("Target database %q doesn't exist. Attempting to create...", dbname)

		postgresDSN := fmt.Sprintf("user=%s password=%s dbname=postgres sslmode=disable", username, password)
		adminDB, err := sql.Open("postgres", postgresDSN)
		if err != nil {
			log.Fatalf("Failed to connect to postgres for DB creation: %v", err)
		}
		defer adminDB.Close()

		if err := createDatabase(adminDB, dbname); err != nil {
			log.Fatalf("Failed to create database %q: %v", dbname, err)
		}
		log.Printf("Database %q created successfully.", dbname)

		db, err = sql.Open("postgres", dsn)
		if err != nil {
			log.Fatalf("Failed to reconnect to newly created database: %v", err)
		}
		if err := db.Ping(); err != nil {
			log.Fatalf("Ping failed on newly created database: %v", err)
		}
	}

	DB = db

	log.Println("Creating tables if not exist...")
	if err := createTables(); err != nil {
		log.Fatalf("Failed to create tables: %v", err)
	}

	log.Println("Connected to DB and tables are ready.")
}

func createDatabase(adminDB *sql.DB, name string) error {
	query := fmt.Sprintf("CREATE DATABASE %s;", pqQuoteIdentifier(name))
	_, err := adminDB.Exec(query)
	return err
}

func pqQuoteIdentifier(identifier string) string {
	return `"` + identifier + `"`
}

func createTables() error {
	query := `
	CREATE TABLE IF NOT EXISTS quizzes (
		id VARCHAR(100) PRIMARY KEY,
		creator_id INTEGER NOT NULL,
		title TEXT NOT NULL,
		question TEXT NOT NULL,
		reason TEXT,
		thumbnail TEXT,
		answers TEXT NOT NULL,
		correct_answer SMALLINT NOT NULL,
		active_time SMALLINT NOT NULL,
		created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	);

	CREATE TABLE IF NOT EXISTS servers (
		id BIGINT PRIMARY KEY,
		quizzes TEXT[],
		active_quiz VARCHAR(100),
		quiz_role BIGSERIAL,
		quiz_channel BIGSERIAL,
		language VARCHAR(2) DEFAULT 'en'
	);

	CREATE TABLE IF NOT EXISTS quiz_packs (
		id VARCHAR(100) PRIMARY KEY,
		creator_id INTEGER NOT NULL,
		creator_name TEXT NOT NULL,
		name TEXT NOT NULL,
		description TEXT NOT NULL,
		created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	);

	CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY,
    language VARCHAR(2) NOT NULL,
    is_premium BOOLEAN DEFAULT FALSE,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

    CREATE TABLE IF NOT EXISTS user_server (
        user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
        server_id BIGINT REFERENCES servers(id) ON DELETE CASCADE,
        xp BIGINT DEFAULT 0,
        answers INT DEFAULT 0,
        correct_answers INT DEFAULT 0,
        streak INT DEFAULT 0,
        best_streak INT DEFAULT 0,
        PRIMARY KEY (user_id, server_id)
    );
	`
	_, err := DB.Exec(query)
	return err
}
