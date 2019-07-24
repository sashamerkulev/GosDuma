package ru.merkulyevsasha.data.network.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.core.models.UserInfo
import ru.merkulyevsasha.network.models.UserInfoResponse
import java.util.*

class UserInfoMapper(private val authorization: String, private val baseUrl: String) : Mapper<UserInfoResponse, UserInfo> {
    override fun map(item: UserInfoResponse): UserInfo {
        return UserInfo(
            item.name ?: "",
            item.phone ?: "",
            baseUrl + "/users/downloadPhoto?nocache=" +UUID.randomUUID(),
            authorization
        )
    }
}