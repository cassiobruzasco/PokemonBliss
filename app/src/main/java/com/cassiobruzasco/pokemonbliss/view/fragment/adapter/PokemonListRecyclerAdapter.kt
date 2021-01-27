package com.cassiobruzasco.pokemonbliss.view.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cassiobruzasco.pokemonbliss.R
import com.cassiobruzasco.pokemonbliss.data.PokemonListResponse
import com.cassiobruzasco.pokemonbliss.data.api.BaseConfig.Companion.ICON_BASE_URL
import kotlinx.android.synthetic.main.item_pokemon_list_recycler.view.*
import java.util.*

class PokemonListRecyclerAdapter(
    private var item: PokemonListResponse,
    private val navToDetails: (id: Int) -> Unit
) : RecyclerView.Adapter<PokemonListRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokemonListRecyclerAdapter.ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon_list_recycler, parent, false)

        return ViewHolder(layout)
    }

    override fun getItemCount() = item.result.size

    override fun onBindViewHolder(holder: PokemonListRecyclerAdapter.ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() = with(itemView) {
            val pos = adapterPosition

            name.text = item.result[pos].name.capitalize(Locale.getDefault())
            val pokemonId = item.result[pos].url.substringAfter("pokemon/").replace("/","" )
            Glide.with(context)
                .asBitmap()
                .load(ICON_BASE_URL.plus(pokemonId.plus(".png")))
                .into(icon)

            card_layout.setOnClickListener {
                navToDetails.invoke(pokemonId.toInt())
            }
        }
    }
}
