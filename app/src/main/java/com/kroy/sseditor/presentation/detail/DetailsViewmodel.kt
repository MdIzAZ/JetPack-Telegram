package com.kroy.sseditor.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kroy.sseditor.data.remote.models.TweetListItem
import com.kroy.sseditor.data.repo.SSEditorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class DetailsViewmodel @Inject constructor(private  val repository: SSEditorRepository,
                                           private  val savedStateHandle: SavedStateHandle):ViewModel() {

    val tweets :StateFlow<List<TweetListItem>>
        get() = repository.tweets

    init {
        viewModelScope.launch {
            val category = savedStateHandle.get<String>("category")?:"android"
            repository.getTweets(category)
        }
    }
}