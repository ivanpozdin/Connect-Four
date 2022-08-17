package connectfour

const val FIRST_WON = 0
const val SECOND_WON = 1
const val DRAW = 2
const val NOT_GAME_OVER_YET = 4

class Player(val symbol: Char, var name: String = "A", var score: Int = 0)

class BoardGame {
    private val firstPlayer = Player('o')
    private val secondPlayer = Player('*')

    private var amountOfGames = 1
    private var gameNumber = 0

    private var rowsAmount: Int = 6
    private var columnsAmount: Int = 7

    fun startGame() {
        println("Connect Four")
        getNames()
        getDimensions()
        getAmountOfGames()
        println("${firstPlayer.name} VS ${secondPlayer.name}")
        println("$rowsAmount X $columnsAmount board")
        when (amountOfGames) {
            1 -> println("Single game")
            else -> println("Total $amountOfGames games")
        }
        if (amountOfGames == 1) {
            processMoves()
        } else if (amountOfGames > 1) {
            processMultipleGames()
        }

    }

    private fun getNames() {
        println("First player's name:")
        firstPlayer.name = readln()
        println("Second player's name:")
        secondPlayer.name = readln()
    }

    private fun matchesCorrectTemplate(input: String?) =
        input?.let { "([ \t]*0*[5-9][ \t]*([xX])[ \t]*0*[5-9][ \t]*)|(^$)".toRegex().matches(it) } ?: false

    private fun matchesBothRowColumnTemplate(input: String?) = input?.let {
        "([ \t]*([0-4]|[1-9]\\d+)[ \t]*([xX])[ \t]*([0-4]|[1-9]\\d+)[ \t]*)".toRegex().matches(it)
    } ?: false

    private fun matchesRowTemplate(input: String?) =
        input?.let { "[ \t]*0*([0-4]|([1-9]\\d+))[ \t]*[xX][ \t]*0*[5-9][ \t]*".toRegex().matches(it) } ?: false

    private fun matchesColumnTemplate(input: String?) = input?.let {
        "[ \t]*0*[5-9][ \t]*[xX][ \t]*0*([0-4]|([1-9]\\d+)[ \t]*)".toRegex().matches(it)
    } ?: false

    private fun getDimensions() {
        var dimensions: String? = null
        while (!matchesCorrectTemplate(dimensions)) {
            println(
                "Set the board dimensions (Rows x Columns)\n" + "Press Enter for default (6 x 7)"
            )
            dimensions = readln()
            when {
                matchesCorrectTemplate(dimensions) -> break
                matchesRowTemplate(dimensions) -> println("Board rows should be from 5 to 9")
                matchesColumnTemplate(dimensions) -> println("Board columns should be from 5 to 9")
                matchesBothRowColumnTemplate(dimensions) -> println("Board rows should be from 5 to 9\n" + "Board columns should be from 5 to 9")
                else -> println("Invalid input")
            }
        }
        if (dimensions == "") {
            rowsAmount = 6
            columnsAmount = 7
        } else {
            val regex = "0*[5-9]".toRegex()
            rowsAmount = dimensions?.let { regex.find(it)?.value.toString().toIntOrNull() } ?: 6
            columnsAmount = dimensions?.let {
                regex.find(it, dimensions.indexOf('x', ignoreCase = true))?.value.toString().toIntOrNull()
            } ?: 7
        }
    }

    private fun getAmountOfGames() {
        var input = " "
        while ((input.toIntOrNull() ?: 0) < 1 && input != "") {
            println(
                "Do you want to play single or multiple games?\n" + "For a single game, input 1 or press Enter\n" + "Input a number of games:"
            )
            input = readln()
            if ((input.toIntOrNull() ?: 0) < 1 && input != "") println("Invalid input")
        }
        amountOfGames = input.toIntOrNull() ?: 1
    }

    private fun drawBoard(board: MutableList<MutableList<Char>>) {
        var amountOfDrawnRows = 0
//        Заполнил 0 строку цифрами-названиями столбцов
        var number = '1'
        for (i in 1..columnsAmount * 2 step 2) {
            board[amountOfDrawnRows][i] = number
            number++
        }
        amountOfDrawnRows++

        while (amountOfDrawnRows <= rowsAmount) {
            for (i in 0..columnsAmount) {
                board[amountOfDrawnRows][i * 2] = '║'
            }
            amountOfDrawnRows++
        }
        board[amountOfDrawnRows][0] = '╚'
        for (i in 2 until 2 * columnsAmount step 2) {
            board[amountOfDrawnRows][i - 1] = '═'
            board[amountOfDrawnRows][i] = '╩'
        }
        board[amountOfDrawnRows][2 * columnsAmount - 1] = '═'
        board[amountOfDrawnRows][2 * columnsAmount] = '╝'
    }

    private fun addSymbolToColumnOrGetFalse(
        columnOrNull: Int?, board: MutableList<MutableList<Char>>, symbol: Char
    ): Boolean {
        val column = columnOrNull ?: 0
        val boardColumn = getBoardColumn(column)
        return when {
            columnOrNull == null -> {
                println("Incorrect column number")
                false
            }
            column !in 1..columnsAmount -> {
                println("The column number is out of range (1 - $columnsAmount)")
                false
            }
            board[1][boardColumn] != ' ' -> {
                println("Column $column is full")
                false
            }
            else -> {
                var boardRow = 1
                while (boardRow < boardRowsAmount - 1 && board[boardRow + 1][boardColumn] == ' ') {
                    boardRow++
                }
                board[boardRow][boardColumn] = symbol
                true
            }
        }
    }

