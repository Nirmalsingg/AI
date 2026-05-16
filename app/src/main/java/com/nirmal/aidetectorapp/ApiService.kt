package com.nirmal.aidetectorapp

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

data class TextRequest(
    val text: String
)

data class Prediction(
    val label: String,
    val score: Float
)

data class ApiResponse(
    val result: List<Prediction>
)

interface ApiService {

    @POST("detect")
    fun detectText(
        @Body request: TextRequest
    ): Call<ApiResponse>

    @Multipart
    @POST("detect/video")
    fun detectVideo(
        @Part video: MultipartBody.Part
    ): Call<ApiResponse>

    @Multipart
    @POST("detect/app")
    fun detectApp(
        @Part app: MultipartBody.Part
    ): Call<ApiResponse>

    @Multipart
    @POST("detect/other")
    fun detectOther(
        @Part file: MultipartBody.Part
    ): Call<ApiResponse>
}