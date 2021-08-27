package com.example.pillskeeper

import android.Manifest
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
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
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

class PillsActivity : AppCompatActivity() {

    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
    lateinit var imageViewTest: ImageView
    lateinit var storageReference: StorageReference
     lateinit var photoButton: ImageButton
    //lateinit var listViewInv : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pills2)

        database = Firebase.database("https://pillskeeper-7e7aa-default-rtdb.europe-west1.firebasedatabase.app/")
        myRef = database.getReference("user")
        //imageViewTest = findViewById(R.id.imageviewtest)

        var listViewInv : ListView = findViewById(R.id.listInv)
        var floatingButton: FloatingActionButton = findViewById(R.id.fab)
        floatingButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                var dialog: Dialog = Dialog(this@PillsActivity)
                dialog.setContentView(R.layout.iteminv)

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

                /*var farmname : EditText = findViewById(R.id.farmacoText)
                var photobutton : Button = findViewById(R.id.aggFoto)
                var savebutton : EditText = findViewById(R.id.aggFarmButton)*/
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
            var captureImage: Bitmap = data?.extras?.get("data") as Bitmap
            imageViewTest.setImageBitmap(captureImage)
            val baos = ByteArrayOutputStream()
            captureImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val dataByte = baos.toByteArray()
            myRef.child("gabri").child("pills").setValue(Pill("greve", dataByte))
            storageReference.putBytes(dataByte).addOnSuccessListener { Toast.makeText(this@PillsActivity, "Contenuto caricato", Toast.LENGTH_SHORT ).show() }
                .addOnFailureListener{Toast.makeText(this@PillsActivity, "Errore", Toast.LENGTH_SHORT ).show()}

        }
    }
}