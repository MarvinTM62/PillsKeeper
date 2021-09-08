package com.example.pillskeeper

import java.io.Serializable

class Notifica(nome: String, fOrA: Boolean): Serializable {
    private val nomeNotifica = nome
    private val notificaID = System.currentTimeMillis().toInt()
    private var isFarmaco = fOrA
    private var dosiConfezione = -1
    private var counterFarmaco = -1
    private var obbligoPrescrizione = false
    private var contattoFarmaco = false
    private var quantitaFarmaco = -1
    private var oraNotifica = 0
    private var minutoNotifica = 0
    private var giorniNotifica = arrayOf(false, false, false, false, false, false, false)

    fun getNomeNotifica(): String {
        return nomeNotifica
    }

    fun getNotificaID(): Int {
        return notificaID
    }

    fun isFarmaco(): Boolean {
        return isFarmaco
    }

    fun setCounterFarmaco(n: Int) {
        counterFarmaco = n
        dosiConfezione = n
    }

    fun resetCounterFarmaco(){
        counterFarmaco = dosiConfezione
    }

    fun getCounterFarmaco(): Int {
        return counterFarmaco
    }

    fun setQuantitaFarmaco(n: Int){
        quantitaFarmaco = n
    }

    fun getQuantitaFarmaco(): Int {
        return quantitaFarmaco
    }

    fun farmacoFinitoNotifica(): Boolean {
        if (counterFarmaco <= 4)
            return true
        return false
    }

    fun removeQuantitaDaCounter(){
        counterFarmaco -= quantitaFarmaco
        if (counterFarmaco < 0)
            counterFarmaco = 0
    }

    fun setObbligoPrescrizione(){
        obbligoPrescrizione = true
    }

    fun getObbligoPrescrizione(): Boolean {
        return obbligoPrescrizione
    }

    fun setStatoContattoFarmaco(){
        contattoFarmaco = true
    }

    fun getStatoContattoNotifica(): Boolean {
        return contattoFarmaco
    }

    fun notifyContatti() {
        //notifica i contatti che il farmaco è finito
    }

    fun setOraNotifica(o: Int){
        oraNotifica = o
    }

    fun getOraNotifica(): Int {
        return oraNotifica
    }

    fun setMinutoNotifica(m: Int){
        minutoNotifica = m
    }

    fun getMinutoNotifica(): Int {
        return minutoNotifica
    }

    fun setGiorniNotifica(g: Int) {
        giorniNotifica[g] = true
    }

    fun getGiorniNotifica(g: Int): Boolean {
       return giorniNotifica[g]
    }

}