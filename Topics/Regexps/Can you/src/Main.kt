fun main() {
    val answer = readln()
    val reg1 = ("I can do my homework on time!".toRegex())
    val reg2 = ("I can't do my homework on time!".toRegex())

    println(reg1.matches(answer) || reg2.matches(answer))
}