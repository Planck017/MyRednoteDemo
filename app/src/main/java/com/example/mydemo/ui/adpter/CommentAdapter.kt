package com.example.mydemo.ui.adpter

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
import com.example.mydemo.data.dto.CommentRecycleViewItem
import com.example.mydemo.data.model.Comment

class CommentAdapter(private val commentList: List<CommentRecycleViewItem>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameText: TextView = itemView.findViewById(R.id.userNameText)
        val commentContentText: TextView = itemView.findViewById(R.id.commentContentText)
        val likeCountText: TextView = itemView.findViewById(R.id.likeCountText)
        val userAvatar: ImageView = itemView.findViewById(R.id.userAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = commentList[position]
        // 这里简化处理，实际应该通过userId获取用户名
        holder.userNameText.text = "用户${comment.userName}"
        holder.commentContentText.text = comment.commentContent


        val imageUrl = BuildConfig.API_BASE_URL + "images/getImage/${comment.avatar}"
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_user) // 设置占位图
            .error(R.drawable.ic_launcher_background) // 设置错误图片
            .into(holder.userAvatar)

        Log.d("NoteDetailActivity", "开始加载用户头像: $imageUrl")

    }

    override fun getItemCount(): Int = commentList.size
}