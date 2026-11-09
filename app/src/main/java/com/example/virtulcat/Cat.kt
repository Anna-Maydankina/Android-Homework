package com.example.virtulcat

data class Cat(
    val name: String,
    var hunger: Int,
    var energy: Int,
    var happiness: Int
)
enum class CatState{
    Happy, Normal, Sad, Hungry, Tired, Bad
}
fun getCatState(cat: Cat): CatState{
    val hungry = cat.hunger<30
    val tired = cat.energy<30
    val sad =cat.happiness<30
    val happy = cat.happiness>=70 && cat.energy>=70 && cat.hunger>=70
    if ((hungry&&tired)||(hungry&&sad)||(tired&&sad)){
        return CatState.Bad
    }
    if (hungry){
        return CatState.Hungry
    }
    if (tired){
        return CatState.Tired
    }
    if (sad){
        return CatState.Sad
    }
    if (happy){
        return CatState.Happy
    }
    return CatState.Normal
}