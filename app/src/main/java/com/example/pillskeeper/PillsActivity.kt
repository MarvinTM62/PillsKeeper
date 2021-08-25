package com.example.pillskeeper

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PillsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pills2)

        var database: FirebaseDatabase
        var myRef: DatabaseReference


        database = Firebase.database("https://pillskeeper-7e7aa-default-rtdb.europe-west1.firebasedatabase.app/")
        myRef = database.getReference("user")

        var auth: FirebaseAuth = FirebaseAuth.getInstance()
        var storageReference: StorageReference = FirebaseStorage.getInstance().getReference("User/"+ auth.currentUser?.uid)
        var imageUri : Uri = Uri.parse("android.resource://$packageName/${R.drawable.aggiungi_farmaco}")
        storageReference.putFile(imageUri).addOnSuccessListener { Toast.makeText(this@PillsActivity, "Contenuto caricato", Toast.LENGTH_SHORT ).show() }
            .addOnFailureListener{Toast.makeText(this@PillsActivity, "Errore", Toast.LENGTH_SHORT ).show()}

    }
}