package com.soten.bmicalculator.view.policy

/**
 * bmi 계산 수치 범위 : 저체중
 * UnderWeight < 18.5
 */
class UnderWeightBMIPolicy : BMIPolicy {

    companion object {
        private const val PRINT_UNDERWEIGHT = "저체중"
    }

    override fun printBMIInfo(): String {
        return PRINT_UNDERWEIGHT
    }

}