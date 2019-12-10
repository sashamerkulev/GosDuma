package ru.merkulyevsasha.preferences

import android.content.Context
import ru.merkulyevsasha.core.models.ThemeEnum
import ru.merkulyevsasha.gdcore.preferences.SettingsSharedPreferences
import java.util.*

class SettingsSharedPreferencesImpl(context: Context) : SettingsSharedPreferences {

    companion object {
        private val KEY_TOKEN = "KEY_TOKEN"
        private val KEY_SETUP_ID = "KEY_SETUP_ID"
        private val KEY_LAST_ARTICLE_READ = "KEY_LAST_ARTICLE_READ"
        private val KEY_LAST_ARTICLE_COMMENT_READ = "KEY_LAST_ARTICLE_COMMENT_READ"
        private val KEY_USER_NAME = "KEY_USER_NAME"
        private val KEY_USER_PHONE = "KEY_USER_PHONE"
        private val KEY_USER_AVATAR_FILE_NAME = "KEY_USER_AVATAR_FILE_NAME"
        private val KEY_APPLICATION_RUN_NUMBER = "KEY_APPLICATION_RUN_NUMBER"
        private val KEY_APPLICATION_RATED_FLAG = "KEY_APPLICATION_RATED_FLAG"
        private val KEY_LAST_APPLICATION_RUN_DATE = "KEY_LAST_APPLICATION_RUN_DATE"
        private val KEY_USER_PROFILE_THEME = "KEY_USER_PROFILE_THEME"
    }

    private val prefs: android.content.SharedPreferences =
        context.getSharedPreferences("keyvalue", Context.MODE_PRIVATE)

    override fun getAccessToken(): String {
        return prefs.getString(KEY_TOKEN, "") ?: ""
    }

    override fun setAccessToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    override fun getSetupId(): String {
        return prefs.getString(KEY_SETUP_ID, "") ?: ""
    }

    override fun setSetupId(setupId: String) {
        prefs.edit().putString(KEY_SETUP_ID, setupId).apply()
    }

    override fun getLastArticleReadDate(): Date? {
        return Date(prefs.getLong(KEY_LAST_ARTICLE_READ, 0))
    }

    override fun setLastArticleReadDate(lastDate: Date) {
        prefs.edit().putLong(KEY_LAST_ARTICLE_READ, lastDate.time).apply()
    }

    override fun getLastArticleCommentReadDate(): Date? {
        return Date(prefs.getLong(KEY_LAST_ARTICLE_COMMENT_READ, 0))
    }

    override fun setLastArticleCommentReadDate(lastDate: Date) {
        prefs.edit().putLong(KEY_LAST_ARTICLE_COMMENT_READ, lastDate.time).apply()
    }

    override fun saveNameAndPhone(name: String, phone: String) {
        prefs
            .edit()
            .putString(KEY_USER_NAME, name)
            .putString(KEY_USER_PHONE, phone)
            .apply()
    }

    override fun saveProfileFileName(profileFileName: String) {
        prefs
            .edit()
            .putString(KEY_USER_AVATAR_FILE_NAME, profileFileName)
            .apply()
    }

    override fun getUserName(): String {
        return prefs.getString(KEY_USER_NAME, "") ?: ""
    }

    override fun getUserPhone(): String {
        return prefs.getString(KEY_USER_PHONE, "") ?: ""
    }

    override fun getUserAvatarFileName(): String {
        return prefs.getString(KEY_USER_AVATAR_FILE_NAME, "") ?: ""
    }

    override fun isApplicationAlreadyRatedFlag(): Boolean {
        return prefs.getBoolean(KEY_APPLICATION_RATED_FLAG, false)
    }

    override fun setApplicationRatedFlag() {
        prefs.edit()
            .putBoolean(KEY_APPLICATION_RATED_FLAG, true)
            .apply()
    }

    override fun getApplicationRunNumber(): Int {
        return prefs.getInt(KEY_APPLICATION_RUN_NUMBER, 0)
    }

    override fun updateApplicationRunNumber() {
        val count = getApplicationRunNumber()
        prefs.edit()
            .putInt(KEY_APPLICATION_RUN_NUMBER, count + 1)
            .apply()
    }

    override fun getLastApplicationRunDate(): Long {
        return prefs.getLong(KEY_LAST_APPLICATION_RUN_DATE, 0)
    }

    override fun updateLastApplicationRunDate() {
        val calendar = Calendar.getInstance()
        prefs.edit()
            .putLong(KEY_LAST_APPLICATION_RUN_DATE, calendar.timeInMillis)
            .apply()
    }

    override fun getUserProfileTheme(): ThemeEnum {
        return ThemeEnum.valueOf(prefs.getString(KEY_USER_PROFILE_THEME, ThemeEnum.Classic.name)
            ?: ThemeEnum.Classic.name)
    }

    override fun setUserProfileTheme(theme: ThemeEnum) {
        prefs.edit().putString(KEY_USER_PROFILE_THEME, theme.name).apply()
    }

}
