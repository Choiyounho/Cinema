package com.soten.bmicalculator.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.soten.bmicalculator.databinding.ActivityResultBinding
import com.soten.bmicalculator.view.policy.BMIPolicyFactory
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

        val bmi = calculateBMI(tall, kg)

        binding.bmiResultTv.append(bmi.toString())

        binding.bmiDescriptionTv.text = BMIPolicyFactory.from(bmi).printBMIInfo()
    }

    private fun calculateBMI(tall: Int, kg: Int): Double {
        return round(kg / (tall.toDouble() / 100).pow(2.0) * 10000) / 10000
    }

}