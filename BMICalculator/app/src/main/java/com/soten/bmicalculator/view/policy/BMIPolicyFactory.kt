package com.soten.bmicalculator.view.policy

/**
 * BMI 수치에 따른 비만도는 WHO 기준으로 함.
 */
class BMIPolicyFactory {
    companion object {
        fun from(bmi: Double): BMIPolicy = when {
            bmi >= 35 -> SeverelyObeseBMIPolicy()
            bmi >= 30 -> ModeratelyObeseBMIPolicy()
            bmi >= 25 -> OverweightBMIPolicy()
            bmi >= 18.5 -> NormalBMIPolicy()
            else -> UnderWeightBMIPolicy()
        }
    }
}