package com.example.pillskeeper

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.button.MaterialButtonToggleGroup
import java.util.*


class AddReminderActivity : AppCompatActivity() {
    val CHANNEL_ID = "pillskeeper"
    val CHANNEL_NAME = "pillskeeper_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        creaCanaleNotifica()

        var calendar: Calendar = Calendar.getInstance()
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

        //setOnClicklistener per selezionare farmaco e prendere stringa e quantita confezione
        //val nome = etc
        //val confezione = etc

        val bottoneCreaNotifica: View = findViewById(R.id.bottoneCreaNotifica)
        bottoneCreaNotifica.setOnClickListener{
            val nomeFarmacoText = findViewById<TextView>(R.id.nomeFarmacoNotificaText).text
            val nomeFarmacoString = nomeFarmacoText.toString()
//            set nome from database farmaci
//            val nomeFarmacoString = nome
            val quantitaFarmacoText = findViewById<TextView>(R.id.quantitaFarmacoText).text
            val quantitaFarmacoString = quantitaFarmacoText.toString()
            val contattiCheck = findViewById<CheckBox>(R.id.contattiCheck)
            val giorniList: List<Int>
            val giorniButtonGroup = findViewById<MaterialButtonToggleGroup>(R.id.giorniButtonsGroup)
            if(nomeFarmacoText.isEmpty())
                Toast.makeText(this@AddReminderActivity, "Selezionare farmaco", Toast.LENGTH_SHORT).show()
            if(quantitaFarmacoText.isEmpty())
                Toast.makeText(this@AddReminderActivity, "Impostare quantità da assumere", Toast.LENGTH_SHORT).show()
            if(giorniButtonGroup.checkedButtonIds.isEmpty()){
                Toast.makeText(this@AddReminderActivity, "Selezionare almeno un giorno", Toast.LENGTH_SHORT).show()
            } else if (!nomeFarmacoText.isEmpty() && !quantitaFarmacoText.isEmpty() && !giorniButtonGroup.checkedButtonIds.isEmpty()){
                val newNotifica = Notifica(nomeFarmacoString, true)
//                set dosi confezione da database
//                newNotifica.setCounterFarmaco(confezione)
                if(!quantitaFarmacoString.isEmpty()){
                    newNotifica.setQuantitaFarmaco(quantitaFarmacoString.toInt())
                }
                if (contattiCheck.isChecked) {
                    newNotifica.setStatoContattoFarmaco()
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
                val intent = Intent(this, BroadcastNotifica::class.java)
                intent.putExtra("ID", newNotifica.getNotificaID())
                val pendingIntent = PendingIntent.getBroadcast(this, newNotifica.getNotificaID(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
                if(newNotifica.getGiorniNotifica(0)) {
                    setAllarmeGiorno(newNotifica.getOraNotifica(), newNotifica.getMinutoNotifica(), Calendar.MONDAY, pendingIntent)
                }
                if(newNotifica.getGiorniNotifica(1)) {
                    setAllarmeGiorno(newNotifica.getOraNotifica(), newNotifica.getMinutoNotifica(), Calendar.TUESDAY, pendingIntent)
                }
                if(newNotifica.getGiorniNotifica(2)) {
                    setAllarmeGiorno(newNotifica.getOraNotifica(), newNotifica.getMinutoNotifica(), Calendar.WEDNESDAY, pendingIntent)
                }
                if(newNotifica.getGiorniNotifica(3)) {
                    setAllarmeGiorno(newNotifica.getOraNotifica(), newNotifica.getMinutoNotifica(), Calendar.THURSDAY, pendingIntent)
                }
                if(newNotifica.getGiorniNotifica(4)) {
                    setAllarmeGiorno(newNotifica.getOraNotifica(), newNotifica.getMinutoNotifica(), Calendar.FRIDAY, pendingIntent)
                }
                if(newNotifica.getGiorniNotifica(5)) {
                    setAllarmeGiorno(newNotifica.getOraNotifica(), newNotifica.getMinutoNotifica(), Calendar.SATURDAY, pendingIntent)
                }
                if(newNotifica.getGiorniNotifica(6)) {
                    setAllarmeGiorno(newNotifica.getOraNotifica(), newNotifica.getMinutoNotifica(), Calendar.SUNDAY, pendingIntent)
                }
                sharedPrefs.saveNotifiche(this, NotificheList.notificheList)
                startActivity(Intent(this, ReminderActivity::class.java))

            }

        }
    }

    private fun creaCanaleNotifica(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val testoDescrizione = "Notifica dell'applicazione PillsKeeper"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = testoDescrizione
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setAllarmeGiorno(ora: Int, minuto: Int, giorno: Int, pendingIntent: PendingIntent) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, giorno)
        calendar.set(Calendar.HOUR_OF_DAY, ora)
        calendar.set(Calendar.MINUTE, minuto)
        calendar.set(Calendar.SECOND, 0)
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 7*24*60*60*1000, pendingIntent)
    }

}
