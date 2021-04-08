package com.soten.bmicalculator.view.policy

/**
 * bmi 계산 수치 범위 : 정상
 * 18.5 <= Normal < 25
 */
class NormalBMIPolicy : BMIPolicy {

    companion object {
        private const val PRINT_NORMAL = "정상"
    }

    override fun printBMIInfo(): String {
        return PRINT_NORMAL
    }
}