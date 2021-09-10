package com.example.pillskeeper

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.util.jar.Manifest

class ContactActivity : AppCompatActivity() {

    private var username: String = ""
    var ContactName: ArrayList<String> = ArrayList()
    var ContactSurname: ArrayList<String> = ArrayList()
    var ContactNumber: ArrayList<String> = ArrayList()
    var ContactEmail: ArrayList<String> = ArrayList()
    var ContactNameOnDatabase: ArrayList<String> = ArrayList()
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

        //Information dialog for first launch
        if(PreferenceManager.getDefaultSharedPreferences(this@ContactActivity).getBoolean("FirstActivityStarts", true)) {
            var dialogInfo: Dialog = Dialog(this@ContactActivity)
            dialogInfo.setContentView(R.layout.dialog_contact_first_launch)
            val buttonOk: Button = dialogInfo.findViewById<Button>(R.id.button_ok)
            buttonOk.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    dialogInfo.dismiss()
                }
            })
            dialogInfo.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogInfo.show()
            val windowInfo: Window? = dialogInfo.getWindow()
            if (windowInfo != null) {
                windowInfo.setLayout(1000, 1000)}
            PreferenceManager.getDefaultSharedPreferences(this@ContactActivity).edit().putBoolean("FirstActivityStarts", false).apply()
        }

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
                    ContactNameOnDatabase.add(ds.key.toString())
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

        val sendEmailToAll = findViewById<ImageButton>(R.id.sendToAll)
        sendEmailToAll.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?) {
                var recipientsList: String = ""
                for(email in ContactEmail) {
                    if (email != "") {recipientsList += "$email, "}
                }
                val recipients = recipientsList.split(",").toTypedArray()

                var arrayList2: ArrayList<String>
                val preferenceManager = PreferenceManager.getDefaultSharedPreferences(this@ContactActivity)
                val emptyList = Gson().toJson(ArrayList<String>())
                var json = preferenceManager.getString("pillsExpired", emptyList)
                val gson = Gson()
                val type = object : TypeToken<ArrayList<String>>() {}.type
                arrayList2 = gson.fromJson(json, type)
                var pills: String = ""
                for(pill in arrayList2) {
                    pills += "$pill, "
                }
                if(!arrayList2.isEmpty()) {
                    pills = pills.substring(0, pills.length-2)
                } else {
                    Toast.makeText(this@ContactActivity, "Al momento non hai farmaci in scadenza", Toast.LENGTH_SHORT).show()
                    return
                }
                var bodyEmail: String =
                    "Ciao, stanno per scadere le confezioni dei seguenti farmaci: $pills.\n Grazie!"

                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_EMAIL, recipients)
                intent.putExtra(Intent.EXTRA_SUBJECT, "Farmaci in scadenza")
                intent.putExtra(Intent.EXTRA_TEXT, pills)
                intent.type = "message/rfc822"
                startActivity(Intent.createChooser(intent, "Scegli l'email client"))
            }
        })
    }


    fun display() {
        var floatingButtonContact: FloatingActionButton = findViewById(R.id.fabContact)
        floatingButtonContact.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                if (username == "Login non effettuato"){
                    val permissionAllert: AlertDialog.Builder = AlertDialog.Builder(this@ContactActivity)
                    permissionAllert.setTitle("Login non effettuato!")
                    permissionAllert.setMessage("Per poter usare questa funzione devi effettuare il login o la registrazione nelle impostazioni")
                    permissionAllert.setIcon(R.drawable.settings_alert)
                    permissionAllert.create().show();
                    return
                }
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
                            var homonymNumber: Int = 1
                            for(name in ContactNameOnDatabase) {
                                if(name == nomeText.text.toString()+" "+cognomeText.text.toString()+ " "+homonymNumber) {
                                    homonymNumber++
                                }
                            }
                            myRef.child(username).child("contacts").child(nomeText.text.toString()+" "+cognomeText.text.toString() + " " + homonymNumber).setValue(Contact(nomeText.text.toString(), cognomeText.text.toString(),
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
                dialogContact.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogContact.show()
            }
        })
        var backArrow = findViewById<ImageButton>(R.id.backArrowContatti)
        backArrow.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
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
                            myRef.child(username).child("contacts").child(ContactNameOnDatabase[position]).removeValue()
                            ContactName.remove(ContactName[position])
                            ContactSurname.remove(ContactSurname[position])
                            ContactNumber.remove(ContactNumber[position])
                            ContactEmail.remove(ContactEmail[position])
                            ContactNameOnDatabase.remove(ContactNameOnDatabase[position])
                            listViewContact.adapter = MyAdapter()
                            Toast.makeText(this@ContactActivity, "Eliminazione avvenuta con successo", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Annulla", null).show()
                }
            })

            val sendEmailToOne = convertViewContact.findViewById<ImageButton>(R.id.sendEmailToOne)
            sendEmailToOne.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    if (ContactEmail[position] == ""){
                        Toast.makeText(this@ContactActivity, "L'e-mail per " + ContactName[position] + " non è stata indicata", Toast.LENGTH_SHORT).show()
                        return
                    }
                    var arrayList: ArrayList<String>
                    val preferenceManager = PreferenceManager.getDefaultSharedPreferences(this@ContactActivity)
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
                        Toast.makeText(this@ContactActivity, "Al momento non hai farmaci in scadenza", Toast.LENGTH_SHORT).show()
                        return
                    }
                    var bodyEmail: String =
                        "Ciao, stanno per scadere le confezioni dei seguenti farmaci: $pills.\n Grazie!"
                    val intent = Intent(Intent.ACTION_SEND)
                    val recipient = ContactEmail[position].split(",").toTypedArray()
                    intent.putExtra(Intent.EXTRA_EMAIL, recipient)
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Farmaci in scadenza")
                    intent.putExtra(Intent.EXTRA_TEXT, bodyEmail)
                    intent.type = "message/rfc822"
                    startActivity(Intent.createChooser(intent, "Scegli l'email client"))
                }
            })

            return convertViewContact
        }
    }

}