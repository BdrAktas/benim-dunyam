package com.simay.lifebank.ui.util

import java.text.NumberFormat
import java.util.Locale

fun formatTRY(amount: Number): String {
    val format = NumberFormat.getCurrencyInstance(Locale("tr", "TR"))
    format.maximumFractionDigits = 0
    format.minimumFractionDigits = 0
    return format.format(amount)
}
