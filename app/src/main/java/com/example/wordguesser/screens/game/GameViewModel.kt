package com.example.wordguesser.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private val timer: CountDownTimer

    companion object {
        //these represent different important times in the game, such as time length
        //game ending

        // Time when the game is over
        private const val DONE = 0L

        //this is the number of milliseconds in the game
        private const val COUNTDOWN_PANIC_SECONDS = 10L

        //Countdown time interval
        private const val ONE_SECOND = 1000L

        //Total time for  the game
        private const val COUNTDOWN_TIME = 60000L
    }


    //The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word


    //The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score


    //Countdown time
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime


    //The String version of the current time
    val currentTimeString: LiveData<String> = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }


    //Event which triggers the end of game
    private val _eventFinishedGame = MutableLiveData<Boolean>()
    val eventFinishedGame: LiveData<Boolean>
        get() = _eventFinishedGame

    //The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>


    private fun resetList() {
        wordList = mutableListOf(
            "Dance",
            "Pet",
            "Dog",
            "snail",
            "soup",
            "bubble",
            "roll",
            "bag",
            "trade",
            "crow",
            "car",
            "jelly",
            "zebra",
            "railway",
            "home",
            "guitar",
            "desk",
            "sad",
            "calendar",
            "change",
            "cat",
            "basketball",
            "hospital",
            "queen"

        )
        wordList.shuffle()
    }


    init {
        _word.value = ""
        _score.value = 0
        Log.i("GameViewModel: ", "GameViewModel Created")
        resetList()
        nextWord()

        //Creates a timer which triggers the end of the game when it finishes
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinish()
            }
        }
        timer.start()
    }

    /**
     * Callback called when the viewModel is destroyed
     */

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel Destroyed")
        timer.cancel()
    }

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }


    private fun nextWord() {
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }


    /** Method for the game completed event**/
    fun onGameFinishedComplete() {
        _eventFinishedGame.value = false
    }

    fun onGameFinish() {
        _eventFinishedGame.value = true
    }


}