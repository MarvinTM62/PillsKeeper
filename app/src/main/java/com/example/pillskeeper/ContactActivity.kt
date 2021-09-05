package com.example.pillskeeper

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.ByteArrayOutputStream

class ContactActivity : AppCompatActivity() {

    var ContactName: ArrayList<String> = ArrayList()
    lateinit var nomeText: EditText
    lateinit var cognomeText: EditText
    lateinit var numeroText: EditText
    lateinit var emailContactText: EditText
    lateinit var aggiungiContactButton: Button
    lateinit var listViewContact : ListView
    lateinit var textViewContact: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        var adapter = MyAdapter()
        listViewContact = findViewById(R.id.listContact)
        listViewContact.adapter = adapter

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
                        else if(numeroText.text.toString() == "" ) {
                            Toast.makeText(this@ContactActivity, "Inserire numero di cellulare", Toast.LENGTH_SHORT ).show()

                        }
                        else if(emailContactText.text.toString() == "" ) {
                            Toast.makeText(this@ContactActivity, "Inserire email", Toast.LENGTH_SHORT ).show()

                        }

                        else {
                            ContactName.add(nomeText.text.toString() + " " + cognomeText.text.toString())
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
            val convertViewContact = layoutInflater.inflate(R.layout.itempersone, parent, false)
            val textViewContact: TextView = convertViewContact.findViewById(R.id.nomePersonaText)
            textViewContact.text = ContactName[position]
            val showInfo = convertViewContact.findViewById<ImageButton>(R.id.showInfo)
            showInfo.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    var dialog: Dialog = Dialog(this@ContactActivity)
                    dialog.setContentView(R.layout.contactpopup)
                    var nomePersona = dialog.findViewById<TextView>(R.id.nomeContattoPop)
                    var cognomePersona = dialog.findViewById<TextView>(R.id.cognomeContattoPop)
                    var cellulareP = dialog.findViewById<TextView>(R.id.cellularePop)
                    var emailP = dialog.findViewById<TextView>(R.id.emailContattoPop)
                    dialog.show()
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
                            ContactName.remove(ContactName[position])
                            listViewContact.adapter = MyAdapter()
                            Toast.makeText(this@ContactActivity, "Eliminazione avvenuta con successo", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Annulla", null).show()
                }
            })
            return convertViewContact
}
    }

}