package com.example.githubusers.data.main

import com.example.githubusers.data.local.Result
import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.data.local.repository.IUserRepository
import com.example.githubusers.data.main.repository.MainRepository
import com.example.githubusers.data.remote.model.ProfileResponse
import com.example.githubusers.data.remote.model.UserResponse
import com.example.githubusers.data.remote.repository.IGithubRepository
import com.example.githubusers.util.constants.requestUserListErrorMessage
import com.example.githubusers.util.constants.updateUserErrorMessage
import com.example.githubusers.util.extensions.toLocalProfile
import com.example.githubusers.util.extensions.toLocalUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class MainRepositoryTest {

    private lateinit var localRepository: IUserRepository
    private lateinit var remoteRepository: IGithubRepository
    private lateinit var mainRepository: MainRepository

    @Before
    fun setup() {
        localRepository = mock(IUserRepository::class.java)
        remoteRepository = mock(IGithubRepository::class.java)
        mainRepository = MainRepository(localRepository, remoteRepository, TestCoroutineDispatcher())
    }

    @Test
    fun test_When_LocalAndRemoteIsEmpty_Should_CallError() = runBlockingTest {
        `when`(localRepository.getAllUsers()).thenReturn(Result.onSuccess(null))
        `when`(remoteRepository.requestUserList(0)).thenReturn(Result.onSuccess(null))

        val mockMainRepositoryListener = MockMainRepositoryListener<List<UserWithProfile>>()
        mainRepository.loadUserList(mockMainRepositoryListener)

        Assert.assertFalse(mockMainRepositoryListener.isSuccess)
        Assert.assertEquals(mockMainRepositoryListener.errorMessage, requestUserListErrorMessage)
        Assert.assertNull(mockMainRepositoryListener.data)
    }

    @Test
    fun test_When_LocalAndRemoteHasValues_Should_CallSuccess() = runBlockingTest {
        val userResponse = UserResponse(
                0, "john",
                "https://www.jd.com/png"
        )
        val profileResponse = ProfileResponse(
                "https://www.jd.com/png",
                "John Doe",
                "",
                12,
                23,
                "Apple",
                ""
        )
        val listOfUserFromLocal = listOf(userResponse.toLocalUser())
        val listOfUserFromRemote = listOf(userResponse)

        `when`(localRepository.getAllUsers()).thenReturn(Result.onSuccess(listOfUserFromLocal))
        `when`(remoteRepository.requestUserList(0)).thenReturn(Result.onSuccess(listOfUserFromRemote))
        `when`(remoteRepository.requestUserProfile(userResponse.name!!)).thenReturn(Result.onSuccess(profileResponse))

        val mockMainRepositoryListener = MockMainRepositoryListener<List<UserWithProfile>>()
        mainRepository.loadUserList(mockMainRepositoryListener)

        val expected = listOf(
                UserWithProfile(
                        userResponse.toLocalUser(),
                        profileResponse.toLocalProfile(0)
                )
        )

        Assert.assertTrue(mockMainRepositoryListener.isSuccess)
        Assert.assertNull(mockMainRepositoryListener.errorMessage)
        Assert.assertEquals(mockMainRepositoryListener.data, expected)
    }

    @Test
    fun test_When_UpdateUserOnLocalIsSuccess_Should_CallOnSuccess() = runBlockingTest {
        val localUser = LocalUser()
        `when`(localRepository.updateUser(localUser)).thenReturn(Result.onSuccess())

        val mockMainRepositoryListener = MockMainRepositoryListener<Nothing>()
        mainRepository.updateUserOnLocal(localUser, mockMainRepositoryListener)

        Assert.assertTrue(mockMainRepositoryListener.isSuccess)
    }

    @Test
    fun test_When_UpdateUserOnLocalIsFail_Should_CallOnFailWithErrorMessage() = runBlockingTest {
        val localUser = LocalUser()
        `when`(localRepository.updateUser(localUser)).thenReturn(Result.onFailed(updateUserErrorMessage))

        val mockMainRepositoryListener = MockMainRepositoryListener<Nothing>()
        mainRepository.updateUserOnLocal(localUser, mockMainRepositoryListener)

        Assert.assertFalse(mockMainRepositoryListener.isSuccess)
        Assert.assertEquals(mockMainRepositoryListener.errorMessage, updateUserErrorMessage)
    }
}