package com.example.pillskeeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import com.google.android.material.button.MaterialButtonToggleGroup
import java.util.*

class AddReminderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

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
            val nomeFarmaco = findViewById<EditText>(R.id.nomeFarmacoNotificaText).text
            val quantitaFarmaco = findViewById<EditText>(R.id.quantitaFarmacoText).text
            //Toast.makeText(this@AddReminderActivity, nomeFarmaco, Toast.LENGTH_SHORT).show() controllo che nome funzioni
            //Toast.makeText(this@AddReminderActivity, "$oraPicked and $minutiPicked", Toast.LENGTH_SHORT).show() controllo che ora e minuti funzionino
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

            when{
                nomeFarmaco.isEmpty() -> Toast.makeText(this@AddReminderActivity, "Selezionare farmaco", Toast.LENGTH_SHORT).show()
                giorniButtonGroup.checkedButtonIds.isEmpty() -> Toast.makeText(this@AddReminderActivity, "Selezionare almeno un giorno", Toast.LENGTH_LONG).show()

            }

        }
    }
}