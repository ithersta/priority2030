package domain.datatypes

import kotlinx.serialization.Serializable
import kotlin.math.floor

@Serializable
data class InformationCost(
    val price: Double
) : FieldData {
    private val digUnits = arrayOf(
        arrayOf("одна", "две", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"), arrayOf("один", "два")
    )
    private val digDozens = arrayOf(
        "десять",
        "одиннадцать",
        "двенадцать",
        "тринадцать",
        "четырнадцать",
        "пятнадцать",
        "шестнадцать",
        "семнадцать",
        "восемнадцать",
        "девятнадцать"
    )
    private val digTwenties = arrayOf(
        "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят", "семьдесят", "восемьдесят", "девяносто"
    )
    private val digHundreds = arrayOf(
        "сто", "двести", "триста", "четыреста", "пятьсот", "шестьсот", "семьсот", "восемьсот", "девятьсот"
    )
    private val digitThousands = arrayOf("тысяча", "тысячи", "тысяч", "0")
    private val digitMillions = arrayOf("миллион", "миллиона", "миллионов", "1")
    private val digitBillions = arrayOf("миллиард", "миллиарда", "миллиардов", "1")
    private val digitTrillions = arrayOf("триллион", "триллиона", "триллионов", "1")


    private val wordsRUB = arrayOf(
        arrayOf("копейка", "копейки", "копеек", "0"),
        arrayOf("рубль", "рубля", "рублей", "1"),
        digitThousands,
        digitMillions,
        digitBillions,
        digitTrillions
    )
    private val stringLen = 255
    private val zeroDigit = 0L
    private val oneDigit = 1
    private val twoDigit = 2
    private val threeDigit = 3
    private val thousandDigit = 1000
    private val hundredDigit = 100
    private val tenDigit = 10

    private fun num2words(num: Long, level: Int, currency: Array<Array<String>>): String {
        val words = StringBuilder(stringLen)
        if (num == zeroDigit) words.append("ноль ")
        val i = currency[level][threeDigit].indexOf("1").inc()
        val h = (num % thousandDigit).toInt()
        var d = h / hundredDigit
        if (d > zeroDigit) words.append(digHundreds[d.dec()]).append(" ")
        var n = h % hundredDigit
        d = n / tenDigit
        n %= threeDigit
        when (d) {
            0 -> {}
            1 -> words.append(digDozens[n]).append(" ")
            else -> words.append(digTwenties[d.dec().dec()]).append(" ")
        }
        if (d == oneDigit) n = zeroDigit.toInt()
        when (n) {
            0 -> {}
            1, 2 -> words.append(digUnits[i][n.dec()]).append(" ")
            else -> words.append(digUnits[zeroDigit.toInt()][n.dec()]).append(" ")
        }
        when (n) {
            1 -> words.append(currency[level][zeroDigit.toInt()])
            2, 3, 4 -> words.append(currency[level][oneDigit])
            else -> if ((h != 0) || ((h == zeroDigit.toInt()) && (level == oneDigit))) {
                words.append(currency[level][twoDigit])
            }
        }
        val nextNum = num / thousandDigit
        return if (nextNum > zeroDigit) {
            (num2words(nextNum, level.inc(), currency) + " " + words.toString()).trim { it <= ' ' }
        } else {
            words.toString().trim { it <= ' ' }
        }
    }
    fun inWords(): String {
        if (price < 0.0) return "error: отрицательное значение"
        val sm = price.toString().format("%.2f")
        val kopecks = sm.substring(sm.length.dec().dec(), sm.length)
        val num = floor(price).toLong()
        return num2words(num, 1, wordsRUB) + " " + num2words(kopecks.toLong(), 0, wordsRUB)
    }
}
