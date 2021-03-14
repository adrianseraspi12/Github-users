package com.example.githubusers.data.remote.model

import org.junit.Assert
import org.junit.Test

class UserResponseTest {

    @Test
    fun test_When_UserResponseValuesIsEmpty_UserResponseValues_Should_AssertNull() {
        val userResponse = UserResponse()
        Assert.assertNull(userResponse.name)
        Assert.assertNull(userResponse.image)
    }

    @Test
    fun test_When_UserResponseValuesIsComplete_UserResponseValues_Should_NotBeNull() {
        val userResponse = UserResponse(
            0,
            "John Doe",
            "https://www.fbi.gov/wanted/vicap/unidentified-persons/john-doe-21/@@images/image/large"
        )
        Assert.assertNotNull(userResponse.name)
        Assert.assertNotNull(userResponse.image)
    }
}