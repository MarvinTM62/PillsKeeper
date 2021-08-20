package com.example.pillskeeper

import android.content.ClipData
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.view.menu.MenuView
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

        // Write a message to the database
        val database = Firebase.database("https://pillskeeper-7e7aa-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference("user")

        //var moment:Pill = Pill("moment", "immagine")
        //myRef.child("davide").push().setValue(moment)
        myRef.child("davide").child("pills").child("momentact").setValue(Pill("momentact", "immagine"))

        myRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChild("davide"))
                    println("ESATTOOOOOOOOOOO")
                else
                    println("MA CHE STAI A DI'")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        myRef.child("davide").child("pills").child("momentact").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    var sd: String = (snapshot.getValue().toString())
                    sd = sd.substring(1, sd.length-1)
                    println(sd)

                    // for(snapshot2 in snapshot.children())


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



        val buttonLayoutInv: LinearLayout = findViewById(R.id.layoutInv)
        val buttonLayoutNotify: LinearLayout = findViewById(R.id.layoutNotify)
        val buttonLayoutContact: LinearLayout = findViewById(R.id.layoutContact)
        val buttonLayoutMap: LinearLayout = findViewById(R.id.layoutMap)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomMenu)

        bottomNavigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId) {
                    R.id.menunav -> println(1)
                    R.id.emailmenu -> println(2)
                    R.id.menusettings -> println(3)
                }
                return true
            }
        })



        /* buttonSettings.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                setContentView(R.layout.activity_login)
            }


        }) */

        buttonLayoutInv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                println("bottone 1")
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

