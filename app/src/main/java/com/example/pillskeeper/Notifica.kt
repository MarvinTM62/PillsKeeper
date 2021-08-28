package com.example.pillskeeper

data class Notifica(val farmaco: String){
    private val farmacoNotifica = farmaco
    private var counterFarmaco = 0
    private var quantitaFarmaco = 0
    private var oraNotifica = 0
    private var minutoNotifica = 0
    private var giorniNotifica = arrayOf(false, false, false, false, false, false, false)

    fun getFarmacoNotifica(): String {
        return farmacoNotifica
    }

    fun setCounterFarmaco(n: Int) {
        counterFarmaco = n
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

    fun getOraNotifica(): Int {
        return oraNotifica
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