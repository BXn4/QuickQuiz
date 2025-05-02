package utils

type Color struct {
	PrimaryColor   int
	SecondaryColor int
	SuccessColor   int
	ErrorColor     int
	InfoColor      int
	WarningColor   int
}

type Emoji struct {
	STATS           string
	LEVEL           string
	PROGRESS        string
	XP              string
	CORRECT_ANSWERS string
	STREAK          string
	BEST_STREAK     string
}

var Colors = Color{
	PrimaryColor:   0x4d7e82,
	SecondaryColor: 0xff5733,
	SuccessColor:   0x28a745,
	ErrorColor:     0xdc3545,
	InfoColor:      0x17a2b8,
	WarningColor:   0xffc107,
}

var Emojis = Emoji{
	STATS:           ":star2:",
	LEVEL:           ":level_slider:",
	PROGRESS:        ":bar_chart: ",
	XP:              ":test_tube:",
	CORRECT_ANSWERS: ":trophy:",
	STREAK:          ":fire:",
	BEST_STREAK:     ":medal:",
}
