package com.soten.bmicalculator.view.policy

/**
 * BMI 수치에 따른 비만도는 WHO 기준으로 함.
 */
object BMIPolicyFactory {

    private const val SEVERELY_OBESE_RANGE = 35
    private const val MODERATELY_OBESE_RANGE = 30
    private const val OVERWEIGHT_RANGE = 25
    private const val NORMAL_RANGE = 18.5

    fun from(bmi: Double): BMIPolicy = when {
        bmi >= SEVERELY_OBESE_RANGE -> SeverelyObeseBMIPolicy()
        bmi >= MODERATELY_OBESE_RANGE -> ModeratelyObeseBMIPolicy()
        bmi >= OVERWEIGHT_RANGE -> OverweightBMIPolicy()
        bmi >= NORMAL_RANGE -> NormalBMIPolicy()
        else -> UnderWeightBMIPolicy()
    }

}