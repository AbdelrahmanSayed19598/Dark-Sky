package com.example.weatherforecast.ui.alarm.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.Repository
import com.example.weatherforecast.ui.alarm.adapter.AlertApadter
import com.example.weatherforecast.ui.alarm.viewModel.AlertViewModel
import com.example.weatherforecast.ui.alarm.viewModel.AlertViewModelFactory
import com.example.weatherforecast.ui.home.view.adapter.HourlyAdapter
import kotlinx.android.synthetic.main.fragment_alert.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.flow.collect


class AlertFragment : Fragment() {
    private val viewModel : AlertViewModel by viewModels {
        AlertViewModelFactory(Repository.getRepoInstance(requireActivity().application))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alert_recycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        alert_recycler.hasFixedSize()

        viewModel.getAlerts()
        lifecycleScope.launchWhenStarted {
            viewModel.alertList.collect {
                val alertAdapter = AlertApadter(it ?: emptyList(), requireContext(),viewModel)
                alert_recycler.adapter = alertAdapter
                alertAdapter.notifyDataSetChanged()
            }
        }



        alert_btn.setOnClickListener(View.OnClickListener {
            AlertDialog().show(requireActivity().supportFragmentManager, "AlertDialog")
        })
    }

}