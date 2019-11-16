fun String.foreground(color: EffectCombination, reset: Boolean = true): String =
	if (reset) {
		"$color$this${EffectCombination.RESET}"
	} else {
		"$color$this"
	}

fun String.background(color: EffectCombination, reset: Boolean = true): String =
	if (reset) {
		"$color$this${EffectCombination.RESET}"
	} else {
		"$color$this"
	}

fun String.foreground(r: Byte, g: Byte, b: Byte, reset: Boolean = true): String {
	val color = ForegroundColorRGB(r, g, b)
	return foreground(color, reset)
}

fun String.background(r: Byte, g: Byte, b: Byte, reset: Boolean = true): String {
	val color = BackgroundColorRGB(r, g, b)
	return background(color, reset)
}

fun String.foreground8Bit(code: Byte, reset: Boolean = true): String {
	val color = ForegroundColor8Bit(code)
	return foreground(color, reset)
}

fun String.background8Bit(code: Byte, reset: Boolean = true): String {
	val color = BackgroundColor8Bit(code)
	return background(color, reset)
}
