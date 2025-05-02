package utils

import (
	"fmt"
	"os"

	"github.com/leonelquinteros/gotext"
)

func GetTranslation(languageCode, messageID string) string {
	po := gotext.NewPo()

	filePath := fmt.Sprintf("internal/data/locales/%s.po", languageCode)

	if _, err := os.Stat(filePath); err != nil {
		return messageID
	}

	po.ParseFile(filePath)

	return po.Get(messageID)
}
