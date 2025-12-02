package com.example.mydemo.ui.adpter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mydemo.R
import com.example.mydemo.data.model.Image

class ImageAdapter(private val imageList: List<Image>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = imageList[position]
        // 使用Glide加载图片
        Glide.with(holder.imageView.context)
            .load(image.imageUrl)
            .placeholder(R.drawable.ic_home) // 设置占位图
            .error(R.drawable.ic_add) // 设置错误图片
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = imageList.size
}