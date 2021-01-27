package com.cassiobruzasco.pokemonbliss.view.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cassiobruzasco.pokemonbliss.R
import com.cassiobruzasco.pokemonbliss.data.MovesModel
import kotlinx.android.synthetic.main.item_pokemon_details_recycler.view.*
import java.util.*

class PokemonDetailsRecyclerAdapter(
    private var items: MutableList<MovesModel>
) : RecyclerView.Adapter<PokemonDetailsRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokemonDetailsRecyclerAdapter.ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon_details_recycler, parent, false)

        return ViewHolder(layout)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PokemonDetailsRecyclerAdapter.ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind() = with(itemView) {
            val pos = adapterPosition
            val item = items[pos]

            move_name.text = item.move.name
            if (item.version[0].levelLearnedAt == 0) {
                learned_at.text = context.getString(R.string.pokemon_details_fragment_learn_from)
            } else {
                learned_at.text = context.getString(R.string.pokemon_details_fragment_learn_at, item.version[0].levelLearnedAt.toString())
            }
        }
    }
}
