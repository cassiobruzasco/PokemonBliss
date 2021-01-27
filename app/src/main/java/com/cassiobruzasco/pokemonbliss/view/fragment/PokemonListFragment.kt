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
import androidx.recyclerview.widget.LinearLayoutManager
import com.cassiobruzasco.pokemonbliss.R
import com.cassiobruzasco.pokemonbliss.databinding.FragmentPokemonListBinding
import com.cassiobruzasco.pokemonbliss.util.ViewUtil
import com.cassiobruzasco.pokemonbliss.view.fragment.adapter.PokemonListRecyclerAdapter
import com.cassiobruzasco.pokemonbliss.view.viewmodel.PokemonModel
import com.cassiobruzasco.pokemonbliss.view.viewmodel.PokemonViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PokemonListFragment: Fragment() {

    companion object {
        const val LIMIT = 151
    }

    private val mViewModel by sharedViewModel<PokemonViewModel>()
    private lateinit var mBinding: FragmentPokemonListBinding
    private val loadingDialog: Dialog? by lazy {
        ViewUtil.getLoadingDialog(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pokemon_list,
            container, false
        )

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureObservables()
        mViewModel.initialize()
        mViewModel.getPokemonList(LIMIT)
    }

    private fun configureComponents() {
        mBinding.recycler.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recycler.adapter = PokemonListRecyclerAdapter(mViewModel.model.pokemonListOb.value!!, ::navToDetails)
    }

    private fun navToDetails(id: Int) {
        mViewModel.model.pokemonIdOb.value = id
        findNavController().navigate(R.id.pokemon_details_fragment_dest)
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
                Snackbar.make(requireView(), getString(R.string.snack_bar_generic_error), Snackbar.LENGTH_LONG)
            }
        }
    }
}