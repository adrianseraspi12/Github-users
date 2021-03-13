package com.example.githubusers.data.remote.repository

import com.example.githubusers.data.remote.model.ProfileResponse
import com.example.githubusers.data.remote.model.UserResponse
import com.example.githubusers.data.remote.service.GithubClient
import com.example.githubusers.util.constants.requestUserListErrorMessage
import com.example.githubusers.util.constants.requestUserProfileErrorMessage
import com.example.githubusers.util.constants.somethingWentWrongErrorMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@ExperimentalCoroutinesApi
class GithubRepositoryTest {

    private lateinit var githubRepository: GithubRepository
    private lateinit var client: GithubClient

    @Before
    fun setup() {
        client = mock(GithubClient::class.java)
        githubRepository = GithubRepository(client, TestCoroutineDispatcher())
    }

    @Test
    fun test_When_RequestUserListIsSuccess_Should_ReturnListOfUser() = runBlockingTest {
        val listOfUserResponse = listOf(
                UserResponse(
                        "John Doe",
                        "https://www.jnd.com/png"
                ),
                UserResponse(
                        "Jane Doe",
                        "https://www.jd.com/png"
                )
        )

        //  Create a mock call for github client and
        val mockCall: Call<List<UserResponse>> = mock()
        `when`(client.getUserList(0)).thenReturn(mockCall)
        doAnswer {
            //  Setup mock api response
            (it.getArgument(0) as Callback<List<UserResponse>>).onResponse(
                    mockCall,
                    Response.success(200, listOfUserResponse)
            )
        }.`when`(mockCall).enqueue(any())

        //  Call RequestUserList
        val mockGithubRepositoryListener = MockGithubRepositoryListener<List<UserResponse>>()
        githubRepository.requestUserList(0, mockGithubRepositoryListener)

        //  Check the listener
        Assert.assertTrue(mockGithubRepositoryListener.isSuccessCalled)
        Assert.assertEquals(mockGithubRepositoryListener.data, listOfUserResponse)
    }

    @Test
    fun test_When_RequestUserListStatusCode500_Should_ReturnErrorMessage() = runBlockingTest {
        //  Create a mock call for github client and
        val mockCall: Call<List<UserResponse>> = mock()
        `when`(client.getUserList(0)).thenReturn(mockCall)
        doAnswer {
            //  Setup mock api response
            (it.getArgument(0) as Callback<List<UserResponse>>).onResponse(
                    mockCall,
                    Response.error(
                            500, ResponseBody.create(
                            MediaType.parse("application/json"),
                            ""
                    )
                    )
            )
        }.`when`(mockCall).enqueue(any())

        val mockGithubRepositoryListener = MockGithubRepositoryListener<List<UserResponse>>()
        githubRepository.requestUserList(0, mockGithubRepositoryListener)

        //  Check the listener
        Assert.assertFalse(mockGithubRepositoryListener.isSuccessCalled)
        Assert.assertNull(mockGithubRepositoryListener.data)
        Assert.assertEquals(mockGithubRepositoryListener.errorMessage, requestUserListErrorMessage)
    }

    @Test
    fun test_When_RequestUserListFail_Should_ReturnThrowableErrorMessage() = runBlockingTest {
        val throwable = Throwable("Fail tor request from server.")
        //  Create a mock call for github client and
        val mockCall: Call<List<UserResponse>> = mock()
        `when`(client.getUserList(0)).thenReturn(mockCall)
        doAnswer {
            //  Setup mock api response
            (it.getArgument(0) as Callback<List<UserResponse>>).onFailure(
                    mockCall,
                    throwable
            )
        }.`when`(mockCall).enqueue(any())

        val mockGithubRepositoryListener = MockGithubRepositoryListener<List<UserResponse>>()
        githubRepository.requestUserList(0, mockGithubRepositoryListener)

        //  Check the listener
        Assert.assertFalse(mockGithubRepositoryListener.isSuccessCalled)
        Assert.assertEquals(mockGithubRepositoryListener.errorMessage, throwable.message)
        Assert.assertNull(mockGithubRepositoryListener.data)
    }

    @Test
    fun test_When_RequestUserListFail_Should_ReturnSomethingWentWrongErrorMessage() =
            runBlockingTest {
                //  Create a mock call for github client and
                val mockCall: Call<List<UserResponse>> = mock()
                `when`(client.getUserList(0)).thenReturn(mockCall)
                doAnswer {
                    //  Setup mock api response
                    (it.getArgument(0) as Callback<List<UserResponse>>).onFailure(
                            mockCall,
                            Throwable()
                    )
                }.`when`(mockCall).enqueue(any())

                val mockGithubRepositoryListener = MockGithubRepositoryListener<List<UserResponse>>()
                githubRepository.requestUserList(0, mockGithubRepositoryListener)

                //  Check the listener
                Assert.assertFalse(mockGithubRepositoryListener.isSuccessCalled)
                Assert.assertEquals(
                        mockGithubRepositoryListener.errorMessage,
                        somethingWentWrongErrorMessage
                )
                Assert.assertNull(mockGithubRepositoryListener.data)
            }

