package com.example.githubusers.data.remote.model

import org.junit.Assert
import org.junit.Test

class ProfileResponseTest {

    @Test
    fun test_When_ProfileResponseValuesIsEmpty_ProfileResponseValues_Should_AssertNull() {
        val profileResponse = ProfileResponse()
        Assert.assertNull(profileResponse.image)
        Assert.assertNull(profileResponse.name)
        Assert.assertNull(profileResponse.blog)
        Assert.assertNull(profileResponse.company)
        Assert.assertNull(profileResponse.followersCount)
        Assert.assertNull(profileResponse.followingCount)
    }

    @Test
    fun test_When_ProfileResponseValuesIsComplete_ProfileResponseValues_Should_NotBeNull() {
        val profileResponse = ProfileResponse(
                "https://www.fbi.gov/wanted/vicap/unidentified-persons/john-doe-21/@@images/image/large",
                "John Doe",
                "",
                58,
                34,
                "Apple",
                "www.apple.com"
        )
        Assert.assertNotNull(profileResponse.image)
        Assert.assertNotNull(profileResponse.name)
        Assert.assertNotNull(profileResponse.blog)
        Assert.assertNotNull(profileResponse.company)
        Assert.assertNotNull(profileResponse.followersCount)
        Assert.assertNotNull(profileResponse.followingCount)
    }

}