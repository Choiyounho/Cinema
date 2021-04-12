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
                Toast.makeText(this, "비밀번호 변경 중입니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser =
                "${binding.numberPicker1.value}${binding.numberPicker2.value}${binding.numberPicker3.value}"

            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                showErrorAlertDialog()
            }
        }

        binding.changePasswordButton.setOnClickListener {

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser =
                "${binding.numberPicker1.value}${binding.numberPicker2.value}${binding.numberPicker3.value}"

            if (changePasswordMode) {
                passwordPreferences.edit(true) {
                    putString("password", passwordFromUser)
                } // commit은

                changePasswordMode = false
                binding.changePasswordButton.setBackgroundColor(Color.BLACK)
            } else {
                if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
                    binding.changePasswordButton.setBackgroundColor(Color.RED)

                } else {
                    showErrorAlertDialog()
                }
            }
        }
    }

    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패!!")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { _, _ -> }
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
}