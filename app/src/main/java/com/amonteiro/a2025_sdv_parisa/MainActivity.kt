package com.amonteiro.a2025_sdv_parisa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.amonteiro.a2025_sdv_parisa.ui.theme._2025_sdv_parisaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            _2025_sdv_parisaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //SearchScreen(modifier = Modifier.padding(innerPadding))
                    TestShareRemember()
                }
            }
        }
    }
}

@Composable
fun TestShareRemember() {
    var expanded = remember { mutableStateOf(false) }

    Column {
        ElevatedButton(
            onClick = { expanded.value = !expanded.value }
        ) {
            Text(if (expanded.value) "Show less" else "Show more")
        }

        ElevatedButton(
            onClick = { expanded.value = !expanded.value },
        ) {
            Text(if (expanded.value) "Show less" else "Show more")
        }
        MyButton(expanded)
        MyButton()
    }
}
//Permet d'écouter l'état en dehors de la méthode
@Composable
fun MyButton(expanded: MutableState<Boolean> = remember { mutableStateOf(false) }) {

    ElevatedButton(
        onClick = { expanded.value = !expanded.value },
    ) {
        Text(if (expanded.value) "Show less" else "Show more")
    }
}