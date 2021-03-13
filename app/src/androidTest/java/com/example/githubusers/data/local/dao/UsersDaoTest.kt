package com.example.githubusers.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.githubusers.data.local.UsersDatabase
import com.example.githubusers.data.local.entity.LocalProfile
import com.example.githubusers.data.local.entity.LocalUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class UsersDaoTest {

    private lateinit var usersDatabase: UsersDatabase

    @Before
    fun setup() {
        //  Use inMemoryDatabase for testing because
        //  the data stored here will erase after
        //  testing
        usersDatabase = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            UsersDatabase::class.java
        ).build()
    }

    @Test
    fun insertListOfUsersAndGetAllUsers() {
        val listOfUsers = listOf(
            LocalUser(
                0,
                    "john",
                "https://www.jd.com/png",
                "",
            ),
            LocalUser(
                1, "jane",
                "https://www.jnd.com/png",
                "Visit my blog",
            ),
            LocalUser(
                2, "uncle",
                "https://www.ub.com/png",
                "notes to uncle",
            )
        )

        usersDatabase.usersDao().insertAll(listOfUsers)

        val listOfUserFromDb = usersDatabase.usersDao().getAllUsers()

        Assert.assertNotNull(listOfUserFromDb)
        Assert.assertEquals(listOfUserFromDb, listOfUsers)
        Assert.assertEquals(listOfUserFromDb.size, 3)
    }

    @Test
    fun insertUserAndGetUserById() {
        val user = LocalUser(
                0,
                "john",
                "https://www.jd.com/png",
                "",
        )
        usersDatabase.usersDao().insertAll(listOf(user))

        val userFromDb = usersDatabase.usersDao().findUserById(0)

        Assert.assertNotNull(userFromDb)
        Assert.assertEquals(userFromDb.id, user.id)
        Assert.assertEquals(userFromDb.username, user.username)
        Assert.assertEquals(userFromDb.image, user.image)
        Assert.assertEquals(userFromDb.notes, user.notes)
    }

    @Test
    fun updateUserAndGetById() {
        //  Insert a user
        val user = LocalUser(
                0,
                "john",
                "https://www.jd.com/png",
                "",
        )
        usersDatabase.usersDao().insertAll(listOf(user))

        //  Update user
        val updatedUser = LocalUser(
                0,
                "john",
                "https://www.jd.com/png",
                "",
        )
        usersDatabase.usersDao().updateUser(updatedUser)

        //  Get update user and check
        val updatedUserFromDb = usersDatabase.usersDao().findUserById(0)
        Assert.assertNotNull(updatedUserFromDb)
        Assert.assertEquals(updatedUserFromDb.id, updatedUser.id)
        Assert.assertEquals(updatedUserFromDb.username, updatedUser.username)
        Assert.assertEquals(updatedUserFromDb.image, updatedUser.image)
        Assert.assertEquals(updatedUserFromDb.notes, updatedUser.notes)
    }

    @Test
    fun insertUserThenDeleteAllUserAndGetAllUser() {
        //  Insert a user
        val user = LocalUser(
                0,
                "john",
                "https://www.jd.com/png",
                "",
        )
        usersDatabase.usersDao().insertAll(listOf(user))

        //  Execute delete all user
        usersDatabase.usersDao().deleteAll()

        //  Get all user and check
        val listOfUserFromDb = usersDatabase.usersDao().getAllUsers()
        Assert.assertNotNull(listOfUserFromDb)
        Assert.assertTrue(listOfUserFromDb.isEmpty())
    }

    @Test
    fun insertUserWithProfileThenGetAllUserWithProfile() {
        //  Insert a user
        val user = LocalUser(
                0,
                "john",
                "https://www.jd.com/png",
                "",
        )
        usersDatabase.usersDao().insertAll(listOf(user))

        //  Insert profile
        val profile =  LocalProfile(
                0,
                user.id!!,
                "John doe",
                23,
                12,
                "Apple",
                "www.apple.com"
        )
        usersDatabase.profileDao().insertProfile(profile)

        //  Execute get user with profile and check
        val listOfUserWithProfile = usersDatabase.usersDao().getAllUserWithProfile()
        Assert.assertEquals(listOfUserWithProfile.size, 1)
        Assert.assertEquals(listOfUserWithProfile[0].user, user)
        Assert.assertEquals(listOfUserWithProfile[0].profile, profile)
    }

    @Test
    fun insertUserWithNoProfileThenGetAllUserWithNoProfile() {
        //  Insert a user
        val user = LocalUser(
                0,
                "john",
                "https://www.jd.com/png",
                "",
        )
        usersDatabase.usersDao().insertAll(listOf(user))

        //  Execute get user with profile and check
        val listOfUserWithProfile = usersDatabase.usersDao().getAllUserWithProfile()
        Assert.assertEquals(listOfUserWithProfile.size, 1)
        Assert.assertEquals(listOfUserWithProfile[0].user, user)
        Assert.assertNull(listOfUserWithProfile[0].profile)
    }

    @After
    fun tearDown() {
        usersDatabase.close()
    }
}