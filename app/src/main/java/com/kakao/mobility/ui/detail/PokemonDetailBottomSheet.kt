package com.kakao.mobility.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.mobility.R
import com.kakao.mobility.databinding.BottomSheetPokemonDetailBinding
import com.kakao.mobility.ui.ViewStatus
import com.kakao.mobility.ui.main.SearchViewStatus
import com.kakao.mobility.ui.map.startMapActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PokemonDetailBottomSheet(private val id: Long = 0L) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetPokemonDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonDetailViewModel by viewModel {
        parametersOf(id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id.takeIf { it <= 0 }?.let {
            onViewStatusChanged(ViewStatus.ErrorFinish(R.string.invalidate_id))
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = BottomSheetPokemonDetailBinding.inflate(inflater, container, false).apply {
        viewModel = this@PokemonDetailBottomSheet.viewModel
        lifecycleOwner = viewLifecycleOwner
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goMap.setOnClickListener {
            context?.startMapActivity(id)
            dismiss()
        }
        viewModel.viewStatus.observe(viewLifecycleOwner, ::onViewStatusChanged)
    }

    private fun onViewStatusChanged(viewStatus: ViewStatus) = when (viewStatus) {
        is ViewStatus.ErrorFinish -> {
            Toast.makeText(context, getString(viewStatus.resourceId), Toast.LENGTH_LONG).show()
            dismiss()
        }
        else -> {
        }//Do Nothing
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

@BindingAdapter("viewStatus")
fun ViewFlipper.viewStatus(viewStatus: ViewStatus) {
    displayedChild = when (viewStatus.index) {
        in 0 until childCount -> viewStatus.index
        else -> SearchViewStatus.Error.index
    }
}