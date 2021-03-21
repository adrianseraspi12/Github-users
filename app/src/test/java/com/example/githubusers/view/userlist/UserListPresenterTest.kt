package com.example.githubusers.view.userlist

import com.example.githubusers.data.local.entity.LocalProfile
import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.data.main.repository.IMainRepository
import com.example.githubusers.data.remote.Listener
import com.example.githubusers.util.constants.requestUserListErrorMessage
import com.example.githubusers.view.BasePresenterTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class UserListPresenterTest : BasePresenterTest() {

    private lateinit var view: UserListContract.View
    private lateinit var mainRepository: IMainRepository
    private lateinit var presenter: UserListPresenter
    private lateinit var mainRepositoryListener: ArgumentCaptor<Listener<List<UserWithProfile>>>

    private val user = LocalUser(
            0, "john",
            "https://www.jd.com/png",
            "",
    )
    private val profile = LocalProfile(
            0, 0,
            "John Doe",
            "",
            12,
            23,
            "Apple",
            ""
    )
    private val listUserWithProfile = listOf(UserWithProfile(user, profile))

    @Before
    fun setup() {
        mainRepositoryListener = argumentCaptor()
        view = mock(UserListContract.View::class.java)
        mainRepository = mock(IMainRepository::class.java)
        presenter = UserListPresenter(view, mainRepository)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @Test
    fun test_When_PresenterInitialized_Should_CallSetUpPresenter() {
        verify(view).setupPresenter(presenter)
    }

    @Test
    fun test_When_DataFromRepositoryHasValues_Should_CallViewSetUserList() = runBlockingTest {
        presenter.requestUserList()

        verify(mainRepository).loadUserList(capture(mainRepositoryListener))

        mainRepositoryListener.value.onSuccess(listUserWithProfile)
        val showListOfUserWithProfile = argumentCaptor<List<UserWithProfile>>()

        verify(view).showLoading()
        verify(view).hideScreenMessage()
        verify(view).stopLoading()
        verify(view).setUserList(capture(showListOfUserWithProfile))

        Assert.assertEquals(showListOfUserWithProfile.value.size, listUserWithProfile.size)
        Assert.assertEquals(showListOfUserWithProfile.value[0], listUserWithProfile[0])
    }

    @Test
    fun test_When_LoadUserListIsFailOnFirstLoad_Should_CallShowScreenMessage() = runBlockingTest {
        presenter.requestUserList()

        verify(view).showLoading()
        verify(mainRepository).loadUserList(capture(mainRepositoryListener))

        mainRepositoryListener.value.onFailed(requestUserListErrorMessage)
        verify(view).stopLoading()
        verify(view).showScreenMessage(requestUserListErrorMessage)
    }

    @Test
    fun test_When_LoadMoreIsSuccessful_Should_ShowNewList() = runBlockingTest {
        val newUser = LocalUser(
                2, "jane",
                "https://www.jane.com/png",
                "",
        )
        val newProfile = LocalProfile(
                2, 2,
                "Jane Doe",
                "Hello I'm Jane",
                10,
                50,
                "Orange",
                ""
        )
        val newListUserWithProfile = listOf(UserWithProfile(newUser, newProfile))

        //  Call setup
        presenter.requestUserList()
        verify(mainRepository).loadUserList(capture(mainRepositoryListener))

        //  Set a value on listener
        mainRepositoryListener.value.onSuccess(listUserWithProfile)
        val showListOfUserWithProfile = argumentCaptor<List<UserWithProfile>>()

        verify(view).setUserList(capture(showListOfUserWithProfile))

        presenter.onScroll(10, 20, 10)

        val showNewListOfUserWithProfile: ArgumentCaptor<Listener<List<UserWithProfile>>> = argumentCaptor()
        verify(mainRepository).loadUserList(eq(listUserWithProfile[0].user?.id!!), capture(showNewListOfUserWithProfile))
        showNewListOfUserWithProfile.value.onSuccess(newListUserWithProfile)

        verify(view).showLoadMoreLoading()
        verify(view).hideLoadMoreLoading()
        verify(view).addNewList(newListUserWithProfile)
    }

    @Test
    fun test_When_LoadMoreIsFail_Should_ShowErrorMessage() = runBlockingTest {
        //  Call setup
        presenter.requestUserList()
        verify(mainRepository).loadUserList(capture(mainRepositoryListener))

        //  Set a value on listener
        mainRepositoryListener.value.onSuccess(listUserWithProfile)
        val showListOfUserWithProfile = argumentCaptor<List<UserWithProfile>>()

        verify(view).setUserList(capture(showListOfUserWithProfile))

        presenter.onScroll(10, 20, 10)

        val showNewListOfUserWithProfile: ArgumentCaptor<Listener<List<UserWithProfile>>> = argumentCaptor()
        verify(mainRepository).loadUserList(eq(listUserWithProfile[0].user?.id!!), capture(showNewListOfUserWithProfile))
        showNewListOfUserWithProfile.value.onFailed(requestUserListErrorMessage)

        verify(view).showLoadMoreLoading()
        verify(view).hideLoadMoreLoading()
        verify(view).showToastMessage(requestUserListErrorMessage)
    }

    @Test
    fun test_When_LoadMoreIsSuccessfulWithEmptyValue_Should_ShowErrorMessage() = runBlockingTest {
        //  Call setup
        presenter.requestUserList()
        verify(mainRepository).loadUserList(capture(mainRepositoryListener))

        //  Set a value on listener
        mainRepositoryListener.value.onSuccess(listUserWithProfile)
        val showListOfUserWithProfile = argumentCaptor<List<UserWithProfile>>()

        verify(view).setUserList(capture(showListOfUserWithProfile))

        presenter.onScroll(10, 20, 10)

        val showNewListOfUserWithProfile: ArgumentCaptor<Listener<List<UserWithProfile>>> = argumentCaptor()
        verify(mainRepository).loadUserList(eq(listUserWithProfile[0].user?.id!!), capture(showNewListOfUserWithProfile))
        showNewListOfUserWithProfile.value.onSuccess(null)

        verify(view).showLoadMoreLoading()
        verify(view).hideLoadMoreLoading()
        verify(view).showToastMessage(requestUserListErrorMessage)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}