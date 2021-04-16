package com.soten.lotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import kotlin.random.Random

class LottoActivity : AppCompatActivity() {

    private val numberPicker: NumberPicker by lazy {
        findViewById(R.id.numberPicker)
    }

    private val addButton: Button by lazy {
        findViewById(R.id.add_button)
    }

    private val clearButton: Button by lazy {
        findViewById(R.id.clear_button)
    }

    private val autoAddNumberButton: Button by lazy {
        findViewById(R.id.auto_add_number_button)
    }

    private val fixButton: Button by lazy {
        findViewById(R.id.fix_button)
    }

    private val ballList: List<TextView> by lazy {
        listOf(
                findViewById(R.id.first_number),
                findViewById(R.id.second_number),
                findViewById(R.id.third_number),
                findViewById(R.id.fourth_number),
                findViewById(R.id.fifth_number),
                findViewById(R.id.sixth_number)
        )
    }

    private val numberList = ArrayList<Int>()

    private var index = 0

    private var pickNumberList = ArrayList<Int>()


    companion object {
        const val ERROR_ADD_BUTTON = "이미 고른 숫자입니다!!"
        const val ERROR_FIX_BUTTON = "저장할 로또 번호가 없습니다."
        const val INFO_FIX_NUMBER = "고정"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lotto)

        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initButton()
    }

    private fun initButton() {
        initAddButton()

        initAutoAddNumberButton()

        initClearButton()

        initFixButton()
    }

    private fun initFixButton() {
        fixButton.setOnClickListener {
            Log.d("printNum", "number List $numberList")
            if (numberList.isEmpty()) {
                Toast.makeText(this, ERROR_FIX_BUTTON, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            pickNumberList = numberList
            Toast.makeText(this, "$pickNumberList $INFO_FIX_NUMBER", Toast.LENGTH_SHORT).show()
            Log.d("printNum", "set List $pickNumberList")
        }
    }

    private fun initAddButton() {
        addButton.setOnClickListener {
            if (numberList.contains(numberPicker.value)) {
                Toast.makeText(this, ERROR_ADD_BUTTON, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            numberList.add(numberPicker.value)
            ballList[index].isVisible = true
            Log.d("printNum", "number List $numberList")

            val printNumberList = numberList.sorted()
            Log.d("sorted", "$printNumberList")
            for (i in printNumberList.indices) {
                ballList[i].text = printNumberList[i].toString()
                setLottoBall(printNumberList, i)
            }

            index++

            if (numberList.size == 6) {
                addButton.isEnabled = false
            }
        }
    }

    private fun initAutoAddNumberButton() {
        autoAddNumberButton.setOnClickListener {
            val setList = if (pickNumberList.isEmpty()) mutableListOf() else pickNumberList.toMutableList()

            index = 0
            Log.d("printNum", "pick set $pickNumberList")
            Log.d("printNum", "set $setList")

            while (setList.size < 6) {
                val random = Random.nextInt(45) + 1
                if (setList.contains(random)) {
                    continue
                }
                setList.add(random)
            }
            Log.d("printNum", "set $setList")

            val printNumberList = setList.sorted().toList()
            Log.d("printNum", "list $printNumberList")

            isVisibleBall()
            for (i in printNumberList.indices) {
                ballList[i].text = printNumberList[i].toString()
                ballList[i].isVisible = true
                Log.d("printNum", "ball ${ballList[i].text}")
                setLottoBall(printNumberList, i)
            }

            addButton.isEnabled = false
        }

    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            numberList.clear()
            isVisibleBall()
            index = 0
            addButton.isEnabled = true
            pickNumberList.clear()
        }
    }

    private fun isVisibleBall() {
        for (i in ballList) {
            i.isVisible = false
        }
    }

    private fun setLottoBall(printNumberList: List<Int>, i: Int) {
        when (printNumberList[i]) {
            in 1..10 -> ballList[i].background = ContextCompat.getDrawable(this, R.drawable.ball_yellow_1_10)
            in 11..20 -> ballList[i].background = ContextCompat.getDrawable(this, R.drawable.ball_blue_11_20)
            in 21..30 -> ballList[i].background = ContextCompat.getDrawable(this, R.drawable.ball_red_21_30)
            in 31..40 -> ballList[i].background = ContextCompat.getDrawable(this, R.drawable.ball_gray_31_40)
            else -> ballList[i].background = ContextCompat.getDrawable(this, R.drawable.ball_green_41_45)
        }
    }
}
