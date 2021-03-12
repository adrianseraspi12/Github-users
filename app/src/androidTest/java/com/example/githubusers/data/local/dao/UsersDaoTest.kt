package com.example.githubusers.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.githubusers.data.local.UsersDatabase
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
                0, "john",
                "https://www.jd.com/png",
                "John Doe",
                12,
                10,
                "Apple",
                "www.apple.com",
                ""
            ),
            LocalUser(
                1, "jane",
                "https://www.jnd.com/png",
                "Jane Doe",
                8,
                14,
                "Orange",
                "www.orange.com",
                "Hello Jane Doe"
            ),
            LocalUser(
                2, "uncle",
                "https://www.ub.com/png",
                "Uncle Bob",
                101,
                8,
                "Clean code",
                "www.cleancode.com",
                "Welcome Uncle"
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
            0, "john",
            "https://www.jd.com/png",
            "John Doe",
            12,
            10,
            "Apple",
            "www.apple.com",
            ""
        )

        usersDatabase.usersDao().insertAll(listOf(user))

        val userFromDb = usersDatabase.usersDao().findUserById(0)

        Assert.assertNotNull(userFromDb)
        Assert.assertEquals(userFromDb.id, user.id)
        Assert.assertEquals(userFromDb.username, user.username)
        Assert.assertEquals(userFromDb.image, user.image)
        Assert.assertEquals(userFromDb.name, user.name)
        Assert.assertEquals(userFromDb.followersCount, user.followersCount)
        Assert.assertEquals(userFromDb.followingCount, user.followingCount)
        Assert.assertEquals(userFromDb.company, user.company)
        Assert.assertEquals(userFromDb.blog, user.blog)
        Assert.assertEquals(userFromDb.notes, user.notes)
    }

    @Test
    fun updateUserAndGetById() {
        //  Insert a user
        val user = LocalUser(
            0, "john",
            "https://www.jd.com/png",
            "John Doe",
            12,
            10,
            "Apple",
            "www.apple.com",
            ""
        )
        usersDatabase.usersDao().insertAll(listOf(user))

        //  Update user
        val updatedUser = LocalUser(
            0, "johnny",
            "https://www.jd.com/png",
            "Johnny Doe",
            15,
            12,
            "Apple",
            "www.apple.com",
            ""
        )
        usersDatabase.usersDao().updateUser(updatedUser)

        //  Get update user and check
        val updatedUserFromDb = usersDatabase.usersDao().findUserById(0)
        Assert.assertNotNull(updatedUserFromDb)
        Assert.assertEquals(updatedUserFromDb.id, updatedUser.id)
        Assert.assertEquals(updatedUserFromDb.username, updatedUser.username)
        Assert.assertEquals(updatedUserFromDb.image, updatedUser.image)
        Assert.assertEquals(updatedUserFromDb.name, updatedUser.name)
        Assert.assertEquals(updatedUserFromDb.followersCount, updatedUser.followersCount)
        Assert.assertEquals(updatedUserFromDb.followingCount, updatedUser.followingCount)
        Assert.assertEquals(updatedUserFromDb.company, updatedUser.company)
        Assert.assertEquals(updatedUserFromDb.blog, updatedUser.blog)
        Assert.assertEquals(updatedUserFromDb.notes, updatedUser.notes)
    }

    @Test
    fun insertUserThenDeleteAllUserAndGetAllUser() {
        //  Insert a user
        val user = LocalUser(
            0, "john",
            "https://www.jd.com/png",
            "John Doe",
            12,
            10,
            "Apple",
            "www.apple.com",
            ""
        )
        usersDatabase.usersDao().insertAll(listOf(user))

        //  Execute delete all user
        usersDatabase.usersDao().deleteAll()

        //  Get all user and check
        val listOfUserFromDb = usersDatabase.usersDao().getAllUsers()
        Assert.assertNotNull(listOfUserFromDb)
        Assert.assertTrue(listOfUserFromDb.isEmpty())
    }

    @After
    fun tearDown() {
        usersDatabase.close()
    }
}