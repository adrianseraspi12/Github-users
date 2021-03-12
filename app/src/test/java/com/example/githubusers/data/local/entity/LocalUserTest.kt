package com.example.githubusers.data.local.entity

import org.junit.Assert
import org.junit.Test

class LocalUserTest {

    @Test
    fun test_When_LocalUserValuesIsComplete_LocalUser_Should_NotBeNull() {
        val localUser = LocalUser(
            0, "john",
            "https://www.jd.com/png",
            "John Doe",
            12,
            10,
            "Apple",
            "www.apple.com",
            ""
        )

        Assert.assertEquals(localUser.id, 0)
        Assert.assertEquals(localUser.username, "john")
        Assert.assertEquals(localUser.image, "https://www.jd.com/png")
        Assert.assertEquals(localUser.name, "John Doe")
        Assert.assertEquals(localUser.followersCount, 12)
        Assert.assertEquals(localUser.followingCount, 10)
        Assert.assertEquals(localUser.company, "Apple")
        Assert.assertEquals(localUser.blog, "www.apple.com")
        Assert.assertEquals(localUser.notes, "")
    }

    @Test
    fun test_When_LocalUserValuesIsSetNull_LocalUser_Should_BeNull() {
        val localUser = LocalUser()

        Assert.assertEquals(localUser.id, null)
        Assert.assertEquals(localUser.username, null)
        Assert.assertEquals(localUser.image, null)
        Assert.assertEquals(localUser.name, null)
        Assert.assertEquals(localUser.followersCount, null)
        Assert.assertEquals(localUser.followingCount, null)
        Assert.assertEquals(localUser.company, null)
        Assert.assertEquals(localUser.blog, null)
        Assert.assertEquals(localUser.notes, null)
    }

}