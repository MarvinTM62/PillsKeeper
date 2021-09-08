package com.example.pillskeeper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class BroadcastNotifica: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
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
                        it.notifyContatti()
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

