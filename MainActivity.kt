package com.example.quizz_project

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat

class MainActivity : Activity() {
    @SuppressLint("MissingInflatedId")

    private val questions = arrayOf(
        "none",
        "Which country has recently been declared by the US as a “non-NATO ally” status?",
        "What is the dangerous disease caused by bacteria in humans and cattle called?",
        "In which of the following state, the Sabarimala Sri Dham Shashtha temple dedicated to Lord Ayyappa is located?",
        "In which of the following year was the budget system introduced for the first time in India?",
        "On which day is the World Oral Health Day celebrated every year?",
        "Who of the following is the only Chief of Air Staff to be given the rank of Marshall of the Indian Air Force?",
        "Which state government has recently launched 'one family, one job' scheme?"
    )

    private val options = arrayOf(
        "none", "China", "Qatar", "Iraq", "Iran", "Little Mother", "Measles", "Anthrax", "Malaria",
        "Maharashtra", "Karnataka", "Manipur", "Kerala", "1867", "1860", "1897", "1890", "18 January",
        "20 January", "30 July", "20 March", "Arjan Singh", "Subroto Mukherjee", "P.C. Lal", "O.P. Mehra",
        "Rajasthan", "Madhya Pradesh", "Chhattisgarh", "Sikkim"
    )

    private val answers = arrayOf("none", "2", "3", "4", "2", "4", "1", "4")

    private var currentQuestionIndex = 1
    private var score = 0
    private lateinit var questionSwitcher: TextSwitcher
    private lateinit var option1: TextSwitcher
    private lateinit var option2: TextSwitcher
    private lateinit var option3: TextSwitcher
    private lateinit var option4: TextSwitcher
    private lateinit var scoreTextView: TextView
    private lateinit var timerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= 16) {
            window.setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT)
            window.decorView.systemUiVisibility = 3328
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        questionSwitcher = findViewById(R.id.question)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        scoreTextView = findViewById(R.id.score)
        timerTextView = findViewById(R.id.timer)

        setupTextSwitcher(questionSwitcher)
        setupTextSwitcher(option1)
        setupTextSwitcher(option2)
        setupTextSwitcher(option3)
        setupTextSwitcher(option4)

        loadQuestion(currentQuestionIndex)

        option1.setOnClickListener { onOptionClicked(1) }
        option2.setOnClickListener { onOptionClicked(2) }
        option3.setOnClickListener { onOptionClicked(3) }
        option4.setOnClickListener { onOptionClicked(4) }
    }

    private fun setupTextSwitcher(textSwitcher: TextSwitcher) {
        textSwitcher.setFactory {
            val textView = TextView(this@MainActivity)
            textView.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            textView.textSize = 20f
            textView.setTextColor(Color.BLACK)
            textView
        }
    }

    private fun loadQuestion(index: Int) {
        if (index < questions.size) {
            questionSwitcher.setText(questions[index])
            option1.setText(options[index * 4 - 3])
            option2.setText(options[index * 4 - 2])
            option3.setText(options[index * 4 - 1])
            option4.setText(options[index * 4])
            resetOptionColors()
            startTimer()
        } else {
            showFinalScore()
        }
    }

    private fun resetOptionColors() {
        option1.setBackgroundColor(Color.WHITE)
        option2.setBackgroundColor(Color.WHITE)
        option3.setBackgroundColor(Color.WHITE)
        option4.setBackgroundColor(Color.WHITE)
    }

    private fun startTimer() {
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTextView.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                onNextQuestion()
            }
        }.start()
    }

    private fun onOptionClicked(selectedOption: Int) {
        val correctOption = answers[currentQuestionIndex].toInt()
        if (selectedOption == correctOption) {
            score += 10
            setOptionColor(selectedOption, Color.GREEN)
        } else {
            setOptionColor(selectedOption, Color.RED)
            setOptionColor(correctOption, Color.GREEN)
        }

        scoreTextView.text = "Score: $score"
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                onNextQuestion()
            }
        }.start()
    }

    private fun setOptionColor(option: Int, color: Int) {
        when (option) {
            1 -> option1.setBackgroundColor(color)
            2 -> option2.setBackgroundColor(color)
            3 -> option3.setBackgroundColor(color)
            4 -> option4.setBackgroundColor(color)
        }
    }

    private fun onNextQuestion() {
        currentQuestionIndex++
        loadQuestion(currentQuestionIndex)
    }

    private fun showFinalScore() {
        questionSwitcher.setText("Quiz Finished!")
        option1.visibility = View.GONE
        option2.visibility = View.GONE
        option3.visibility = View.GONE
        option4.visibility = View.GONE
        timerTextView.visibility = View.GONE
        scoreTextView.text = "Final Score: $score"
    }
}

data class Question(val text: String, val answers: List<String>, val correctAnswerIndex: Int)
