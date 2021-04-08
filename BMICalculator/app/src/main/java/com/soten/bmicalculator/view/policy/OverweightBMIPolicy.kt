package com.soten.bmicalculator.view.policy

/**
 * bmi 계산 수치 범위 : 과체중
 * 25 <= Overweight < 30
 */
class OverweightBMIPolicy : BMIPolicy {
    override fun printBMIInfo(): String {
        return "과체중"
    }
}