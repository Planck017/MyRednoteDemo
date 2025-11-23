package com.example.mydemo.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mydemo.R
import com.example.mydemo.data.model.Note

class NoteAdapter(private val noteList: MutableList<Note>) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val likeCountText: TextView = itemView.findViewById(R.id.likeCountText)
        val commentCountText: TextView = itemView.findViewById(R.id.commentCountText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        // 绑定数据到视图
        holder.titleText.text = note.noteTitle
        holder.likeCountText.text = "Likes: ${note.likes}"
        holder.commentCountText.text = "Comments: ${note.comments}"
        // 这里可以加载图片，暂时使用占位符
    }

    override fun getItemCount(): Int = noteList.size
}