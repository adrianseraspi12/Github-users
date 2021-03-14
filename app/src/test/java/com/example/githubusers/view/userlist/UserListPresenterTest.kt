package com.example.githubusers.view.userlist

import com.example.githubusers.data.local.entity.LocalProfile
import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.data.main.repository.IMainRepository
import com.example.githubusers.data.remote.Listener
import com.example.githubusers.util.constants.requestUserListErrorMessage
import com.example.githubusers.view.BasePresenterTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class UserListPresenterTest: BasePresenterTest() {

    private lateinit var view: UserListContract.View
    private lateinit var mainRepository: IMainRepository
    private lateinit var presenter: UserListPresenter
    private lateinit var mainRepositoryListener: ArgumentCaptor<Listener<List<UserWithProfile>>>

    @Before
    fun setup() {
        mainRepositoryListener = argumentCaptor()
        view = mock(UserListContract.View::class.java)
        mainRepository = mock(IMainRepository::class.java)
        presenter = UserListPresenter(view, mainRepository)
    }

    @Test
    fun test_When_PresenterInitialized_Should_CallSetUpPresenter() {
        verify(view).setupPresenter(presenter)
    }

    @Test
    fun test_When_DataFromRepositoryHasValues_Should_CallViewSetUserList() = runBlockingTest {
        val user = LocalUser(
                0, "john",
                "https://www.jd.com/png",
                "",
        )
        val profile = LocalProfile(
                0, 0,
                "John Doe",
                "",
                12,
                23,
                "Apple",
                ""
        )

        val listUserWithProfile = listOf(UserWithProfile(user, profile))

        presenter.setup()

        verify(view).showLoading()
        verify(mainRepository).loadUserList(capture(mainRepositoryListener))

        mainRepositoryListener.value.onSuccess(listUserWithProfile)
        val showListOfUserWithProfile = argumentCaptor<List<UserWithProfile>>()

        verify(view).stopLoading()
        verify(view).setUserList(capture(showListOfUserWithProfile))

        Assert.assertEquals(showListOfUserWithProfile.value.size, listUserWithProfile.size)
        Assert.assertEquals(showListOfUserWithProfile.value[0], listUserWithProfile[0])
    }

    @Test
    fun test_When_DataFromRepositoryIsFail_Should_CallViewFail() = runBlockingTest {
        presenter.setup()

        verify(view).showLoading()
        verify(mainRepository).loadUserList(capture(mainRepositoryListener))

        mainRepositoryListener.value.onFailed(requestUserListErrorMessage)
        verify(view).stopLoading()
    }

}