package org.dreando.krt.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class WrongParamFormatException(message: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, message)