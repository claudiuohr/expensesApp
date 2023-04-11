package com.sd.laborator.pojo

// Define a data class to hold the query results
data class User(
    var firstName: String,
    var lastName: String,
    var username: String,
    var password: String,
    var rent: Double,
    var food: Double,
    var havingFun: Double,
    var school: Double,
    var personal: Double
)

