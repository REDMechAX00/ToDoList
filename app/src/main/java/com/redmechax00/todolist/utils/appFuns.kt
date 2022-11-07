package com.redmechax00.todolist.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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