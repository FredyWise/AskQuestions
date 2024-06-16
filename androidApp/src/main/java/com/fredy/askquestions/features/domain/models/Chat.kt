package com.fredy.askquestions.features.domain.models

data class Chat(
    val chatId: String,
    val userObjects: ArrayList<User> = arrayListOf()
) {
    fun addUser(user: User) {
        userObjects.add(user)
    }
}