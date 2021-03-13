package com.example.githubusers.data.local.entity

import org.junit.Assert
import org.junit.Test

class UserWithProfileTest {

    @Test
    fun test_When_UserWithProfileValuesIsComplete_UserWithProfile_Should_NotBeNull() {
        val user = LocalUser(
            0, "john",
            "https://www.jd.com/png",
            "",
        )
        val profile = LocalProfile(
            0, 0,
            "John Doe",
            12,
            23,
            "Apple",
            ""
        )

        val userWithProfile = UserWithProfile(user, profile)
        Assert.assertEquals(userWithProfile.user, user)
        Assert.assertEquals(userWithProfile.profile, profile)
    }

    @Test
    fun test_When_UserWithProfileValuesIsComplete_UserWithProfile_Should_BeNull() {
        val userWithProfile = UserWithProfile()
        Assert.assertNull(userWithProfile.user)
        Assert.assertNull(userWithProfile.profile)
    }

}