package com.example.pillskeeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonLayoutInv: LinearLayout = findViewById(R.id.layoutInv)
        val buttonLayoutNotify: LinearLayout = findViewById(R.id.layoutNotify)
        val buttonLayoutContact: LinearLayout = findViewById(R.id.layoutContact)
        val buttonLayoutMap: LinearLayout = findViewById(R.id.layoutMap)

        buttonLayoutInv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                println("Bottone 1")
            }


        })
        buttonLayoutNotify.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                println("Bottone 2")
            }
        })
        buttonLayoutContact.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                /* if(preferences.getBoolean("Contact permission", false) == false) {
                    val permissionAllert3: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                    permissionAllert3.setTitle("Permessi non attivi!")
                    permissionAllert3.setMessage("Per poter usare questa funzione devi consentire all'applicazione di accedere ai tuoi contatti nelle impostazioni")
                    permissionAllert3.setIcon(R.drawable.contatti_farmaco)
                    permissionAllert3.create().show();
                } else */
                    println("Bottone 3")
            }
        })
        buttonLayoutMap.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                /* if(preferences.getBoolean("Localitation permission", false) == false) {
                    val permissionAllert4: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                    permissionAllert4.setTitle("Permessi non attivi!")
                    permissionAllert4.setMessage("Per poter usare questa funzione devi attivare i permessi di localizzazione nelle impostazioni")
                    permissionAllert4.setIcon(R.drawable.googlemaps_icon)
                    permissionAllert4.create().show();
                } else */
                    openGoogleMapsActivity()
            }
        })
    }

    private fun openGoogleMapsActivity() {
        val intent: Intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}

