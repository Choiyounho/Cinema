package com.soten.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.soten.alarm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initOnOffButton()
        initChangeAlramTimeButton()
        // todo step 1 : 뷰 초기화
        // todo step 2 : 데이터 가져오기
        // todo step 2 : 뷰에 데이터 그려주기기
    }

    private fun initOnOffButton() {
        TODO("Not yet implemented")
    }

    private fun initChangeAlramTimeButton() {
        TODO("Not yet implemented")
    }
}
