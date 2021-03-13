package com.example.githubusers.data.local.entity

import org.junit.Assert
import org.junit.Test

class LocalUserTest {

    @Test
    fun test_When_LocalUserValuesIsComplete_LocalUser_Should_NotBeNull() {
        val localUser = LocalUser(
            0, "john",
            "https://www.jd.com/png",
            "",
        )

        Assert.assertEquals(localUser.id, 0)
        Assert.assertEquals(localUser.username, "john")
        Assert.assertEquals(localUser.image, "https://www.jd.com/png")
        Assert.assertEquals(localUser.notes, "")
    }

    @Test
    fun test_When_LocalUserValuesIsSetNull_LocalUser_Should_BeNull() {
        val localUser = LocalUser()

        Assert.assertNull(localUser.id)
        Assert.assertNull(localUser.username)
        Assert.assertNull(localUser.image)
        Assert.assertNull(localUser.notes)
    }

}