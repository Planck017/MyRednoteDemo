package com.example.mydemo.ui.adpter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mydemo.BuildConfig
import com.example.mydemo.R
import com.example.mydemo.data.dto.NoteFragmentItem
import com.example.mydemo.data.model.Note
import com.example.mydemo.ui.activity.NoteDetailActivity

class NoteAdapter(private val noteList: MutableList<NoteFragmentItem>) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val likeCountText: TextView = itemView.findViewById(R.id.likeCountText)
        val userNameText: TextView = itemView.findViewById(R.id.usernameText)
        val avatarImage: ImageView = itemView.findViewById(R.id.avatarImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        // 绑定数据到视图
        holder.titleText.text = note.title
        holder.likeCountText.text = "Likes: ${note.likes}"
        holder.userNameText.text = note.userName


        // 加载封面图片
        if (note.cover != null) {
            val imageUrl = BuildConfig.API_BASE_URL + "images/getImage/${note.cover}"
            // 加载封面图片
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView)
        } else {
            // 如果没有封面图片，加载默认图片
            holder.imageView.setImageResource(R.drawable.ic_launcher_background)
        }

        // 加载用户头像
        if (note.avatar != null) {
            val avatarUrl = BuildConfig.API_BASE_URL + "images/getImage/${note.avatar}"
            Glide.with(holder.itemView.context)
                .load(avatarUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.avatarImage)
        } else {
            holder.avatarImage.setImageResource(R.drawable.ic_launcher_background)
        }



        // 设置点击事件
        holder.itemView.setOnClickListener {
            Log.d("NoteAdapter", "点击了笔记: ${note.title}, ID: ${note.id}")

            val context = holder.itemView.context
            val intent = Intent(context, NoteDetailActivity::class.java)
            // 只传递Note的ID
            intent.putExtra(NoteDetailActivity.Companion.EXTRA_NOTE_ID, note.id)
            intent.putExtra(NoteDetailActivity.Companion.EXTRA_NOTE_TITLE, note.title)
            try {
                context.startActivity(intent)
                Log.d("NoteAdapter", "成功启动NoteDetailActivity")
            } catch (e: Exception) {
                Log.e("NoteAdapter", "启动NoteDetailActivity失败", e)
            }
        }
    }

    override fun getItemCount(): Int = noteList.size
}