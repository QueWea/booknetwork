package com.quewea.booknetwork.book_management_ui.contact.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.quewea.booknetwork.R
import com.quewea.booknetwork.book_management_ui.contact.adapters.ChatAdapter
import com.quewea.booknetwork.book_management_ui.contact.models.Chat
import kotlinx.android.synthetic.main.activity_list_of_chats.*
import java.util.*

@Suppress("DEPRECATION")

class ListOfChatsActivity : AppCompatActivity() {
    private var idBook = ""
    private var emailUserLog = ""
    private var tituloBook = ""
    private var ownerName = ""
    private var emailOwnerBook = ""
    private var nameUserLog = ""

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_chats)

        val progressDialog = ProgressDialog(this@ListOfChatsActivity)

        val prefs = getSharedPreferences("user", Context.MODE_PRIVATE)
        emailUserLog = prefs.getString("username", "").toString()


        progressDialog.setMessage("Cargando información...")
        progressDialog.show()
        tituloBook = intent.extras?.getString("titulo").toString()
        ownerName = intent.extras?.getString("owner").toString()
        emailOwnerBook = intent.extras?.getString("email").toString()
        idBook = intent.extras?.getString("idBook").toString()
        getnameUserLog(emailUserLog)
        progressDialog.dismiss()

        if (emailUserLog != null) {
            if (emailUserLog.isNotEmpty()){
                initViews()
            }
        }
    }


    private fun initViews() {
        btn_back.setOnClickListener {
            finish();
        }

        var idChat = ""
            db.collection("Chats")
                    .whereEqualTo("idBook", idBook)
                    .whereEqualTo("idUser", emailUserLog)
                    .whereEqualTo("idOwner", emailOwnerBook)
                    .limit(1)
                    .get().addOnSuccessListener { documentSnapshot ->
                for (document in documentSnapshot) {
                    idChat = (document.id)}
                        if (emailOwnerBook == "null") {
                            return@addOnSuccessListener
                            /* newChat() */
                        } else if (idChat != null && idChat.isNotEmpty()) startChat(idChat)
                        else newChat()

            }

            listChatsRecyclerView.layoutManager = LinearLayoutManager(this)
        listChatsRecyclerView.adapter =
            ChatAdapter { chat ->
                chatSelected(chat)
            }

        val userRef = db.collection("Users").document(emailUserLog)

        userRef.collection("Chats")
            .get()
            .addOnSuccessListener { chats ->
                val listChats = chats.toObjects(Chat::class.java)

                (listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
            }

        userRef.collection("Chats")
            .addSnapshotListener { chats, error ->
                if(error == null){
                    chats?.let {
                        val listChats = it.toObjects(Chat::class.java)

                        (listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
                    }
                }
            }
    }

    private fun chatSelected(chat: Chat){
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chat.id)
        intent.putExtra("user", emailUserLog)
        startActivity(intent)
    }

    private fun newChat(){
        val chatId = UUID.randomUUID().toString()
        val otherUser = emailOwnerBook
        val users = listOf(nameUserLog, ownerName)

        val chat = Chat(
                id = chatId,
                idBook = idBook,
                name = "Chat entre $users para el libro de $tituloBook",
                users = users,
                idOwner = emailOwnerBook,
                idUser = emailUserLog
        )

        val chatUserLog = Chat(
            id = chatId,
            idBook = idBook,
            name = "Chat con $ownerName para el libro de $tituloBook",
            users = users,
            idOwner = emailOwnerBook,
            idUser = emailUserLog
        )

        val chatOwner = Chat(
                id = chatId,
                idBook = idBook,
                name = "Chat con $nameUserLog para el libro de $tituloBook",
                users = users,
                idOwner = emailOwnerBook,
                idUser = emailUserLog
        )

        db.collection("Chats").document(chatId).set(chat)
        db.collection("Users").document(emailUserLog).collection("Chats").document(chatId).set(chatUserLog)
        db.collection("Users").document(otherUser).collection("Chats").document(chatId).set(chatOwner)

        startChat(chatId)
    }

    private fun startChat(chatId: String) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chatId)
        intent.putExtra("user", emailUserLog)
        intent.putExtra("titleChat", "Chat con $ownerName para el libro de $tituloBook")
        startActivity(intent)
    }

    private fun getnameUserLog(username: String) {
        db.collection("Users").document(username).get().addOnSuccessListener { documentSnapshot ->
            nameUserLog = (documentSnapshot["firstname"].toString()
                    + " " + documentSnapshot["lastname"].toString())
        }
    }
}