package com.soten.bmicalculator.view.policy

/**
 * bmi 계산 수치 범위 : 저체중
 * UnderWeight < 18.5
 */
class UnderWeightBMIPolicy : BMIPolicy {
    override fun printBMIInfo(): String {
        return "저체중"
    }

}