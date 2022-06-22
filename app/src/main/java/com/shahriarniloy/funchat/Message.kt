package com.shahriarniloy.funchat

import java.sql.Time

class Message {
    var message: String? = null
    var senderId: String? = null
    var sendingTime: String? = null

    constructor(){}
    constructor(message: String?, senderId: String?, sendingTime: String?){
        this.message = message
        this.senderId = senderId
        this.sendingTime = sendingTime
    }
}