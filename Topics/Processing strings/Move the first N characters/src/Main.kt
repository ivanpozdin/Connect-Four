fun main() {
    val strWhole = readln()
    val number = strWhole.reversed().substringBefore(" ").reversed().toInt()
    val str = strWhole.removeSuffix(" $number")
    if (str.length < number){
        println(str)
    } else {
        print(str.substring(startIndex = number))
        println(str.substring(0, number))
    }

}