package com.sivalabs.bookmarker.config

import org.springframework.stereotype.Component

import java.util.Date

@Component
class TimeProvider {

    fun now(): Date {
        return Date()
    }
}
