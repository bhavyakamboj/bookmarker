package com.sivalabs.bookmarker.domain.annotation

import java.lang.annotation.Inherited
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION

@Target(FUNCTION, CLASS)
@Retention(RUNTIME)
@Inherited
annotation class Loggable
