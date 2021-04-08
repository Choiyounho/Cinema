package com.soten.bmicalculator.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.soten.bmicalculator.databinding.ActivityResultBinding
import kotlin.math.pow
import kotlin.math.round

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val tall = intent.getIntExtra("tall", 0)
        val kg = intent.getIntExtra("kg", 0)

        val bmi = bmiCalculate(tall, kg)

        binding.bmiResultTv.append(bmi.toString())

        binding.bmiDescriptionTv.text = description(bmi)
    }

    private fun bmiCalculate(tall: Int, kg: Int): Double {
        return round(kg / (tall.toDouble() / 100).pow(2.0) * 10000) / 10000
    }

    private fun description(bmi: Double): String = when {
        bmi >= 35 -> "고도 비만"
        bmi >= 30 -> "중도비만"
        bmi >= 25 -> "경도비만"
        bmi >= 23 -> "과제충"
        bmi >= 18.5 -> "정상"
        else -> "저체중"
    }

}