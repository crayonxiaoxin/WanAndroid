package com.github.crayonxiaoxin.wanandroid.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

/**
 * 用户 sharedPreference
 */
val Context.userStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

object User {
    private val Key_isLogin = booleanPreferencesKey("is_login")
    private val Key_accessToken = stringPreferencesKey("access_token")
    private val Key_refreshToken = stringPreferencesKey("refresh_token")
    private val Key_username = stringPreferencesKey("username")
    private val Key_password = stringPreferencesKey("password")
    private val Key_displayName = stringPreferencesKey("display_name")

    lateinit var store: DataStore<Preferences>
    fun provide(context: Context) {
        this.store = context.userStore
    }

    // .first() 发出第一个元素, preference
    suspend fun origin() = store.data.first()

    suspend fun isLogin() = origin()[Key_isLogin] ?: false
    suspend fun isLogin(value: Boolean) = store.edit {
        it[Key_isLogin] = value
    }

    suspend fun accessToken() = origin()[Key_accessToken] ?: ""
    suspend fun accessToken(value: String) = store.edit {
        it[Key_accessToken] = value
        isLogin(value.isNotEmpty())
    }

    suspend fun refreshToken() = origin()[Key_refreshToken] ?: ""
    suspend fun refreshToken(value: String) = store.edit {
        it[Key_refreshToken] = value
    }

    suspend fun username() = origin()[Key_username] ?: ""
    suspend fun username(value: String) = store.edit {
        it[Key_username] = value
    }

    suspend fun displayName() = origin()[Key_displayName] ?: ""
    suspend fun displayName(value: String) = store.edit {
        it[Key_displayName] = value
    }

    suspend fun password() = origin()[Key_displayName] ?: ""
    suspend fun password(value: String) = store.edit {
        it[Key_password] = value
    }
}
