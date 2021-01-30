package com.cassiobruzasco.pokemonbliss.view.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cassiobruzasco.pokemonbliss.R
import com.cassiobruzasco.pokemonbliss.data.PokemonDetailsResponse
import com.cassiobruzasco.pokemonbliss.data.api.BaseConfig
import com.cassiobruzasco.pokemonbliss.databinding.FragmentPokemonDetailsBinding
import com.cassiobruzasco.pokemonbliss.util.ViewUtil
import com.cassiobruzasco.pokemonbliss.view.MainActivity
import com.cassiobruzasco.pokemonbliss.view.fragment.adapter.PokemonDetailsRecyclerAdapter
import com.cassiobruzasco.pokemonbliss.view.viewmodel.PokemonModel
import com.cassiobruzasco.pokemonbliss.view.viewmodel.PokemonViewModel
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

        NavigationUI.setupActionBarWithNavController(
            (activity as MainActivity),
            findNavController()
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
        pokemonData?.let { initializeValues(it) }
        pokemonData?.let { configureFavorite(it) }
        animateProgressBars()
    }

    private fun initializeValues(pokemonData: PokemonDetailsResponse) {
        Glide.with(requireContext())
            .asBitmap()
            .load(BaseConfig.ICON_BASE_URL.plus(pokemonData.id.toString().plus(".png")))
            .into(mBinding.icon)
        val pokemonType = if (pokemonData.types.size > 1) {
            "${pokemonData.types[0].type.name.capitalize(Locale.getDefault())}/${
                pokemonData.types[1].type.name.capitalize(Locale.getDefault())
            }"
        } else {
            pokemonData.types[0].type.name.capitalize(Locale.getDefault())
        }

        mBinding.title.text = getString(R.string.pokemon_details_fragment_pokemon_name,
            pokemonData.name.capitalize(Locale.getDefault()))
        mBinding.height.text = getString(R.string.pokemon_details_fragment_height, pokemonData.height.toString())
        mBinding.weight.text = getString(R.string.pokemon_details_fragment_weight, pokemonData.weight.toString())
        mBinding.baseXp.text = getString(R.string.pokemon_details_fragment_base_xp, pokemonData.exp.toString())
        mBinding.type.text = getString(R.string.pokemon_details_fragment_type, pokemonType)
        mBinding.recycler.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recycler.adapter = PokemonDetailsRecyclerAdapter(pokemonData.moves)
        mBinding.hpProgress.progress = pokemonData.stats[0].baseStat
        mBinding.hpValue.text = pokemonData.stats[0].baseStat.toString()
        mBinding.attackProgress.progress = pokemonData.stats[1].baseStat
        mBinding.attackValue.text = pokemonData.stats[1].baseStat.toString()
        mBinding.defenseProgress.progress = pokemonData.stats[2].baseStat
        mBinding.defenseValue.text = pokemonData.stats[2].baseStat.toString()
        mBinding.satkProgress.progress = pokemonData.stats[3].baseStat
        mBinding.satkValue.text = pokemonData.stats[3].baseStat.toString()
        mBinding.sdefProgress.progress = pokemonData.stats[4].baseStat
        mBinding.sdefValue.text = pokemonData.stats[4].baseStat.toString()
        mBinding.speedProgress.progress = pokemonData.stats[5].baseStat
        mBinding.speedValue.text = pokemonData.stats[5].baseStat.toString()
    }

    private fun configureFavorite(pokemonData: PokemonDetailsResponse) {
        val prefKey = "${BaseConfig.SHARED_PREF_FAV_KEY}_${pokemonData.id}"
        val sharedPref = activity?.getSharedPreferences(prefKey, Context.MODE_PRIVATE) ?: return
        var isFavorite = sharedPref.getBoolean(prefKey, false)
        val image = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_empty
        mBinding.fav.setImageResource(image)
        mBinding.fav.setOnClickListener {
            if (isFavorite) {
                mBinding.fav.setImageResource(R.drawable.ic_favorite_empty)
                with (sharedPref.edit()) {
                    putBoolean(prefKey, false)
                    apply()
                }
                isFavorite = false
            } else {
                mViewModel.saveFavorite(pokemonData.id, pokemonData.name)
                mBinding.fav.setImageResource(R.drawable.ic_favorite)
                with (sharedPref.edit()) {
                    putBoolean(prefKey, true)
                    apply()
                }
                isFavorite = true
            }
        }
    }

    private fun animateProgressBars() {
        ViewUtil.animateProgress(mBinding.hpProgress, mBinding.hpProgress.progress)
        ViewUtil.animateProgress(mBinding.attackProgress, mBinding.attackProgress.progress)
        ViewUtil.animateProgress(mBinding.defenseProgress, mBinding.defenseProgress.progress)
        ViewUtil.animateProgress(mBinding.satkProgress, mBinding.satkProgress.progress)
        ViewUtil.animateProgress(mBinding.sdefProgress, mBinding.sdefProgress.progress)
        ViewUtil.animateProgress(mBinding.speedProgress, mBinding.speedProgress.progress)
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
                Toast.makeText(
                    context,
                    getString(R.string.snack_bar_generic_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}