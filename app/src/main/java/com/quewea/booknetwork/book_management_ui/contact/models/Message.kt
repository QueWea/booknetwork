package com.quewea.booknetwork.book_management_ui.contact.models

import java.util.*

data class Message (
    var message: String = "",
    var from: String = "",
    var dob: Date = Date()
)