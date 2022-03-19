package com.example.weatherforecast.ui.alarm.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import com.example.weatherforecast.ui.activity.lat
import com.example.weatherforecast.ui.alarm.adapter.AlertApadter
import com.example.weatherforecast.ui.alarm.viewModel.AlertViewModel
import com.example.weatherforecast.ui.alarm.viewModel.AlertViewModelFactory
import com.example.weatherforecast.ui.home.view.adapter.HourlyAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_alert.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.flow.collect


class AlertFragment : Fragment() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
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
                if(it.isNullOrEmpty()){
                    alarm_video.visibility = View.VISIBLE
                    val alertAdapter = AlertApadter(it ?: emptyList(), requireContext(),viewModel)
                    alert_recycler.adapter = alertAdapter
                    alertAdapter.notifyDataSetChanged()
                }else{
                    alarm_video.visibility = View.INVISIBLE
                    val alertAdapter = AlertApadter(it ?: emptyList(), requireContext(),viewModel)
                    alert_recycler.adapter = alertAdapter
                    alertAdapter.notifyDataSetChanged()
                }

            }
        }



        alert_btn.setOnClickListener(View.OnClickListener {
            if(checkFirstAdd()){
                checkDrawOverlayPermission()
                setFirstAdd()
            }else{
                AlertDialog().show(requireActivity().supportFragmentManager, "AlertDialog")

            }

        })
    }
    private fun checkFirstAdd(): Boolean {
        sharedPreferences = activity?.getSharedPreferences(lat, Context.MODE_PRIVATE)!!
        return sharedPreferences.getBoolean("firstTime", true)
    }

    private fun setFirstAdd(){
        sharedPreferences = activity?.getSharedPreferences(lat, Context.MODE_PRIVATE)!!
        editor = sharedPreferences.edit()
        sharedPreferences.edit().putBoolean("firstTime", false).apply()
    }

     private fun checkDrawOverlayPermission() {
        // Check if we already  have permission to draw over other apps
        if (
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                !Settings.canDrawOverlays(context)
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) {
            // if not construct intent to request permission
            val alertDialogBuilder =
                MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle("We need Your Permission")
                .setMessage("Let's enjoy our features")
                .setPositiveButton("Let's Go") { dialog: DialogInterface, i: Int ->
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + requireContext().applicationContext.packageName)
                    )
                    // request permission via start activity for result
                    startActivityForResult(
                        intent,
                        1
                    ) //It will call onActivityResult Function After you press Yes/No and go Back after giving permission
                    dialog.dismiss()
                    AlertDialog().show(requireActivity().supportFragmentManager, "tag")
                }.setNegativeButton(
                    "Cancel"
                ) { dialog: DialogInterface, i: Int -> dialog.dismiss()
                    AlertDialog().show(requireActivity().supportFragmentManager, "tag")
                }.show()
        }
    }

}