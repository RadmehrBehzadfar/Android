package com.radmehr.roamto_radmehr.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.radmehr.roamto_radmehr.data.Place
import com.radmehr.roamto_radmehr.databinding.ItemPlaceBinding

class PlaceAdapter(
    private val onEdit:   (Place)->Unit,
    private val onDelete: (Place)->Unit,
    private val onClick:  (Place)->Unit
) : ListAdapter<Place, PlaceAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(getItem(position))

    inner class VH(private val b: ItemPlaceBinding)
        : RecyclerView.ViewHolder(b.root) {

        fun bind(p: Place) {
            b.tvTitle.text = p.title
            b.tvDate.text  = p.date

            b.btnEdit.setOnClickListener   { onEdit(p) }
            b.btnDelete.setOnClickListener { onDelete(p) }
            b.root.setOnClickListener      { onClick(p) }
        }
    }

    companion object {
        private val DIFF = object: DiffUtil.ItemCallback<Place>() {
            override fun areItemsTheSame(a: Place, b: Place)    = a.id == b.id
            override fun areContentsTheSame(a: Place, b: Place) = a == b
        }
    }
}