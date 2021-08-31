package com.example.pillskeeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ChooseReminderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_reminder)

        val addFarmacoReminder: View = findViewById(R.id.addFarmacoReminder)
        val addAzioneReminder: View = findViewById(R.id.addAzioneReminder)
        val backArrowReminder2: View = findViewById(R.id.backArrowReminder2)

        addFarmacoReminder.setOnClickListener{
            startActivity(Intent(this, AddReminderActivity::class.java))
        }

        addAzioneReminder.setOnClickListener {
            startActivity(Intent(this, AddAzioneActivity::class.java))
        }

        backArrowReminder2.setOnClickListener{
            startActivity(Intent(this, ReminderActivity::class.java))
        }


    }
}