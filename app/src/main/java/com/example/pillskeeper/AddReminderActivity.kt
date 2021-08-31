package com.example.pillskeeper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.button.MaterialButtonToggleGroup
import java.util.*

class AddReminderActivity : AppCompatActivity() {
    private val CHANNEL_ID = "channel_id"
    private val notificationID = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        creaCanaleNotifica()

        val calendar: Calendar = Calendar.getInstance()
        val oraLocale = calendar.get(Calendar.HOUR)
        var oraPicked = 0
        val oraPicker = findViewById<NumberPicker>(R.id.oraPicker)
        if (oraPicker != null) {
            oraPicker.minValue = 0
            oraPicker.maxValue = 24
            oraPicker.value = oraLocale + 1
            oraPicker.wrapSelectorWheel = true
            oraPicker.setOnValueChangedListener { numberPicker, i, i2 -> oraPicked = i2 }
        }

        val minutoLocale = calendar.get(Calendar.MINUTE)
        var minutiPicked = 0
        val minutiPicker = findViewById<NumberPicker>(R.id.minutiPicker)
        if (minutiPicker != null) {
            minutiPicker.minValue = 0
            minutiPicker.maxValue = 59
            minutiPicker.value = minutoLocale
            minutiPicker.wrapSelectorWheel = true
            minutiPicker.setOnValueChangedListener { numberPicker, i, i2 -> minutiPicked = i2 }
        }


        val bottoneCreaNotifica: View = findViewById(R.id.bottoneCreaNotifica)
        bottoneCreaNotifica.setOnClickListener{
            val nomeFarmacoText = findViewById<TextView>(R.id.nomeFarmacoNotificaText).text
            val nomeFarmacoString = nomeFarmacoText.toString()
            val quantitaFarmaco = (R.id.quantitaFarmacoText).toInt()
            var giorniSelezionati = arrayOf(false, false, false, false, false, false, false)
            val giorniButtonGroup = findViewById<MaterialButtonToggleGroup>(R.id.giorniButtonsGroup)
            giorniButtonGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    if (checkedId == R.id.buttonLun)
                        giorniSelezionati[0] = true
                    if (checkedId == R.id.buttonMar)
                        giorniSelezionati[1] = true
                    if (checkedId == R.id.buttonMer)
                        giorniSelezionati[2] = true
                    if (checkedId == R.id.buttonGio)
                        giorniSelezionati[3] = true
                    if (checkedId == R.id.buttonVen)
                        giorniSelezionati[4] = true
                    if (checkedId == R.id.buttonSab)
                        giorniSelezionati[5] = true
                    if (checkedId == R.id.buttonDom)
                        giorniSelezionati[6] = true
                }
            }

            if(nomeFarmacoText.isEmpty())
                Toast.makeText(this@AddReminderActivity, "Selezionare farmaco", Toast.LENGTH_SHORT).show()
            if(giorniButtonGroup.checkedButtonIds.isEmpty()){
                Toast.makeText(this@AddReminderActivity, "Selezionare almeno un giorno", Toast.LENGTH_LONG).show()
            } else {
                val newNotifica = Notifica(nomeFarmacoString)
                newNotifica.setQuantitaFarmaco(quantitaFarmaco)
//                var builder = NotificationCompat.Builder(this, CHANNEL_ID)
//                    .setSmallIcon(R.drawable.icona_pills_keeper)
//                    .setContentTitle("Prendi" + quantitaFarmaco.toString() + nomeFarmacoString)
//                    .setContentText("Much longer text that cannot fit one line...")
//                    .setStyle(NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
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