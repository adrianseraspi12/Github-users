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
import com.example.githubusers.util.constants.requestUserProfileErrorMessage
import com.example.githubusers.util.constants.updateUserErrorMessage
import com.example.githubusers.util.extensions.toLocalProfile
import com.example.githubusers.util.extensions.toLocalUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer

@ExperimentalCoroutinesApi
class MainRepositoryTest {

    private lateinit var localRepository: IUserRepository
    private lateinit var remoteRepository: IGithubRepository
    private lateinit var mainRepository: MainRepository

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private val userResponse = UserResponse(
        30, "john",
        "https://www.jd.com/png"
    )
    private val profileResponse = ProfileResponse(
        "https://www.jd.com/png",
        "John Doe",
        "",
        12,
        23,
        "Apple",
        ""
    )

    private val listOfUserFromRemote = mutableListOf(userResponse)
    private val listOfUserWithProfileLocal = mutableListOf(
        UserWithProfile(
            userResponse.toLocalUser(),
            profileResponse.toLocalProfile(userResponse.id)
        )
    )

    @Before
    fun setup() {
        localRepository = mock(IUserRepository::class.java)
        remoteRepository = mock(IGithubRepository::class.java)
        mainRepository =
            MainRepository(localRepository, remoteRepository, testCoroutineDispatcher)
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @Test
    fun test_When_LocalAndRemoteIsEmpty_Should_CallError() =
        runBlocking {
            `when`(localRepository.getAllUserWithProfile()).thenReturn(Result.onSuccess(null))
            `when`(remoteRepository.requestUserList(0)).thenReturn(Result.onSuccess(null))

            val mockMainRepositoryListener = MockMainRepositoryListener<List<UserWithProfile>>()
            mainRepository.loadUserList(mockMainRepositoryListener)

            Assert.assertFalse(mockMainRepositoryListener.isSuccess)
            Assert.assertEquals(
                mockMainRepositoryListener.errorMessage,
                requestUserListErrorMessage
            )
            Assert.assertNull(mockMainRepositoryListener.data)
        }

    @Test
    fun test_When_LocalAndRemoteHasValues_Should_CallSuccess() =
        runBlocking {
            `when`(localRepository.getAllUserWithProfile()).thenReturn(
                Result.onSuccess(
                    listOfUserWithProfileLocal
                )
            )
            `when`(remoteRepository.requestUserList(0)).thenReturn(
                Result.onSuccess(
                    listOfUserFromRemote
                )
            )
            `when`(remoteRepository.requestUserProfile(userResponse.name!!)).thenReturn(
                Result.onSuccess(
                    profileResponse
                )
            )

            val mockMainRepositoryListener = MockMainRepositoryListener<List<UserWithProfile>>()
            mainRepository.loadUserList(mockMainRepositoryListener)

            val expected = listOf(
                UserWithProfile(
                    userResponse.toLocalUser(),
                    profileResponse.toLocalProfile(userResponse.id)
                )
            )

            Assert.assertTrue(mockMainRepositoryListener.isSuccess)
            Assert.assertNull(mockMainRepositoryListener.errorMessage)
            Assert.assertEquals(mockMainRepositoryListener.data, expected)
        }

    @Test
    fun test_When_UpdateUserOnLocalIsSuccess_Should_CallOnSuccess() =
        runBlocking {
            val localUser = LocalUser()
            `when`(localRepository.updateUser(localUser)).thenReturn(Result.onSuccess())

            val mockMainRepositoryListener = MockMainRepositoryListener<Nothing>()
            mainRepository.updateUserOnLocal(localUser, mockMainRepositoryListener)

            Assert.assertTrue(mockMainRepositoryListener.isSuccess)
        }

    @Test
    fun test_When_UpdateUserOnLocalIsFail_Should_CallOnFailWithErrorMessage() =
        runBlocking {
            val localUser = LocalUser()
            `when`(localRepository.updateUser(localUser)).thenReturn(
                Result.onFailed(
                    updateUserErrorMessage
                )
            )

            val mockMainRepositoryListener = MockMainRepositoryListener<Nothing>()
            mainRepository.updateUserOnLocal(localUser, mockMainRepositoryListener)

            Assert.assertFalse(mockMainRepositoryListener.isSuccess)
            Assert.assertEquals(mockMainRepositoryListener.errorMessage, updateUserErrorMessage)
        }

    @Test
    fun test_When_LocalValuesIsMoreThanRemote_RemoteValues_Should_RequestAndAddValuesToMakeEqualToLocal() =
        runBlocking {
            val newUserResponse = UserResponse(
                45, "john",
                "https://www.jd.com/png"
            )
            val newProfileResponse = ProfileResponse(
                "https://www.jd.com/png",
                "John Doe",
                "",
                12,
                23,
                "Apple",
                ""
            )

            //  Need to add another value so that
            //  it will enter the while loop
            val listOfUserWithProfileLocalWithAddedValue = listOfUserWithProfileLocal
            listOfUserWithProfileLocalWithAddedValue.add(
                UserWithProfile(
                    newUserResponse.toLocalUser(),
                    newProfileResponse.toLocalProfile(newUserResponse.id)
                )
            )

            `when`(localRepository.getAllUserWithProfile()).thenReturn(
                Result.onSuccess(
                    listOfUserWithProfileLocalWithAddedValue
                )
            )

            //  Return the correct value based on
            //  the parameter given
            doAnswer(object : Answer<Result<List<UserResponse>>> {

                override fun answer(invocation: InvocationOnMock?): Result<List<UserResponse>> {
                    val since = invocation!!.arguments[0] as Int
                    if (since == 0) {
                        return Result.onSuccess(listOfUserFromRemote)
                    } else if (since == userResponse.id!!) {
                        return Result.onSuccess(listOf(newUserResponse))
                    }
                    return Result.onFailed(requestUserListErrorMessage)
                }

            }).`when`(remoteRepository).requestUserList(anyInt())

            //  Return the correct value based on
            //  the parameter given
            doAnswer(object : Answer<Result<ProfileResponse>> {

                override fun answer(invocation: InvocationOnMock?): Result<ProfileResponse> {
                    val username = invocation!!.arguments[0] as String
                    if (username == userResponse.name) {
                        return Result.onSuccess(profileResponse)
                    } else if (username == newProfileResponse.name) {
                        return Result.onSuccess(newProfileResponse)
                    }
                    return Result.onFailed(requestUserProfileErrorMessage)
                }

            }).`when`(remoteRepository).requestUserProfile(anyString())

            //  Call the loadUserList
            val mockMainRepositoryListener = MockMainRepositoryListener<List<UserWithProfile>>()
            mainRepository.loadUserList(mockMainRepositoryListener)

            //  Create an expected list
            val expected = listOf(
                UserWithProfile(
                    userResponse.toLocalUser(),
                    profileResponse.toLocalProfile(userResponse.id)
                ),
                UserWithProfile(
                    newUserResponse.toLocalUser(),
                    newProfileResponse.toLocalProfile(newUserResponse.id)
                )
            )

            Assert.assertTrue(mockMainRepositoryListener.isSuccess)
            Assert.assertNull(mockMainRepositoryListener.errorMessage)
            Assert.assertEquals(mockMainRepositoryListener.data, expected)
        }

    @Test
    fun test_When_LoadUserListIsSuccessful_Should_CallSuccess() =
        runBlocking {
            `when`(remoteRepository.requestUserList(0)).thenReturn(
                Result.onSuccess(
                    listOfUserFromRemote
                )
            )
            `when`(remoteRepository.requestUserProfile(userResponse.name!!)).thenReturn(
                Result.onSuccess(
                    profileResponse
                )
            )

            val mockMainRepositoryListener = MockMainRepositoryListener<List<UserWithProfile>>()
            mainRepository.loadUserList(0, mockMainRepositoryListener)

            val expected = listOf(
                UserWithProfile(
                    userResponse.toLocalUser(),
                    profileResponse.toLocalProfile(userResponse.id)
                )
            )

            Assert.assertTrue(mockMainRepositoryListener.isSuccess)
            Assert.assertNull(mockMainRepositoryListener.errorMessage)
            Assert.assertEquals(mockMainRepositoryListener.data, expected)
        }

    @Test
    fun test_When_LoadUserIsFail_Should_CallFailWithErrorMessage() = runBlocking {
        `when`(remoteRepository.requestUserList(0)).thenReturn(Result.onSuccess(null))

        val mockMainRepositoryListener = MockMainRepositoryListener<List<UserWithProfile>>()
        mainRepository.loadUserList(0, mockMainRepositoryListener)

        Assert.assertFalse(mockMainRepositoryListener.isSuccess)
        Assert.assertEquals(
            mockMainRepositoryListener.errorMessage,
            requestUserListErrorMessage
        )
        Assert.assertNull(mockMainRepositoryListener.data)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}