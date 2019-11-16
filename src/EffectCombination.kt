import Effect.BACKGROUND
import Effect.FOREGROUND

open class EffectCombination(private vararg val effects: Int) {
	override fun toString(): String {
		return "\u001B[${effects.joinToString(";")}m"
	}
}

open class ForegroundColor(vararg arguments: Int) : EffectCombination(FOREGROUND, *arguments)

open class BackgroundColor(vararg arguments: Int) : EffectCombination(BACKGROUND, *arguments)

class ForegroundColor8Bit(code: Byte) : ForegroundColor(5, code.toInt())

class BackgroundColor8Bit(code: Byte) : BackgroundColor(5, code.toInt())

class ForegroundColorRGB(r: Byte, g: Byte, b: Byte) :
	ForegroundColor(2, r.toInt(), g.toInt(), b.toInt())

class BackgroundColorRGB(r: Byte, g: Byte, b: Byte) :
	BackgroundColor(2, r.toInt(), g.toInt(), b.toInt())
