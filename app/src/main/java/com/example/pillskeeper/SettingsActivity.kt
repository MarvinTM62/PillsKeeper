package com.example.pillskeeper

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.InputType
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class SettingsActivity : AppCompatActivity() {

    private var username: String = ""
    private var emailDoctor: String = ""

    lateinit var usernameView : TextView
    lateinit var buttonRegister: Button
    lateinit var buttonLogin: Button
    lateinit var buttonRichiedi: Button
    lateinit var doctorText: EditText
    val REGISTER_ACTIVITY_REQUEST_CODE = 1
    val LOGIN_ACTIVITY_REQUEST_CODE = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        username = PreferenceManager.getDefaultSharedPreferences(this@SettingsActivity).getString("username", "Login non effettuato")!!
        emailDoctor = PreferenceManager.getDefaultSharedPreferences(this@SettingsActivity).getString("emaildoctor", "")!!

        var bottomNavigationView: BottomNavigationView= findViewById(R.id.bottomMenu)
        bottomNavigationView.selectedItemId = R.id.menusettings
        bottomNavigationView.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId) {
                    R.id.menunav -> openMenuActivity()
                    R.id.emailmenu -> openEmailActivity()
                    R.id.menusettings -> null
                }
                return true
            }
        })

        usernameView = findViewById(R.id.usernameTextView)
        buttonRegister = findViewById(R.id.registratiButtonSettings)
        buttonLogin = findViewById(R.id.loginButtonSettings)
        doctorText = findViewById(R.id.doctorEmail)
        if (username != "Login non effettuato"){
            usernameView.setText(username)
            buttonRegister.visibility = View.INVISIBLE
            buttonRegister.isEnabled = false
            buttonLogin.setText("LOG OUT")
        }
        doctorText.setText(emailDoctor)


        buttonRegister.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                openRegisterActivity()
            }
        })

        buttonLogin.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                if (username == "Login non effettuato"){
                    openLoginActivity()
                } else {
                    buttonLogin.setText("LOG IN")
                    usernameView.setText("Login non effettuato")
                    buttonRegister.visibility = View.VISIBLE
                    buttonRegister.isEnabled = true
                    username = "Login non effettuato"
                    PreferenceManager.getDefaultSharedPreferences(this@SettingsActivity).edit().putString("username", username).apply()
                }


            }
        })

        doctorText.setOnEditorActionListener(object: OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    emailDoctor = doctorText.text.toString()
                    PreferenceManager.getDefaultSharedPreferences(this@SettingsActivity).edit().putString("emaildoctor", emailDoctor).apply()
                    val inputManager: InputMethodManager =
                        this@SettingsActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(
                        currentFocus?.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                    handled = true;
                }
                return handled
            }
        })

        buttonRichiedi = findViewById(R.id.richiedi_button)
        buttonRichiedi.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                if (ContextCompat.checkSelfPermission(this@SettingsActivity, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED ){
                    Toast.makeText(this@SettingsActivity, "I permessi sono già stati accettati", Toast.LENGTH_SHORT).show()
                }
                else{
                    ActivityCompat.requestPermissions(this@SettingsActivity, arrayOf(android.Manifest.permission.SEND_SMS), 100)}
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REGISTER_ACTIVITY_REQUEST_CODE || requestCode == LOGIN_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                if (data != null) {
                    username = data.getStringExtra("usernameResult")
                }
            }
        }
       if (username != "Login non effettuato"){
           usernameView.setText(username)
           buttonRegister.visibility = View.INVISIBLE
           buttonRegister.isEnabled = false
           buttonLogin.setText("LOG OUT")
       }
    }

    private fun openLoginActivity() {
        val intent: Intent = Intent(this, Login::class.java)
        startActivityForResult(intent, 1)
    }
    private fun openMenuActivity() {
        val intent: Intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }

    private fun openRegisterActivity() {
        val intent: Intent = Intent(this, Register::class.java)
        startActivityForResult(intent, REGISTER_ACTIVITY_REQUEST_CODE)
    }

    private fun openEmailActivity() {
        val intent: Intent = Intent(this, EmailActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }
}