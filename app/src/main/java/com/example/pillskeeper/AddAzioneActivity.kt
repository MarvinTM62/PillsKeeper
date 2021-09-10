package com.example.pillskeeper

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButtonToggleGroup
import java.util.*

class AddAzioneActivity : AppCompatActivity() {

    val CHANNEL_ID = "pillskeeper"
    val CHANNEL_NAME = "pillskeeper_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_azione)

        creaCanaleNotifica()

        var calendar: Calendar = Calendar.getInstance()
        val oraLocale = calendar.get(Calendar.HOUR_OF_DAY)
        var oraPicked = oraLocale + 1
        val oraPicker2 = findViewById<NumberPicker>(R.id.oraPicker2)
        if (oraPicker2 != null) {
            oraPicker2.minValue = 0
            oraPicker2.maxValue = 23
            oraPicker2.value = oraLocale + 1
            oraPicker2.wrapSelectorWheel = true
            oraPicker2.setOnValueChangedListener { numberPicker, i, i2 -> oraPicked = i2 }
        }

        val minutoLocale = calendar.get(Calendar.MINUTE)
        var minutiPicked = minutoLocale + 1
        val minutiPicker2 = findViewById<NumberPicker>(R.id.minutiPicker2)
        if (minutiPicker2 != null) {
            minutiPicker2.minValue = 0
            minutiPicker2.maxValue = 59
            if(minutiPicker2.value == 59){
                minutiPicker2.value = 0
            } else {
                minutiPicker2.value = minutoLocale + 1
            }
            minutiPicker2.wrapSelectorWheel = true
            minutiPicker2.setOnValueChangedListener { numberPicker, i, i2 -> minutiPicked = i2 }
        }
        val bottoneCreaNotifica2: View = findViewById(R.id.bottoneCreaNotifica2)
        bottoneCreaNotifica2.setOnClickListener {
            val nomeAzioneText = findViewById<TextView>(R.id.nomeAzioneNotificaText).text
            val nomeAzioneString = nomeAzioneText.toString()
            val giorniList: List<Int>
            val giorniButtonGroup2 = findViewById<MaterialButtonToggleGroup>(R.id.giorniButtonsGroup2)
            if (nomeAzioneText.isEmpty())
                Toast.makeText(this@AddAzioneActivity, "Selezionare azione", Toast.LENGTH_SHORT).show()
            if (giorniButtonGroup2.checkedButtonIds.isEmpty()) {
                Toast.makeText(this@AddAzioneActivity, "Selezionare almeno un giorno", Toast.LENGTH_SHORT).show()
            } else if (!nomeAzioneText.isEmpty() && !giorniButtonGroup2.checkedButtonIds.isEmpty()){
                val newNotifica = Notifica(nomeAzioneString, false)
                newNotifica.setOraNotifica(oraPicked)
                newNotifica.setMinutoNotifica(minutiPicked)
                giorniList = giorniButtonGroup2.checkedButtonIds
                if (giorniList.contains(R.id.buttonLun2)) {
                    newNotifica.setGiorniNotifica(0)
                }
                if (giorniList.contains(R.id.buttonMar2)) {
                    newNotifica.setGiorniNotifica(1)
                }
                if (giorniList.contains(R.id.buttonMer2)) {
                    newNotifica.setGiorniNotifica(2)
                }
                if (giorniList.contains(R.id.buttonGio2)) {
                    newNotifica.setGiorniNotifica(3)
                }
                if (giorniList.contains(R.id.buttonVen2)) {
                    newNotifica.setGiorniNotifica(4)
                }
                if (giorniList.contains(R.id.buttonSab2)) {
                    newNotifica.setGiorniNotifica(5)
                }
                if (giorniList.contains(R.id.buttonDom2)) {
                    newNotifica.setGiorniNotifica(6)
                }
                NotificheList.notificheList.add(newNotifica)
                val sharedPrefs = SharedPreferencesNotifiche()
                val intent = Intent(this, BroadcastNotifica::class.java)
                intent.putExtra("ID", newNotifica.getNotificaID())
                if(newNotifica.getGiorniNotifica(0)) {
                    val pendingIntent = PendingIntent.getBroadcast(this, newNotifica.getNotificaID(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    setAllarmeGiorno(newNotifica.getOraNotifica(), newNotifica.getMinutoNotifica(), Calendar.MONDAY, pendingIntent)
                }
                if(newNotifica.getGiorniNotifica(1)) {
                    val pendingIntent = PendingIntent.getBroadcast(this, newNotifica.getNotificaID() + 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    setAllarmeGiorno(newNotifica.getOraNotifica(), newNotifica.getMinutoNotifica(), Calendar.TUESDAY, pendingIntent)
                }
                if(newNotifica.getGiorniNotifica(2)) {
                    val pendingIntent = PendingIntent.getBroadcast(this, newNotifica.getNotificaID() + 2, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    setAllarmeGiorno(newNotifica.getOraNotifica(), newNotifica.getMinutoNotifica(), Calendar.WEDNESDAY, pendingIntent)
                }
                if(newNotifica.getGiorniNotifica(3)) {
                    val pendingIntent = PendingIntent.getBroadcast(this, newNotifica.getNotificaID() + 3, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    setAllarmeGiorno(newNotifica.getOraNotifica(), newNotifica.getMinutoNotifica(), Calendar.THURSDAY, pendingIntent)
                }
                if(newNotifica.getGiorniNotifica(4)) {
                    val pendingIntent = PendingIntent.getBroadcast(this, newNotifica.getNotificaID() + 4, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    setAllarmeGiorno(newNotifica.getOraNotifica(), newNotifica.getMinutoNotifica(), Calendar.FRIDAY, pendingIntent)
                }
                if(newNotifica.getGiorniNotifica(5)) {
                    val pendingIntent = PendingIntent.getBroadcast(this, newNotifica.getNotificaID() + 5, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    setAllarmeGiorno(newNotifica.getOraNotifica(), newNotifica.getMinutoNotifica(), Calendar.SATURDAY, pendingIntent)
                }
                if(newNotifica.getGiorniNotifica(6)) {
                    val pendingIntent = PendingIntent.getBroadcast(this, newNotifica.getNotificaID() + 6, intent, PendingIntent.FLAG_UPDATE_CURRENT)
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
        if(calendar.timeInMillis < System.currentTimeMillis())
            calendar.add(Calendar.DAY_OF_WEEK, 7)
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 7*24*60*60*1000, pendingIntent)
    }
}