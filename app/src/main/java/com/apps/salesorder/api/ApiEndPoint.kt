package com.apps.salesorder.api

import com.apps.salesorder.data.response.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface ApiEndPoint {

    @FormUrlEncoded
    @POST("api/so/login")
    suspend fun login(
        @Field("username") username: String?,
        @Field("password") password: String?,
        @Field("android_id") androidId: String?,
    ): Response<LoginResp>

    @FormUrlEncoded
    @POST("api/so/logout")
    suspend fun logout(
        @Field("android_id") androidId: String?,
    ): Response<LoginResp>

    @FormUrlEncoded
    @POST("api/so/so_sync")
    suspend fun syncSo(
        @Field("so_request") jsonData: String?,
    ): Response<SyncSoResp>

    @GET("api/so/download_master_data/branch")
    suspend fun dataBranch(
        @Header("Authorization") authorization : String,
    ): Response<BranchResp>

    @GET("api/so/download_master_data/currency")
    suspend fun dataCurrency(
        @Header("Authorization") authorization : String,
    ): Response<CurrencyResp>

    @GET("api/so/download_master_data/debtor")
    suspend fun dataDebtor(
        @Header("Authorization") authorization : String,
        @Query("SalesAgent") salesAgent: String
    ): Response<DebtorResp>

    @GET("api/so/download_master_data/item")
    suspend fun dataItem(
        @Header("Authorization") authorization : String,
        @Query("SalesAgent") salesAgent: String
    ): Response<ItemResp>

    @GET("api/so/download_master_data/itemUom")
    suspend fun dataItemUOM(
        @Header("Authorization") authorization : String,
    ): Response<ItemUomResp>

    @GET("api/so/download_master_data/itemPrice")
    suspend fun dataItemPrice(
        @Header("Authorization") authorization : String,
    ): Response<ItemPriceResp>

    @GET("api/so/download_master_data/taxType")
    suspend fun dataTax(
        @Header("Authorization") authorization : String,
    ): Response<TaxResp>

    @GET("api/so/download_master_data/company_setting")
    suspend fun companySetting(
        @Header("Authorization") authorization : String,
    ): Response<SettingResp>


}