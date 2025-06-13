package com.onmoim.core.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    @SerialName("city")
    val city: String,
    @SerialName("code")
    val code: String,
    @SerialName("district")
    val district: String,
    @SerialName("dong")
    val dong: String,
    @SerialName("locationId")
    val locationId: Int
)