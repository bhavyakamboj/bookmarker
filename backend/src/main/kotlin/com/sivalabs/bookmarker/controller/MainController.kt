package com.sivalabs.bookmarker.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController {
    @GetMapping(value = ["/", "/login"])
    fun entry(): String {
        return "index.html"
    }
}
