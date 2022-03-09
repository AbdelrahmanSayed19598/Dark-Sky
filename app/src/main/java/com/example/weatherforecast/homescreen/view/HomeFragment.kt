package com.example.weatherforecast.homescreen.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.model.HoursPojo
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {
    val hoursPojo = ArrayList<HoursPojo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {

            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hoursPojo.add(HoursPojo("1 am",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add( HoursPojo("2 am",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("3 am",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("4 am",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("5 am",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("6 am",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("7 am",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("8 am",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("9 am",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("10 am",R.drawable.ic_baseline_wb_sunny_24,"25 c"))
        hoursPojo.add(HoursPojo("11 am",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("12 am",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("1 pm",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("2 pm",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("3 pm",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("4 pm",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("5 pm",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("6 pm",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("7 pm",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("8 pm",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("9 pm",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("10 pm",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("11 pm",R.drawable.ic_moon_svgrepo_com,"25 c"))
        hoursPojo.add(HoursPojo("12 pm",R.drawable.ic_moon_svgrepo_com,"25 c"))

        hourly_recycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL,false)


        val myadaptet = HourlyAdapter(hoursPojo)
        hourly_recycler.adapter = myadaptet

    }
}