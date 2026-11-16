package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.in_dto.SharedPostResponse
import com.impuls.billing.side_billing.repository.PostService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/post")
class PostController(
    private val postService: PostService
) {
    @GetMapping("")
    suspend fun getClientSharedPosts(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam(name = "date_from", required = false) dateFrom: String?,
    ): List<SharedPostResponse> {
        return postService.getClientSharedPosts(
            authHeader = authHeader,
            dateFrom = dateFrom
        )
    }
}