package com.example.kakaoimagesearch.retrofit

import com.example.kakaoimagesearch.data.ImageModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NetWorkInterface {
    @GET("v2/search/image")
    fun searchImage(
        @Header("Authorization") apiKey: String?,
        @Query("query") query: String?,
        // 결과 페이지 번호, 1~50 사이의 값, 기본 값 1
        @Query("page") page: Int,
        // 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy
        @Query("sort") sort: String?,
        // 한 페이지에 보여질 문서 수, 1~80 사이의 값, 기본 값 80
        @Query("size") size: Int
    ) : Call<ImageModel?>?
}
