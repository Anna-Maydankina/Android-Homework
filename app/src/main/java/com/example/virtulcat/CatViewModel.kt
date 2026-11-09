package com.example.virtulcat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
data class CatUiState(
    val cats: List<Cat>,
    val selectedCat: Int = 0
)
class CatViewModel : ViewModel() {
    private var nextCatNumber = 11
    private val _uiState = MutableStateFlow(
        CatUiState(
            cats = listOf(
                Cat("Нагломорд", 70, 80, 90),
                Cat("Пушистик", 50, 60, 70),
                Cat("Мурчальник", 90, 40, 50),
                Cat("Барсик", 20, 80, 90),
                Cat("Снежок", 80, 20, 75),
                Cat("Уголёк", 70, 70, 15),
                Cat("Персик", 20, 20, 80),
                Cat("Пончик", 85, 90, 95),
                Cat("Клякса", 45, 55, 60),
                Cat("Кексик", 60, 35, 50)
            )
        )
    )
    val uiState: StateFlow<CatUiState> = _uiState.asStateFlow()

    fun selectCat(name: String) {
        val currentState = _uiState.value
        val index = currentState.cats.indexOfFirst {
            it.name == name
        }

        if (index != -1) {
            _uiState.value = currentState.copy(
                selectedCat = index
            )
        }
    }
    fun addCat() {
        val currentState = _uiState.value

        val newCat = Cat(
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

        _uiState.value = currentState.copy(
            cats = updatedCats,
            selectedCat = currentState.selectedCat.coerceIn(
                0,
                updatedCats.lastIndex
            )
        )
    }
    fun selectPreviousCat() {
        val currentState = _uiState.value

        if (currentState.selectedCat > 0) {
            _uiState.value = currentState.copy(
                selectedCat = currentState.selectedCat - 1
            )
        }
    }
    fun selectNextCat() {
        val currentState = _uiState.value

        if (currentState.selectedCat < currentState.cats.lastIndex) {
            _uiState.value = currentState.copy(
                selectedCat = currentState.selectedCat + 1
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