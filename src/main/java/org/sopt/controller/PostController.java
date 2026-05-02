package org.sopt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.ApiResponse;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post", description = "게시글 관련 API")
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // POST /posts
    @Operation(
            summary = "게시글 작성",
            description = "작성자 userId, 제목, 내용을 받아 게시글을 작성합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "게시글 작성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "message": "게시글 등록 완료!",
                                              "data": {
                                                "id": 1
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 사용자",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "존재하지 않는 사용자입니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "게시글 작성 요청 body",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreatePostRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "userId": 1,
                                              "title": "첫 번째 게시글",
                                              "content": "게시글 내용입니다."
                                            }
                                            """
                            )
                    )
            )
            @RequestBody CreatePostRequest request
    ) {
        CreatePostResponse response = postService.createPost(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("게시글 등록 완료!", response));
    }

    // GET /posts?page=0&size=10
    @Operation(
            summary = "게시글 목록 조회",
            description = "게시글 목록을 페이지네이션으로 조회합니다. page는 0부터 시작합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "게시글 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "message": "게시글 목록 조회 성공!",
                                              "data": [
                                                {
                                                  "id": 1,
                                                  "title": "첫 번째 게시글",
                                                  "content": "게시글 내용입니다.",
                                                  "author": "가윤",
                                                  "createdAt": "2026-05-01T16:57:20.105"
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts(
            @Parameter(description = "페이지 번호, 0부터 시작", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "한 페이지에 조회할 게시글 개수", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        List<PostResponse> responses = postService.getAllPosts(page, size);

        return ResponseEntity.ok(
                ApiResponse.success("게시글 목록 조회 성공!", responses)
        );
    }

    // GET /posts/{id}
    @Operation(
            summary = "게시글 단건 조회",
            description = "게시글 id를 이용해 특정 게시글을 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "게시글 단건 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "message": "게시글 단건 조회 성공!",
                                              "data": {
                                                "id": 1,
                                                "title": "첫 번째 게시글",
                                                "content": "게시글 내용입니다.",
                                                "author": "가윤",
                                                "createdAt": "2026-05-01T16:57:20.105"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 게시글",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "게시글을 찾을 수 없습니다. id: 1",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(
            @Parameter(description = "조회할 게시글 id", example = "1", required = true)
            @PathVariable Long id
    ) {
        PostResponse response = postService.getPost(id);

        return ResponseEntity.ok(
                ApiResponse.success("게시글 단건 조회 성공!", response)
        );
    }

    // PUT /posts/{id}
    @Operation(
            summary = "게시글 수정",
            description = "게시글 id를 이용해 특정 게시글의 제목과 내용을 수정합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "게시글 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "message": "게시글 수정 완료!",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 게시글",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "존재하지 않는 게시글입니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updatePost(
            @Parameter(description = "수정할 게시글 id", example = "1", required = true)
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "게시글 수정 요청 body",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdatePostRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "title": "수정된 제목",
                                              "content": "수정된 게시글 내용입니다."
                                            }
                                            """
                            )
                    )
            )
            @RequestBody UpdatePostRequest request
    ) {
        postService.updatePost(id, request);

        return ResponseEntity.ok(
                ApiResponse.success("게시글 수정 완료!", null)
        );
    }

    // DELETE /posts/{id}
    @Operation(
            summary = "게시글 삭제",
            description = "게시글 id를 이용해 특정 게시글을 삭제합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "게시글 삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "message": "게시글 삭제 완료!",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 게시글",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "message": "게시글을 찾을 수 없습니다. id: 1",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @Parameter(description = "삭제할 게시글 id", example = "1", required = true)
            @PathVariable Long id
    ) {
        postService.deletePost(id);

        return ResponseEntity.ok(
                ApiResponse.success("게시글 삭제 완료!", null)
        );
    }
}