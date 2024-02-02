package com.example.kakaoimagesearch.retrofit

import com.example.kakaoimagesearch.util.Constants
import com.google.gson.GsonBuilder
import de.hdodenhof.circleimageview.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetWorkClient {

    private const val SEARCH_BASE_URL = "https://dapi.kakao.com/v2/search/image"

    val apiService: NetWorkInterface
        get() = instance.create(NetWorkInterface::class.java)

    // Retrofit 인스턴스를 초기화하고 반환한다.
    private val instance: Retrofit
        private get() {
            // Gson 객체 생성. setLenient()는 JSON 파싱이 좀 더 유연하게 처리되도록 한다.
            val gson = GsonBuilder().setLenient().create()

            // Retrofit 빌더를 사용하여 Retrofit 인스턴스 생성
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)  // 기본 URL 설정
                .addConverterFactory(GsonConverterFactory.create(gson))  // JSON 파싱을 위한 컨버터 추가
                .build()
        }
}