    @Test
    fun test_When_RequestUserProfileIsSuccess_Should_ReturnUserProfile() = runBlockingTest {
        val profileResponse = ProfileResponse(
                "https://www.fbi.gov/wanted/vicap/unidentified-persons/john-doe-21/@@images/image/large",
                "John Doe",
                "",
                58,
                34,
                "Apple",
                "www.apple.com"
        )

        //  Create a mock call for github client and
        //  Setup mock api response
        val mockCall: Call<ProfileResponse> = mock()
        `when`(client.getProfile("johndoe")).thenReturn(mockCall)
        doAnswer {
            (it.getArgument(0) as Callback<ProfileResponse>).onResponse(
                    mockCall,
                    Response.success(200, profileResponse)
            )
        }.`when`(mockCall).enqueue(any())

        //  Call RequestUserList
        val mockGithubRepositoryListener = MockGithubRepositoryListener<ProfileResponse>()
        githubRepository.requestUserProfile("johndoe", mockGithubRepositoryListener)

        //  Check the listener
        Assert.assertTrue(mockGithubRepositoryListener.isSuccessCalled)
        Assert.assertEquals(mockGithubRepositoryListener.data, profileResponse)
    }

    @Test
    fun test_When_RequestUserProfileStatusCode500_Should_ReturnErrorMessage() = runBlockingTest {
        //  Create a mock call for github client and
        //  Setup mock api response
        val mockCall: Call<ProfileResponse> = mock()
        `when`(client.getProfile("johndoe")).thenReturn(mockCall)
        doAnswer {
            //  Setup mock api response
            (it.getArgument(0) as Callback<ProfileResponse>).onResponse(
                    mockCall,
                    Response.error(
                            500,
                            ResponseBody.create(MediaType.parse("application/json"), "")
                    )
            )
        }.`when`(mockCall).enqueue(any())

        //  Call RequestUserList
        val mockGithubRepositoryListener = MockGithubRepositoryListener<ProfileResponse>()
        githubRepository.requestUserProfile("johndoe", mockGithubRepositoryListener)

        //  Check the listener
        Assert.assertFalse(mockGithubRepositoryListener.isSuccessCalled)
        Assert.assertEquals(
                mockGithubRepositoryListener.errorMessage,
                requestUserProfileErrorMessage
        )
        Assert.assertNull(mockGithubRepositoryListener.data)
    }

    @Test
    fun test_When_RequestUserProfileFail_Should_ReturnThrowableErrorMessage() = runBlockingTest {
        val throwable = Throwable("Fail tor request from server.")
        //  Create a mock call for github client and
        val mockCall: Call<ProfileResponse> = mock()
        `when`(client.getProfile("johndoe")).thenReturn(mockCall)
        doAnswer {
            //  Setup mock api response
            (it.getArgument(0) as Callback<ProfileResponse>).onFailure(
                    mockCall,
                    throwable
            )
        }.`when`(mockCall).enqueue(any())

        val mockGithubRepositoryListener = MockGithubRepositoryListener<ProfileResponse>()
        githubRepository.requestUserProfile("johndoe", mockGithubRepositoryListener)

        //  Check the listener
        Assert.assertFalse(mockGithubRepositoryListener.isSuccessCalled)
        Assert.assertEquals(mockGithubRepositoryListener.errorMessage, throwable.message)
        Assert.assertNull(mockGithubRepositoryListener.data)
    }

    @Test
    fun test_When_RequestUserProfileFail_Should_ReturnSomethingWentWrongErrorMessage() =
            runBlockingTest {
                //  Create a mock call for github client and
                val mockCall: Call<ProfileResponse> = mock()
                `when`(client.getProfile("johndoe")).thenReturn(mockCall)
                doAnswer {
                    //  Setup mock api response
                    (it.getArgument(0) as Callback<ProfileResponse>).onFailure(
                            mockCall,
                            Throwable()
                    )
                }.`when`(mockCall).enqueue(any())

                val mockGithubRepositoryListener = MockGithubRepositoryListener<ProfileResponse>()
                githubRepository.requestUserProfile("johndoe", mockGithubRepositoryListener)

                //  Check the listener
                Assert.assertFalse(mockGithubRepositoryListener.isSuccessCalled)
                Assert.assertEquals(
                        mockGithubRepositoryListener.errorMessage,
                        somethingWentWrongErrorMessage
                )
                Assert.assertNull(mockGithubRepositoryListener.data)
            }

    private inline fun <reified T : Any> mock(): T = mock(T::class.java)!!

}