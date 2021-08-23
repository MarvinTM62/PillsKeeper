package com.example.pillskeeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {

    lateinit var usernameView : TextView
    lateinit var buttonRegister: Button
    lateinit var buttonLogin: Button
    lateinit var buttonAccedi: Button
    val REGISTER_ACTIVITY_REQUEST_CODE = 1
    var username: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        var bottomNavigationView: BottomNavigationView= findViewById(R.id.bottomMenu)
        bottomNavigationView.selectedItemId = R.id.menusettings
        bottomNavigationView.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId) {
                    R.id.menunav -> openMenuActivity()
                    R.id.emailmenu -> openLoginActivity()
                    R.id.menusettings -> null
                }
                return true
            }
        })

        usernameView = findViewById(R.id.usernameTextView)
        buttonRegister = findViewById(R.id.registratiButtonSettings)
        buttonLogin = findViewById(R.id.loginButtonSettings)
        buttonAccedi = findViewById(R.id.accediEmailSettingsButton)



        buttonRegister.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                openRegisterActivity()
            }
        })

        buttonLogin.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
               openLoginActivity()
            }
        })

        buttonAccedi.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {

            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REGISTER_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                if (data != null) {
                    username = data.getStringExtra("usernameResult")
                }
            }

        }
       if (username != ""){
           usernameView.setText(username)
           buttonRegister.visibility = View.GONE
           buttonLogin.setText("LOG OUT")
       }
    }

    private fun openLoginActivity() {
        val intent: Intent = Intent(this, Login::class.java)
        startActivityForResult(intent, 1)
    }
    private fun openMenuActivity() {
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivityForResult(intent, 1)
    }

    private fun openRegisterActivity() {
        val intent: Intent = Intent(this, Register::class.java)
        startActivityForResult(intent, REGISTER_ACTIVITY_REQUEST_CODE)
    }
}