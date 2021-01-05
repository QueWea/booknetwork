package com.quewea.booknetwork.book_management_ui.contact.models

data class Chat(
    var id: String = "",
    var name: String = "",
    var users: List<String> = emptyList()
)