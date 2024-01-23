package com.example.concert.exception

class MemberNameAlreadyExistsException(message: String) : RuntimeException(message)
class NotFoundException(message: String) : RuntimeException(message)