package com.example.kakaoimagesearch.retrofit

import com.example.kakaoimagesearch.data.ImageResponse
import com.example.kakaoimagesearch.data.SearchModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NetWorkInterface {
//    @GET("72f1bc517f9c7728c12eadfefd955397") //시도별 실시간 측정정보 조회 주소
//    suspend fun getImage(@QueryMap param: HashMap<String, String>): KakaoImage

    @GET("v2/search/image")
    fun searchImage(
        @Header("Authorization") authorization: String,
        @Query("query") query: String,
        // 결과 페이지 번호, 1~50 사이의 값, 기본 값 1
        @Query("page") page: Int? = null,
        // 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy
        @Query("sort") sort: String = "accuracy",
        // 한 페이지에 보여질 문서 수, 1~80 사이의 값, 기본 값 80
        @Query("size") size: Int? = 80
    ) : Call<ImageResponse>

    suspend fun saveItem(searchModel: SearchModel)

    suspend fun removeItem(searchModel: SearchModel)

    suspend fun getItem(): List<SearchModel>

}
