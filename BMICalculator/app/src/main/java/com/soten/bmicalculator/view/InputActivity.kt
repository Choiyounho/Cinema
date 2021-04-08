package com.soten.bmicalculator.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.soten.bmicalculator.databinding.ActivityInputBinding

class InputActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.bmiBtn.setOnClickListener {
            buttonClick()
        }

        binding.kgEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                buttonClick()
            }
            false
        }

    }

    private fun buttonClick() {
        if (binding.tallEt.text.isEmpty() || binding.kgEt.text.isEmpty()) {
            Log.d("bmi", "토스트")
            Toast.makeText(applicationContext, "키와 몸무게 모두 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val tall = binding.tallEt.text.toString().toInt()
        val kg = binding.kgEt.text.toString().toInt()

        val intent = Intent(this@InputActivity, ResultActivity::class.java)
        intent.putExtra("tall", tall)
        intent.putExtra("kg", kg)
        startActivity(intent)
    }

}