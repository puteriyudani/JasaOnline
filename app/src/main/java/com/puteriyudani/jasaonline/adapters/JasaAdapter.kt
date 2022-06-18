package com.puteriyudani.jasaonline.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.puteriyudani.jasaonline.R
import com.puteriyudani.jasaonline.helpers.Config
import com.puteriyudani.jasaonline.models.Jasa
import kotlinx.android.synthetic.main.jasa_list.view.*

class JasaAdapter (private val jasaList: List<Jasa>,
                   private val adapterOnClick: (Jasa) -> Unit) :
    RecyclerView.Adapter<JasaAdapter.ServiceHolder>(){
    inner class ServiceHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        fun bindHero(jasa: Jasa) {
            itemView.apply {
                tvNamaJasa.text = jasa.namaJasa
                tvDeskripsiSingkat.text = jasa.deskripsiSingkat
                tvRating.text = jasa.rating.toString()
                // tampilkan gambar dengan library glide pada view imgList
                Glide.with(context)
                    .load(Config.IMAGE_URL + jasa.gambar)
                    .into(imageView)
                setOnClickListener {
                    adapterOnClick(jasa)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ServiceHolder {
        return ServiceHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.jasa_list, parent,false))
    }
    override fun getItemCount(): Int {
        return jasaList.size
    }
    override fun onBindViewHolder(holder: ServiceHolder, position: Int) {
        holder.bindHero(jasaList[position])
    }
}