package telegram

public class IntegerToString {
    private val digUnits = arrayOf(
        arrayOf("одна", "две", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"),
        arrayOf("один", "два")
    )
    private val digDozens = arrayOf(
        "десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать",
        "пятнадцать", "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать"
    )
    private val digTwenties = arrayOf(
        "двадцать", "тридцать", "сорок", "пятьдесят",
        "шестьдесят", "семьдесят", "восемьдесят", "девяносто"
    )
    private val digHundreds = arrayOf(
        "сто", "двести", "триста", "четыреста", "пятьсот",
        "шестьсот", "семьсот", "восемьсот", "девятьсот"
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
    private val WordsUSD = arrayOf(
        arrayOf("цент", "цента", "центов", "0"),
        arrayOf("доллар", "доллара", "долларов", "1"),
        digitThousands,
        digitMillions,
        digitBillions,
        digitTrillions
    )

    private val WordsEUR = arrayOf(
        arrayOf("евроцент", "евроцента", "евроцентов", "0"),
        arrayOf("евро", "евро", "евро", "1"),
        digitThousands,
        digitMillions,
        digitBillions,
        digitTrillions
    )

    //todo: перенести в файл со всеми строчками и подключить его сюда:
    object Strings {
        const val USD = "USD"
        const val EUR = "EUR"
        const val RUB = "RUB"
    }

    private fun num2words(num: Long, level: Int, currency: Array<Array<String>>): String {
        val words = StringBuilder(150)
        if (num == 0L) words.append("ноль ")
        val sex = currency[level][3].indexOf("1") + 1
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
            1, 2 -> words.append(digUnits[sex][n - 1]).append(" ")
            else -> words.append(digUnits[0][n - 1]).append(" ")
        }
        when (n) {
            1 -> words.append(currency[level][0])
            2, 3, 4 -> words.append(currency[level][1])
            else -> if ((h != 0) || ((h == 0) && (level == 1)))
                words.append(currency[level][2])
        }
        val nextNum = num / 1000
        return if (nextNum > 0) {
            (num2words(nextNum, level + 1, currency) + " " + words.toString()).trim { it <= ' ' }
        } else {
            words.toString().trim { it <= ' ' }
        }
    }

    fun inWords(money: Double, currency: String): String? {
        if (money < 0.0) return "error: отрицательное значение"
        val sm = String.format("%.2f", money)
        val skop = sm.substring(sm.length - 2, sm.length)
        val iw: Int = when (skop.substring(1)) {
            "1" -> 0
            "2", "3", "4" -> 1
            else -> 2
        }
        val num = Math.floor(money).toLong()
        return if (num < 1000000000000000L && currency == Strings.RUB) {
            num2words(num, 1, WordsRUB) + " " + skop + " " + WordsRUB[0][iw]
        } else if (num < 1000000000000000L && currency == Strings.USD) {
            num2words(num, 1, WordsUSD) + " " + skop + " " + WordsUSD[0][iw]
        } else if (num < 1000000000000000L && currency == Strings.EUR) {
            num2words(num, 1, WordsEUR) + " " + skop + " " + WordsEUR[0][iw]
        } else {
            "error: слишком много денег $skop "
        }
    }
}
