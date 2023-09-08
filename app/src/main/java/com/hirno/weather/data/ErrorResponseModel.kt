package com.hirno.weather.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Model of server-returned errors
 */
@Parcelize
data class ErrorResponseModel(
    @SerializedName("reason")
    var reason: String? = null,
    @SerializedName("error")
    var error: Boolean = false,
) : Parcelable