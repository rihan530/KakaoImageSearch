package com.example.kakaoimagesearch.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ImageModel(
    @SerializedName("meta")
    val metaData: MetaData,

    @SerializedName("documents")
    var documents: ArrayList<Document>
) {
    data class MetaData(
        @SerializedName("total_count")
        val totalCount: Int,

        @SerializedName("pageable_count")
        var pageableCount: Int,

        @SerializedName("is_end")
        val isEnd: Boolean
    )

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
    )
}