package com.kakao.mobility.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.kakao.mobility.R
import com.kakao.mobility.databinding.ActivityMainBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

fun Context.startMainActivity() = startActivity(Intent(this, MainActivity::class.java))

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainSearchView.setOnQueryTextListener(queryTextChangeListener)
        binding.mainSearchView.requestFocus()
        search("")
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    @ExperimentalCoroutinesApi
    private fun search(query: String?) = lifecycleScope.launch {
        val fixedQuery = query?.replace("\n", "")?.trim() ?: ""
        viewModel.queryChannel.send(fixedQuery)
    }

    @ExperimentalCoroutinesApi
    private val queryTextChangeListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            search(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            search(newText)
            return true
        }
    }
}