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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import kotlinx.serialization.Serializable
import com.example.virtulcat.ui.theme.VirtulCatTheme

@Serializable
data object CatListRoute : NavKey

@Serializable
data object CatDetailRoute : NavKey

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            VirtulCatTheme {

                val catViewModel: CatViewModel = viewModel()
                val uiState by catViewModel.uiState.collectAsState()

                val backStack = rememberNavBackStack(CatListRoute)

                NavDisplay(
                    backStack = backStack,

                    onBack = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    },

                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),

                    entryProvider = entryProvider {

                        entry<CatListRoute> {
                            CatListScreen(
                                cats = uiState.cats,

                                onCatClick = { name ->
                                    catViewModel.selectCat(name)
                                    backStack.add(CatDetailRoute)
                                },

                                onAddCat = {
                                    catViewModel.addCat()
                                },

                                onRemoveCat = { name ->
                                    catViewModel.removeCat(name)
                                }
                            )
                        }

                        entry<CatDetailRoute> {
                            CatScreen(
                                uiState = uiState,

                                onPrevious = {
                                    catViewModel.selectPreviousCat()
                                },

                                onNext = {
                                    catViewModel.selectNextCat()
                                },

                                onFeed = {
                                    catViewModel.feedSelectedCat()
                                },

                                onPlay = {
                                    catViewModel.playWithSelectedCat()
                                },

                                onSleep = {
                                    catViewModel.sleepSelectedCat()
                                },

                                onReset = {
                                    catViewModel.resetSelectedCat()
                                },

                                onBack = {
                                    if (backStack.size > 1) {
                                        backStack.removeLastOrNull()
                                    }
                                },

                                onReloadFact = {
                                    catViewModel.loadCatFact()
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CatScreen(
    uiState: CatUiState,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onFeed: () -> Unit,
    onPlay: () -> Unit,
    onSleep: () -> Unit,
    onReset: () -> Unit,
    onBack: () -> Unit,
    onReloadFact: () -> Unit
) {
    val cat = uiState.cats[uiState.selectedCat]

    val state = getCatState(cat)
    val catImage = getCatImage(state)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        Button(onClick = onBack) {
            Text("⬅️ Назад к списку")
        }

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

        val message = getStateMessage(state)

        if (message.isNotEmpty()) {
            Text(text = message)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            enabled = uiState.selectedCat > 0,
            onClick = onPrevious
        ) {
            Text("⬅️ Предыдущий")
        }

        Button(
            enabled = uiState.selectedCat < uiState.cats.lastIndex,
            onClick = onNext
        ) {
            Text("Следующий ➡️")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            enabled = cat.hunger < 100,
            onClick = onFeed
        ) {
            Text("🍚 Покормить")
        }

        Button(
            enabled = cat.energy >= 20 && cat.hunger >= 30,
            onClick = onPlay
        ) {
            Text("🧶 Поиграть")
        }

        Button(
            enabled = cat.energy < 100,
            onClick = onSleep
        ) {
            Text("😴 Поспать")
        }

        Button(onClick = onReset) {
            Text("Сброс")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("🐾 Факт о котиках")

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.isFactLoading) {
            Text("Загружаем интересный факт...")
        }

        if (uiState.catFact != null) {
            Text(
                text = uiState.catFact,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (uiState.factError != null) {
            Text(uiState.factError)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onReloadFact,
            enabled = !uiState.isFactLoading
        ) {
            Text("🐈 Новый факт")
        }

        Spacer(modifier = Modifier.height(16.dp))
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