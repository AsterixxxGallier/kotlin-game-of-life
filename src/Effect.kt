@Suppress("all", "unused")
enum class Effect(val code: Int) {
	RESET(0),
	BOLD(1),
	FAINT(2),
	ITALIC(3),
	UNDERLINE(4),
	SLOW_BLINK(5),
	RAPID_BLINK(6),
	REVERSE(7),
	CONCEAL(8),
	CROSSED_OUT(9),
	RESET_FONT(10),
	ALTERNATE_FONT_1(11),
	ALTERNATE_FONT_2(12),
	ALTERNATE_FONT_3(13),
	ALTERNATE_FONT_4(14),
	ALTERNATE_FONT_5(15),
	ALTERNATE_FONT_6(16),
	ALTERNATE_FONT_7(17),
	ALTERNATE_FONT_8(18),
	ALTERNATE_FONT_9(19),
	FRAKTUR(20),
	DOUBLE_UNDERLINE(21),
	RESET_INTENSITY(22),
	RESET_ITALIC_FRAKTUR(23),
	RESET_UNDERLINE(24),
	RESET_BLINK(25),
	RESET_INVERSE(27),
	REVEAL(28),
	RESET_CROSSED_OUT(29),
	FOREGROUND_BLACK(30),
	FOREGROUND_RED(31),
	FOREGROUND_GREEN(32),
	FOREGROUND_YELLOW(33),
	FOREGROUND_BLUE(34),
	FOREGROUND_MAGENTA(35),
	FOREGROUND_CYAN(36),
	FOREGROUND_WHITE(37),
	FOREGROUND(38),
	RESET_FOREGROUND(39),
	BACKGROUND_BLACK(40),
	BACKGROUND_RED(41),
	BACKGROUND_GREEN(42),
	BACKGROUND_YELLOW(43),
	BACKGROUND_BLUE(44),
	BACKGROUND_MAGENTA(45),
	BACKGROUND_CYAN(46),
	BACKGROUND_WHITE(47),
	BACKGROUND(48),
	RESET_BACKGROUND(49),
	FRAMED(51),
	ENCIRCLED(52),
	OVERLINED(53),
	RESET_FRAMED_ENCIRCLED(54),
	RESET_OVERLINED(55),
	IDEOGRAM_UNDERLINE(60),
	IDEOGRAM_DOUBLE_UNDERLINE(61),
	IDEOGRAM_OVERLINE(62),
	IDEOGRAM_DOUBLE_OVERLINE(63),
	IDEOGRAM_STRESS_MARKING(64),
	RESET_IDEOGRAM(65),
	FOREGROUND_BRIGHT_BLACK(90),
	FOREGROUND_BRIGHT_RED(91),
	FOREGROUND_BRIGHT_GREEN(92),
	FOREGROUND_BRIGHT_YELLOW(93),
	FOREGROUND_BRIGHT_BLUE(94),
	FOREGROUND_BRIGHT_MAGENTA(95),
	FOREGROUND_BRIGHT_CYAN(96),
	FOREGROUND_BRIGHT_WHITE(97),
	BACKGROUND_BRIGHT_BLACK(100),
	BACKGROUND_BRIGHT_RED(101),
	BACKGROUND_BRIGHT_GREEN(102),
	BACKGROUND_BRIGHT_YELLOW(103),
	BACKGROUND_BRIGHT_BLUE(104),
	BACKGROUND_BRIGHT_MAGENTA(105),
	BACKGROUND_BRIGHT_CYAN(106),
	BACKGROUND_BRIGHT_WHITE(107),
}
