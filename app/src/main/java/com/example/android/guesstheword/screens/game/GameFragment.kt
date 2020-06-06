/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding
import com.example.android.guesstheword.screens.game.GameViewModel.BuzzType.*

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var gameViewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        Log.i("GameFragment", "Called ViewModelProvicer.of")
        gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        //Init XML to Implement the GameViewModels
        binding.gameViewModelsfrmXml = gameViewModel //If this Commented, the Button not working
        binding.lifecycleOwner = this //Setting for Comm data ViewModel and Layout

        /*binding.correctButton.setOnClickListener {
            gameViewModel.onCorrect()
        }
        binding.skipButton.setOnClickListener {
            gameViewModel.onSkip()
        }*/

        //Change to using Observe from XML
        /*gameViewModel.score.observe(this, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })

        gameViewModel.word.observe(this, Observer { newWord ->
            binding.wordText.text = newWord
        })*/

        gameViewModel.isFinished.observe(this, Observer { hasFinished ->
            if (hasFinished) {
                gameFinished()
                gameViewModel.onGameFinishedComplete()
            }
        })

        gameViewModel.eventBuzz.observe(this, Observer { buzzType ->
            if (buzzType != NO_BUZZ) {
                buzz(buzzType.pattern)
                gameViewModel.onBuzzComplete()
            }
        })

        /*gameViewModel.currentTime.observe(this, Observer { timeLeft ->
            binding.timerText.text = DateUtils.formatElapsedTime(timeLeft)
        })*/

        return binding.root
    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(gameViewModel.score.value ?: 0)
        findNavController(this).navigate(action)

        Toast.makeText(this.activity, "Game has Finished", Toast.LENGTH_SHORT).show()
    }

    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()

        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                buzzer.vibrate(pattern, -1)
            }
        }
    }
}
