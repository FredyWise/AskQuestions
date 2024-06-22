package com.fredy.askquestions.features.domain.usecases.UserUseCases

import com.fredy.mysavings.Feature.Domain.UseCases.UserUseCases.GetAllUsersOrderedByName

data class UserUseCases(
    val insertUser: InsertUser,
    val updateUser: UpdateUser,
    val deleteUser: DeleteUser,
    val getUser: GetUser,
    val getCurrentUser: GetCurrentUser,
    val getAllUsersOrderedByName: GetAllUsersOrderedByName,
    val searchUsers: SearchUsers
)


