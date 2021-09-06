package com.example.pillskeeper

import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.util.jar.Manifest

class ContactActivity : AppCompatActivity() {

    private var username: String = ""
    var ContactName: ArrayList<String> = ArrayList()
    var ContactSurname: ArrayList<String> = ArrayList()
    var ContactNumber: ArrayList<String> = ArrayList()
    var ContactEmail: ArrayList<String> = ArrayList()
    lateinit var nomeText: EditText
    lateinit var cognomeText: EditText
    lateinit var numeroText: EditText
    lateinit var emailContactText: EditText
    lateinit var aggiungiContactButton: Button
    lateinit var listViewContact : ListView
    lateinit var textViewContact: TextView
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
    var positionGlobal: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        username = PreferenceManager.getDefaultSharedPreferences(this@ContactActivity).getString("username", "Login non effettuato")!!
        database = Firebase.database("https://pillskeeper-7e7aa-default-rtdb.europe-west1.firebasedatabase.app/")
        myRef = database.getReference("user")

        val eventListener: ValueEventListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    ContactName.add(ds.child("name").value.toString())
                    ContactSurname.add(ds.child("surname").value.toString())
                    ContactNumber.add(ds.child("phoneNumber").value.toString())
                    ContactEmail.add(ds.child("email").value.toString())
                }
                var adapter = MyAdapter()
                listViewContact = findViewById(R.id.listContact)
                listViewContact.adapter = adapter
                display()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", databaseError.message)
            }
        }
        myRef.child(username).child("contacts").addListenerForSingleValueEvent(eventListener)
    }


    fun display() {
        var floatingButtonContact: FloatingActionButton = findViewById(R.id.fabContact)
        floatingButtonContact.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                var dialogContact: Dialog = Dialog(this@ContactActivity)
                dialogContact.setContentView(R.layout.itemcontact)

                nomeText = dialogContact.findViewById<EditText>(R.id.nomeContattoText)
                cognomeText = dialogContact.findViewById<EditText>(R.id.cognomeContattoText)
                numeroText = dialogContact.findViewById<EditText>(R.id.cellulareText)
                emailContactText = dialogContact.findViewById<EditText>(R.id.emailContattoText)
                aggiungiContactButton = dialogContact.findViewById<Button>(R.id.aggContattoButton)

                aggiungiContactButton.setOnClickListener(object: View.OnClickListener{
                    override fun onClick(p0: View?) {
                        if(nomeText.text.toString() == "" ) {
                            Toast.makeText(this@ContactActivity, "Inserire nome", Toast.LENGTH_SHORT ).show()

                        }
                        else if(cognomeText.text.toString() == "" ) {
                            Toast.makeText(this@ContactActivity, "Inserire cognome", Toast.LENGTH_SHORT ).show()

                        }
                        else if(numeroText.text.toString() == "" && emailContactText.text.toString() == "" ) {
                            Toast.makeText(this@ContactActivity, "Inserire numero di cellulare o email", Toast.LENGTH_SHORT ).show()
                        }
                        else {
                            myRef.child(username).child("contacts").child(nomeText.text.toString()+" "+cognomeText.text.toString()).setValue(Contact(nomeText.text.toString(), cognomeText.text.toString(),
                                numeroText.text.toString(), emailContactText.text.toString()))
                            ContactName.add(nomeText.text.toString())
                            ContactSurname.add(cognomeText.text.toString())
                            ContactNumber.add((numeroText.text.toString()))
                            ContactEmail.add(emailContactText.text.toString())
                            listViewContact.adapter = null
                            listViewContact.adapter = MyAdapter()
                            dialogContact.dismiss()
                            Toast.makeText(this@ContactActivity, "Contatto salvato", Toast.LENGTH_SHORT ).show()
                        }
                    }
                })

                dialogContact.show()
            }
        })
    }


    inner class MyAdapter : BaseAdapter() {

        override fun getCount(): Int {
            return ContactName.size
        }

        override fun getItem(p0: Int): Any? {
            return null
        }

        override fun getItemId(p0: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            positionGlobal = position
            val convertViewContact = layoutInflater.inflate(R.layout.itempersone, parent, false)
            val textViewContact: TextView = convertViewContact.findViewById(R.id.nomePersonaText)
            textViewContact.text = ContactName[position] + " " + ContactSurname[position]
            val showInfo = convertViewContact.findViewById<ImageButton>(R.id.showInfo)
            showInfo.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    var dialog: Dialog = Dialog(this@ContactActivity)
                    dialog.setContentView(R.layout.contactpopup)
                    var nomePersona = dialog.findViewById<TextView>(R.id.nomeContattoPop)
                    var cognomePersona = dialog.findViewById<TextView>(R.id.cognomeContattoPop)
                    var cellulareP = dialog.findViewById<TextView>(R.id.cellulareContattoPop)
                    var emailP = dialog.findViewById<TextView>(R.id.emailContattoPop)
                    var titoloDialog = dialog.findViewById<TextView>(R.id.titoloDialogContact)
                    nomePersona.text = ContactName[position]
                    cognomePersona.text = ContactSurname[position]
                    cellulareP.text = ""+ContactNumber[position]
                    emailP.text = ContactEmail[position]
                    titoloDialog.text = "INFORMAZIONI SU " + ContactName[position].toUpperCase()
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.show()
                    val window: Window? = dialog.getWindow()
                    if (window != null) {
                        window.setLayout(1000, 800)
                    }
                }
            })

            val deleteContact = convertViewContact.findViewById<ImageButton>(R.id.deleteContatto)
            deleteContact.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    AlertDialog.Builder(this@ContactActivity)
                        .setTitle("Elimina contatto")
                        .setMessage("Vuoi eliminare il contatto definitivamente?")
                        .setIcon(R.drawable.ic_baseline_warning_contact)
                        .setPositiveButton("Sì") {
                                dialog, whichButton ->
                            myRef.child(username).child("contacts").child(ContactName[position]+" "+ContactSurname[position]).removeValue()
                            ContactName.remove(ContactName[position])
                            ContactSurname.remove(ContactSurname[position])
                            ContactNumber.remove(ContactNumber[position])
                            ContactEmail.remove(ContactEmail[position])
                            listViewContact.adapter = MyAdapter()
                            Toast.makeText(this@ContactActivity, "Eliminazione avvenuta con successo", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Annulla", null).show()
                }
            })

            val sendSms = convertViewContact.findViewById<ImageButton>(R.id.sendMessage)
            sendSms.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    if (ContextCompat.checkSelfPermission(this@ContactActivity, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED ){
                        sendMessage()
                    }
                    else{ActivityCompat.requestPermissions(this@ContactActivity, arrayOf(android.Manifest.permission.SEND_SMS), 100)}
                }
            })

            return convertViewContact
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            sendMessage()
        } else{Toast.makeText(this@ContactActivity, "Permessi rifiutati", Toast.LENGTH_SHORT).show()}
    }

    private fun sendMessage() {

        if (ContactNumber[positionGlobal] != ""){
            var smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(ContactNumber[positionGlobal], null, "prova", null , null)
            Toast.makeText(this@ContactActivity, "messaggio inviato", Toast.LENGTH_SHORT).show()
        }

        else {Toast.makeText(this@ContactActivity, "il numero non è stato indicato", Toast.LENGTH_SHORT).show()}

    }

}