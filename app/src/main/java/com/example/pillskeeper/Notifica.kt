package com.example.pillskeeper

import android.text.Editable
import org.w3c.dom.Text
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class Notifica(val nome: String, fOrA: Boolean){
    private val nomeNotifica = nome
    private var isFarmaco = fOrA
    private var dosiConfezione = -1
    private var counterFarmaco = -1
    private var obbligoPrescrizione = false
    private var contattoFarmaco = String()
    private var quantitaFarmaco = -1
    private var oraNotifica = 0
    private var minutoNotifica = 0
    private var giorniNotifica = arrayOf(false, false, false, false, false, false, false)

    fun getNomeNotifica(): String {
        return nome
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

    fun setContattoFarmaco(contact: Contact){
        contattoFarmaco = contact.email
    }

    fun notifyContatto() {
        //notifica il contatto che il farmaco è finito
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


    fun broadcastNotifica(context: Context, intent: Intent){
        val builder = NotificationCompat.Builder(context, "pillskeeper")
            .setSmallIcon(R.drawable.pillola)
                if(!isFarmaco) {
                    builder.setContentTitle(nomeNotifica)
                    if(minutoNotifica <= 9){
                        builder.setStyle(NotificationCompat.BigTextStyle()
                            .bigText("Ricordati di " + nomeNotifica + " alle " + oraNotifica + ":0" + minutoNotifica + "."))
                    } else{
                        builder.setStyle(NotificationCompat.BigTextStyle()
                            .bigText("Ricordati di " + nomeNotifica + " alle " + oraNotifica + ":" + minutoNotifica + "."))
                    }
                } else {
                    builder.setContentTitle("Prendi " + quantitaFarmaco + " " + nomeNotifica)
                    removeQuantitaDaCounter()
                    if(minutoNotifica <= 9){
                        builder.setStyle(NotificationCompat.BigTextStyle()
                            .bigText("Ricordati di prendere " + quantitaFarmaco + " " + nomeNotifica + " alle " + oraNotifica + ":0" + minutoNotifica + "."))
                    } else {
                        builder.setStyle(NotificationCompat.BigTextStyle()
                            .bigText("Ricordati di prendere " + quantitaFarmaco + " " + nomeNotifica + " alle " + oraNotifica + ":" + minutoNotifica + "."))
                    }
                    if(farmacoFinitoNotifica()){
                        val builder2 = NotificationCompat.Builder(context, "pillskeeper")
                            .setSmallIcon(R.drawable.pillola)
                            .setContentTitle(nomeNotifica + " è quasi finito/a.")
                                if(contattoFarmaco.isEmpty()){
                                    builder2.setStyle(NotificationCompat.BigTextStyle()
                                        .bigText("Ricordati di prendere un'altra confezione e resettare la notifica."))
                                } else {
                                    builder2.setStyle(NotificationCompat.BigTextStyle()
                                        .bigText(contattoFarmaco + " è stato/a avvisato/a. Ricordati di resettare la notifica all'arrivo della nuova confezione."))
                                    notifyContatto()
                                }
                        builder2.setPriority(NotificationCompat.PRIORITY_HIGH)
                        val notificationManager2 = NotificationManagerCompat.from(context)
                        notificationManager2.notify(System.currentTimeMillis().toInt(), builder2.build())
                    }

                }
            builder.setPriority(NotificationCompat.PRIORITY_HIGH)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())

    }


}