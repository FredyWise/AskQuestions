package com.fredy.askquestions.features.domain.util

import com.google.firebase.Timestamp

@Suppress("UNCHECKED_CAST")
data class APICache<T>(
    val cacheId: String = "",
    val cache: T = Any() as T,
    val cachedTime: Timestamp = Timestamp.now()
)