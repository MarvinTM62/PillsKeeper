package com.example.pillskeeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var username: String
        var password: String

        var database: FirebaseDatabase
        var myRef: DatabaseReference

        val usernameText: EditText = findViewById(R.id.loginusername)
        val passwordText: EditText = findViewById(R.id.loginpassword)

        val accediButton: Button = findViewById(R.id.accediButton)

        accediButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                username = usernameText.text.toString()
                password = passwordText.text.toString()

                if(username.isEmpty() || password.isEmpty())
                    Toast.makeText(this@Login,"I campi devono essere riempiti tutti", Toast. LENGTH_SHORT).show()
                else {
                    database = Firebase.database("https://pillskeeper-7e7aa-default-rtdb.europe-west1.firebasedatabase.app/")
                    myRef = database.getReference("user")
                    myRef.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(!snapshot.hasChild(username))
                                Toast.makeText(this@Login,"L'username " + username + " non esiste",
                                    Toast. LENGTH_SHORT).show()
                            else {
                                var passwordUser: String = snapshot.child(username).child("credentials").getValue().toString()
                                passwordUser = passwordUser.substring(1, passwordUser.length-1)
                                if(passwordUser != "password="+password+", name="+username) {
                                    Toast.makeText(this@Login,"La password è errata", Toast.LENGTH_SHORT).show()
                                    passwordText.text.clear()
                                } else {
                                    finish()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }
        })

    }
}