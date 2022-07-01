package com.shahriarniloy.funchat

import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.provider.Telephony
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>, val ReceiverRoom: String?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mDbRef: DatabaseReference
    val SENT = 1
    val REVEIVE = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         if(viewType == SENT){
             val view: View = LayoutInflater.from(context).inflate(R.layout.sent_layout, parent, false)
             return SentViewHolder(view)
         }else{
             val view: View = LayoutInflater.from(context).inflate(R.layout.receive_layout, parent, false)
             return ReceiveViewHolder(view)
         }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var currentMessage = messageList[position]
        val animationFadeIn = AnimationUtils.loadAnimation(context,R.anim.fade_in)
        val animationFadeOut = AnimationUtils.loadAnimation(context,R.anim.fade_out)

        if(holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder

            viewHolder.sentMessage.text = currentMessage.message
            viewHolder.sentMessage.setOnClickListener{
                viewHolder.sendingTimeTxtBox.text = currentMessage.sendingTime
                if (viewHolder.sendingTimeTxtBox.visibility == View.VISIBLE){
                    viewHolder.sendingTimeTxtBox.startAnimation(animationFadeOut)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewHolder.sendingTimeTxtBox.visibility = View.GONE
                    }, 500)
                } else{
                    viewHolder.sendingTimeTxtBox.visibility = View.VISIBLE
                    viewHolder.sendingTimeTxtBox.startAnimation(animationFadeIn)
                }
            }
        }else{
            var viewHolder = holder as ReceiveViewHolder
            viewHolder.receivingTimeTxtBox.text = currentMessage.sendingTime
            viewHolder.receiveMessage.text = currentMessage.message

            mDbRef = Firebase.database.reference
            viewHolder.receiveMessage.viewTreeObserver.addOnGlobalLayoutListener {
                when (viewHolder.receiveMessage.visibility){

                }
            }


            viewHolder.receiveMessage.setOnClickListener{
                viewHolder.receivingTimeTxtBox
                if (viewHolder.receivingTimeTxtBox.visibility == View.VISIBLE){
                    viewHolder.receivingTimeTxtBox.startAnimation(animationFadeOut)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewHolder.receivingTimeTxtBox.visibility = View.GONE
                    }, 500)
                } else{
                    viewHolder.receivingTimeTxtBox.visibility = View.VISIBLE
                    viewHolder.receivingTimeTxtBox.startAnimation(animationFadeIn)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            SENT
        }else{
            REVEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
    class SentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sentMessage =itemView.findViewById<TextView>(R.id.txtSentMessage)
        val sendingTimeTxtBox =itemView.findViewById<TextView>(R.id.sendingTimeTxtBox)
    }
    class ReceiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val receiveMessage =itemView.findViewById<TextView>(R.id.txtReceiveMessage)
        val receivingTimeTxtBox =itemView.findViewById<TextView>(R.id.receivingTimeTxtBox)

    }

    private fun updateView(){

        mDbRef.child("chats").child(ReceiverRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.childrenCount
                    for(postSnapshot in snapshot.children){
                        var message = postSnapshot.getValue(Message::class.java)
                        if(message?.viewed == false){
                            message?.viewed = true
                            Toast.makeText(context, "viewed", Toast.LENGTH_SHORT).show()
                        }

                        mDbRef.child("chats").child(ReceiverRoom!!).child("messages")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}