package com.example.pillskeeper

data class Notifica(val farmaco: String, val ora: Int, val minuto: Int){
    private val farmacoNotifica = farmaco
    private val oraNotifica = ora
    private val minutoNotifica = minuto
    private var giorniNotifica = arrayOf(false, false, false, false, false, false, false)

    fun getFarmacoNotifica(): String {
        return farmacoNotifica
    }

    fun getOraNotifica(): Int {
        return oraNotifica
    }

    fun getMinutoNotifica(): Int {
        return minutoNotifica
    }

    fun setGiorniNotifica(lun: Boolean, mar: Boolean, mer: Boolean, gio: Boolean, ven: Boolean, sab: Boolean, dom: Boolean) {
        giorniNotifica[1] = lun
        giorniNotifica[2] = mar
        giorniNotifica[3] = mer
        giorniNotifica[4] = gio
        giorniNotifica[5] = ven
        giorniNotifica[6] = sab
        giorniNotifica[7] = dom
    }

    fun getGiorno(g: Int): Boolean {
       return giorniNotifica[g]
    }

    fun notificaCompleta(): Boolean {
        if (farmacoNotifica.isEmpty())
            else if (oraNotifica.equals(0))
                else if (minutoNotifica.equals(0))
                    else if (giorniNotifica.any { true }.not())
                        return false
        return true

    }

}