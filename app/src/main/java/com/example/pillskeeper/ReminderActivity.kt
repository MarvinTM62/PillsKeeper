package com.example.pillskeeper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ReminderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        val addReminderButton: View = findViewById(R.id.addReminderButton)
        val backArrowReminder: View = findViewById(R.id.backArrowReminder)
        var arrayList: ArrayList<Notifica> = NotificheList.notificheList
        var listView: ListView = findViewById(R.id.notificheListView)

        addReminderButton.setOnClickListener{
            startActivity(Intent(this, ChooseReminderActivity::class.java))
        }

        backArrowReminder.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

        var adapter = NotificaAdapter(this, arrayList)
        listView.adapter = adapter

//        loadNotifica()



//        var notificheListString: ArrayList<String> = ArrayList()
//        var notificheListView: ListView = findViewById(R.id.notificheListView)
//        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, notificheListString)
//        notificheListString.add("test")
//        notificheListString.add("test2")
//        notificheListView.adapter = arrayAdapter


    }

//    private fun loadNotifica() {
//        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
//        val hashSet = sharedPreferences.getStringSet("notifica1", null)
//        var notificheListView: ListView = findViewById(R.id.notificheListView)
//        val arrayAdapter: ArrayAdapter<Notifica> = ArrayAdapter(this, android.R.layout.simple_list_item_1, NotificheList.notificheList)
//        notificheListView.adapter = arrayAdapter
//
//    }

}

class NotificaAdapter(private val context: Context, private val arrayList: ArrayList<Notifica>): BaseAdapter() {
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

    override fun getCount(): Int {
        return arrayList.size
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
        nomeNotifica.text = arrayList[position].getNomeNotifica()
        if(arrayList[position].getQuantitaFarmaco() == 0) {
            quantitaFarmaco.text = " "
            counterFarmaco.text = " "
        } else {
            quantitaFarmaco.text = "Dosi: " + arrayList[position].getQuantitaFarmaco()
            counterFarmaco.text = "Rimanenti: " + arrayList[position].getCounterFarmaco().toString()
        }
        if(arrayList[position].getCounterFarmaco() == 0) {
            counterFarmaco.text = " "
        }
        oraNotifica.text = arrayList[position].getOraNotifica().toString() + " : " + arrayList[position].getMinutoNotifica().toString()
        if(!arrayList[position].getGiorniNotifica(0)) {
            lun.text = " "
        }
        if(!arrayList[position].getGiorniNotifica(1)) {
            mar.text = " "
        }
        if(!arrayList[position].getGiorniNotifica(2)) {
            mer.text = " "
        }
        if(!arrayList[position].getGiorniNotifica(3)) {
            gio.text = " "
        }
        if(!arrayList[position].getGiorniNotifica(4)) {
            ven.text = " "
        }
        if(!arrayList[position].getGiorniNotifica(5)) {
            sab.text = " "
        }
        if(!arrayList[position].getGiorniNotifica(6)) {
            dom.text = " "
        }
        return convertView
    }




}