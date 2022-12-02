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


    private val WordsRUB = arrayOf(
        arrayOf("копейка", "копейки", "копеек", "0"),
        arrayOf("рубль", "рубля", "рублей", "1"),
        digitThousands,
        digitMillions,
        digitBillions,
        digitTrillions
    )

    private fun num2words(num: Long, level: Int, currency: Array<Array<String>>): String {
        val words = StringBuilder(255)
        if (num == 0L) words.append("ноль ")
        val i = currency[level][3].indexOf("1") + 1
        val h = (num % 1000).toInt()
        var d = h / 100
        if (d > 0) words.append(digHundreds[d - 1]).append(" ")
        var n = h % 100
        d = n / 10
        n %= 10
        when (d) {
            0 -> {}
            1 -> words.append(digDozens[n]).append(" ")
            else -> words.append(digTwenties[d - 2]).append(" ")
        }
        if (d == 1) n = 0
        when (n) {
            0 -> {}
            1, 2 -> words.append(digUnits[i][n - 1]).append(" ")
            else -> words.append(digUnits[0][n - 1]).append(" ")
        }
        when (n) {
            1 -> words.append(currency[level][0])
            2, 3, 4 -> words.append(currency[level][1])
            else -> if ((h != 0) || ((h == 0) && (level == 1))) words.append(currency[level][2])
        }
        val nextNum = num / 1000
        return if (nextNum > 0) {
            (num2words(nextNum, level + 1, currency) + " " + words.toString()).trim { it <= ' ' }
        } else {
            words.toString().trim { it <= ' ' }
        }
    }

    fun inWords(): String {
        if (price < 0.0) return "error: отрицательное значение"
        val sm = String.format("%.2f", price)
        val skop = sm.substring(sm.length - 2, sm.length)
        val iw: Int = when (skop.substring(1)) {
            "1" -> 0
            "2", "3", "4" -> 1
            else -> 2
        }
        val num = floor(price).toLong()
        return num2words(num, 1, WordsRUB) + " " + skop + " " + WordsRUB[0][iw]
    }
}
