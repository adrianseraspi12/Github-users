package com.example.githubusers.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.githubusers.data.local.UsersDatabase
import com.example.githubusers.data.local.entity.LocalProfile
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ProfileDaoTest {

    private lateinit var usersDatabase: UsersDatabase

    @Before
    fun setup() {
        //  Use inMemoryDatabase for testing because
        //  the data stored here will erase after
        //  testing
        usersDatabase = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                UsersDatabase::class.java
        ).build()
    }

    @Test
    fun insertProfileAndGetProfileById() {
        val localProfile = LocalProfile(
                0, 0,
                "John Doe",
                "",
                12,
                23,
                "Apple",
                ""
        )
        usersDatabase.profileDao().insertProfile(localProfile)
        val profileFromDb = usersDatabase.profileDao().findProfileByUserId(0)

        Assert.assertNotNull(profileFromDb)
        Assert.assertEquals(profileFromDb.profileId, localProfile.profileId)
        Assert.assertEquals(profileFromDb.userId, localProfile.userId)
        Assert.assertEquals(profileFromDb.name, localProfile.name)
        Assert.assertEquals(profileFromDb.bio, localProfile.bio)
        Assert.assertEquals(profileFromDb.followingCount, localProfile.followingCount)
        Assert.assertEquals(profileFromDb.followersCount, localProfile.followersCount)
        Assert.assertEquals(profileFromDb.company, localProfile.company)
        Assert.assertEquals(profileFromDb.blog, localProfile.blog)
    }

    @Test
    fun updateProfileAndGetProfileById() {
        val localProfile = LocalProfile(
                0, 0,
                "John Doe",
                "",
                12,
                23,
                "Apple",
                ""
        )
        usersDatabase.profileDao().insertProfile(localProfile)

        val updatedProfile = LocalProfile(
                0, 0,
                "John Doe",
                "QWE",
                10,
                15,
                "Orange",
                "www.orange.com"
        )
        usersDatabase.profileDao().updateProfile(updatedProfile)
        val profileFromDb = usersDatabase.profileDao().findProfileByUserId(0)

        Assert.assertNotNull(profileFromDb)
        Assert.assertEquals(profileFromDb.profileId, updatedProfile.profileId)
        Assert.assertEquals(profileFromDb.userId, updatedProfile.userId)
        Assert.assertEquals(profileFromDb.name, updatedProfile.name)
        Assert.assertEquals(profileFromDb.bio, updatedProfile.bio)
        Assert.assertEquals(profileFromDb.followingCount, updatedProfile.followingCount)
        Assert.assertEquals(profileFromDb.followersCount, updatedProfile.followersCount)
        Assert.assertEquals(profileFromDb.company, updatedProfile.company)
        Assert.assertEquals(profileFromDb.blog, updatedProfile.blog)
    }

    @Test
    fun insertProfileThenDeleteAllProfileAndGetProfileById() {
        val localProfile = LocalProfile(
                0, 0,
                "John Doe",
                "",
                12,
                23,
                "Apple",
                ""
        )
        usersDatabase.profileDao().insertProfile(localProfile)
        usersDatabase.profileDao().deleteAll()

        val profileFromDb = usersDatabase.profileDao().findProfileByUserId(0)
        Assert.assertNull(profileFromDb)
    }

    @After
    fun tearDown() {
        usersDatabase.close()
    }
}