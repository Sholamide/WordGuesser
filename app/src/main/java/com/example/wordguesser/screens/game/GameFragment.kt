package com.example.wordguesser.screens.game


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.example.wordguesser.R
import com.example.wordguesser.databinding.FragmentGameBinding

/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_game,
            container,
            false
        )

        //gets the viewModel
        Log.i("GameFragment", "called ViewModelProviders.of!")
        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        //set the viewModel for dataBinding - this allows the bound layout access to all of the data in the viewModel
        binding.gameViewModel = viewModel

        //specify the fragment view as the lifecycle owner of the binding.
        //This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        //Observer for the Game finished event
        viewModel.eventFinishedGame.observe(this, Observer { gameHasFinished ->
            if (gameHasFinished) {
                gameFinished()
                viewModel.onGameFinishedComplete()
            }
        })

        return binding.root
    }


    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0)
        findNavController().navigate(action)
        viewModel.onGameFinishedComplete()
    }


}
