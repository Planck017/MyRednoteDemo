package com.example.mydemo.ui.adpter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mydemo.R

class SelectedImageAdapter(
    private val imageUris: MutableList<Uri>,
    private val onImageRemoveListener: OnImageRemoveListener
) : RecyclerView.Adapter<SelectedImageAdapter.SelectedImageViewHolder>() {

    interface OnImageRemoveListener {
        fun onImageRemove(position: Int)
    }

    class SelectedImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewSelected: ImageView = itemView.findViewById(R.id.imageViewSelected)
        val imageViewRemove: ImageView = itemView.findViewById(R.id.imageViewRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_selected_image, parent, false)
        return SelectedImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectedImageViewHolder, position: Int) {
        val uri = imageUris[position]

        // 使用Glide加载图片
        Glide.with(holder.imageViewSelected.context)
            .load(uri)
            .placeholder(R.drawable.ic_home)
            .error(R.drawable.ic_add)
            .into(holder.imageViewSelected)

        // 设置删除按钮点击事件
        holder.imageViewRemove.setOnClickListener {
            onImageRemoveListener.onImageRemove(position)
        }
    }

    override fun getItemCount(): Int = imageUris.size

    fun removeImage(position: Int) {
        if (position >= 0 && position < imageUris.size) {
            imageUris.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun addImage(uri: Uri) {
        imageUris.add(uri)
        notifyItemInserted(imageUris.size - 1)
    }
}
