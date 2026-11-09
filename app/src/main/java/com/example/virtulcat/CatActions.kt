package com.example.virtulcat

fun feedCat(cat: Cat): Cat {
    return cat.copy(
        hunger = (cat.hunger + 30).coerceAtMost(100),
        happiness = (cat.happiness - 10).coerceAtLeast(0),
        energy = (cat.energy - 5).coerceAtLeast(0)
    )
}

fun playWithCat(cat: Cat): Cat {
    return cat.copy(
        hunger = (cat.hunger - 15).coerceAtLeast(0),
        energy = (cat.energy - 20).coerceAtLeast(0),
        happiness = (cat.happiness + 20).coerceAtMost(100)
    )
}

fun sleepCat(cat: Cat): Cat {
    return cat.copy(
        energy = (cat.energy + 30).coerceAtMost(100),
        happiness = (cat.happiness - 5).coerceAtLeast(0),
        hunger = (cat.hunger - 10).coerceAtLeast(0)
    )
}