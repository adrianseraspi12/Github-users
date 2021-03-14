package com.example.githubusers.view.userlist

import com.example.githubusers.data.local.entity.LocalProfile
import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.data.main.repository.IMainRepository
import com.example.githubusers.data.remote.Listener
import com.example.githubusers.util.constants.successfulSaveNote
import com.example.githubusers.util.constants.updateProfileErrorMessage
import com.example.githubusers.view.BasePresenterTest
import com.example.githubusers.view.profile.ProfileContract
import com.example.githubusers.view.profile.ProfilePresenter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class ProfilePresenterTest : BasePresenterTest() {

    private lateinit var view: ProfileContract.View
    private lateinit var mainRepository: IMainRepository
    private lateinit var presenter: ProfilePresenter
    private lateinit var userWithProfile: UserWithProfile
    private lateinit var saveNoteListener: ArgumentCaptor<Listener<Nothing>>

    @Before
    fun setup() {
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

        view = mock(ProfileContract.View::class.java)
        mainRepository = mock(IMainRepository::class.java)
        userWithProfile = UserWithProfile(user, profile)
        saveNoteListener = argumentCaptor()
        presenter = ProfilePresenter(view, userWithProfile, mainRepository)
    }

    @Test
    fun test_When_PresenterInitialized_Should_CallSetupPresenter() {
        verify(view).setPresenter(presenter)
    }

    @Test
    fun test_When_SetupIsCalled_Should_ShowUserWithProfileDetails() {
        val user = userWithProfile.user
        val profile = userWithProfile.profile
        val followersCount = (profile?.followersCount ?: 0).toString()
        val followingCount = (profile?.followingCount ?: 0).toString()

        presenter.setup()

        verify(view).showProfileDetails(
                profile?.name ?: "",
                user?.image ?: "",
                followersCount,
                followingCount,
                profile?.company ?: "",
                profile?.blog ?: "",
                user?.notes ?: ""
        )
    }

    @Test
    fun test_When_SaveNoteIsSuccess_Should_CallUpdateUserWithProfileFromLastScreen() = runBlockingTest {
        val testNotes = "Hello. This is a note."

        presenter.saveNote(testNotes)
        userWithProfile.user?.notes = testNotes
        val user = userWithProfile.user ?: LocalUser()

        verify(mainRepository).updateUserOnLocal(safeEq(user), capture(saveNoteListener))
        saveNoteListener.value.onSuccess()

        val userWithProfileCaptor = argumentCaptor<UserWithProfile>()

        verify(view).showToastMessage(successfulSaveNote)
        verify(view).updateUserWithProfileFromLastScreen(capture(userWithProfileCaptor))

        Assert.assertEquals(userWithProfileCaptor.value.user, userWithProfile.user)
    }

    @Test
    fun test_When_SaveNoteIsFail_Should_CallToastMessage() = runBlockingTest {
        val testNotes = "Hello. This is a note."

        presenter.saveNote(testNotes)
        userWithProfile.user?.notes = testNotes
        val user = userWithProfile.user ?: LocalUser()

        verify(mainRepository).updateUserOnLocal(safeEq(user), capture(saveNoteListener))
        saveNoteListener.value.onFailed(updateProfileErrorMessage)

        verify(view).showToastMessage(updateProfileErrorMessage)
    }
}