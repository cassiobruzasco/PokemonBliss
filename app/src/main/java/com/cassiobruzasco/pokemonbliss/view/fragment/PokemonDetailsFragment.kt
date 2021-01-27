package com.cassiobruzasco.pokemonbliss.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cassiobruzasco.pokemonbliss.R
import com.cassiobruzasco.pokemonbliss.data.api.BaseConfig
import com.cassiobruzasco.pokemonbliss.databinding.FragmentPokemonDetailsBinding
import com.cassiobruzasco.pokemonbliss.util.ViewUtil
import com.cassiobruzasco.pokemonbliss.view.MainActivity
import com.cassiobruzasco.pokemonbliss.view.fragment.adapter.PokemonDetailsRecyclerAdapter
import com.cassiobruzasco.pokemonbliss.view.fragment.adapter.PokemonListRecyclerAdapter
import com.cassiobruzasco.pokemonbliss.view.viewmodel.PokemonModel
import com.cassiobruzasco.pokemonbliss.view.viewmodel.PokemonViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_pokemon_list_recycler.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class PokemonDetailsFragment : Fragment() {

    private val mViewModel by sharedViewModel<PokemonViewModel>()
    private lateinit var mBinding: FragmentPokemonDetailsBinding
    private val loadingDialog: Dialog? by lazy {
        ViewUtil.getLoadingDialog(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pokemon_details,
            container, false
        )

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureObservables()
        mViewModel.initialize()
        mViewModel.getPokemon(mViewModel.model.pokemonIdOb.value ?: 0)
    }

    private fun configureComponents() {
        val pokemonData = mViewModel.model.pokemonDetailsOb.value
        Glide.with(requireContext())
            .asBitmap()
            .load(BaseConfig.ICON_BASE_URL.plus(pokemonData?.id.toString().plus(".png")))
            .into(mBinding.icon)
        var pokemonType = ""
        if (pokemonData?.types != null) {
            pokemonType = if (pokemonData.types.size > 1) {
                "${pokemonData.types[0].type.name.capitalize(Locale.getDefault())}/${
                    pokemonData.types[1].type.name.capitalize(Locale.getDefault())
                }"
            } else {
                pokemonData.types[0].type.name
            }
        }

        mBinding.title.text = getString(R.string.pokemon_details_fragment_pokemon_name,
            pokemonData?.name?.capitalize(Locale.getDefault()))
        mBinding.height.text = getString(R.string.pokemon_details_fragment_height, pokemonData?.height.toString())
        mBinding.weight.text = getString(R.string.pokemon_details_fragment_weight, pokemonData?.weight.toString())
        mBinding.baseXp.text = getString(R.string.pokemon_details_fragment_base_xp, pokemonData?.exp.toString())
        mBinding.type.text = getString(R.string.pokemon_details_fragment_type, pokemonType)
        mBinding.recycler.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recycler.adapter = PokemonDetailsRecyclerAdapter(pokemonData?.moves!!)
    }

    private fun configureObservables() {
        mViewModel.model.pokemonStateOb.observe(viewLifecycleOwner, Observer {
            handleState(it)
        })
        mViewModel.model.pokemonLoadedOb.observe(viewLifecycleOwner, Observer {
            if (it) configureComponents()
        })
    }

    private fun handleState(state: PokemonModel.PokemonState?) {
        when (state) {
            is PokemonModel.PokemonState.Loading -> {
                if (state.isLoading) {
                    loadingDialog?.show()
                } else {
                    loadingDialog?.dismiss()
                }
            }
            is PokemonModel.PokemonState.GenericError -> {
                Snackbar.make(
                    requireView(),
                    getString(R.string.snack_bar_generic_error),
                    Snackbar.LENGTH_LONG
                )
            }
        }
    }
}