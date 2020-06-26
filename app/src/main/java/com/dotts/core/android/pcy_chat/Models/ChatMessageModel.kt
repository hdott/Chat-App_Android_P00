package com.dotts.core.android.pcy_chat.Models

data class ChatMessageModel (
    var action: String,
    var from: String,
    var to: String,
    var msg: String
){
}