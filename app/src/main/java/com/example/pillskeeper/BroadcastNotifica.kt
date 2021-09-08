package com.example.pillskeeper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BroadcastNotifica: BroadcastReceiver() {
    var username: String = ""
    override fun onReceive(context: Context, intent: Intent) {
        username = PreferenceManager.getDefaultSharedPreferences(context).getString("username", "Login non effettuato")!!
        val ID = intent.getIntExtra("ID", 0)
        NotificheList.notificheList.forEach { it
            if (it.getNotificaID() == ID) {
                val builder = NotificationCompat.Builder(context, "pillskeeper")
                    .setSmallIcon(R.drawable.pillola)
                if (!it.isFarmaco()) {
                    builder.setContentTitle(it.getNomeNotifica())
                    if (it.getMinutoNotifica() <= 9) {
                        builder.setStyle(
                            NotificationCompat.BigTextStyle()
                                .bigText("Ricordati di " + it.getNomeNotifica() + " alle " + it.getOraNotifica() + ":0" + it.getMinutoNotifica() + ".")
                        )
                    } else {
                        builder.setStyle(
                            NotificationCompat.BigTextStyle()
                                .bigText("Ricordati di " + it.getNomeNotifica() + " alle " + it.getOraNotifica() + ":" + it.getMinutoNotifica() + ".")
                        )
                    }
                } else {
                    builder.setContentTitle("Prendi " + it.getQuantitaFarmaco() + " " + it.getNomeNotifica())
                    it.removeQuantitaDaCounter()
                    if (it.getMinutoNotifica() <= 9) {
                        builder.setStyle(
                            NotificationCompat.BigTextStyle()
                                .bigText("Ricordati di prendere " + it.getQuantitaFarmaco() + " " + it.getNomeNotifica() + " alle " + it.getOraNotifica() + ":0" + it.getMinutoNotifica() + ".")
                        )
                    } else {
                        builder.setStyle(
                            NotificationCompat.BigTextStyle()
                                .bigText("Ricordati di prendere " + it.getQuantitaFarmaco() + " " + it.getNomeNotifica() + " alle " + it.getOraNotifica() + ":" + it.getMinutoNotifica() + ".")
                        )
                    }
                    if (it.farmacoFinitoNotifica() && it.getStatoContattoNotifica()) {
                        val builder2 = NotificationCompat.Builder(context, "pillskeeper")
                            .setSmallIcon(R.drawable.pillola)
                            .setContentTitle(it.getNomeNotifica() + " è quasi finito/a.")
                        builder2.setStyle(NotificationCompat.BigTextStyle()
                                    .bigText("I contatti sono stati avvisati. Ricordati di resettare la notifica all'arrivo della nuova confezione."))
                        if(username != "Login non effettuato")
                            it.notifyContatti(username)

                        var arrayList: ArrayList<String>
                        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
                        val emptyList = Gson().toJson(ArrayList<String>())
                        var json2 = preferenceManager.getString("pillsExpired", emptyList)
                        val gson2 = Gson()
                        val type = object : TypeToken<ArrayList<String>>() {}.type
                        arrayList = gson2.fromJson(json2, type)
                        arrayList.add(it.getNomeNotifica())
                        val gson = Gson()
                        var json = gson.toJson(arrayList)
                        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                        var editor = sharedPreferences.edit()
                        editor.putString("pillsExpired", json)
                        editor.apply()

                        builder2.setPriority(NotificationCompat.PRIORITY_HIGH)
                        val notificationManager2 = NotificationManagerCompat.from(context)
                        notificationManager2.notify(System.currentTimeMillis().toInt(), builder2.build())
                    } else if(it.farmacoFinitoNotifica()) {
                        val builder2 = NotificationCompat.Builder(context, "pillskeeper")
                            .setSmallIcon(R.drawable.pillola)
                            .setContentTitle(it.getNomeNotifica() + " è quasi finito/a.")
                        builder2.setStyle(NotificationCompat.BigTextStyle()
                            .bigText("Ricordati di prendere una nuova confezione e resettare la notifica."))
                        builder2.setPriority(NotificationCompat.PRIORITY_HIGH)
                        val notificationManager2 = NotificationManagerCompat.from(context)
                        notificationManager2.notify(System.currentTimeMillis().toInt(), builder2.build())
                    }


                }
                val sharedPrefs = SharedPreferencesNotifiche()
                sharedPrefs.saveNotifiche(context, NotificheList.notificheList)
                builder.setPriority(NotificationCompat.PRIORITY_HIGH)
                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
            }
        }

    }

}

