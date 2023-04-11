package com.sd.laborator.interfaces

import com.sd.laborator.pojo.User

interface UserRepository {

    fun initDatabase()
    fun createDatabase()
    fun getUserByUsername(username: String): User?
    fun addUser(user: User): Boolean
    fun updateUser(username: String, firstName: String, lastName: String): User?
    fun updatePassword(username: String,oldPassword: String, newPassword:String):Boolean
    fun addExpense(username: String,expenseType: String,sum: Double):Boolean
    fun getUserIfPassword(username: String, password: String): User?
    fun deleteUser(username: String, password: String): Boolean
}