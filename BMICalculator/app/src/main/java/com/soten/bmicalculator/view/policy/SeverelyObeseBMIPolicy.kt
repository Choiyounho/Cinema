package com.soten.bmicalculator.view.policy

/**
 * bmi 계산 수치 범위 : 고도비만
 * 35 <= SeverelyObese
 */
class SeverelyObeseBMIPolicy : BMIPolicy {
    override fun printBMIInfo(): String {
        return "고도비만"
    }
}