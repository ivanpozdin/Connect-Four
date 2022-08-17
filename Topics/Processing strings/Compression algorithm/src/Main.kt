fun main() {
    val str: String = readln()
    var startIndex = 0
    while (startIndex < str.length) {
        var endIndex = startIndex
        while (endIndex + 1 < str.length && str[endIndex + 1] == str[startIndex]) {
            endIndex++
        }
        print("${str[startIndex]}${endIndex - startIndex + 1}")
        startIndex = endIndex + 1
    }
}