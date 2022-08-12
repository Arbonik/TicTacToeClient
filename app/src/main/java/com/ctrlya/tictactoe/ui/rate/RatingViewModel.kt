package com.ctrlya.tictactoe.ui.rate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctrlya.tictactoe.network.NetworkGameInteractor
import com.ctrlya.tictactoe.network.model.RoomsResponse
import com.ctrlya.tictactoe.ui.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RatingViewModel(val interactor: NetworkGameInteractor) : ViewModel()
{
    private val _ratingResponse: MutableStateFlow<Resource<List<RatingResponse>>> =
        MutableStateFlow(Resource.Success(listOf()))
    val rating: StateFlow<Resource<List<RatingResponse>>> = _ratingResponse

    fun getRating() {
        _ratingResponse.value = Resource.Loading()
        viewModelScope.launch {
            _ratingResponse.value = try {
                Resource.Success(interactor.getRating())
            } catch (e: Exception) {
                Resource.Error(e.message.toString())
            }
        }
    }
}