package com.hirno.weather.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Model of server-returned errors
 */
@Parcelize
data class ErrorDTO(
    @SerializedName("reason")
    val reason: String? = null,
    @SerializedName("error")
    val error: Boolean = false,
) : Parcelable