package com.fredy.askquestions.features.domain.models

data class Chat(
    val chatId: String = "", // Unique identifier for the chat
    val chatName: String? = null, // Chat name (optional for group chats)
    val imageUrl: String? = null, // URL of the chat image (optional)
    val lastMessage: Message? = null, // Reference to the last message object
    val participants: List<User> = emptyList() // List of participants in the chat
)