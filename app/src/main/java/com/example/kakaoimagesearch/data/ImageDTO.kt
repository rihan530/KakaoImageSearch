package com.example.kakaoimagesearch.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.Date
import kotlinx.parcelize.Parcelize

data class KakaoImage(val response: ImageResponse)

data class ImageResponse(
    @SerializedName("meta")
    val metaData: MetaData,

    @SerializedName("documents")
    var documents: MutableList<Document>
)

data class MetaData(
    @SerializedName("total_count")
    val totalCount: Int = 0,

    @SerializedName("pageable_count")
    var pageableCount: Int = 0
)

@Parcelize
data class Document(
    @SerializedName("datetime")
    var datetime: String = "",
    @SerializedName("display_sitename")
    var sitename: String = "",
    @SerializedName("doc_url")
    var docUrl: String = "",
    @SerializedName("image_url")
    var imageUrl: String = "",
    @SerializedName("thumbnail_url")
    var thumbnailUrl: String = ""
) : Parcelable

@Parcelize
data class SearchModel (
    val sitename: String,
    val datetime: String,
    val image: String
) : Parcelable
