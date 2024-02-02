package com.example.kakaoimagesearch.util

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.kakaoimagesearch.search.SearchFragment
import com.example.kakaoimagesearch.util.Constants.Companion.KEY_SEARCH
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object Utils {

    fun getDateFromTimestampWithFormat(
        timestamp: String?,
        fromFormatformat: String?,
        toFormatformat: String?
    ): String {
        var date: Date? = null
        var res = ""
        try {
            val format = SimpleDateFormat(fromFormatformat)
            date = format.parse(timestamp)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        Log.d("date", "getDateFromTimestampWithFormat date >> $date")

        val df = SimpleDateFormat(toFormatformat)
        res = df.format(date)
        return res
    }

    fun savePref(context: Context, query: String) {
        val sharedPreferences = context.getSharedPreferences(KEY_SEARCH, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_SEARCH, query)
        Log.d(ContentValues.TAG, "savePref: $KEY_SEARCH")
        editor.apply()
    }

    fun loadPref(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(KEY_SEARCH, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_SEARCH, null)
    }
}