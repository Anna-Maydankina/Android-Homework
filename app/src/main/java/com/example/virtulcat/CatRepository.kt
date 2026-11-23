package com.example.virtulcat

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

class CatRepository {

    suspend fun getRandomCat(): CatFactDto {
        var delayTime = 500L

        repeat(3) { attempt ->
            try {
                return RetrofitInstance.catApi.getRandomCat()
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                if (attempt == 2) throw e
                delay(delayTime)
                delayTime *= 2
            } catch (e: HttpException) {
                if (e.code() < 500 || attempt == 2) throw e
                delay(delayTime)
                delayTime *= 2
            }
        }

        throw IOException("Не удалось получить факт о котиках")
    }
}
