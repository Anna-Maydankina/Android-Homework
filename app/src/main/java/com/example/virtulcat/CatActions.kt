package com.example.virtulcat

fun feedCat(cat: Cat){
    cat.hunger = (cat.hunger + 30).coerceAtMost(100)
    cat.happiness = (cat.happiness - 10).coerceAtLeast(0)
    cat.energy = (cat.energy - 5).coerceAtLeast(0)
}
fun playWithCat(cat: Cat){
    cat.hunger = (cat.hunger - 15).coerceAtLeast(0)
    cat.energy = (cat.energy - 20).coerceAtLeast(0)
    cat.happiness = (cat.happiness+20).coerceAtMost(100)
}
fun sleepCat(cat: Cat){
    cat.energy = (cat.energy+30).coerceAtMost(100)
    cat.happiness = (cat.happiness-5).coerceAtLeast(0)
    cat.hunger = (cat.hunger - 10).coerceAtLeast(0)
}