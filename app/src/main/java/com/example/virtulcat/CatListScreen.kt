package com.example.virtulcat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CatListScreen(
    cats: List<Cat>,
    onCatClick: (String) -> Unit,
    onAddCat: () -> Unit,
    onRemoveCat: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Мои коты"
        )

        Button(
            onClick = onAddCat,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text("Добавить кота")
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = cats,
                key = { it.id}
            ) { cat ->

                val state = getCatState(cat)

                val isBadState =
                    state == CatState.Bad ||
                            state == CatState.Hungry ||
                            state == CatState.Tired ||
                            state == CatState.Sad

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onCatClick(cat.name)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isBadState) {
                            Color(0xFFFFE0E0)
                        } else {
                            Color(0xFFE0F2E9)
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🐱 ${cat.name}")
                            Text("Состояние: $state")
                            Text("Сытость: ${cat.hunger}/100")
                            Text("Энергия: ${cat.energy}/100")
                            Text("Счастье: ${cat.happiness}/100")
                        }

                        Button(
                            onClick = {
                                onRemoveCat(cat.name)
                            }
                        ) {
                            Text("Удалить")
                        }
                    }
                }
            }
        }
    }
}