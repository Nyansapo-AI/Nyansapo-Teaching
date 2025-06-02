package com.nyansapoai.teaching.data.network

private const val URL = "api.nyansapoai.net"

//https://api.nyansapoai.net/docs

fun httpUrlBuilder(): String {
    return "https://$URL"
}