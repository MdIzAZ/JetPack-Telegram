package com.kroy.sseditor.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kroy.sseditor.data.repo.SSEditorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class CategoryViewmodel @Inject constructor(private  val repository: SSEditorRepository):ViewModel() {

    val categories :StateFlow<List<String>>
        get() = repository.categories

    init {
        viewModelScope.launch {
            repository.getCategory()
        }
    }
}