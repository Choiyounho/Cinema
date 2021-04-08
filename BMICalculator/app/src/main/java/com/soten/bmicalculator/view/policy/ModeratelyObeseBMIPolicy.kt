package com.soten.bmicalculator.view.policy

/**
 * bmi 계산 수치 범위 : 중도비만
 * 30 <= ModeratelyObese < 35
 */
class ModeratelyObeseBMIPolicy : BMIPolicy {
    override fun printBMIInfo(): String {
        return "중도비만"
    }
}