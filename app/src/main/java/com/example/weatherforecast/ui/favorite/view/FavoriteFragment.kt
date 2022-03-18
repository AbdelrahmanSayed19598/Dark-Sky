package com.example.weatherforecast.ui.favorite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.Repository
import com.example.weatherforecast.ui.favorite.viewModel.FavoriteViewModel
import com.example.weatherforecast.ui.favorite.viewModel.FavoriteViewModelFactory
import kotlinx.android.synthetic.main.fragment_favorite.*


class FavoriteFragment : Fragment() {
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fav_btn.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_favoriteFragment_to_map2)

        })

        fav_recycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        fav_recycler.hasFixedSize()

        viewModel =
            ViewModelProvider(
                this,
                FavoriteViewModelFactory(Repository.getRepoInstance(requireActivity().application))
            )
                .get(FavoriteViewModel::class.java)
        viewModel.getData()
        viewModel.favoriteList.observe(viewLifecycleOwner){
            val adapter = FavoriteAdapter(it?: emptyList(),requireContext(),viewModel)
            fav_recycler.adapter = adapter
        }
    }


}