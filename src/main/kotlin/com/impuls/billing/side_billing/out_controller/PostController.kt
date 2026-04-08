package com.impuls.billing.side_billing.out_controller

import com.impuls.billing.side_billing.model.in_dto.SharedPostResponse
import com.impuls.billing.side_billing.repository.PostService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/post")
class PostController(
    private val postService: PostService
) {
    @GetMapping("")
    fun getClientSharedPosts(
        @RequestHeader("Authorization") authHeader: String,
    ): Mono<List<SharedPostResponse>> {
        return postService.getClientSharedPosts(authHeader)
    }
}