package com.example.pillskeeper

import android.app.Dialog
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.renderscript.Sampler
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //Information dialog for first launch
        if(PreferenceManager.getDefaultSharedPreferences(this@MainActivity).getBoolean("FirstSmsStart", true)) {
            var dialogInfo: Dialog = Dialog(this@MainActivity)
            dialogInfo.setContentView(R.layout.smspermission)
            val buttonOk: Button = dialogInfo.findViewById<Button>(R.id.button_accetto)
            buttonOk.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    if (ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.SEND_SMS), 100)
                    }
                    dialogInfo.dismiss()
                }
            })
            dialogInfo.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogInfo.show()
            val windowInfo: Window? = dialogInfo.getWindow()
            if (windowInfo != null) {
                windowInfo.setLayout(1000, 900)}
            PreferenceManager.getDefaultSharedPreferences(this@MainActivity).edit().putBoolean("FirstSmsStart", false).apply()
        }

        val buttonLayoutInv: LinearLayout = findViewById(R.id.layoutInv)
        val buttonLayoutNotify: LinearLayout = findViewById(R.id.layoutNotify)
        val buttonLayoutContact: LinearLayout = findViewById(R.id.layoutContact)
        val buttonLayoutMap: LinearLayout = findViewById(R.id.layoutMap)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomMenu)
        bottomNavigation.selectedItemId = R.id.menunav

        bottomNavigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId) {
                    R.id.menunav -> null
                    R.id.emailmenu -> openEmailActivity()
                    R.id.menusettings -> openSettingsActivity()
                }
                return true
            }
        })



        buttonLayoutInv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                openPillsActivity()
            }


        })
        buttonLayoutNotify.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                openReminderActivity()
            }
        })
        buttonLayoutContact.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                openContactActivity()
            }
        })
        buttonLayoutMap.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                openGoogleMapsActivity()
            }
        })
    }

    private fun openGoogleMapsActivity() {
        val intent: Intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    private fun openSettingsActivity() {
        val intent: Intent = Intent(this, SettingsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }

    private fun openReminderActivity() {
        val intent: Intent = Intent(this, ReminderActivity::class.java)
        startActivity(intent)
    }

    private fun openPillsActivity() {
        val intent: Intent = Intent(this, PillsActivity::class.java)
        startActivity(intent)
    }

    private fun openContactActivity(){
        val intent: Intent = Intent(this, ContactActivity::class.java)
        startActivity(intent)
    }

    private fun openEmailActivity() {
        val intent: Intent = Intent(this, EmailActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }

}

