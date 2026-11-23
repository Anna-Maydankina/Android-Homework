package com.example.virtulcat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CatUiState(
    val cats: List<Cat>,
    val selectedCat: Int = 0
)

class CatViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var nextCatNumber = 11

    private val _uiState = MutableStateFlow(
        CatUiState(
            cats = listOf(
                Cat(1, "Нагломорд", 70, 80, 90),
                Cat(2, "Пушистик", 50, 60, 70),
                Cat(3, "Мурчальник", 90, 40, 50),
                Cat(4, "Барсик", 20, 80, 90),
                Cat(5, "Снежок", 80, 20, 75),
                Cat(6, "Уголёк", 70, 70, 15),
                Cat(7, "Персик", 20, 20, 80),
                Cat(8, "Пончик", 85, 90, 95),
                Cat(9, "Клякса", 45, 55, 60),
                Cat(10, "Кексик", 60, 35, 50)
            ),
            selectedCat = savedStateHandle["selectedCat"] ?: 0
        )
    )

    val uiState: StateFlow<CatUiState> = _uiState.asStateFlow()

    fun selectCat(name: String) {
        val currentState = _uiState.value

        val index = currentState.cats.indexOfFirst {
            it.name == name
        }

        if (index != -1) {
            savedStateHandle["selectedCat"] = index

            _uiState.value = currentState.copy(
                selectedCat = index
            )
        }
    }

    fun addCat() {
        val currentState = _uiState.value

        val newCat = Cat(
            id = nextCatNumber,
            name = "Котик $nextCatNumber",
            hunger = start_hunger,
            energy = start_energy,
            happiness = start_happiness
        )

        nextCatNumber++

        _uiState.value = currentState.copy(
            cats = currentState.cats + newCat
        )
    }

    fun removeCat(name: String) {
        val currentState = _uiState.value

        if (currentState.cats.size <= 1) {
            return
        }

        val updatedCats = currentState.cats.filterNot {
            it.name == name
        }

        val newSelectedCat = currentState.selectedCat.coerceIn(
            0,
            updatedCats.lastIndex
        )

        savedStateHandle["selectedCat"] = newSelectedCat

        _uiState.value = currentState.copy(
            cats = updatedCats,
            selectedCat = newSelectedCat
        )
    }

    fun selectPreviousCat() {
        val currentState = _uiState.value

        if (currentState.selectedCat > 0) {
            val newSelectedCat = currentState.selectedCat - 1

            savedStateHandle["selectedCat"] = newSelectedCat

            _uiState.value = currentState.copy(
                selectedCat = newSelectedCat
            )
        }
    }

    fun selectNextCat() {
        val currentState = _uiState.value

        if (currentState.selectedCat < currentState.cats.lastIndex) {
            val newSelectedCat = currentState.selectedCat + 1

            savedStateHandle["selectedCat"] = newSelectedCat

            _uiState.value = currentState.copy(
                selectedCat = newSelectedCat
            )
        }
    }

    fun feedSelectedCat() {
        updateSelectedCat { cat ->
            feedCat(cat)
        }
    }

    fun playWithSelectedCat() {
        updateSelectedCat { cat ->
            playWithCat(cat)
        }
    }

    fun sleepSelectedCat() {
        updateSelectedCat { cat ->
            sleepCat(cat)
        }
    }

    fun resetSelectedCat() {
        updateSelectedCat { cat ->
            Cat(
                id = cat.id,
                name = cat.name,
                hunger = start_hunger,
                energy = start_energy,
                happiness = start_happiness
            )
        }
    }

    private fun updateSelectedCat(action: (Cat) -> Cat) {
        val currentState = _uiState.value
        val index = currentState.selectedCat

        val updatedCats = currentState.cats.toMutableList()

        val selectedCat = updatedCats[index]

        updatedCats[index] = action(selectedCat)

        _uiState.value = currentState.copy(
            cats = updatedCats
        )
    }
}
