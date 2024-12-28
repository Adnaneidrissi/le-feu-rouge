package com.example.lefeurougeapp

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var redLight: View
    private lateinit var yellowLight: View
    private lateinit var greenLight: View
    private lateinit var btnScenario1: Button
    private lateinit var btnScenario2: Button
    private val handler = Handler(Looper.getMainLooper())
    private var isScenario1Running = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation des vues
        redLight = findViewById(R.id.redLight)
        yellowLight = findViewById(R.id.yellowLight)
        greenLight = findViewById(R.id.greenLight)
        btnScenario1 = findViewById(R.id.scenario1Button)
        btnScenario2 = findViewById(R.id.scenario2Button)

        // Gestion des clics pour les scénarios
        btnScenario1.setOnClickListener { startScenario1() }
        btnScenario2.setOnClickListener { startScenario2() }
    }

    private fun startScenario1() {
        if (isScenario1Running) return // Évite de démarrer plusieurs boucles
        isScenario1Running = true

        // Éteindre toutes les lumières et démarrer la boucle
        resetLights()
        runScenario1Loop()
    }

    private fun runScenario1Loop() {
        if (!isScenario1Running) return // Arrêter la boucle si nécessaire

        // Allumer les feux successivement : Rouge -> Jaune -> Vert
        turnOnLight(redLight)
        handler.postDelayed({
            turnOnLight(yellowLight)
        }, 5000)

        handler.postDelayed({
            turnOnLight(greenLight)
        }, 8000)

        handler.postDelayed({
            runScenario1Loop() // Recommence la boucle après le vert
        }, 13000) // 5s (rouge) + 3s (jaune) + 5s (vert)
    }

    private fun startScenario2() {
        // Arrêter le scénario 1 si en cours
        isScenario1Running = false
        handler.removeCallbacksAndMessages(null)

        // Éteindre toutes les lumières
        resetLights()

        // Allumer uniquement le feu jaune
        turnOnLight(yellowLight)
    }

    private fun resetLights() {
        // Réinitialiser toutes les lumières
        redLight.setBackgroundResource(R.drawable.circle_red_off)
        yellowLight.setBackgroundResource(R.drawable.circle_yellow_off)
        greenLight.setBackgroundResource(R.drawable.circle_green_off)
    }

    private fun turnOnLight(light: View) {
        // Activer la lumière choisie et désactiver les autres
        resetLights()
        when (light) {
            redLight -> light.setBackgroundResource(R.drawable.circle_red_on)
            yellowLight -> light.setBackgroundResource(R.drawable.circle_yellow_on)
            greenLight -> light.setBackgroundResource(R.drawable.circle_green_on)
        }
        animateLight(light)
    }

    private fun animateLight(light: View) {
        // Animation pour un effet visuel de fondu
        val animator = ObjectAnimator.ofFloat(light, "alpha", 0f, 1f)
        animator.duration = 500 // Durée de l'animation (0.5 seconde)
        animator.start()
    }
}
