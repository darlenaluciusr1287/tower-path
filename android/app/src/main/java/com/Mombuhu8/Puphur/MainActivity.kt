package com.Mombuhu8.Puphur

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.Mombuhu8.Puphur.core.di.ServiceLocator
import com.Mombuhu8.Puphur.core.theme.AppTheme
import com.Mombuhu8.Puphur.presentation.app.AppRoot

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ServiceLocator.initialise(applicationContext)
        setContent {
            AppTheme {
                AppRoot()
            }
        }
    }
}
