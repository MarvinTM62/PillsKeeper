package com.example.pillskeeper

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ReminderActivity : AppCompatActivity() {

    private lateinit var nomeNotifica: TextView
    private lateinit var quantitaFarmaco: TextView
    private lateinit var counterFarmaco: TextView
    private lateinit var oraNotifica: TextView
    private lateinit var minutoNotifica: TextView
    private lateinit var lun: TextView
    private lateinit var mar: TextView
    private lateinit var mer: TextView
    private lateinit var gio: TextView
    private lateinit var ven: TextView
    private lateinit var sab: TextView
    private lateinit var dom: TextView
    private lateinit var deleteNot: ImageButton
    private lateinit var resetNot: ImageButton
    val sharedPrefs = SharedPreferencesNotifiche()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        NotificheList.notificheList = sharedPrefs.loadNotifiche(this)
        if(NotificheList.notificheList == null)
            NotificheList.notificheList = ArrayList()

        val addReminderButton: View = findViewById(R.id.addReminderButton)
        val backArrowReminder: View = findViewById(R.id.backArrowReminder)
        var listView: ListView = findViewById(R.id.notificheListView)

        addReminderButton.setOnClickListener {
            startActivity(Intent(this, ChooseReminderActivity::class.java))
        }

        backArrowReminder.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        var adapter = NotificaAdapter(this, NotificheList.notificheList)
        listView.adapter = adapter

    }
    inner class NotificaAdapter(private val context: Context, private val arrayList: ArrayList<Notifica>): BaseAdapter() {

        override fun getCount(): Int {
            return NotificheList.notificheList.size
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var convertView = inflater.inflate(R.layout.itemnotifica, parent, false)
            nomeNotifica = convertView.findViewById(R.id.nomeNotText)
            quantitaFarmaco = convertView.findViewById(R.id.quantitaNotText)
            counterFarmaco = convertView.findViewById(R.id.counterNotText)
            oraNotifica = convertView.findViewById(R.id.oraNotText)
            lun = convertView.findViewById(R.id.lunNotText)
            mar = convertView.findViewById(R.id.marNotText)
            mer = convertView.findViewById(R.id.merNotText)
            gio = convertView.findViewById(R.id.gioNotText)
            ven = convertView.findViewById(R.id.venNotText)
            sab = convertView.findViewById(R.id.sabNotText)
            dom = convertView.findViewById(R.id.domNotText)
            nomeNotifica.text = NotificheList.notificheList[position].getNomeNotifica()
            if(NotificheList.notificheList[position].getQuantitaFarmaco() == -1) {
                quantitaFarmaco.text = " "
                counterFarmaco.text = " "
            } else {
                quantitaFarmaco.text = "Dosi: " + NotificheList.notificheList[position].getQuantitaFarmaco()
                counterFarmaco.text = "Rimanenti: " + NotificheList.notificheList[position].getCounterFarmaco()
            }
            if(NotificheList.notificheList[position].getCounterFarmaco() == -1) {
                counterFarmaco.text = " "
            }
            if(NotificheList.notificheList[position].getMinutoNotifica() <= 9) {
                oraNotifica.text = NotificheList.notificheList[position].getOraNotifica().toString() + " : 0" + NotificheList.notificheList[position].getMinutoNotifica().toString()
            } else {
                oraNotifica.text = NotificheList.notificheList[position].getOraNotifica().toString() + " : " + NotificheList.notificheList[position].getMinutoNotifica().toString()
            }
            if(!NotificheList.notificheList[position].getGiorniNotifica(0)) {
                lun.text = " "
            }
            if(!NotificheList.notificheList[position].getGiorniNotifica(1)) {
                mar.text = " "
            }
            if(!NotificheList.notificheList[position].getGiorniNotifica(2)) {
                mer.text = " "
            }
            if(!NotificheList.notificheList[position].getGiorniNotifica(3)) {
                gio.text = " "
            }
            if(!NotificheList.notificheList[position].getGiorniNotifica(4)) {
                ven.text = " "
            }
            if(!NotificheList.notificheList[position].getGiorniNotifica(5)) {
                sab.text = " "
            }
            if(!NotificheList.notificheList[position].getGiorniNotifica(6)) {
                dom.text = " "
            }
            deleteNot = convertView.findViewById(R.id.deleteNot)
            resetNot = convertView.findViewById(R.id.resetNot)
            if(!NotificheList.notificheList[position].isFarmaco()){
                resetNot.setVisibility(View.INVISIBLE)
            }
            deleteNot.setOnClickListener {
                AlertDialog.Builder(this@ReminderActivity)
                    .setTitle("Elimina notifica")
                    .setMessage("Vuoi eliminare la notifica definitivamente?")
                    .setIcon(R.drawable.ic_baseline_warning)
                    .setPositiveButton("Sì") { dialog, whichButton ->
                        val intent = Intent(this@ReminderActivity, BroadcastNotifica::class.java)
                        val pendingIntent = PendingIntent.getBroadcast(this@ReminderActivity, NotificheList.notificheList[position].getNotificaID(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
                        pendingIntent.cancel()
                        NotificheList.notificheList.remove(NotificheList.notificheList[position])
                        sharedPrefs.saveNotifiche(this@ReminderActivity, NotificheList.notificheList)
                        var adapter = NotificaAdapter(this@ReminderActivity, NotificheList.notificheList)
                        var listView: ListView = findViewById(R.id.notificheListView)
                        adapter.notifyDataSetChanged()
                        listView.invalidateViews()
                        Toast.makeText(this@ReminderActivity, "Eliminazione avvenuta con successo", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Annulla", null).show()
            }
            if(NotificheList.notificheList[position].isFarmaco()) {
                resetNot.setOnClickListener {
                    AlertDialog.Builder(this@ReminderActivity)
                        .setTitle("Reset dosi rimanenti")
                        .setMessage("Vuoi resettare il numero di dosi rimanenti?")
                        .setIcon(R.drawable.ic_baseline_warning)
                        .setPositiveButton("Sì") { dialog, whichButton ->
                            NotificheList.notificheList[position].resetCounterFarmaco()
                            sharedPrefs.saveNotifiche(
                                this@ReminderActivity,
                                NotificheList.notificheList
                            )
                            var adapter = NotificaAdapter(this@ReminderActivity, NotificheList.notificheList)
                            var listView: ListView = findViewById(R.id.notificheListView)
                            adapter.notifyDataSetChanged()
                            listView.invalidateViews()
                            Toast.makeText(
                                this@ReminderActivity,
                                "Reset avvenuto con successo",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .setNegativeButton("Annulla", null).show()
                }
            }

            return convertView
        }


    }


}


