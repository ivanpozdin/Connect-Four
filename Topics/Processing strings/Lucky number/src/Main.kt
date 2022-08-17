fun main() {
    val wholeNumber = readln()
    val firstHalf = wholeNumber.substring(0, wholeNumber.length/2)
    val secondHalf = wholeNumber.substring(wholeNumber.length/2, wholeNumber.length)
    var firstSum: Int = 0
    var secondSum: Int = 0
    for (ch in firstHalf){
        firstSum += ch.code
    }
    for (ch in secondHalf){
        secondSum += ch.code
    }
    println(if (firstSum == secondSum)"YES" else "NO")
}