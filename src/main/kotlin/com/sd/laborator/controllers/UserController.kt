package com.sd.laborator.controllers

import com.sd.laborator.interfaces.UserRepository
import com.sd.laborator.pojo.Expense
import com.sd.laborator.pojo.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UserController {

    @Autowired
    lateinit var database : UserRepository

    @GetMapping("/users/{username}")
    fun getUser(@PathVariable username: String,@RequestParam password: String): ResponseEntity<User> {
        database.initDatabase()
        val user: User? =database.getUserIfPassword(username,password)
        return if (user != null) {
            ResponseEntity.ok(user)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @PostMapping("/users/addUser")
    fun addUser(@RequestBody user: User): ResponseEntity<User>{
        database.initDatabase()
        val existingUser = database.getUserByUsername(user.username)
        if(existingUser != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
        database.addUser(user)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/users/addExpense")
    fun addExpense(@RequestBody expense: Expense):ResponseEntity<User>{
        database.initDatabase()
        var user = database.getUserByUsername(expense.username) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        database.addExpense(expense.username,expense.category,expense.amount)
        user= database.getUserByUsername(user.username)!!
        return ResponseEntity.status(HttpStatus.OK).body(user)
    }

    @PatchMapping("/users/updateCredentials/{username}")
    fun updateUser(@PathVariable username: String,@RequestParam fname:String,@RequestParam lname:String):ResponseEntity<User>{
        val user = database.updateUser(username,fname,lname) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        return ResponseEntity.status(HttpStatus.OK).body(user)
    }

    @PatchMapping("/users/updatePassword/{username}")
    fun updatePassword(@PathVariable username: String,@RequestParam oldp:String,@RequestParam newp:String):ResponseEntity<User>{
        val changed = database.updatePassword(username,oldp,newp)
        if (changed)
            return ResponseEntity.status(HttpStatus.OK).body(database.getUserByUsername(username))
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    @DeleteMapping("/users/deleteUser/{username}")
    fun deleteUser(@PathVariable username:String,@RequestParam password : String):ResponseEntity<User>{
        if (database.deleteUser(username,password))
            return ResponseEntity.status(HttpStatus.OK).build()
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(database.getUserByUsername(username))
    }
}