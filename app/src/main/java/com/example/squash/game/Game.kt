package com.example.squash.game

import android.app.AlertDialog
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.squash.R
import com.example.squash.databinding.FragmentGameBinding
import com.example.squash.datasource.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.util.Assert.fail
import kotlinx.android.synthetic.main.finished_game_modal.*
import kotlinx.android.synthetic.main.fragment_game.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class Game : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private val viewModel: GameViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var getData: String
    private lateinit var time: CustomTimer
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var myMedia: MediaPlayer

    override fun onStart() {
        super.onStart()
        time.start()
    }

    override fun onStop() {
        super.onStop()
        time.onFinish
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGameBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this)

        getArgument()

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.good)
        myMedia = MediaPlayer.create(requireContext(), R.raw.fail)
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
        }

        if (myMedia.isPlaying) {
            myMedia.pause()
            myMedia.seekTo(0)
        }

        val args = this.arguments
        getData = args?.getString("cat").toString()
        val getTimer = args?.getString("time").toString()

        if (getData == "Medicine") {
            viewModel.getNextWord(medicine)
        } else if (getData == "Sport") {
            viewModel.getNextWord(sports)
        } else if (getData == "Finance") {
            viewModel.getNextWord(finance)

        } else if (getData == "Random") {
            viewModel.getNextWord(allWordsList)
        } else if (getData == "Countries") {
            viewModel.getNextWord(countries)
        }

        when (getTimer) {
            "1 minute" -> aMinuteTimer()
            "2 minutes" -> twoMinutesTimer()
            "3 minutes" -> threeMinutesTimer()
            "4 minutes" -> fourMinuteTimer()
            "5 minutes" -> fiveMinuteTimer()
            "Set timer" -> {

            }

            else -> {
                twoMinutesTimer()
            }
        }



        return binding.root
    }


    private fun fail() {
        val v = View.inflate(requireContext(), R.layout.finished_game_modal, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(v)
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val playAgain = v.findViewById<MaterialButton>(R.id.play_again)
        val score = v.findViewById<TextView>(R.id.You)
        val yourScore = v.findViewById<TextView>(R.id.your_score)
        val image = v.findViewById<ImageView>(R.id.image)
        val sad = v.findViewById<ImageView>(R.id.sad)
        var congrats = v.findViewById<TextView>(R.id.congrats)
        val time = v.findViewById<TextView>(R.id.time)
        val good = v.findViewById<ImageView>(R.id.good)
        val tv_good = v.findViewById<TextView>(R.id.tv_good)
        val tv_good_score = v.findViewById<TextView>(R.id.tv_good_score)
        val wow = v.findViewById<ImageView>(R.id.wow)
        val wow_congrats = v.findViewById<TextView>(R.id.wow_congrat)
        val wow_score = v.findViewById<TextView>(R.id.wow_score)
        val hat = v.findViewById<ImageView>(R.id.hat)

        if (viewModel.score.value!! <= 45) {
            image.visibility = View.GONE
            sad.visibility = View.VISIBLE
            congrats.visibility = View.GONE
            time.visibility = View.VISIBLE
            score.visibility = View.GONE
            yourScore.visibility = View.VISIBLE
            yourScore.text = "Your final score ${viewModel.score.value}"
        } else if (viewModel.score.value!! in 50..65) {
            image.visibility = View.GONE
            good.visibility = View.VISIBLE
            tv_good.visibility = View.VISIBLE
            congrats.visibility = View.GONE
            score.visibility = View.GONE
            tv_good_score.visibility = View.VISIBLE
            tv_good_score.text = "Your final score ${viewModel.score.value}"
        } else {
            image.visibility = View.GONE
            wow.visibility = View.VISIBLE
            wow_congrats.visibility = View.VISIBLE
            congrats.visibility = View.GONE
            hat.visibility = View.VISIBLE
            score.visibility = View.GONE
            wow_score.visibility = View.VISIBLE
            wow_score.text = "Your final score ${viewModel.score.value}"
        }

        // image.setImageResource(R.drawable.sad)
        val exit = v.findViewById<AppCompatButton>(R.id.exitGame)


        playAgain.setOnClickListener {
            view?.let { it1 ->
                restartGame()
            }
            dialog.dismiss()
        }

        exit.setOnClickListener {
            view?.let { it1 ->
                exitGame()
            }
            dialog.dismiss()

        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun aMinuteTimer() {
        time = CustomTimer(60000, 1000)
        var timeLeft = 0L
        time.onTick = { millisUntilFinished ->
            timeLeft = millisUntilFinished / 1000
            binding.timer.text = timeLeft.toString()

            if (timeLeft <= 10){
                binding.timer.setTextColor(resources.getColor(R.color.snackBarFailure))
            }
        }
        time.onFinish = {
            fail()
        }
    }


    private fun twoMinutesTimer() {
        time = CustomTimer(120000, 1000)

        var timeLeft = 0L
        time.onTick = { millisUntilFinished ->
            timeLeft = millisUntilFinished / 1000
            binding.timer.text = timeLeft.toString()

            if (timeLeft <= 10){
                binding.timer.setTextColor(resources.getColor(R.color.snackBarFailure))
            }
        }
        time.onFinish = {
            fail()
        }
    }


    private fun threeMinutesTimer() {
        time = CustomTimer(180000, 1000)

        var timeLeft = 0L
        time.onTick = { millisUntilFinished ->
            timeLeft = millisUntilFinished / 1000
            binding.timer.text = timeLeft.toString()

            if (timeLeft <= 10){
                binding.timer.setTextColor(resources.getColor(R.color.snackBarFailure))
            }
        }
        time.onFinish = {
            fail()
        }
    }

    private fun fourMinuteTimer() {
        time = CustomTimer(240000, 1000)

        var timeLeft = 0L
        time.onTick = { millisUntilFinished ->
            timeLeft = millisUntilFinished / 1000
            binding.timer.text = timeLeft.toString()

            if (timeLeft <= 10){
                binding.timer.setTextColor(resources.getColor(R.color.snackBarFailure))
            }
        }
        time.onFinish = {
            fail()
        }
    }

    private fun fiveMinuteTimer() {
        time = CustomTimer(300000, 1000)
        var timeLeft = 0L
        time.onTick = { millisUntilFinished ->
            timeLeft = millisUntilFinished / 1000
            binding.timer.text = timeLeft.toString()

            if (timeLeft <= 10){
                binding.timer.setTextColor(resources.getColor(R.color.snackBarFailure))
            }
        }
        time.onFinish = {
            fail()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentWordCount.observe(viewLifecycleOwner) { newWordCount ->
            binding.wordCount.text = getString(R.string.wordcount, newWordCount, MAX_NO_WORDS)
        }

        viewModel.score.observe(viewLifecycleOwner) { newScore ->
            binding.score.text = newScore.toString()
        }

        binding.quit.setOnClickListener {
            time.pause()
            showDialog()

        }

        binding.hint.setOnClickListener {
            helpDialog()
        }
        viewModel.currentScrambledWord.observe(viewLifecycleOwner) { newWord ->
            binding.word.text = newWord
        }

        binding.submit.setOnClickListener {

            submitWord()
        }

        binding.skip.setOnClickListener {
            binding.userWord.text?.clear()
            skip()
        }


    }

    override fun onDestroy() {
        if (!this::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }

        if (!this::myMedia.isInitialized) {
            myMedia.stop()
            myMedia.release()
        }
        super.onDestroy()
    }

    private fun submitWord() {
        if (viewModel.isWordCorrect(binding.userWord.text.toString().trim { it <= ' ' })) {
            mediaPlayer.start()
            val konfetti = binding.konfetti
            konfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(5f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1)
                .addShapes(Shape.Square, Shape.Circle)
                .addSizes(Size(12))
                .setPosition(-50f, konfetti.width + 50f, -50f, -50f)
                .streamFor(300, 1000L)
            setErrorTextField(false)
            binding.userWord.text?.clear()
            if (viewModel.nextWord()) {

            } else {
                fail()
            }
        } else {
            myMedia.start()
            setErrorTextField(true)
        }
    }

    private fun getArgument() {
        val args = this.arguments
        val getData = args?.get("name")
        when (getData) {
            "Medicine" -> {
                viewModel.getNextWord(medicine)

            }
            "Sport" -> {
                viewModel.getNextWord(sports)

            }
            "Random" -> {
                viewModel.getNextWord(allWordsList)

            }
            "Countries" -> {
                viewModel.getNextWord(countries)
            }

            "Finance" -> {
                viewModel.getNextWord(finance)
            }
        }
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Are you sure?")
            .setMessage("Your Current Score ${viewModel.score.value}")
            .setCancelable(false)
            .setNegativeButton("Exit") { _, _ -> exitGame() }
            .setPositiveButton("Resume") { dialog, _ ->
                time.resume()
                dialog.dismiss()
            }
            .show()

    }


    private fun helpDialog() {
        val v = View.inflate(requireContext(), R.layout.help_modal, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(v)
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val close = v.findViewById<AppCompatButton>(R.id.close)
        val text = v.findViewById<TextView>(R.id.text1)
        text.text =
            getString(R.string.hint, viewModel.hint().toCharArray()[0].toString().toUpperCase())

        close.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun restartGame() {
        time.restart()
        viewModel.reinitializeData()
        setErrorTextField(false)
    }

    private fun skip() {
        if (viewModel.nextWord()) {
            setErrorTextField(false)
        } else {
            fail()
        }
    }


    private fun exitGame() {
        navController.navigate(R.id.action_game_to_homeFragment)
    }

    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.layout.isErrorEnabled = true
            binding.layout.error = "Try Again"
        } else {
            binding.layout.isErrorEnabled = false
            binding.layout.error = null
        }
    }


}