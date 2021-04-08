package com.soten.bmicalculator.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.soten.bmicalculator.databinding.ActivityResultBinding
import com.soten.bmicalculator.view.InputActivity.Companion.TALL
import com.soten.bmicalculator.view.policy.BMIPolicy
import com.soten.bmicalculator.view.policy.BMIPolicyFactory
import kotlin.math.pow
import kotlin.math.round

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    companion object {
        private const val DECIMAL_POINT = 10000
        private const val DIVISION_TALL_CM_TO_M = 100
        private const val BMI_POW_CONSTANT = 2.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        val tall = intent.getIntExtra(TALL, 0)
        val kg = intent.getIntExtra(InputActivity.KG, 0)

        val bmi = calculateBMI(tall, kg)

        binding.bmiResultTv.append(bmi.toString())

        applyBMI(bmi)
    }

    private fun applyBMI(bmi: Double) {
        val bmiPolicy = BMIPolicyFactory.from(bmi)
        binding.bmiDescriptionTv.text = bmiPolicy.printBMIInfo()
    }

    private fun calculateBMI(tall: Int, kg: Int): Double {
        return round(kg / (tall.toDouble() / DIVISION_TALL_CM_TO_M)
                .pow(BMI_POW_CONSTANT) * DECIMAL_POINT) / DECIMAL_POINT
    }

}