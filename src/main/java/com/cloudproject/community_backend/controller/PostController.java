package com.cloudproject.community_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.cloudproject.community_backend.entity.Post;
import com.cloudproject.community_backend.entity.User;
import com.cloudproject.community_backend.repository.PostRepository;
import com.cloudproject.community_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "게시물", description = "게시물 관련 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // DTO 정의 (Swagger에서 Request Body 명확히 보여주기)
    public record PostCreateRequest(
            @Schema(description = "게시물 제목", example = "첫 번째 글")
            String title,

            @Schema(description = "게시물 내용", example = "내용을 입력하세요")
            String content,

            @Schema(description = "작성자 ID", example = "1")
            Long authorId
    ) {}

    @Operation(summary = "게시물 작성", description = "새로운 게시물을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시물 작성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public Post createPost(@RequestBody PostCreateRequest req) {
        User author = userRepository.findById(req.authorId())
                .orElseThrow(() -> new RuntimeException("작성자 없음"));

        Post post = new Post();
        post.setTitle(req.title());
        post.setContent(req.content());
        post.setAuthor(author);

        return postRepository.save(post);
    }

    @Operation(summary = "게시물 전체 조회", description = "등록된 모든 게시물을 조회합니다.")
    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
