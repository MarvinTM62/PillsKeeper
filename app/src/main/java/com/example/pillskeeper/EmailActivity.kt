package com.example.pillskeeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent;
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


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

        var arrayList: ArrayList<String>
        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(this@EmailActivity)
        val emptyList = Gson().toJson(ArrayList<String>())
        var json2 = preferenceManager.getString("pillsExpired", emptyList)
        val gson2 = Gson()
        val type = object : TypeToken<ArrayList<String>>() {}.type
        arrayList = gson2.fromJson(json2, type)
        var pills: String = ""
        for(pill in arrayList) {
            pills += "$pill, "
        }
        if(!arrayList.isEmpty()) {
            pills = pills.substring(0, pills.length-2)
        } else {
            pills = "\"al momento non ci sono pillole scadute\""
        }
        mEditTextTo = findViewById(R.id.edit_text_to)
        mEditTextSubject = findViewById(R.id.edit_text_subject)!!
        mEditTextMessage = findViewById(R.id.edit_text_message)!!
        mEditTextTo.setText(PreferenceManager.getDefaultSharedPreferences(this@EmailActivity).getString("emaildoctor", ""))
        mEditTextSubject.setText("Prescrizione nuovi medicinali")
        mEditTextMessage.setText("Buongiorno, avrei bisogno dei seguenti medicinali: " +
                pills + ".\nGrazie mille, buona giornata.")

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
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }

    private fun openMenuActivity() {
        val intent: Intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }

}




