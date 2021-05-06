package com.soten.alarm

import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
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

        val model = fetchDataFromSharedPreferences()
        renderView(model)
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
                val model = saveAlarmModel(hour, minute, false)
                renderView(model)

                
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

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            putString(ALARM_KEY, model.makeDateForDB())
            putBoolean(ONOFF_KEY, model.onOff)
            apply()
        }

        return model
    }

    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        val timeDBValue = sharedPreferences.getString(ALARM_KEY, "9:30") ?: "9:30"
        val onOffDBValue = sharedPreferences.getBoolean(ONOFF_KEY, false)
        val alarmData = timeDBValue.split(":")

        val alarmModel = AlarmDisplayModel(
                hour = alarmData[0].toInt(),
                minute = alarmData[1].toInt(),
                onOff = onOffDBValue
        )

//        val pendingIntent = PendingIntent.getBroadcast(
//                this,
//                ALARM_REQUEST_CODE,
//                Intent(this, AlarmReceiver::class.java),
//                PendingIntent.FLAG_NO_CREATE)
//
//        if ((pendingIntent == null) and alarmModel.onOff) {
//            // 알람은 꺼져 있는데 데이터는 켜져있는 경우
//            alarmModel.onOff = false
//        }
//        if ((pendingIntent != null) and alarmModel.onOff.not()) {
//            // 알람은 켜져있는데, 데이터는 꺼져있는 경우 알람을 취소함
//            pendingIntent.cancel()
//        }
        return alarmModel
    }

    private fun renderView(model: AlarmDisplayModel) {
        binding.ampmTextVIew.run {
            text = model.ampmText
        }

        binding.timeTextView.run {
            text = model.timeText
        }

        binding.onOffButton.run {
            text = model.onOffText
            tag = model
        }

    }

    companion object {
        private const val ALARM_KEY = "alarm"
        private const val ONOFF_KEY = "onOff"
        private const val SHARED_PREFERENCES_NAME = "time"

        private const val ALARM_REQUEST_CODE = 1000
    }
}
