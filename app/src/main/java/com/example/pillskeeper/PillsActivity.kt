package com.example.pillskeeper

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import android.widget.TextView




class PillsActivity : AppCompatActivity() {

    var pillsName : ArrayList<String> = ArrayList()
    var pillsImage : ArrayList<Bitmap> = ArrayList()
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
    lateinit var storageReference: StorageReference
    lateinit var photoButton: ImageButton
    lateinit var farmName: EditText
    lateinit var saveButton: Button
    lateinit var captureImage: Bitmap
    private var username: String = ""
    lateinit var itemFarmaci: List<CardView>
    lateinit var listViewInv : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pills2)

        pillsName.add("Moment")
        pillsName.add("Oki")
        pillsName.add("asdhjk")
        pillsName.add("dsadsad")
        pillsName.add("asdoiu")
        pillsName.add("asdkjfv")
        listViewInv = findViewById(R.id.listInv)
        var adapter : MyAdapter = MyAdapter()
        listViewInv.adapter = adapter

        username = PreferenceManager.getDefaultSharedPreferences(this@PillsActivity).getString("username", "Login non effettuato")!!
        database = Firebase.database("https://pillskeeper-7e7aa-default-rtdb.europe-west1.firebasedatabase.app/")
        myRef = database.getReference("user")
        storageReference = FirebaseStorage.getInstance().getReference("User/")


        var listViewInv : ListView = findViewById(R.id.listInv)
        var floatingButton: FloatingActionButton = findViewById(R.id.fab)
        floatingButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (username == "Login non effettuato"){
                    val permissionAllert: AlertDialog.Builder = AlertDialog.Builder(this@PillsActivity)
                    permissionAllert.setTitle("Login non effettuato!")
                    permissionAllert.setMessage("Per poter usare questa funzione devi effettuare il login o la registrazione nelle impostazioni")
                    permissionAllert.setIcon(R.drawable.settings_alert)
                    permissionAllert.create().show();
                    return
                }
                var dialog: Dialog = Dialog(this@PillsActivity)
                dialog.setContentView(R.layout.iteminv)

                farmName = dialog.findViewById<EditText>(R.id.farmacoText)

                photoButton = dialog.findViewById<ImageButton>(R.id.photoButton)
                photoButton.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(p0: View?) {
                        if(ContextCompat.checkSelfPermission(this@PillsActivity,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this@PillsActivity,
                                Array(100) {
                                    Manifest.permission.CAMERA
                                }, 100)
                        }
                        var intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent, 100)
                    }
                })

                saveButton = dialog.findViewById<Button>(R.id.aggFarmButton)
                saveButton.setOnClickListener(object: View.OnClickListener{
                    override fun onClick(p0: View?) {
                        if(farmName.text.toString() == "" ) {
                            Toast.makeText(this@PillsActivity, "Inserire nome farmaco", Toast.LENGTH_SHORT ).show()

                        }

                        else if (captureImage == null){
                            Toast.makeText(this@PillsActivity, "Foto farmaco mancante", Toast.LENGTH_SHORT ).show()
                        }
                        else {
                            val baos = ByteArrayOutputStream()
                            captureImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                            val dataByte = baos.toByteArray()
                            storageReference.child(username).child(farmName.text.toString()).putBytes(dataByte).addOnSuccessListener { Toast.makeText(this@PillsActivity, "Contenuto caricato", Toast.LENGTH_SHORT ).show() }
                                .addOnFailureListener{Toast.makeText(this@PillsActivity, "Errore", Toast.LENGTH_SHORT ).show()}
                            dialog.dismiss()
                        }
                    }
                })

                dialog.show()
                val window: Window? = dialog.getWindow()
                if (window != null) {
                    window.setLayout(1000, 1000)
                }
            }
        })



        /*var auth: FirebaseAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().getReference("User/"+ auth.currentUser?.uid)
        val ONE_MEGABYTE: Long = 1024 * 1024
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(object :
            OnSuccessListener<ByteArray> {
            override fun onSuccess(bytearray: ByteArray?) {
                var bitmap: Bitmap = BitmapFactory.decodeByteArray(bytearray, 0, bytearray?.size!!)
                imageViewTest.setImageBitmap(bitmap)
            }
        })*/


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100) {
            captureImage = data?.extras?.get("data") as Bitmap
            /*val baos = ByteArrayOutputStream()
            captureImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val dataByte = baos.toByteArray()
            storageReference.putBytes(dataByte).addOnSuccessListener { Toast.makeText(this@PillsActivity, "Contenuto caricato", Toast.LENGTH_SHORT ).show() }
                .addOnFailureListener{Toast.makeText(this@PillsActivity, "Errore", Toast.LENGTH_SHORT ).show()}*/

        }
    }



    inner class MyAdapter : BaseAdapter() {

        override fun getCount(): Int {
            return pillsName.size
        }

        override fun getItem(p0: Int): Any? {
            return null
        }

        override fun getItemId(p0: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val convertView2 = layoutInflater.inflate(R.layout.itemfarmaci, parent, false)
            val textView: TextView = convertView2.findViewById(R.id.nomeFarmText)
            textView.text = pillsName[position]
            return convertView2
        }

    }
}



