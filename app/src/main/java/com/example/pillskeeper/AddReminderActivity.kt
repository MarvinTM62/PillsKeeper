package com.example.pillskeeper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class AddReminderActivity : AppCompatActivity() {
    private val CHANNEL_ID = "channel_id"
    private val notificationID = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        creaCanaleNotifica()

        val calendar: Calendar = Calendar.getInstance()
        val oraLocale = calendar.get(Calendar.HOUR_OF_DAY)
        var oraPicked = oraLocale + 1
        val oraPicker = findViewById<NumberPicker>(R.id.oraPicker)
        if (oraPicker != null) {
            oraPicker.minValue = 0
            oraPicker.maxValue = 23
            oraPicker.value = oraLocale + 1
            oraPicker.wrapSelectorWheel = true
            oraPicker.setOnValueChangedListener { numberPicker, i, i2 -> oraPicked = i2 }
        }

        val minutoLocale = calendar.get(Calendar.MINUTE)
        var minutiPicked = minutoLocale + 1
        val minutiPicker = findViewById<NumberPicker>(R.id.minutiPicker)
        if (minutiPicker != null) {
            minutiPicker.minValue = 0
            minutiPicker.maxValue = 59
            if(minutiPicker.value == 59){
                minutiPicker.value = 0
            } else {
                minutiPicker.value = minutoLocale + 1
            }
            minutiPicker.wrapSelectorWheel = true
            minutiPicker.setOnValueChangedListener { numberPicker, i, i2 -> minutiPicked = i2 }
        }


        val bottoneCreaNotifica: View = findViewById(R.id.bottoneCreaNotifica)
        bottoneCreaNotifica.setOnClickListener{
            val nomeFarmacoText = findViewById<TextView>(R.id.nomeFarmacoNotificaText).text
            val nomeFarmacoString = nomeFarmacoText.toString()
            //get nome from database farmaci
            val quantitaFarmacoText = findViewById<TextView>(R.id.quantitaFarmacoText).text
            val quantitaFarmacoInt = quantitaFarmacoText.toString()
            val giorniList: List<Int>
            val giorniButtonGroup = findViewById<MaterialButtonToggleGroup>(R.id.giorniButtonsGroup)
            if(nomeFarmacoText.isEmpty())
                Toast.makeText(this@AddReminderActivity, "Selezionare farmaco", Toast.LENGTH_SHORT).show()
            if(giorniButtonGroup.checkedButtonIds.isEmpty()){
                Toast.makeText(this@AddReminderActivity, "Selezionare almeno un giorno", Toast.LENGTH_SHORT).show()
            } else if (!nomeFarmacoText.isEmpty()){
                val newNotifica = Notifica(nomeFarmacoString)
                //set counter farmaci rimanenti da database
                if(quantitaFarmacoInt.isEmpty()){
                    newNotifica.setQuantitaFarmaco(0)
                } else {
                    newNotifica.setQuantitaFarmaco(quantitaFarmacoInt.toInt())
                }
                newNotifica.setOraNotifica(oraPicked)
                newNotifica.setMinutoNotifica(minutiPicked)
                giorniList = giorniButtonGroup.checkedButtonIds
                if(giorniList.contains(R.id.buttonLun)){
                    newNotifica.setGiorniNotifica(0)
                }
                if(giorniList.contains(R.id.buttonMar)) {
                    newNotifica.setGiorniNotifica(1)
                }
                if(giorniList.contains(R.id.buttonMer)){
                    newNotifica.setGiorniNotifica(2)
                }
                if(giorniList.contains(R.id.buttonGio)){
                    newNotifica.setGiorniNotifica(3)
                }
                if(giorniList.contains(R.id.buttonVen)){
                    newNotifica.setGiorniNotifica(4)
                }
                if(giorniList.contains(R.id.buttonSab)){
                    newNotifica.setGiorniNotifica(5)
                }
                if(giorniList.contains(R.id.buttonDom)){
                    newNotifica.setGiorniNotifica(6)
                }
                NotificheList.notificheList.add(newNotifica)
                val sharedPrefs = SharedPreferencesNotifiche()
                sharedPrefs.saveNotifiche(this, NotificheList.notificheList)
                startActivity(Intent(this, ReminderActivity::class.java))
//                var builder = NotificationCompat.Builder(this, CHANNEL_ID)
//                    .setSmallIcon(R.drawable.icona_pills_keeper)
//                    .setContentTitle("Prendi " + quantitaFarmaco + " " + nomeFarmacoString)
//                    .setStyle(NotificationCompat.BigTextStyle()
//                        .bigText("Descrizione"))
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                with(NotificationManagerCompat.from(this)){
//                    notify(notificationID, builder.build())
//                }

            }

        }
    }

    private fun creaCanaleNotifica(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val nome = "Notifica PillsKeeper"
            val testoDescrizione = "Notifica della applicazione PillsKeeper"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val canale = NotificationChannel(CHANNEL_ID, nome, importance).apply {
                description = testoDescrizione
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canale)
        }
    }

}