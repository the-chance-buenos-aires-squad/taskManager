package org.buinos.presentation

data class User (
    val userId : String,
    val username : String,
    val passwordHash : String, // md5
    val role: UserRole,

){
}


enum class UserRole { ADMIN, MATE }