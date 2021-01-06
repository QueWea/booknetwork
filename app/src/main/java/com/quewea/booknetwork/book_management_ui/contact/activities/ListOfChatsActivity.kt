package com.quewea.booknetwork.book_management_ui.contact.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
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
    private var user = ""
    private var titulo = ""
    private var owner = ""
    private var email = ""
    private var nameUser = ""

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_chats)

        val progressDialog = ProgressDialog(this@ListOfChatsActivity)

        val prefs = getSharedPreferences("user", Context.MODE_PRIVATE)
        user = prefs.getString("username", "").toString()


        progressDialog.setMessage("Cargando información...")
        progressDialog.show()
        titulo = intent.extras?.getString("titulo").toString()
        owner = intent.extras?.getString("owner").toString()
        email = intent.extras?.getString("email").toString()
        idBook = intent.extras?.getString("idBook").toString()
        getnameUserLog(user)
        progressDialog.dismiss()

        Toast.makeText(this, "...$nameUser", Toast.LENGTH_SHORT).show();
        if (user != null) {
            if (user.isNotEmpty()){
                initViews()
            }
        }
    }

    private fun initViews() {
        var idChat = ""
            db.collection("Chats").document(idBook).get().addOnSuccessListener { documentSnapshot ->
                idChat = (documentSnapshot["id"].toString())
            }
        if (idChat != null && idChat.isNotEmpty()) startChat(idChat)
        else if (email.isNotEmpty()) newChat()


            listChatsRecyclerView.layoutManager = LinearLayoutManager(this)
        listChatsRecyclerView.adapter =
            ChatAdapter { chat ->
                chatSelected(chat)
            }

        val userRef = db.collection("Users").document(user)

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
        intent.putExtra("user", user)
        startActivity(intent)
    }

    private fun newChat(){
        val chatId = UUID.randomUUID().toString()
        val otherUser = email
        val users = listOf(nameUser, owner)

        val chat = Chat(
            id = chatId,
            idBook = idBook,
            name = "Chat con $owner para el libro de $titulo",
            users = users
        )

        db.collection("Chats").document(chatId).set(chat)
        db.collection("Users").document(user).collection("Chats").document(chatId).set(chat)
        db.collection("Users").document(otherUser).collection("Chats").document(chatId).set(chat)

        startChat(chatId)
    }

    private fun startChat(chatId: String) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chatId)
        intent.putExtra("user", user)
        intent.putExtra("titleChat", "Chat con $owner para el libro de $titulo")
        startActivity(intent)
    }

    private fun getnameUserLog(username: String) {
        db.collection("Users").document(username).get().addOnSuccessListener { documentSnapshot ->
            nameUser = (documentSnapshot["firstname"].toString()
                    + " " + documentSnapshot["lastname"].toString())
        }
    }
}