package com.apps.salesorder.api

import android.content.Context
import com.apps.salesorder.data.constants.Constants
import com.apps.salesorder.data.preferences.Preferences
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    fun getClient(context: Context): ApiEndPoint {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("key", "apiKey" ).build()
                chain.proceed(request)
            }
            .build()

        val gson = GsonBuilder().serializeNulls().create()

        var baseUrl = Preferences(context).getString(Constants.DEFAULT.BASE_URL).toString()
        if(baseUrl.equals("null")){
            baseUrl = Constants.DEFAULT.BASE_URL
        }

        return Retrofit.Builder()
            .baseUrl( baseUrl )
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(ApiEndPoint::class.java)
    }

}