import kotlin.random.Random
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty

interface Grid {
	val rowCount: Int
	val columnCount: Int

	val relevantPart: Grid
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
				println(boolRows.withIndex().joinToString("\n") { rowWithIndex ->
					if (rowWithIndex.index == rowIndex) {
						rowWithIndex.value.withIndex().joinToString(" ") {
							if (it.index == columnCenter) {
								if (it.value) "\u001B[32m1\u001B[0m" else "\u001B[32m0\u001B[0m"
							} else {
								if (it.value) "1" else "0"
							}
						}
					} else {
						rowWithIndex.value.joinToString(" ") { if (it) "1" else "0" }
					}
				})
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
	val boolGrid: BoolGrid
		get() { return state.boolGrid }
	val neighbourGrid: IntGrid
		get() { return state.neighbourGrid }

	fun step() {
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
				val neighbourCount = neighbourRow[columnIndex]
				if (boolRow[columnIndex]) {
					boolRow[columnIndex] = neighbourCount == 3 || neighbourCount == 2
				} else {
					boolRow[columnIndex] = neighbourCount == 3
				}
			}
		}
	}
}

fun main() {
	val boolGrid = BoolGrid(10, 10) { _, _ -> Random.nextBoolean() }
	println(boolGrid)
	println()

	val state = GameState(boolGrid)
	println("BOOLGRID")
	println(boolGrid.relevantPart)
	println("NEIGHBOURGRID")
	println(state.neighbourGrid.relevantPart)

	val game = Game(state)
	println("BOOLGRID")
	println(game.state.boolGrid.relevantPart)
	println("NEIGHBOURGRID")
	println(state.neighbourGrid.relevantPart)
}
