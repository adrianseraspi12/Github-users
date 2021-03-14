package com.example.githubusers.data.remote.repository

import com.example.githubusers.data.local.Result
import com.example.githubusers.data.remote.model.ProfileResponse
import com.example.githubusers.data.remote.model.UserResponse
import com.example.githubusers.data.remote.service.GithubClient
import com.example.githubusers.util.constants.somethingWentWrongErrorMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Call
import retrofit2.Response

@Suppress("BlockingMethodInNonBlockingContext")
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
        //  Setup mock api response
        val mockCall: Call<List<UserResponse>> = mock()
        `when`(client.getUserList(0)).thenReturn(mockCall)
        doReturn(Response.success(listOfUserResponse)).`when`(mockCall).execute()

        //  Call requestUserProfile
        val result = githubRepository.requestUserList(0)

        //  Check the listener
        Assert.assertTrue(result is Result.onSuccess)
        Assert.assertEquals((result as Result.onSuccess).data, listOfUserResponse)
    }

    @Test
    fun test_When_RequestUserListFail_Should_ReturnErrorMessage() =
            runBlockingTest {
                //  Create a mock call for github client and
                //  Setup mock api response
                val mockCall: Call<List<UserResponse>> = mock()
                `when`(client.getUserList(0)).thenReturn(mockCall)
                doThrow(RuntimeException()).`when`(mockCall).execute()

                //  Call requestUserProfile
                val result = githubRepository.requestUserList(0)

                //  Check the listener
                Assert.assertTrue(result is Result.onFailed)
                Assert.assertEquals(
                        (result as Result.onFailed).errorMessage,
                        somethingWentWrongErrorMessage
                )
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
        doReturn(Response.success(profileResponse)).`when`(mockCall).execute()

        //  Call requestUserProfile
        val result = githubRepository.requestUserProfile("johndoe")

        //  Assert result
        Assert.assertTrue(result is Result.onSuccess)
        Assert.assertEquals((result as Result.onSuccess).data, profileResponse)
    }

    @Test
    fun test_When_RequestUserProfileFail_Should_ReturnErrorMessage() =
            runBlockingTest {
                //  Create a mock call for github client and
                //  Setup mock api response
                val mockCall: Call<ProfileResponse> = mock()
                `when`(client.getProfile("johndoe")).thenReturn(mockCall)
                doThrow(RuntimeException()).`when`(mockCall).execute()

                //  Call requestUserProfile
                val result = githubRepository.requestUserProfile("johndoe")

                Assert.assertTrue(result is Result.onFailed)
                Assert.assertEquals(
                        (result as Result.onFailed).errorMessage,
                        somethingWentWrongErrorMessage
                )
            }

    private inline fun <reified T : Any> mock(): T = mock(T::class.java)!!
}