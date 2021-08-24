package com.example.pillskeeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBar

class ReminderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        val addReminderButton: View = findViewById(R.id.addReminderButton)
        val backArrowReminder: View = findViewById(R.id.backArrowReminder)
        addReminderButton.setOnClickListener{
            val intentRemind: Intent = Intent(this, AddReminderActivity::class.java)
            startActivity(intentRemind)
        }
        backArrowReminder.setOnClickListener{
            val intentMain: Intent = Intent(this, MainActivity::class.java)
            startActivity(intentMain)
        }

    }









}