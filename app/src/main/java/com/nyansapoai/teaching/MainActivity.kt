package com.nyansapoai.teaching

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.nyansapoai.teaching.navigation.Navigation
import com.nyansapoai.teaching.ui.theme.NyansapoTeachingTheme
import com.nyansapoai.teaching.utils.Utils
import org.koin.compose.KoinContext

lateinit var navController: NavHostController

private lateinit var analytics: FirebaseAnalytics

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analytics = Firebase.analytics

        enableEdgeToEdge()
        setContent {
            NyansapoTeachingTheme {
                navController = rememberNavController()
                KoinContext {
                    Navigation()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
    }

}
