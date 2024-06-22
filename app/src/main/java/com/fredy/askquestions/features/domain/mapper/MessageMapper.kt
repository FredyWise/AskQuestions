package com.fredy.askquestions.features.domain.mapper

import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.domain.models.MessageMap

fun Message.toMessageMap(userId:String):MessageMap{
    return MessageMap(this, this.senderId == userId)
}