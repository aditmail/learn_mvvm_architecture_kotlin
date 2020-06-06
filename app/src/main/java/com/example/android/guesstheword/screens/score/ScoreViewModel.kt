package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int) : ViewModel() {

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _isPlayAgain = MutableLiveData<Boolean>()
    val isPlayAgain: LiveData<Boolean>
        get() = _isPlayAgain

    init {
        Log.i("ScoreViewModel", "ViewModelCreated, and Final Score $finalScore")
        _score.value = finalScore
    }

    fun onPlayAgain() {
        _isPlayAgain.value = true
    }

    fun onPlayAgainCompleted() {
        _isPlayAgain.value = false
    }
}