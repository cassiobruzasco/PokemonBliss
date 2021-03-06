package com.cassiobruzasco.pokemonbliss.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.text.HtmlCompat
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
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PokemonListFragment: Fragment() {

    companion object {
        const val FIRST_GEN = 151
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
        mViewModel.getPokemonList(FIRST_GEN)
    }

    private fun configureComponents() {
        mBinding.recycler.layoutManager = LinearLayoutManager(requireContext())
        val pokemonList = mViewModel.model.pokemonListOb.value
        mBinding.search.queryHint = getString(R.string.pokemon_list_fragment_title)
        mBinding.recycler.adapter = PokemonListRecyclerAdapter(::navToDetails)
        (mBinding.recycler.adapter as PokemonListRecyclerAdapter).updateItems(pokemonList!!.result)

        mBinding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (newText.length > 1) {
                        (mBinding.recycler.adapter as PokemonListRecyclerAdapter).filter(newText)
                        if ((mBinding.recycler.adapter as PokemonListRecyclerAdapter).itemCount > 0) {
                            mBinding.recycler.visibility = View.VISIBLE
                            mBinding.notFound.visibility = View.GONE
                        } else {
                            mBinding.recycler.visibility = View.GONE
                            mBinding.notFound.visibility = View.VISIBLE
                        }
                    } else {
                        mBinding.recycler.visibility = View.VISIBLE
                        mBinding.notFound.visibility = View.GONE
                        (mBinding.recycler.adapter as PokemonListRecyclerAdapter).updateItems(pokemonList.result)
                    }
                }
                return false
            }

        })
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
                Toast.makeText(
                    context,
                    getString(R.string.snack_bar_generic_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}