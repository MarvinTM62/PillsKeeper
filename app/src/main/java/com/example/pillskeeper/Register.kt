package com.example.pillskeeper

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var username : String = ""
        var password1: String
        var password2: String

        var database: FirebaseDatabase
        var myRef: DatabaseReference

        val usernameText: EditText = findViewById(R.id.username)
        val password1Text: EditText = findViewById(R.id.password1)
        val password2Text: EditText = findViewById(R.id.password2)

        val signButton: Button = findViewById(R.id.signButton)

        signButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                username = usernameText.text.toString()
                password1 = password1Text.text.toString()
                password2 = password2Text.text.toString()

                if(username.isEmpty() || password1.isEmpty() || password2.isEmpty())
                    Toast.makeText(this@Register,"I campi devono essere riempiti tutti",Toast. LENGTH_SHORT).show()
                else {
                    database = Firebase.database("https://pillskeeper-7e7aa-default-rtdb.europe-west1.firebasedatabase.app/")
                    myRef = database.getReference("user")
                    myRef.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.hasChild(username))
                                Toast.makeText(this@Register,"L'username " + username + " è già utilizzato",Toast. LENGTH_SHORT).show()
                            else if(password1 != password2)
                                Toast.makeText(this@Register,"Le password non combaciano",Toast. LENGTH_SHORT).show()
                            else {
                                myRef.child(username).child("credentials").setValue(Credential(username, password1))
                                usernameText.text.clear()
                                password1Text.text.clear()
                                password2Text.text.clear()
                                Toast.makeText(this@Register,"Registrazione avvenuta con successo!",Toast. LENGTH_SHORT).show()
                                var intent: Intent = Intent()
                                intent.putExtra("usernameResult", username)
                                setResult(RESULT_OK, intent)
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