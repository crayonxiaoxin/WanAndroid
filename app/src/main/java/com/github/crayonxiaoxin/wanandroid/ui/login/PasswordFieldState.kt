package com.github.crayonxiaoxin.wanandroid.ui.login

class PasswordFieldState : TextFieldState(
    validator = ::isPasswordValid,
    errorFor = ::passwordValidationError
)

private fun isPasswordValid(password: String): Boolean {
    return password.length >= 4
}

private fun passwordValidationError(password: String): String {
    return "密码长度不能少于4位"
}