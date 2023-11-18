package com.apps.salesorder.helper

import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Utils {

     object DATE{
         fun convertDate(string: String?, formatDate: String): String? {
             val inputSDF = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
             val outputSDF = SimpleDateFormat(formatDate, Locale.getDefault())
             var date: Date? = null
             date = try {
                 //here you get Date object from string
                 inputSDF.parse(string)
             } catch (e: ParseException) {
                 return string
             }
             //after changing date format again you can change to string with changed format
             return outputSDF.format(date)
         }
     }

    object NUMBER{
        fun currencyFormat(amount: String): String? {
            val formatter = DecimalFormat("###,###,##0.00")
            return formatter.format(amount.toDouble())
        }
    }
}