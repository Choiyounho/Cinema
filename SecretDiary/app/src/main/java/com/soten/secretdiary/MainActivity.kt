package com.soten.secretdiary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import com.soten.secretdiary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initNumberPicker()

        binding.openButton.setOnClickListener {
            if (changePasswordMode) {
                Toast.makeText(this, TOAST_CHANGE_PASSWORD_ING, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passwordPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            val passwordFromUser =
                "${binding.numberPicker1.value}${binding.numberPicker2.value}${binding.numberPicker3.value}"

            if (passwordPreferences.getString(SHARED_PREFERENCES_NAME, DEFAULT_PASSWORD).equals(passwordFromUser)) {
                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                showErrorAlertDialog()
            }
        }

        binding.changePasswordButton.setOnClickListener {

            val passwordPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            val passwordFromUser =
                "${binding.numberPicker1.value}${binding.numberPicker2.value}${binding.numberPicker3.value}"

            if (changePasswordMode) {
                passwordPreferences.edit(true) {
                    putString(SHARED_PREFERENCES_NAME, passwordFromUser)
                }

                changePasswordMode = false
                binding.changePasswordButton.setBackgroundColor(Color.BLACK)
            } else {
                if (passwordPreferences.getString(SHARED_PREFERENCES_NAME, DEFAULT_PASSWORD).equals(passwordFromUser)) {
                    changePasswordMode = true
                    Toast.makeText(this, TOAST_CHANGE_PASSWORD, Toast.LENGTH_SHORT).show()
                    binding.changePasswordButton.setBackgroundColor(Color.RED)

                } else {
                    showErrorAlertDialog()
                }
            }
        }
    }

    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle(ALERT_DIALOG_TITLE)
            .setMessage(ALERT_DIALOG_MESSAGE)
            .setPositiveButton(ALERT_DIALOG_POSITIVE) { _, _ -> }
            .create()
            .show()
    }

    private fun initNumberPicker() {
        binding.numberPicker1.minValue = 0
        binding.numberPicker1.maxValue = 9

        binding.numberPicker2.minValue = 0
        binding.numberPicker2.maxValue = 9

        binding.numberPicker3.minValue = 0
        binding.numberPicker3.maxValue = 9
    }

    companion object {
        private const val ALERT_DIALOG_TITLE = "실패!!"
        private const val ALERT_DIALOG_POSITIVE = "확인!!"
        private const val ALERT_DIALOG_MESSAGE = "비밀번호가 잘못되었습니다."

        private const val TOAST_CHANGE_PASSWORD = "변경할 패스워드를 입력해주세요"
        private const val TOAST_CHANGE_PASSWORD_ING = "비밀번호 변경 중입니다"

        private const val DEFAULT_PASSWORD = "000"

        private const val SHARED_PREFERENCES_NAME = "password"
    }
}