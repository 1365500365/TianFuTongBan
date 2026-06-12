package com.example.tianfu.network.model

import kotlinx.serialization.Serializable

// ── 基础网络响应模型 ──
// 此文件包含通用的 API 响应封装，可直接复用。
// 业务实体模型请根据你的项目需求自行添加。

/**
 * 统一的 API 响应封装模型
 *
 * 所有后端接口应返回此统一格式，便于前端统一处理成功/失败逻辑。
 *
 * @param T 具体的业务数据类型
 * @property code 服务端返回的状态码（例如：200 表示成功）
 * @property msg 服务端返回的提示信息
 * @property data 实际的响应数据，可能为空
 * @property count 数据总条数（可选，常用于未分页的列表总数）
 *
 * 使用示例：
 * ```kotlin
 * val response: ApiResponse<User> = apiService.getUserProfile()
 * if (response.code == 200 && response.data != null) {
 *     // 处理成功
 * }
 * ```
 */
@Serializable
data class ApiResponse<T>(
    val code: Int,
    val msg: String,
    val data: T? = null,
    val count: Int? = null
)

/**
 * 分页数据封装模型
 *
 * 当后端接口支持分页查询时，data 字段应使用此模型包装列表数据。
 *
 * @param T 列表项的数据类型
 * @property items 当前页的数据列表
 * @property total 数据总条数
 * @property page 当前页码
 * @property pageSize 每页的数据量大小
 * @property totalPages 总页数
 *
 * 使用示例：
 * ```kotlin
 * val response: ApiResponse<PagedData<Item>> = apiService.getItems(page = 1, pageSize = 20)
 * val items = response.data?.items ?: emptyList()
 * ```
 */
@Serializable
data class PagedData<T>(
    val items: List<T> = emptyList(),
    val total: Int = 0,
    val page: Int = 0,
    val pageSize: Int = 0,
    val totalPages: Int = 0
)

// ── 认证相关模型 ── 根据项目需求修改字段

/**
 * 用户登录请求模型
 * @property account 用户登录账号
 * @property password 用户登录密码
 * @property appId 当前客户端所属的应用 ID
 */
@Serializable
data class UserLoginRequest(val account: String, val password: String, val appId: Int)

/**
 * 登录成功后的响应模型
 * @param T 用户信息的具体类型
 * @property accessToken 用于接口鉴权的短效 Token
 * @property refreshToken 用于刷新 Access Token 的长效 Token
 * @property userType 用户类型
 * @property userInfo 登录用户的基本信息
 */
@Serializable
data class LoginResponse<T>(
    val accessToken: String,
    val refreshToken: String,
    val userType: String,
    val userInfo: T? = null
)

/**
 * 刷新 Token 请求模型
 * @property refreshToken 登录时获取的 Refresh Token
 */
@Serializable
data class RefreshTokenRequest(val refreshToken: String)

/**
 * 刷新 Token 成功后的响应模型
 * @property accessToken 新生成的 Access Token
 */
@Serializable
data class RefreshTokenResponse(val accessToken: String)

// ── 业务实体模型示例 ── 请替换为你自己项目的数据模型

/**
 * 示例：用户实体模型
 *
 * TODO: 根据你的后端 API 返回结构修改此类的字段
 */
@Serializable
data class User(
    val id: Int = 0,
    val avatar: String = "",
    val name: String = "",
    val account: String = "",
)

/**
 * 示例：列表项实体模型
 *
 * TODO: 替换为你项目中的实际业务实体（如商品、文章、订单等）
 */
@Serializable
data class ExampleItem(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val createdAt: String = "",
)

@Serializable
data class UserDto(
    val realName: String,
    val idType: String,
    val idNumber: String,
    val phone: String,
    val password: String? = null,
)

@Serializable
data class LoginResponseDto(
    val token: String,
    val user: UserDto,
)

@Serializable
data class PropertyInfoDto(
    val district: String,
    val street: String,
    val doorNo: String,
    val buildingNo: Int? = null,
    val unitNo: Int? = null,
    val floor: String,
    val roomNo: Int? = null,
    val plannedUse: String,
    val areaInCasing: Double? = null,
    val sharedArea: Double? = null,
)
