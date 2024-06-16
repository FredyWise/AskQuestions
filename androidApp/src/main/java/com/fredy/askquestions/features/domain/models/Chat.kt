package com.fredy.askquestions.features.domain.models

data class Chat(
    val chatId: String,
    val chatName: String? = null,
    val chatImage: String? = null,
    val lastMessage: Message? = null,
    val userObjects: ArrayList<User> = arrayListOf()
) {
    fun addUser(user: User) {
        userObjects.add(user)
    }
}