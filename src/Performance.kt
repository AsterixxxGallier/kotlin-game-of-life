import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.system.measureNanoTime

@Target(AnnotationTarget.FUNCTION)
annotation class Benchmark(val times: Int = 1)

class Benchmarks {
	private val nextCellStateFromDead = arrayOf(
		// 00000000
		false,
		// 00000001
		false,
		// 00000010
		false,
		// 00000011
		false,
		// 00000100
		false,
		// 00000101
		false,
		// 00000110
		false,
		// 00000111
		true, // BIRTH
		// 00001000
		false,
		// 00001001
		false,
		// 00001010
		false,
		// 00001011
		true, // BIRTH
		// 00001100
		false,
		// 00001101
		true, // BIRTH
		// 00001110
		true, // BIRTH
		// 00001111
		false,
		// 00010000
		false,
		// 00010001
		false,
		// 00010010
		false,
		// 00010011
		true, // BIRTH
		// 00010100
		false,
		// 00010101
		true, // BIRTH
		// 00010110
		true, // BIRTH
		// 00010111
		false,
		// 00011000
		false,
		// 00011001
		true, // BIRTH
		// 00011010
		true, // BIRTH
		// 00011011
		false,
		// 00011100
		true, // BIRTH
		// 00011101
		false,
		// 00011110
		false,
		// 00011111
		false,
		// 00100000
		false,
		// 00100001
		false,
		// 00100010
		false,
		// 00100011
		true, // BIRTH
		// 00100100
		false,
		// 00100101
		true, // BIRTH
		// 00100110
		true, // BIRTH
		// 00100111
		false,
		// 00101000
		false,
		// 00101001
		true, // BIRTH
		// 00101010
		true, // BIRTH
		// 00101011
		false,
		// 00101100
		true, // BIRTH
		// 00101101
		false,
		// 00101110
		false,
		// 00101111
		false,
		// 00110000
		false,
		// 00110001
		true, // BIRTH
		// 00110010
		true, // BIRTH
		// 00110011
		false,
		// 00110100
		true, // BIRTH
		// 00110101
		false,
		// 00110110
		false,
		// 00110111
		false,
		// 00111000
		true, // BIRTH
		// 00111001
		false,
		// 00111010
		false,
		// 00111011
		false,
		// 00111100
		false,
		// 00111101
		false,
		// 00111110
		false,
		// 00111111
		false,
		// 01000000
		false,
		// 01000001
		false,
		// 01000010
		false,
		// 01000011
		true, // BIRTH
		// 01000100
		false,
		// 01000101
		true, // BIRTH
		// 01000110
		true, // BIRTH
		// 01000111
		false,
		// 01001000
		false,
		// 01001001
		true, // BIRTH
		// 01001010
		true, // BIRTH
		// 01001011
		false,
		// 01001100
		true, // BIRTH
		// 01001101
		false,
		// 01001110
		false,
		// 01001111
		false,
		// 01010000
		false,
		// 01010001
		true, // BIRTH
		// 01010010
		true, // BIRTH
		// 01010011
		false,
		// 01010100
		true, // BIRTH
		// 01010101
		false,
		// 01010110
		false,
		// 01010111
		false,
		// 01011000
		true, // BIRTH
		// 01011001
		false,
		// 01011010
		false,
		// 01011011
		false,
		// 01011100
		false,
		// 01011101
		false,
		// 01011110
		false,
		// 01011111
		false,
		// 01100000
		false,
		// 01100001
		true, // BIRTH
		// 01100010
		true, // BIRTH
		// 01100011
		false,
		// 01100100
		true, // BIRTH
		// 01100101
		false,
		// 01100110
		false,
		// 01100111
		false,
		// 01101000
		true, // BIRTH
		// 01101001
		false,
		// 01101010
		false,
		// 01101011
		false,
		// 01101100
		false,
		// 01101101
		false,
		// 01101110
		false,
		// 01101111
		false,
		// 01110000
		true, // BIRTH
		// 01110001
		false,
		// 01110010
		false,
		// 01110011
		false,
		// 01110100
		false,
		// 01110101
		false,
		// 01110110
		false,
		// 01110111
		false,
		// 01111000
		false,
		// 01111001
		false,
		// 01111010
		false,
		// 01111011
		false,
		// 01111100
		false,
		// 01111101
		false,
		// 01111110
		false,
		// 01111111
		false,
		// 10000000
		false,
		// 10000001
		false,
		// 10000010
		false,
		// 10000011
		true, // BIRTH
		// 10000100
		false,
		// 10000101
		true, // BIRTH
		// 10000110
		true, // BIRTH
		// 10000111
		false,
		// 10001000
		false,
		// 10001001
		true, // BIRTH
		// 10001010
		true, // BIRTH
		// 10001011
		false,
		// 10001100
		true, // BIRTH
		// 10001101
		false,
		// 10001110
		false,
		// 10001111
		false,
		// 10010000
		false,
		// 10010001
		true, // BIRTH
		// 10010010
		true, // BIRTH
		// 10010011
		false,
		// 10010100
		true, // BIRTH
		// 10010101
		false,
		// 10010110
		false,
		// 10010111
		false,
		// 10011000
		true, // BIRTH
		// 10011001
		false,
		// 10011010
		false,
		// 10011011
		false,
		// 10011100
		false,
		// 10011101
		false,
		// 10011110
		false,
		// 10011111
		false,
		// 10100000
		false,
		// 10100001
		true, // BIRTH
		// 10100010
		true, // BIRTH
		// 10100011
		false,
		// 10100100
		true, // BIRTH
		// 10100101
		false,
		// 10100110
		false,
		// 10100111
		false,
		// 10101000
		true, // BIRTH
		// 10101001
		false,
		// 10101010
		false,
		// 10101011
		false,
		// 10101100
		false,
		// 10101101
		false,
		// 10101110
		false,
		// 10101111
		false,
		// 10110000
		true, // BIRTH
		// 10110001
		false,
		// 10110010
		false,
		// 10110011
		false,
		// 10110100
		false,
		// 10110101
		false,
		// 10110110
		false,
		// 10110111
		false,
		// 10111000
		false,
		// 10111001
		false,
		// 10111010
		false,
		// 10111011
		false,
		// 10111100
		false,
		// 10111101
		false,
		// 10111110
		false,
		// 10111111
		false,
		// 11000000
		false,
		// 11000001
		true, // BIRTH
		// 11000010
		true, // BIRTH
		// 11000011
		false,
		// 11000100
		true, // BIRTH
		// 11000101
		false,
		// 11000110
		false,
		// 11000111
		false,
		// 11001000
		true, // BIRTH
		// 11001001
		false,
		// 11001010
		false,
		// 11001011
		false,
		// 11001100
		false,
		// 11001101
		false,
		// 11001110
		false,
		// 11001111
		false,
		// 11010000
		true, // BIRTH
		// 11010001
		false,
		// 11010010
		false,
		// 11010011
		false,
		// 11010100
		false,
		// 11010101
		false,
		// 11010110
		false,
		// 11010111
		false,
		// 11011000
		false,
		// 11011001
		false,
		// 11011010
		false,
		// 11011011
		false,
		// 11011100
		false,
		// 11011101
		false,
		// 11011110
		false,
		// 11011111
		false,
		// 11100000
		true, // BIRTH
		// 11100001
		false,
		// 11100010
		false,
		// 11100011
		false,
		// 11100100
		false,
		// 11100101
		false,
		// 11100110
		false,
		// 11100111
		false,
		// 11101000
		false,
		// 11101001
		false,
		// 11101010
		false,
		// 11101011
		false,
		// 11101100
		false,
		// 11101101
		false,
		// 11101110
		false,
		// 11101111
		false,
		// 11110000
		false,
		// 11110001
		false,
		// 11110010
		false,
		// 11110011
		false,
		// 11110100
		false,
		// 11110101
		false,
		// 11110110
		false,
		// 11110111
		false,
		// 11111000
		false,
		// 11111001
		false,
		// 11111010
		false,
		// 11111011
		false,
		// 11111100
		false,
		// 11111101
		false,
		// 11111110
		false,
		// 11111111
		false
	)
	val nextCellStateFromAlive = arrayOf(
		// 00000000
		false,
		// 00000001
		false,
		// 00000010
		false,
		// 00000011
		true, // FINE
		// 00000100
		false,
		// 00000101
		true, // FINE
		// 00000110
		true, // FINE
		// 00000111
		true, // FINE
		// 00001000
		false,
		// 00001001
		true, // FINE
		// 00001010
		true, // FINE
		// 00001011
		true, // FINE
		// 00001100
		true, // FINE
		// 00001101
		true, // FINE
		// 00001110
		true, // FINE
		// 00001111
		false,
		// 00010000
		false,
		// 00010001
		true, // FINE
		// 00010010
		true, // FINE
		// 00010011
		true, // FINE
		// 00010100
		true, // FINE
		// 00010101
		true, // FINE
		// 00010110
		true, // FINE
		// 00010111
		false,
		// 00011000
		true, // FINE
		// 00011001
		true, // FINE
		// 00011010
		true, // FINE
		// 00011011
		false,
		// 00011100
		true, // FINE
		// 00011101
		false,
		// 00011110
		false,
		// 00011111
		false,
		// 00100000
		false,
		// 00100001
		true, // FINE
		// 00100010
		true, // FINE
		// 00100011
		true, // FINE
		// 00100100
		true, // FINE
		// 00100101
		true, // FINE
		// 00100110
		true, // FINE
		// 00100111
		false,
		// 00101000
		true, // FINE
		// 00101001
		true, // FINE
		// 00101010
		true, // FINE
		// 00101011
		false,
		// 00101100
		true, // FINE
		// 00101101
		false,
		// 00101110
		false,
		// 00101111
		false,
		// 00110000
		true, // FINE
		// 00110001
		true, // FINE
		// 00110010
		true, // FINE
		// 00110011
		false,
		// 00110100
		true, // FINE
		// 00110101
		false,
		// 00110110
		false,
		// 00110111
		false,
		// 00111000
		true, // FINE
		// 00111001
		false,
		// 00111010
		false,
		// 00111011
		false,
		// 00111100
		false,
		// 00111101
		false,
		// 00111110
		false,
		// 00111111
		false,
		// 01000000
		false,
		// 01000001
		true, // FINE
		// 01000010
		true, // FINE
		// 01000011
		true, // FINE
		// 01000100
		true, // FINE
		// 01000101
		true, // FINE
		// 01000110
		true, // FINE
		// 01000111
		false,
		// 01001000
		true, // FINE
		// 01001001
		true, // FINE
		// 01001010
		true, // FINE
		// 01001011
		false,
		// 01001100
		true, // FINE
		// 01001101
		false,
		// 01001110
		false,
		// 01001111
		false,
		// 01010000
		true, // FINE
		// 01010001
		true, // FINE
		// 01010010
		true, // FINE
		// 01010011
		false,
		// 01010100
		true, // FINE
		// 01010101
		false,
		// 01010110
		false,
		// 01010111
		false,
		// 01011000
		true, // FINE
		// 01011001
		false,
		// 01011010
		false,
		// 01011011
		false,
		// 01011100
		false,
		// 01011101
		false,
		// 01011110
		false,
		// 01011111
		false,
		// 01100000
		true, // FINE
		// 01100001
		true, // FINE
		// 01100010
		true, // FINE
		// 01100011
		false,
		// 01100100
		true, // FINE
		// 01100101
		false,
		// 01100110
		false,
		// 01100111
		false,
		// 01101000
		true, // FINE
		// 01101001
		false,
		// 01101010
		false,
		// 01101011
		false,
		// 01101100
		false,
		// 01101101
		false,
		// 01101110
		false,
		// 01101111
		false,
		// 01110000
		true, // FINE
		// 01110001
		false,
		// 01110010
		false,
		// 01110011
		false,
		// 01110100
		false,
		// 01110101
		false,
		// 01110110
		false,
		// 01110111
		false,
		// 01111000
		false,
		// 01111001
		false,
		// 01111010
		false,
		// 01111011
		false,
		// 01111100
		false,
		// 01111101
		false,
		// 01111110
		false,
		// 01111111
		false,
		// 10000000
		false,
		// 10000001
		true, // FINE
		// 10000010
		true, // FINE
		// 10000011
		true, // FINE
		// 10000100
		true, // FINE
		// 10000101
		true, // FINE
		// 10000110
		true, // FINE
		// 10000111
		false,
		// 10001000
		true, // FINE
		// 10001001
		true, // FINE
		// 10001010
		true, // FINE
		// 10001011
		false,
		// 10001100
		true, // FINE
		// 10001101
		false,
		// 10001110
		false,
		// 10001111
		false,
		// 10010000
		true, // FINE
		// 10010001
		true, // FINE
		// 10010010
		true, // FINE
		// 10010011
		false,
		// 10010100
		true, // FINE
		// 10010101
		false,
		// 10010110
		false,
		// 10010111
		false,
		// 10011000
		true, // FINE
		// 10011001
		false,
		// 10011010
		false,
		// 10011011
		false,
		// 10011100
		false,
		// 10011101
		false,
		// 10011110
		false,
		// 10011111
		false,
		// 10100000
		true, // FINE
		// 10100001
		true, // FINE
		// 10100010
		true, // FINE
		// 10100011
		false,
		// 10100100
		true, // FINE
		// 10100101
		false,
		// 10100110
		false,
		// 10100111
		false,
		// 10101000
		true, // FINE
		// 10101001
		false,
		// 10101010
		false,
		// 10101011
		false,
		// 10101100
		false,
		// 10101101
		false,
		// 10101110
		false,
		// 10101111
		false,
		// 10110000
		true, // FINE
		// 10110001
		false,
		// 10110010
		false,
		// 10110011
		false,
		// 10110100
		false,
		// 10110101
		false,
		// 10110110
		false,
		// 10110111
		false,
		// 10111000
		false,
		// 10111001
		false,
		// 10111010
		false,
		// 10111011
		false,
		// 10111100
		false,
		// 10111101
		false,
		// 10111110
		false,
		// 10111111
		false,
		// 11000000
		true, // FINE
		// 11000001
		true, // FINE
		// 11000010
		true, // FINE
		// 11000011
		false,
		// 11000100
		true, // FINE
		// 11000101
		false,
		// 11000110
		false,
		// 11000111
		false,
		// 11001000
		true, // FINE
		// 11001001
		false,
		// 11001010
		false,
		// 11001011
		false,
		// 11001100
		false,
		// 11001101
		false,
		// 11001110
		false,
		// 11001111
		false,
		// 11010000
		true, // FINE
		// 11010001
		false,
		// 11010010
		false,
		// 11010011
		false,
		// 11010100
		false,
		// 11010101
		false,
		// 11010110
		false,
		// 11010111
		false,
		// 11011000
		false,
		// 11011001
		false,
		// 11011010
		false,
		// 11011011
		false,
		// 11011100
		false,
		// 11011101
		false,
		// 11011110
		false,
		// 11011111
		false,
		// 11100000
		true, // FINE
		// 11100001
		false,
		// 11100010
		false,
		// 11100011
		false,
		// 11100100
		false,
		// 11100101
		false,
		// 11100110
		false,
		// 11100111
		false,
		// 11101000
		false,
		// 11101001
		false,
		// 11101010
		false,
		// 11101011
		false,
		// 11101100
		false,
		// 11101101
		false,
		// 11101110
		false,
		// 11101111
		false,
		// 11110000
		false,
		// 11110001
		false,
		// 11110010
		false,
		// 11110011
		false,
		// 11110100
		false,
		// 11110101
		false,
		// 11110110
		false,
		// 11110111
		false,
		// 11111000
		false,
		// 11111001
		false,
		// 11111010
		false,
		// 11111011
		false,
		// 11111100
		false,
		// 11111101
		false,
		// 11111110
		false,
		// 11111111
		false
	)

	private val index = 123

	@Benchmark(100000000)
	fun countOnes() {
		index.countOneBits() == 3
	}

	@Benchmark(100000000)
	fun arrayAccess() {
		nextCellStateFromDead[index]
	}
}

fun main() {
	val benchmarks = Benchmarks::class.memberFunctions.filter { it.hasAnnotation<Benchmark>() }
	val instance = Benchmarks()

	benchmarks.forEach {function ->
		val times = function.findAnnotation<Benchmark>()!!.times

		println("Running \u001B[32m${function.name}\u001B[0m $times times")

		var totalNanos = 0L
		repeat(times) {
			totalNanos += measureNanoTime { function.call(instance) }
		}

		val averageNanos = totalNanos / times
		println("-> took on average $averageNanos nanos or ${averageNanos / 1_000_000_000} seconds\n")
	}
}