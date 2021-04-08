package com.soten.bmicalculator.view.policy

/**
 * bmi 계산 수치 범위 : 중도비만
 * 30 <= ModeratelyObese < 35
 */
class ModeratelyObeseBMIPolicy : BMIPolicy {

    companion object {
        private const val PRINT_MODERATELY_OBESE = "중도비만"
    }

    override fun printBMIInfo(): String {
        return PRINT_MODERATELY_OBESE
    }
}