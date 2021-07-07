package com.soten.githubrepository

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.system.measureTimeMillis

class CoroutinesTest {

    @Test
    fun test01() = runBlocking {
        val time = measureTimeMillis {
            val name = getFirstName()
            val lastName = getLastName()

            println("Hello, $name $lastName")
        }
        println("$time")
    }

    @Test
    fun test02() = runBlocking {
        val time = measureTimeMillis {
            val name = async { getFirstName() }
            val lastName = async { getLastName() }

            println("Hello, ${name.await()} ${lastName.await()}")
        }
        println("$time")
    }

    private suspend fun getFirstName(): String {
        delay(1000)
        return "Choi"
    }

    private suspend fun getLastName(): String {
        delay(1000)
        return "younho"
    }
}