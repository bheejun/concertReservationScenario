package com.example.concert.exception

class DuplicateException(message: String) : RuntimeException(message)
class NotFoundException(message: String) : RuntimeException(message)
class AuthenticationFailureExceptions(message: String) : RuntimeException(message)

class DoesNotMatchSecretCode (message: String): RuntimeException(message)

