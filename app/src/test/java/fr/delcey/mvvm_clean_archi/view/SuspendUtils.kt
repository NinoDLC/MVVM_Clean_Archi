package fr.delcey.mvvm_clean_archi.view

import com.nhaarman.mockitokotlin2.KStubbing
import kotlinx.coroutines.runBlocking
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

fun <T : Any, R> KStubbing<T>.onBlocking(
    m: suspend T.() -> R
): OngoingStubbing<R> {
    return runBlocking { Mockito.`when`(mock.m()) }
}