package com.soten.alarm

import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.soten.alarm.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initOnOffButton()
        initChangeAlarmTimeButton()
        // todo step 1 : 뷰 초기화
        // todo step 2 : 데이터 가져오기
        // todo step 2 : 뷰에 데이터 그려주기기
    }

    private fun initOnOffButton() {
        binding.onOffButton.setOnClickListener {
        }
    }

    private fun initChangeAlarmTimeButton() {
        binding.changeAlarmTImeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(this, { picker, hour, minute ->

                saveAlarmModel(hour, minute, false)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
                    .show()
        }
    }

    private fun saveAlarmModel(hour: Int, minute: Int, onOff: Boolean): AlarmDisplayModel {
        val model = AlarmDisplayModel(
                hour = hour,
                minute = minute,
                onOff = onOff
        )

        val sharedPreferences = getSharedPreferences("time", Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            putString("alarm", model.makeDateForDB())
            putBoolean("onOff", model.onOff)
            apply()
        }

        return model
    }
}
