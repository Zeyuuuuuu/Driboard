package com.sktbd.driboard.data.model

data class AuthToken (
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("created_at")
    val createdAt: Int
)