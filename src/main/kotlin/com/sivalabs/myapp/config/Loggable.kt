package com.sivalabs.myapp.config

import java.lang.annotation.*
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*

@Target(FUNCTION, CLASS)
@Retention(RUNTIME)
@Inherited
annotation class Loggable
