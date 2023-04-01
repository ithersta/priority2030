package services.morpher

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PetrovichTest {
    @Test
    fun test() {
        val petrovich = Petrovich()
        runBlocking {
            assertEquals("Владислава", petrovich.morphFullName("Владислав").genitive)
        }
    }
}
