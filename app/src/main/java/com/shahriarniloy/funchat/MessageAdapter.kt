package com.shahriarniloy.funchat

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
}