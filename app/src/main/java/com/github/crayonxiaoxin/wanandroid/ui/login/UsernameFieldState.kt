package com.github.crayonxiaoxin.wanandroid.ui.login

class UsernameFieldState : TextFieldState(
    validator = ::isUsernameValid,
    errorFor = ::usernameValidationError
)

private fun isUsernameValid(username: String): Boolean {
    return username.isNotEmpty()
}

private fun usernameValidationError(username: String): String {
    return "用户名不能为空"
}
