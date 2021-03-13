package com.example.githubusers.data.local.entity

import org.junit.Assert
import org.junit.Test

class LocalProfileTest {

    @Test
    fun test_When_LocalProfileValuesIsComplete_LocalProfile_Should_NotBeNull() {
        val localProfile = LocalProfile(
            0, 0,
            "John Doe",
                "",
            12,
            23,
            "Apple",
            ""
        )

        Assert.assertEquals(localProfile.profileId, 0)
        Assert.assertEquals(localProfile.userId, 0)
        Assert.assertEquals(localProfile.name, "John Doe")
        Assert.assertEquals(localProfile.followersCount, 12)
        Assert.assertEquals(localProfile.followingCount, 23)
        Assert.assertEquals(localProfile.company, "Apple")
        Assert.assertEquals(localProfile.blog, "")
    }

    @Test
    fun test_When_LocalProfileValuesIsSetNull_LocalProfile_Should_BeNull() {
        val localProfile = LocalProfile()

        Assert.assertNull(localProfile.profileId)
        Assert.assertNull(localProfile.userId)
        Assert.assertNull(localProfile.name)
        Assert.assertNull(localProfile.followersCount)
        Assert.assertNull(localProfile.followingCount)
        Assert.assertNull(localProfile.company)
        Assert.assertNull(localProfile.blog)
    }

}