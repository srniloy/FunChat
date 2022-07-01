package com.shahriarniloy.funchat

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Message(
    var message: String? = "",
    var senderId: String? = "",
    var sendingTime: String? = "",
    var viewed: Boolean? = false
) {


    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "message" to message,
            "senderId" to senderId,
            "sendingTime" to sendingTime,
            "viewed" to viewed
        )
    }
}