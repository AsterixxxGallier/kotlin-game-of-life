
import kotlin.random.Random
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty

fun String.highlight(vararg codes: Int): String =
	"\u001B[${codes.joinToString(";")}m$this\u001B[0m"

interface Grid {
	val rowCount: Int
	val columnCount: Int

	val relevantPart: Grid

	fun toHighlightedString(rowIndex: Int, columnIndex: Int): String
}

class BoolGrid(
	override val rowCount: Int,
	override val columnCount: Int,
	init: (rowIndex: Int, columnIndex: Int) -> Boolean = { _, _ -> false }
) : Grid {
	var rows: Array<BooleanArray> = Array(rowCount) { rowIndex ->
		BooleanArray(columnCount) { columnIndex ->
			init(rowIndex, columnIndex)
		}
	}

	override fun toHighlightedString(rowIndex: Int, columnIndex: Int): String =
		rows.withIndex().joinToString("\n") { rowWithIndex ->
			if (rowWithIndex.index == rowIndex) {
				rowWithIndex.value.withIndex().joinToString(" ") {
					val stringified = if (it.value) "1" else "0"
					if (it.index == columnIndex) stringified.highlight(32)
					else stringified
				}
			} else {
				rowWithIndex.value.joinToString(" ") { if (it) "1" else "0" }
			}
		}

	override val relevantPart: Grid
		get() {
			return BoolGrid(rowCount - 2, columnCount - 2) { r, c -> rows[r + 1][c + 1] }
		}

	override fun toString(): String {
		return rows.joinToString("\n") { row -> row.joinToString(" ") { if (it) "1" else "0" } }
	}
}

class IntGrid(
	override val rowCount: Int,
	override val columnCount: Int,
	init: (rowIndex: Int, columnIndex: Int) -> Int = { _, _ -> 0 }
) : Grid {
	var rows: Array<IntArray> = Array(rowCount) { rowIndex ->
		IntArray(columnCount) { columnIndex ->
			init(rowIndex, columnIndex)
		}
	}

	override fun toHighlightedString(rowIndex: Int, columnIndex: Int): String =
		rows.withIndex().joinToString("\n") { rowWithIndex ->
			if (rowWithIndex.index == rowIndex) {
				rowWithIndex.value.withIndex().joinToString(" ") {
					val stringified = it.toString()
					if (it.index == columnIndex) stringified.highlight(32)
					else stringified
				}
			} else {
				rowWithIndex.value.joinToString(" ") { it.toString() }
			}
		}

	override val relevantPart: Grid
		get() {
			return IntGrid(rowCount - 2, columnCount - 2) { r, c -> rows[r + 1][c + 1] }
		}

	override fun toString(): String {
		return rows.joinToString("\n") { row ->
			row.joinToString(" ") {
				it.toString()
			}
		}
	}
}

class UpdatableLazy<T, R, P>(
	private val observedReceiver: R,
	private val observedProperty: KMutableProperty1<R, P>,
	private val getter: (P) -> T
) {
	private var lastObservedValue = observedProperty.get(observedReceiver)
	private var value = getter(observedProperty.get(observedReceiver))

	operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
		val observedValue = observedProperty.get(observedReceiver)

		if (observedValue != lastObservedValue) {
			lastObservedValue = observedValue
			value = getter(observedValue)
		}

		return value
	}
}

class GameState(
	var boolGrid: BoolGrid
) {
	val neighbourGrid: IntGrid by UpdatableLazy(this, GameState::boolGrid) { boolGrid ->
		println("neighbourGrid getter called")

		val boolRows = boolGrid.rows
		val neighbourGrid = IntGrid(boolGrid.rowCount, boolGrid.columnCount)
		println(neighbourGrid)
		val neighbourRows = neighbourGrid.rows
		for (rowIndex in 1..(boolGrid.rowCount - 2)) {
			val boolRow = boolRows[rowIndex]

			//region neighbour rows
			val neighbourRowTop = neighbourRows[rowIndex - 1]
			val neighbourRowCenter = neighbourRows[rowIndex]
			val neighbourRowBottom = neighbourRows[rowIndex + 1]
			//endregion
			for (columnCenter in 1..(boolGrid.columnCount - 2)) {
				println()
				println(boolGrid.relevantPart.toHighlightedString(rowIndex, columnCenter))
				if (boolRow[columnCenter]) {
					println("Incrementing neighbour counts for surrounding cells...")

					val columnRight = columnCenter + 1
					val columnLeft = columnCenter - 1

					// top center
					neighbourRowTop[columnCenter] += 1

					// top right
					neighbourRowTop[columnRight] += 1

					// center right
					neighbourRowCenter[columnRight] += 1

					// bottom right
					neighbourRowBottom[columnRight] += 1

					// bottom center
					neighbourRowBottom[columnCenter] += 1

					// bottom left
					neighbourRowBottom[columnLeft] += 1

					// center left
					neighbourRowCenter[columnLeft] += 1

					// top left
					neighbourRowTop[columnLeft] += 1

					println(neighbourGrid)
				}
			}
		}
		neighbourGrid
	}
}

class Game(var state: GameState) {
	companion object {
		private val survival = listOf(2, 3)
		private val birth = listOf(3)
	}

	val boolGrid: BoolGrid
		get() {
			return state.boolGrid
		}

	val neighbourGrid: IntGrid
		get() {
			return state.neighbourGrid
		}

	fun step() {
// 		println("step called")
		val boolGrid = state.boolGrid
		val neighbourGrid = state.neighbourGrid
		val boolRows = boolGrid.rows
		val neighbourRows = neighbourGrid.rows
		val rowCount = boolGrid.rowCount
		val columnCount = boolGrid.columnCount

		for (rowIndex in 1 until rowCount - 2) {
			val boolRow = boolRows[rowIndex]
			val neighbourRow = neighbourRows[rowIndex]
			for (columnIndex in 1 until columnCount - 2) {
				println()
				println(boolGrid.relevantPart.toHighlightedString(rowIndex, columnIndex))
				val neighbourCount = neighbourRow[columnIndex]
				if (boolRow[columnIndex]) {
					boolRow[columnIndex] = survival.contains(neighbourCount)
				} else {
					boolRow[columnIndex] = birth.contains(neighbourCount)
				}
			}
		}
	}
}

const val ROW_COUNT = 10
const val COLUMN_COUNT = 10
fun main() {
	val boolGrid = BoolGrid(ROW_COUNT, COLUMN_COUNT) { _, _ -> Random.nextBoolean() }
	println(boolGrid)
	println()

	val state = GameState(boolGrid)
	val game = Game(state)
	repeat(100) {
		println("STEP")
		// 	println("BOOLGRID")
		println(game.boolGrid.relevantPart)
		// 	println("NEIGHBOURGRID")
		// 	println(game.neighbourGrid.relevantPart)

		game.step()
	}

// 	println("${"TEST".highlight(32)}TEST")
}
