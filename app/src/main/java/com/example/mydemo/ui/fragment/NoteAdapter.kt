package com.example.mydemo.ui.fragment

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mydemo.R
import com.example.mydemo.data.model.Note
import com.example.mydemo.ui.activity.NoteDetailActivity

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

        // 设置点击事件
        holder.itemView.setOnClickListener {
            Log.d("NoteAdapter", "点击了笔记: ${note.noteTitle}, ID: ${note.noteId}")
            
            val context = holder.itemView.context
            val intent = Intent(context, NoteDetailActivity::class.java)
            // 只传递Note的ID
            intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, note.noteId)
            intent.putExtra(NoteDetailActivity.EXTRA_NOTE_TITLE, note.noteTitle)
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