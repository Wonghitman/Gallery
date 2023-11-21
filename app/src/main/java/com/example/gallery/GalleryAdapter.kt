package com.example.gallery

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.gallery.databinding.GalleryCellBinding


class GalleryAdapter:androidx.recyclerview.widget.ListAdapter<PhotoItem,MyViewHolder>(DiffCallback){

    private lateinit var binding: GalleryCellBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val holder = MyViewHolder(View.inflate(parent.context,R.layout.gallery_cell,parent))
        val holder  = MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gallery_cell,parent,false))
        holder.itemView.setOnClickListener{
            Bundle().apply {
                putParcelable("PHOTO",getItem(holder.adapterPosition))
                holder.itemView.findNavController().navigate(R.id.action_galleryFragment_to_photoFragment,this)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        binding = GalleryCellBinding.bind(holder.itemView)

        binding.shimmerLayoutCell.apply {
            setShimmerColor(0x55FFFFFF)
            setShimmerAngle(0)
            startShimmerAnimation()
        }
        Glide.with(holder.itemView)
                .load(getItem(position).previewURL)
                .placeholder(R.drawable.ic_photo_gray_24)
                .listener(object :RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        return false.also { binding.shimmerLayoutCell?.stopShimmerAnimation() }
                    }

                })
                .into(binding.imageView)
    }
    object DiffCallback:androidx.recyclerview.widget.DiffUtil.ItemCallback<PhotoItem>(){
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return  oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.photoId == newItem.photoId
        }

    }
}
class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
