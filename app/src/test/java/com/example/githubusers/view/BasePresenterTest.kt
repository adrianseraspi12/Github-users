package com.example.githubusers.view

import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers

open class BasePresenterTest {

    inline fun <reified T : Any> argumentCaptor(): ArgumentCaptor<T> = ArgumentCaptor.forClass(T::class.java)
    fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
    fun <T : Any> safeEq(value: T): T = ArgumentMatchers.eq(value) ?: value

}