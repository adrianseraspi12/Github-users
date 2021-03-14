package com.example.githubusers.data.local.repository

import com.example.githubusers.data.local.Result
import com.example.githubusers.data.local.dao.ProfileDao
import com.example.githubusers.data.local.dao.UsersDao
import com.example.githubusers.data.local.entity.LocalProfile
import com.example.githubusers.data.local.entity.LocalUser
import com.example.githubusers.data.local.entity.UserWithProfile
import com.example.githubusers.util.constants.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val usersDao: UsersDao,
    private val profileDao: ProfileDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IUserRepository {

    override suspend fun getAllUsers(): Result<List<LocalUser>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.onSuccess(usersDao.getAllUsers())
        } catch (e: Exception) {
            Result.onFailed(getAllUsersErrorMessage)
        }
    }

    override suspend fun getAllUserWithProfile(): Result<List<UserWithProfile>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Result.onSuccess(usersDao.getAllUserWithProfile())
            } catch (e: Exception) {
                Result.onFailed(getAllUserWithProfileErrorMessage)
            }
        }

    override suspend fun getUser(id: Int): Result<LocalUser> = withContext(ioDispatcher) {
        return@withContext try {
            Result.onSuccess(usersDao.findUserById(id))
        } catch (e: Exception) {
            Result.onFailed(getUserErrorMessage)
        }
    }

    override suspend fun insertProfile(profile: LocalProfile): Result<Nothing> =
        withContext(ioDispatcher) {
            return@withContext try {
                profileDao.insertProfile(profile)
                Result.onSuccess()
            } catch (e: Exception) {
                Result.onFailed(insertProfileErrorMessage)
            }
        }

    override suspend fun updateProfile(profile: LocalProfile): Result<Nothing> =
        withContext(ioDispatcher) {
            return@withContext try {
                profileDao.updateProfile(profile)
                Result.onSuccess()
            } catch (e: Exception) {
                Result.onFailed(updateProfileErrorMessage)
            }
        }

    override suspend fun updateUser(user: LocalUser): Result<Nothing> = withContext(ioDispatcher) {
        return@withContext try {
            usersDao.updateUser(user)
            Result.onSuccess()
        } catch (e: Exception) {
            Result.onFailed(updateUserErrorMessage)
        }
    }

    override suspend fun insertUser(user: LocalUser): Result<Nothing> =
        withContext(ioDispatcher) {
            return@withContext try {
                usersDao.insert(user)
                Result.onSuccess()
            } catch (e: Exception) {
                Result.onFailed(saveAllErrorMessage)
            }
        }

    override suspend fun deleteAllUser(): Result<Nothing> = withContext(ioDispatcher) {
        return@withContext try {
            usersDao.deleteAll()
            Result.onSuccess()
        } catch (e: Exception) {
            Result.onFailed(deleteAllUserErrorMessage)
        }
    }
}