    private fun checkBoard(board: MutableList<MutableList<Char>>): Int {
        for (r in board.indices) {
            for (c in 1..board[r].lastIndex - 6 step 2) {
                if (board[r][c] == board[r][c + 2] && board[r][c] == board[r][c + 4] && board[r][c] == board[r][c + 6]) {
                    when {
                        board[r][c] == firstPlayer.symbol -> return FIRST_WON
                        board[r][c] == secondPlayer.symbol -> return SECOND_WON
                    }
                }
            }
        }

        for (r in 1..board.lastIndex - 3) {
            for (c in 1..board[r].lastIndex step 2) {
                if (board[r][c] == board[r + 1][c] && board[r][c] == board[r + 2][c] && board[r][c] == board[r + 3][c]) {
                    when {
                        board[r][c] == firstPlayer.symbol -> return FIRST_WON
                        board[r][c] == secondPlayer.symbol -> return SECOND_WON
                    }
                }
            }
        }

        for (r in 1..board.lastIndex - 3) {
            for (c in 1..board[r].lastIndex - 6 step 2) {
                if (board[r][c] == board[r + 1][c + 2] && board[r][c] == board[r + 2][c + 4] && board[r][c] == board[r + 3][c + 6]) {
                    when {
                        board[r][c] == firstPlayer.symbol -> return FIRST_WON
                        board[r][c] == secondPlayer.symbol -> return SECOND_WON
                    }
                }
            }
        }

        for (r in 4..board.lastIndex) {
            for (c in 1..board[r].lastIndex - 6 step 2) {
                if (board[r][c] == board[r - 1][c + 2] && board[r][c] == board[r - 2][c + 4] && board[r][c] == board[r - 3][c + 6]) {
                    when {
                        board[r][c] == firstPlayer.symbol -> return FIRST_WON
                        board[r][c] == secondPlayer.symbol -> return SECOND_WON
                    }
                }
            }
        }

        var isBoardFull = true
        for (c in 1..board[1].lastIndex step 2) {
            if (board[1][c] == ' ') {
                isBoardFull = false
            }
        }
        if (isBoardFull) {
            return DRAW
        }

        return NOT_GAME_OVER_YET
    }

    private fun returnAnotherSymbol(symbol: Char) =
        if (symbol == firstPlayer.symbol) firstPlayer.symbol else firstPlayer.symbol

    private fun askForTurns(board: MutableList<MutableList<Char>>) {
        printBoard(board)
        var symbol = firstPlayer.symbol
        while (true) {
            println("${if (symbol == firstPlayer.symbol) firstPlayer.name else secondPlayer.name}\'s turn:")
            val input = readln()
            if (input == "end") {
                break
            } else if (addSymbolToColumnOrGetFalse(input.toIntOrNull(), board, symbol)) {
                symbol = returnAnotherSymbol(symbol)
                printBoard(board)
            }

            when (checkBoard(board)) {
                FIRST_WON -> {
                    println("Player ${firstPlayer.name} won")
                    break
                }
                SECOND_WON -> {
                    println("Player ${secondPlayer.name} won")
                    break
                }
                DRAW -> {
                    println("It is a draw")
                    break
                }
            }

        }
        println("Game over!")
    }

    private fun getBoardColumn(column: Int) = column * 2 - 1
    private val boardRowsAmount: Int
        get() = rowsAmount + 2

    private fun printBoard(board: MutableList<MutableList<Char>>) {
        for (str in board) {
            for (symbol in str) {
                print(symbol)
            }
            println()
        }
    }

    private fun processMoves() {
        val board = MutableList(rowsAmount + 2) { MutableList(2 * columnsAmount + 1) { ' ' } }
        drawBoard(board)
        askForTurns(board)
    }

    private fun processMultipleGames() {
        while (gameNumber < amountOfGames) {
            gameNumber++
            val board = MutableList(rowsAmount + 2) { MutableList(2 * columnsAmount + 1) { ' ' } }
            drawBoard(board)
            askForTurnsMultipleVersion(board)
        }
        println("Game over!")
    }

    private fun askForTurnsMultipleVersion(board: MutableList<MutableList<Char>>) {
        println("Game #$gameNumber")
        printBoard(board)
        var symbol = if (gameNumber % 2 == 1) firstPlayer.symbol else secondPlayer.symbol
        while (true) {
            println("${if (symbol == firstPlayer.symbol) firstPlayer.name else secondPlayer.name}\'s turn:")
            val input = readln()
            if (input == "end") {
                break
            } else if (addSymbolToColumnOrGetFalse(input.toIntOrNull(), board, symbol)) {
                symbol = returnAnotherSymbol(symbol)
                printBoard(board)
            }

            when (checkBoard(board)) {
                FIRST_WON -> {
                    println("Player ${firstPlayer.name} won")
                    firstPlayer.score += 2
                    break
                }
                SECOND_WON -> {
                    println("Player ${secondPlayer.name} won")
                    secondPlayer.score += 2
                    break
                }
                DRAW -> {
                    println("It is a draw")
                    firstPlayer.score++
                    secondPlayer.score++
                    break
                }
            }
        }
        println("Score")
        println("${firstPlayer.name}: ${firstPlayer.score} ${secondPlayer.name}: ${secondPlayer.score}")
    }

}

fun main() {
    val boardGame = BoardGame()
    boardGame.startGame()
}