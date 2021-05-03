package com.soten.pushnotifier

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessaging
import com.soten.pushnotifier.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 파이어베이스에서 메세지가 오는 지 확인
        initFirebase()
        updateResult()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent)
        updateResult(true)
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.firebaseTokenTextView.text = task.result
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun updateResult(isNewIntent: Boolean = false) {
        binding.resultTextView.text =
            (intent.getStringExtra("notificationType") ?: "앱 런처 ") +
                    if (isNewIntent) {
                        "으로 갱신했습니다."
                    } else {
                        "실행 했습니다."
                    }
    }
}