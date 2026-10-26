package com.example.virtulcat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.virtulcat.ui.theme.VirtulCatTheme

const val start_hunger = 70
const val start_energy = 80
const val start_happiness = 90

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            VirtulCatTheme {
                CatScreen()
            }
        }
    }
}

val catsSaver = listSaver<List<Cat>, String>(
    save = { cats ->
        cats.map { cat ->
            "${cat.name}|${cat.hunger}|${cat.energy}|${cat.happiness}"
        }
    },
    restore = { saved ->
        saved.map { catData ->
            val parts = catData.split("|")

            Cat(
                name = parts[0],
                hunger = parts[1].toInt(),
                energy = parts[2].toInt(),
                happiness = parts[3].toInt()
            )
        }
    }
)

@Composable
fun CatScreen() {

    val cats = rememberSaveable(saver = catsSaver) {
        listOf(
            Cat(
                name = "Нагломорд",
                hunger = start_hunger,
                energy = start_energy,
                happiness = start_happiness
            ),
            Cat(
                name = "Пушистик",
                hunger = 50,
                energy = 60,
                happiness = 70
            ),
            Cat(
                name = "Мурчальник",
                hunger = 90,
                energy = 40,
                happiness = 50
            )
        )
    }

    var selectedCat by rememberSaveable {
        mutableStateOf(0)
    }

    var update by remember {
        mutableStateOf(0)
    }

    val cat = cats[selectedCat]

    val state = update.let {
        getCatState(cat)
    }

    val catImage = getCatImage(state)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(catImage),
            contentDescription = "Кот в состоянии $state"
        )

        Text(text = "🐱 ${cat.name}")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "🍗 Сытость: ${cat.hunger}/100")
        Text(text = "⚡ Энергия: ${cat.energy}/100")
        Text(text = "💕 Счастье: ${cat.happiness}/100")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Состояние: $state")

        if (getStateMessage(state).isNotEmpty()) {
            Text(text = getStateMessage(state))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            enabled = selectedCat > 0,
            onClick = {
                selectedCat--
                update++
            }
        ) {
            Text("⬅️ Предыдущий")
        }

        Button(
            enabled = selectedCat < cats.lastIndex,
            onClick = {
                selectedCat++
                update++
            }
        ) {
            Text("Следующий ➡️")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            enabled = cat.hunger < 100,
            onClick = {
                feedCat(cat)
                update++
            }
        ) {
            Text("🍚 Покормить")
        }

        Button(
            enabled = cat.energy >= 20 && cat.hunger >= 30,
            onClick = {
                playWithCat(cat)
                update++
            }
        ) {
            Text("🧶 Поиграть")
        }

        Button(
            enabled = cat.energy < 100,
            onClick = {
                sleepCat(cat)
                update++
            }
        ) {
            Text("😴 Поспать")
        }

        Button(
            onClick = {
                cat.hunger = start_hunger
                cat.energy = start_energy
                cat.happiness = start_happiness
                update++
            }
        ) {
            Text("Сброс")
        }
    }
}

fun getCatImage(state: CatState): Int {
    return when (state) {
        CatState.Happy -> R.drawable.cathappy
        CatState.Sad -> R.drawable.catsad
        CatState.Bad -> R.drawable.catbad
        CatState.Normal -> R.drawable.catnormal
        CatState.Hungry -> R.drawable.cathungry
        CatState.Tired -> R.drawable.catsleep
    }
}

fun getStateMessage(state: CatState): String {
    return when (state) {
        CatState.Tired -> "⚠️ Котик устал!"
        CatState.Hungry -> "⚠️ Котик голоден!"
        CatState.Sad -> "💔 Котик грустит!"
        CatState.Bad -> "🚨 Котику плохо!"
        else -> ""
    }
}