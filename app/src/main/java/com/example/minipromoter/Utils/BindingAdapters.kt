package com.example.minipromoter.Utils

import android.graphics.Color
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("setCartBacgroundColor")
fun setCartBacgroundColor(view: CardView, errorMessage: String?) {
    view.setCardBackgroundColor(getRandomColorCode())

}

fun getRandomColorCode(): Int {
    val random = Random()
    return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
}

@BindingAdapter("setCreationTime")
fun com.google.android.material.textview.MaterialTextView.setCreationDate(timeInMilliSec: Long) {
    text = com.example.minipromoter.Utils.timeConverted(timeInMilliSec)


}

fun timeConverted(time: Long): String {
    val date = Date(time)
    val formatter: DateFormat = SimpleDateFormat("HH:mm   MMM, dd, YYYY")
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(date)
}
