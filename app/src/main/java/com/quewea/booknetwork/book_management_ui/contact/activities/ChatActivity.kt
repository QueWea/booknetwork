package com.quewea.booknetwork.book_management_ui.contact.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.quewea.booknetwork.book_management_ui.contact.adapters.MessageAdapter
import com.quewea.booknetwork.R
import com.quewea.booknetwork.book_management_ui.contact.models.Message
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    private var chatId = ""
    private var user = ""
    private var titleChat = ""

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        intent.getStringExtra("chatId")?.let { chatId = it }
        intent.getStringExtra("user")?.let { user = it }
        intent.getStringExtra("titleChat")?.let { titleChat = it }

        if(chatId.isNotEmpty() && user.isNotEmpty()) {
            initViews()
        }
    }

    private fun initViews(){
        messageTitle.setText(titleChat)

        messagesRecylerView.layoutManager = LinearLayoutManager(this)
        messagesRecylerView.adapter = MessageAdapter(user)

        sendMessageButton.setOnClickListener { if (messageTextField.text.toString() != "" && messageTextField.text.toString().isNotEmpty()) sendMessage() }

        val chatRef = db.collection("Chats").document(chatId)

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { messages ->
                val listMessages = messages.toObjects(Message::class.java)
                (messagesRecylerView.adapter as MessageAdapter).setData(listMessages)
            }

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .addSnapshotListener { messages, error ->
                if(error == null){
                    messages?.let {
                        val listMessages = it.toObjects(Message::class.java)
                        (messagesRecylerView.adapter as MessageAdapter).setData(listMessages)
                    }
                }
            }
    }

    private fun sendMessage(){
        val message = Message(
            message = messageTextField.text.toString(),
            from = user
        )

        db.collection("Chats").document(chatId).collection("messages").document().set(message)
        messageTextField.setText("")
    }
}