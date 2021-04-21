package com.kakao.mobility.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.kakao.mobility.databinding.FragmentSearchBinding
import com.kakao.mobility.model.dto.PokemonName
import com.kakao.mobility.ui.detail.PokemonDetailBottomSheet
import com.kakao.mobility.ui.main.MainViewModel
import com.kakao.mobility.ui.main.SearchViewStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchFragment : Fragment(), OnPokemonItemEvent {
    private val viewModel: MainViewModel by sharedViewModel()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { SearchResultAdapter(this) }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) = FragmentSearchBinding.inflate(inflater, container, false).apply {
        _binding = this
        viewModel = this@SearchFragment.viewModel
        lifecycleOwner = viewLifecycleOwner
    }.root

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(adapter) {
            viewModel.searchResult.observe(viewLifecycleOwner, ::onSearchResultChanged)
            viewModel.viewStatus.observe(viewLifecycleOwner, ::onViewStatusChanged)
            binding.searchResultView.adapter = this
        }
    }

    private fun onSearchResultChanged(result: List<PokemonName>) {
        adapter.submitList(result) { binding.searchResultView.scrollToPosition(0) }
    }

    private fun onViewStatusChanged(viewStatus: SearchViewStatus) = when (viewStatus) {
        is SearchViewStatus.Empty -> adapter.submitList(null)
        is SearchViewStatus.ErrorToast -> Toast.makeText(context, viewStatus.throwable.message, Toast.LENGTH_LONG).show()
        else -> {
        }// Do Nothing
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onPokemonSelected(id: Long) {
        PokemonDetailBottomSheet(id).show(childFragmentManager, PokemonDetailBottomSheet::class.java.simpleName)
    }
}

@BindingAdapter("viewStatus")
fun ViewFlipper.viewStatus(viewStatus: SearchViewStatus) {
    displayedChild = when (viewStatus.index) {
        in 0 until childCount -> viewStatus.index
        else -> SearchViewStatus.Error.index
    }
}