package com.example.pillskeeper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
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

    private val CHANNEL_ID = "channel_id"
    private val notificationID = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_azione)

        creaCanaleNotifica()

        val calendar: Calendar = Calendar.getInstance()
        val oraLocale = calendar.get(Calendar.HOUR)
        var oraPicked = 0
        val oraPicker2 = findViewById<NumberPicker>(R.id.oraPicker2)
        if (oraPicker2 != null) {
            oraPicker2.minValue = 0
            oraPicker2.maxValue = 24
            oraPicker2.value = oraLocale + 1
            oraPicker2.wrapSelectorWheel = true
            oraPicker2.setOnValueChangedListener { numberPicker, i, i2 -> oraPicked = i2 }
        }

        val minutoLocale = calendar.get(Calendar.MINUTE)
        var minutiPicked = 0
        val minutiPicker2 = findViewById<NumberPicker>(R.id.minutiPicker2)
        if (minutiPicker2 != null) {
            minutiPicker2.minValue = 0
            minutiPicker2.maxValue = 59
            minutiPicker2.value = minutoLocale
            minutiPicker2.wrapSelectorWheel = true
            minutiPicker2.setOnValueChangedListener { numberPicker, i, i2 -> minutiPicked = i2 }
        }
        val bottoneCreaNotifica2: View = findViewById(R.id.bottoneCreaNotifica2)
        bottoneCreaNotifica2.setOnClickListener{
            val nomeAzioneText = findViewById<TextView>(R.id.nomeAzioneNotificaText).text
            val nomeAzioneString = nomeAzioneText.toString()
            var giorniSelezionati = arrayOf(false, false, false, false, false, false, false)
            val giorniButtonGroup2 = findViewById<MaterialButtonToggleGroup>(R.id.giorniButtonsGroup2)
            giorniButtonGroup2.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    if (checkedId == R.id.buttonLun2)
                        giorniSelezionati[0] = true
                    if (checkedId == R.id.buttonMar2)
                        giorniSelezionati[1] = true
                    if (checkedId == R.id.buttonMer2)
                        giorniSelezionati[2] = true
                    if (checkedId == R.id.buttonGio2)
                        giorniSelezionati[3] = true
                    if (checkedId == R.id.buttonVen2)
                        giorniSelezionati[4] = true
                    if (checkedId == R.id.buttonSab2)
                        giorniSelezionati[5] = true
                    if (checkedId == R.id.buttonDom2)
                        giorniSelezionati[6] = true
                }
            }
            if(nomeAzioneText.isEmpty())
                Toast.makeText(this@AddAzioneActivity, "Impostare un'azione", Toast.LENGTH_SHORT).show()
            if(giorniButtonGroup2.checkedButtonIds.isEmpty()){
                Toast.makeText(this@AddAzioneActivity, "Selezionare almeno un giorno", Toast.LENGTH_LONG).show()
            } else {

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