package com.example.pillskeeper

import android.telephony.SmsManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.Serializable

class Notifica(nome: String, fOrA: Boolean): Serializable {
    private val nomeNotifica = nome
    private val notificaID = System.currentTimeMillis().toInt()
    private var isFarmaco = fOrA
    private var dosiConfezione = -1
    private var counterFarmaco = -1
    private var obbligoPrescrizione = false
    private var contattoFarmaco = false
    private var quantitaFarmaco = -1
    private var oraNotifica = 0
    private var minutoNotifica = 0
    private var giorniNotifica = arrayOf(false, false, false, false, false, false, false)

    fun getNomeNotifica(): String {
        return nomeNotifica
    }

    fun getNotificaID(): Int {
        return notificaID
    }

    fun isFarmaco(): Boolean {
        return isFarmaco
    }

    fun setCounterFarmaco(n: Int) {
        counterFarmaco = n
        dosiConfezione = n
    }

    fun resetCounterFarmaco(){
        counterFarmaco = dosiConfezione
    }

    fun getCounterFarmaco(): Int {
        return counterFarmaco
    }

    fun setQuantitaFarmaco(n: Int){
        quantitaFarmaco = n
    }

    fun getQuantitaFarmaco(): Int {
        return quantitaFarmaco
    }

    fun farmacoFinitoNotifica(): Boolean {
        if (counterFarmaco <= 4)
            return true
        return false
    }

    fun removeQuantitaDaCounter(){
        counterFarmaco -= quantitaFarmaco
        if (counterFarmaco < 0)
            counterFarmaco = 0
    }

    fun setObbligoPrescrizione(){
        obbligoPrescrizione = true
    }

    fun getObbligoPrescrizione(): Boolean {
        return obbligoPrescrizione
    }

    fun setStatoContattoFarmaco(){
        contattoFarmaco = true
    }

    fun getStatoContattoNotifica(): Boolean {
        return contattoFarmaco
    }

    fun notifyContatti(username: String) {
        var database: FirebaseDatabase = Firebase.database("https://pillskeeper-7e7aa-default-rtdb.europe-west1.firebasedatabase.app/")
        var myRef: DatabaseReference = database.getReference("user")
        var contactNumber: ArrayList<String> = ArrayList<String>()
        val eventListener: ValueEventListener = object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    var number = ds.child("phoneNumber").value.toString()
                    if(number != "")
                        contactNumber.add(number)
                }
                for(number in contactNumber) {
                    var smsManager: SmsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(number, null,
                        "Ciao, sta per scadere la confezione del seguente farmaco: $nomeNotifica.\n Grazie!", null , null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", databaseError.message)
            }
        }
        myRef.child(username).child("contacts").addListenerForSingleValueEvent(eventListener)
    }

    fun setOraNotifica(o: Int){
        oraNotifica = o
    }

    fun getOraNotifica(): Int {
        return oraNotifica
    }

    fun setMinutoNotifica(m: Int){
        minutoNotifica = m
    }

    fun getMinutoNotifica(): Int {
        return minutoNotifica
    }

    fun setGiorniNotifica(g: Int) {
        giorniNotifica[g] = true
    }

    fun getGiorniNotifica(g: Int): Boolean {
       return giorniNotifica[g]
    }

}