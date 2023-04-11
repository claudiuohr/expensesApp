package com.sd.laborator.services

import com.sd.laborator.interfaces.EncryptionInterface
import com.sd.laborator.interfaces.IAppConfig
import com.sd.laborator.interfaces.UserRepository
import com.sd.laborator.pojo.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.stereotype.Service
import java.sql.DriverManager

@Service
class MariaDBDatabaseInitializer : UserRepository {

    @Autowired
    private lateinit var encryptionService: EncryptionInterface
    @Autowired
    private lateinit var appConfig: IAppConfig
    private lateinit var dataSource: DriverManagerDataSource
    private lateinit var jdbcTemplate: JdbcTemplate

    override fun createDatabase() {

        dataSource= appConfig.dataSource()
        jdbcTemplate = appConfig.jdbcTemplate(dataSource)

        //creeaza baza de date daca nu exista deja
        jdbcTemplate.execute("CREATE DATABASE IF NOT EXISTS cheltuieli;")

        //foloseste baza de date
        jdbcTemplate.execute("USE cheltuieli;")

        //creaza tabelul daca nu exista deja
        jdbcTemplate.execute(
            """
           CREATE TABLE IF NOT EXISTS users(
           firstName VARCHAR(150) NOT NULL,
           lastName VARCHAR(150) NOT NULL,
           username VARCHAR(100) NOT NULL UNIQUE,
           password VARCHAR(150) NOT NULL,
           rent DOUBLE DEFAULT 0.0,
           food DOUBLE DEFAULT 0.0,
           havingFun DOUBLE DEFAULT 0.0,
           school DOUBLE DEFAULT 0.0,
           personal DOUBLE DEFAULT 0.0
           );
        """
        )
    }

    override fun initDatabase() {
        createDatabase()
    }

    override fun getUserByUsername(username: String): User? {
        val connection = DriverManager.getConnection(dataSource.url, dataSource.username, dataSource.password)
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT * FROM users WHERE username = '$username';")
        if (!resultSet.next())
            return null

        return User(
            firstName = encryptionService.decrypt(resultSet.getString("firstName")),
            lastName = encryptionService.decrypt(resultSet.getString("lastName")),
            username = resultSet.getString("username"),
            password = resultSet.getString("password"),
            rent = resultSet.getDouble("rent"),
            food = resultSet.getDouble("food"),
            havingFun = resultSet.getDouble("havingFun"),
            school = resultSet.getDouble("school"),
            personal = resultSet.getDouble("personal")
        )
    }

    override fun getUserIfPassword(username: String,password: String):User?{
        val user: User? = getUserByUsername(username)
        if (user != null) {
            val encryptedPassword = encryptionService.hash(password+username)
            println(user.password)
            println(encryptedPassword)
            if (encryptedPassword == user.password)
                return user
        }
        return null
    }

    override fun addUser(user:User): Boolean {
        val connection = DriverManager.getConnection(dataSource.url,dataSource.username,dataSource.password)
        val encryptedFName = encryptionService.encrypt(user.firstName)
        val encryptedLName = encryptionService.encrypt(user.lastName)
        val encryptedPword = encryptionService.hash(user.password+user.username)
        val statement = connection.prepareStatement("INSERT INTO users (firstName,lastName,username,password) " +
                "VALUES ('${encryptedFName}','${encryptedLName}','${user.username}','${encryptedPword}');")
        val result = statement.executeUpdate()
        if (result==0)
            return false
        return true
    }

    override fun updateUser(username: String, firstName: String, lastName: String): User? {
        val connection = DriverManager.getConnection(dataSource.url,dataSource.username,dataSource.password)
        val statement = connection.createStatement()
        val encryptedFName = encryptionService.encrypt(firstName)
        val encryptedLName = encryptionService.encrypt(lastName)
        val result = statement.executeUpdate("UPDATE users SET firstName='${encryptedFName}',lastName='${encryptedLName}' WHERE username='${username}';")
        if (result==0)
            return null
        return getUserByUsername(username)
    }

    override fun updatePassword(username: String, oldPassword: String, newPassword: String): Boolean {

        val eOldPassword = encryptionService.hash(oldPassword+username)
        val eNewPassword = encryptionService.hash(newPassword+username)

        val connection = DriverManager.getConnection(dataSource.url,dataSource.username,dataSource.password)
        val statement = connection.createStatement()
        val result = statement.executeUpdate("UPDATE users SET password='${eNewPassword}' WHERE username='${username}' AND password='${eOldPassword}';")
        if (result==0)
            return false
        return true
    }

    override fun deleteUser(username: String, password: String): Boolean {
        val connection = DriverManager.getConnection(dataSource.url,dataSource.username,dataSource.password)
        val statement = connection.createStatement()
        val epassword = encryptionService.hash(password+username)
        val result = statement.executeUpdate("DELETE FROM users WHERE username='${username}' AND password='${epassword}'")
        if (result==0)
            return false
        return true
    }

    override fun addExpense(username: String, expenseType: String, sum: Double): Boolean {
        val connection = DriverManager.getConnection(dataSource.url,dataSource.username,dataSource.password)
        val statement = connection.createStatement()
        val result = statement.executeUpdate("UPDATE users SET ${expenseType}=${expenseType}+${sum} WHERE username='${username}'")
        if (result==0)
            return false
        return true
    }
}