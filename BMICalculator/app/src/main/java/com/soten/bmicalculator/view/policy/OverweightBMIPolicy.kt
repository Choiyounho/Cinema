package com.soten.bmicalculator.view.policy

/**
 * bmi 계산 수치 범위 : 과체중
 * 25 <= Overweight < 30
 */
class OverweightBMIPolicy : BMIPolicy {

    companion object {
        private const val PRINT_OVERWEIGHT = "과체중"
    }

    override fun printBMIInfo(): String {
        return PRINT_OVERWEIGHT
    }
}