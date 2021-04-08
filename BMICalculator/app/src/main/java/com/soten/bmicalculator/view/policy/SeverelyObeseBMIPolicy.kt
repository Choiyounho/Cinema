package com.soten.bmicalculator.view.policy

/**
 * bmi 계산 수치 범위 : 고도비만
 * 35 <= SeverelyObese
 */
class SeverelyObeseBMIPolicy : BMIPolicy {

    companion object {
        private const val PRINT_SEVERELY_OBESE = "고도비만"
    }

    override fun printBMIInfo(): String {
        return PRINT_SEVERELY_OBESE
    }
}