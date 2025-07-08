package com.onmoim.core.network.model.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    @SerialName("locationId")
    val locationId: Int,
    @SerialName("birth")
    val birth: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("name")
    val name: String
)