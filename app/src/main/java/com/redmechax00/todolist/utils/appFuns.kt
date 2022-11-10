package com.redmechax00.todolist.utils

import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.redmechax00.todolist.App
import com.redmechax00.todolist.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


fun replaceFragment(
    context: AppCompatActivity,
    fragment: Fragment,
    itemId: String?,
    addToStack: Boolean
) {
    val fragmentTransaction = context.supportFragmentManager.beginTransaction()
    if (addToStack) {
        fragmentTransaction.addToBackStack(null)
    }

    itemId?.let {
        val bundle = Bundle()
        bundle.putString(TAG_ITEM_ID, itemId)
        fragment.arguments = bundle
    }

    fragmentTransaction
        .replace(R.id.data_container, fragment)
        .commit()
}

fun generateNewId(position: Int): String {
    val currentTime: Date = Calendar.getInstance().time
    return "${currentTime.time}$position"
}

fun getCurrentTime(): Long {
    val c = Calendar.getInstance()
    return c.timeInMillis
}

fun getTime(year: Int, month: Int, dayOfMonth: Int): Long? {
    val df = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    return df.parse("$year.${month + 1}.$dayOfMonth")?.time
}

fun getDateFromTime(time: Long): String {
    val date = Date(time)
    val df: DateFormat =
        DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
    return df.format(date)
}

fun getColoredText(str: String, colorRes: Int): SpannableString {
    val text =
        SpannableString(str)
    text.setSpan(
        ForegroundColorSpan(
            App.Colors.get(colorRes)
        ), 0, text.length, 0
    )
    return text
}