package me.jincrates.reservation.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import springfox.documentation.annotations.ApiIgnore

@ApiIgnore
@Controller
class RootController {

    @RequestMapping("/")
    fun homeRedirect(): String {
        return "redirect:/swagger-ui/"
    }
}