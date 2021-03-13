package com.example.githubusers.data.local.repository

import com.example.githubusers.data.local.dao.ProfileDao
import com.example.githubusers.data.local.dao.UsersDao
import com.example.githubusers.data.local.entity.LocalProfile
import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.data.util.constants.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class UserRepositoryTest {

    private lateinit var userDao: UsersDao
    private lateinit var profileDao: ProfileDao
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        userDao = mock(UsersDao::class.java)
        profileDao = mock(ProfileDao::class.java)
        userRepository = UserRepository(userDao, profileDao, TestCoroutineDispatcher())
    }

    @Test
    fun test_When_GetAllUsersIsNotEmpty_ListOfUsers_Should_BeEqualToListFromDao() =
        runBlockingTest {
            val listOfUsers = listOf(
                LocalUser(
                    0, "john",
                    "https://www.jd.com/png",
                    "John Doe",
                ),
                LocalUser(
                    1, "jane",
                    "https://www.jnd.com/png",
                    "Jane Doe",
                ),
                LocalUser(
                    2, "uncle",
                    "https://www.ub.com/png",
                    "Uncle Bob"
                )
            )

            `when`(userDao.getAllUsers()).thenReturn(listOfUsers)

            val mockUserRepoListener = MockUserRepositoryListener<List<LocalUser>>()
            userRepository.getAllUsers(mockUserRepoListener)

            Assert.assertNotNull(mockUserRepoListener.data)
            Assert.assertEquals(mockUserRepoListener.data, listOfUsers)
        }

    @Test
    fun test_When_GetAllUsersEmpty_ListOfUsers_Should_ReturnEmpty() = runBlockingTest {
        `when`(userDao.getAllUsers()).thenReturn(listOf())

        val mockUserRepoListener = MockUserRepositoryListener<List<LocalUser>>()
        userRepository.getAllUsers(mockUserRepoListener)

        Assert.assertNotNull(mockUserRepoListener.data)
        Assert.assertTrue(mockUserRepoListener.data!!.isEmpty())
    }

    @Test
    fun test_When_GetAllUsersIsFail_Should_ReturnErrorMessage() = runBlockingTest {
        `when`(userDao.getAllUsers()).thenThrow(IllegalStateException())

        val mockUserRepoListener = MockUserRepositoryListener<List<LocalUser>>()
        userRepository.getAllUsers(mockUserRepoListener)

        Assert.assertEquals(mockUserRepoListener.errorMessage, getAllUsersErrorMessage)
        Assert.assertNull(mockUserRepoListener.data)
    }

    @Test
    fun test_When_GetUserIsSuccess_Should_ReturnCorrectUserProfile() = runBlockingTest {
        val localUser = LocalUser(
            0, "john",
            "https://www.jd.com/png",
            "John Doe",
        )
        `when`(userDao.findUserById(0)).thenReturn(localUser)

        val mockUserRepoListener = MockUserRepositoryListener<LocalUser>()
        userRepository.getUser(anyInt(), mockUserRepoListener)

        Assert.assertNotNull(mockUserRepoListener.data)
        Assert.assertEquals(mockUserRepoListener.data, localUser)
    }

    @Test
    fun test_When_GetUserIsFail_Should_ReturnErrorMessage() = runBlockingTest {
        `when`(userDao.findUserById(0)).thenThrow(IllegalStateException())

        val mockUserRepoListener = MockUserRepositoryListener<LocalUser>()
        userRepository.getUser(anyInt(), mockUserRepoListener)

        Assert.assertNull(mockUserRepoListener.data)
        Assert.assertEquals(mockUserRepoListener.errorMessage, getUserErrorMessage)
    }

    @Test
    fun test_When_UpdateUserIsSuccessful_Should_CallOnSuccess() = runBlockingTest {
        val mockUserRepoListener = MockUserRepositoryListener<Any>()
        userRepository.updateUser(LocalUser(), mockUserRepoListener)
        Assert.assertTrue(mockUserRepoListener.isSuccessCalled)
    }

    @Test
    fun test_When_UpdateUserIsFail_Should_ReturnErrorMessage() = runBlockingTest {
        `when`(userDao.updateUser(LocalUser())).thenThrow(IllegalStateException())

        val mockUserRepoListener = MockUserRepositoryListener<Any>()
        userRepository.updateUser(LocalUser(), mockUserRepoListener)

        Assert.assertFalse(mockUserRepoListener.isSuccessCalled)
        Assert.assertEquals(mockUserRepoListener.errorMessage, updateUserErrorMessage)
    }

    @Test
    fun test_When_SaveAllIsSuccessful_Should_CallOnSuccess() = runBlockingTest {
        val mockUserRepoListener = MockUserRepositoryListener<Any>()
        userRepository.saveAll(listOf(LocalUser()), mockUserRepoListener)
        Assert.assertTrue(mockUserRepoListener.isSuccessCalled)
    }

    @Test
    fun test_When_SaveAllIsFail_Should_ReturnErrorMessage() = runBlockingTest {
        `when`(userDao.insertAll(listOf(LocalUser()))).thenThrow(IllegalStateException())

        val mockUserRepoListener = MockUserRepositoryListener<Any>()
        userRepository.saveAll(listOf(LocalUser()), mockUserRepoListener)

        Assert.assertFalse(mockUserRepoListener.isSuccessCalled)
        Assert.assertEquals(mockUserRepoListener.errorMessage, saveAllErrorMessage)
    }

    @Test
    fun test_When_DeleteAllUserIsSuccessful_Should_CallOnSuccess() = runBlockingTest {
        val mockUserRepoListener = MockUserRepositoryListener<Any>()
        userRepository.deleteAllUser(mockUserRepoListener)
        Assert.assertTrue(mockUserRepoListener.isSuccessCalled)
    }

    @Test
    fun test_When_DeleteAllUserIsFail_Should_ReturnErrorMessage() = runBlockingTest {
        `when`(userDao.deleteAll()).thenThrow(IllegalStateException())

        val mockUserRepoListener = MockUserRepositoryListener<Any>()
        userRepository.deleteAllUser(mockUserRepoListener)

        Assert.assertFalse(mockUserRepoListener.isSuccessCalled)
        Assert.assertEquals(mockUserRepoListener.errorMessage, deleteAllUserErrorMessage)
    }

    @Test
    fun test_When_GetAllUserWithProfileIsSuccessful_Should_CallOnSuccess() = runBlockingTest {
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
        val listOfUserWithProfile = listOf(UserWithProfile(user, profile))
        `when`(userDao.getAllUserWithProfile()).thenReturn(listOfUserWithProfile)

        val mockUserRepoListener = MockUserRepositoryListener<List<UserWithProfile>>()
        userRepository.getAllUserWithProfile(mockUserRepoListener)

        Assert.assertTrue(mockUserRepoListener.isSuccessCalled)
        Assert.assertNotNull(mockUserRepoListener.data)
        Assert.assertEquals(mockUserRepoListener.data!!.size, listOfUserWithProfile.size)
        Assert.assertEquals(mockUserRepoListener.data!![0].user, user)
        Assert.assertEquals(mockUserRepoListener.data!![0].profile, profile)
    }

    @Test
    fun test_When_GetAllUserWithProfileIsFail_Should_ReturnErrorMessage() = runBlockingTest {
        `when`(userDao.getAllUserWithProfile()).thenThrow(IllegalStateException())

        val mockUserRepoListener = MockUserRepositoryListener<List<UserWithProfile>>()
        userRepository.getAllUserWithProfile(mockUserRepoListener)

        Assert.assertFalse(mockUserRepoListener.isSuccessCalled)
        Assert.assertNull(mockUserRepoListener.data)
        Assert.assertEquals(mockUserRepoListener.errorMessage, getAllUserWithProfileErrorMessage)
    }

    @Test
    fun test_When_UpdateProfileIsSuccessful_Should_CallOnSuccess() = runBlockingTest {
        val mockUserRepoListener = MockUserRepositoryListener<Any>()
        userRepository.updateProfile(LocalProfile(), mockUserRepoListener)
        Assert.assertTrue(mockUserRepoListener.isSuccessCalled)
    }

    @Test
    fun test_When_UpdateProfileIsFail_Should_ReturnErrorMessage() = runBlockingTest {
        `when`(profileDao.updateProfile(LocalProfile())).thenThrow(IllegalStateException())

        val mockUserRepoListener = MockUserRepositoryListener<Any>()
        userRepository.updateProfile(LocalProfile(), mockUserRepoListener)

        Assert.assertFalse(mockUserRepoListener.isSuccessCalled)
        Assert.assertEquals(mockUserRepoListener.errorMessage, updateProfileErrorMessage)
    }

    @Test
    fun test_When_InsertProfileIsSuccessful_Should_CallOnSuccess() = runBlockingTest {
        val mockUserRepoListener = MockUserRepositoryListener<Any>()
        userRepository.insertProfile(LocalProfile(), mockUserRepoListener)
        Assert.assertTrue(mockUserRepoListener.isSuccessCalled)
    }

    @Test
    fun test_When_InsertProfileIsFail_Should_ReturnErrorMessage() = runBlockingTest {
        `when`(profileDao.insertProfile(LocalProfile())).thenThrow(IllegalStateException())

        val mockUserRepoListener = MockUserRepositoryListener<Any>()
        userRepository.insertProfile(LocalProfile(), mockUserRepoListener)

        Assert.assertFalse(mockUserRepoListener.isSuccessCalled)
        Assert.assertEquals(mockUserRepoListener.errorMessage, insertProfileErrorMessage)
    }
}