package com.shahriarniloy.funchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ChatActivity : AppCompatActivity() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference

    var SenderRoom: String? = null
    var ReceiverRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        mDbRef = FirebaseDatabase.getInstance().reference

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        SenderRoom = receiverUid + senderUid
        ReceiverRoom = senderUid + receiverUid


        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        getSupportActionBar()?.setCustomView(R.layout.custom_action_bar)
        val actionBarTitle = findViewById<TextView>(R.id.action_bar_title)
        actionBarTitle.setText(name)

        messageRecyclerView = findViewById(R.id.chatRecyclerView)
        sendButton = findViewById(R.id.msgSendBtn)
        messageBox = findViewById(R.id.msgTextBox)



        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList, ReceiverRoom)

        val layoutManager = LinearLayoutManager(this)
        messageRecyclerView.layoutManager = layoutManager
        messageRecyclerView.adapter = messageAdapter

        mDbRef.child("chats").child(ReceiverRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children){
                        var message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                    if(messageList.size > 1){
                        messageRecyclerView.smoothScrollToPosition(messageList.size -1)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        sendButton.setOnClickListener{
            val message = messageBox.text.toString()

            val current = LocalDateTime.now()

            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            val timeWithDate = current.format(formatter)


            val messageObject = Message(message,senderUid, timeWithDate)
            mDbRef.child("chats").child(SenderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(ReceiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")
        }
        messageBox.setOnFocusChangeListener{ view, b ->
            scrollChat()
        }
        messageBox.setOnClickListener{
            scrollChat()
        }




    }

    fun scrollChat(){
        Handler(Looper.getMainLooper()).postDelayed({
            if(messageList.size > 1){
                messageRecyclerView.smoothScrollToPosition(messageList.size +5)
            }
        },500)
    }
}


