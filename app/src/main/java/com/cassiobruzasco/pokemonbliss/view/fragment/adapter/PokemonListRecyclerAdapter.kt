package com.cassiobruzasco.pokemonbliss.view.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cassiobruzasco.pokemonbliss.R
import com.cassiobruzasco.pokemonbliss.data.PokemonListResponseItem
import com.cassiobruzasco.pokemonbliss.data.api.BaseConfig.Companion.ICON_BASE_URL
import kotlinx.android.synthetic.main.item_pokemon_list_recycler.view.*
import java.text.Normalizer
import java.util.*

class PokemonListRecyclerAdapter(
    private val navToDetails: (id: Int) -> Unit
) : RecyclerView.Adapter<PokemonListRecyclerAdapter.ViewHolder>() {

    private val items = mutableListOf<PokemonListResponseItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokemonListRecyclerAdapter.ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon_list_recycler, parent, false)

        return ViewHolder(layout)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PokemonListRecyclerAdapter.ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() = with(itemView) {
            val pos = adapterPosition
            val item = items[pos]

            name.text = item.name.capitalize(Locale.getDefault())
            val pokemonId = item.url.substringAfter("pokemon/").replace("/","" )
            Glide.with(context)
                .asBitmap()
                .load(ICON_BASE_URL.plus(pokemonId.plus(".png")))
                .into(icon)

            card_layout.setOnClickListener {
                navToDetails.invoke(pokemonId.toInt())
            }
        }
    }

    private fun String.normalizeString(): String {
        return Normalizer.normalize(this, Normalizer.Form.NFD).replace(Regex("[^\\p{ASCII}]"), "").toLowerCase(
            Locale.getDefault())
    }

    fun filter(text: String) {
        val newList = mutableListOf<PokemonListResponseItem>()
        items.forEach { item ->
            if (item.name.normalizeString().contains(text.normalizeString())) {
                newList.add(item)
            }
        }
        updateItems(newList)
    }

    fun updateItems(newItems: MutableList<PokemonListResponseItem>) {
        items.apply {
            items.clear()
            items.addAll(newItems)
        }
        notifyDataSetChanged()
    }
}
