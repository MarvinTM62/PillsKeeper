package com.example.pillskeeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent;
import android.view.MenuItem
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.material.bottomnavigation.BottomNavigationView


class EmailActivity : AppCompatActivity() {

    lateinit var mEditTextTo: EditText
    lateinit var mEditTextSubject: EditText
    lateinit var mEditTextMessage: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomMenu)
        bottomNavigation.selectedItemId = R.id.emailmenu
        bottomNavigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId) {
                    R.id.menunav -> openMenuActivity()
                    R.id.emailmenu -> null
                    R.id.menusettings -> openSettingsActivity()
                }
                return true
            }
        })

        mEditTextTo = findViewById(R.id.edit_text_to)
        mEditTextSubject = findViewById(R.id.edit_text_subject)!!
        mEditTextMessage = findViewById(R.id.edit_text_message)!!
        mEditTextSubject.setText("Prescrizione nuovi medicinali")


        val buttonSend = findViewById<Button>(R.id.button_send)
        buttonSend.setOnClickListener {
            sendMail()
        }
    }

    private fun sendMail() {
        val recipientList = mEditTextTo.text.toString()
        val recipients = recipientList.split(",").toTypedArray()
        val subject = mEditTextSubject.text.toString()
        val message = mEditTextMessage.text.toString()

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, recipients)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = "message/rfc822"
        startActivity(Intent.createChooser(intent, "Scegli l'email client"))
    }

    private fun openSettingsActivity() {
        val intent: Intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun openMenuActivity() {
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}




