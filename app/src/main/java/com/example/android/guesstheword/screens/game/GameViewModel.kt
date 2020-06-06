package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    //Define Timer
    private var timer: CountDownTimer

    private var _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    //Transforming Data which can be view in XML
    //You Would have to learn deeper for it
    //Like SwitchMap
    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    //Encapsulate the MutableLiveData using Live Data
    //Which the mutable will not exposed outside of viewmodel
    // The current word
    private var _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word //Ambil data dari mutable...

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score //Ambil data dari mutable...

    //The Game has Finished
    private val _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean>
        get() = _isFinished

    //Set Buzz - Vibrate
    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    //MutableLiveData -> data Bisa diubah2
    //LiveData -> data Tidak bisa diubah2

    // The list of words - the front of the list is the next word to guess
    private lateinit
    var wordList: MutableList<String>

    init {
        Log.i("GameViewModel", "GameViewModel Created")

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onFinish() {
                _isFinished.value = true
                _currentTime.value = DONE

                _eventBuzz.value = BuzzType.GAME_OVER
            }

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)

                if (millisUntilFinished / ONE_SECOND <= COUNTDOWN_PANIC) {
                    //_eventBuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }
        }

        timer.start()

        //First Run
        resetList()
        nextWord()

        //define var value
        _score.value = 0
        _isFinished.value = false
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel() //Cleared The timer countdown
        Log.i("GameViewModel", "GameViewModel Destroyed")
    }

    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            //_isFinished.value = true
            resetList()
        } else {
            //word = wordList.removeAt(0)
            _word.value = wordList.removeAt(0)
        }
    }

    fun onSkip() {
        //score-- //Because it isnt Integer again.. -- not valid
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        //score++
        _score.value = (score.value)?.plus(1)
        nextWord()

        _eventBuzz.value = BuzzType.CORRECT
    }

    fun onGameFinishedComplete() {
        _isFinished.value = false
    }

    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }

    //Adding ENUM
    //No Itsnot Enum
    companion object {
        const val DONE = 0L
        const val ONE_SECOND = 1000L
        const val COUNTDOWN_TIME = 60000L
        const val COUNTDOWN_PANIC = 100L
    }

    //This is Enum
    enum class BuzzType(val pattern: LongArray) {
        CORRECT(longArrayOf(100, 100, 100, 100, 100, 100)),
        GAME_OVER(longArrayOf(0, 200)),
        COUNTDOWN_PANIC(longArrayOf(0, 2000)),
        NO_BUZZ(longArrayOf(0))
    }
}
