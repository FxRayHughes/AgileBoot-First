package com.agileboot.domain.system.post.model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.agileboot.common.exception.ApiException;
import com.agileboot.common.exception.error.ErrorCode.Business;
import com.agileboot.orm.system.service.ISysPostService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PostModelTest {

    private final ISysPostService postService = mock(ISysPostService.class);
    private static final long POST_ID = 1L;

    @AfterEach
    public void clean() {
        Mockito.reset(postService);
    }


    @Test
    void testCheckCanBeDeleteWhenFailed() {
        PostModel postModel = new PostModel();
        postModel.setPostId(POST_ID);

        when(postService.isAssignedToUsers(eq(POST_ID))).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> postModel.checkCanBeDelete(postService));
        Assertions.assertEquals(Business.POST_ALREADY_ASSIGNED_TO_USER_CAN_NOT_BE_DELETED, exception.getErrorCode());
    }

    @Test
    void testCheckCanBeDeleteWhenSuccessful() {
        PostModel postModel = new PostModel();
        postModel.setPostId(POST_ID);

        when(postService.isAssignedToUsers(eq(POST_ID))).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> postModel.checkCanBeDelete(postService));
        Assertions.assertEquals(Business.POST_ALREADY_ASSIGNED_TO_USER_CAN_NOT_BE_DELETED, exception.getErrorCode());
    }


    @Test
    void testCheckPostNameUnique() {
        PostModel postWithSameName = new PostModel();
        postWithSameName.setPostId(POST_ID);
        postWithSameName.setPostName("post 1");
        PostModel postWithNewName = new PostModel();
        postWithNewName.setPostName("post 2");
        postWithNewName.setPostId(POST_ID);

        when(postService.isPostNameDuplicated(eq(POST_ID), eq("post 1"))).thenReturn(true);
        when(postService.isPostNameDuplicated(eq(POST_ID), eq("post 2"))).thenReturn(false);

        ApiException exception = assertThrows(ApiException.class,
            () -> postWithSameName.checkPostNameUnique(postService));
        Assertions.assertEquals(Business.POST_NAME_IS_NOT_UNIQUE, exception.getErrorCode());
        Assertions.assertDoesNotThrow(() -> postWithNewName.checkPostNameUnique(postService));
    }

    @Test
    void testCheckPostCodeUnique() {
        PostModel postWithSameCode = new PostModel();
        postWithSameCode.setPostId(POST_ID);
        postWithSameCode.setPostCode("code 1");
        PostModel postWithNewCode = new PostModel();
        postWithNewCode.setPostId(POST_ID);
        postWithNewCode.setPostCode("code 2");

        when(postService.isPostCodeDuplicated(eq(POST_ID), eq("code 1"))).thenReturn(true);
        when(postService.isPostCodeDuplicated(eq(POST_ID), eq("code 2"))).thenReturn(false);

        ApiException exception = assertThrows(ApiException.class,
            () -> postWithSameCode.checkPostCodeUnique(postService));
        Assertions.assertEquals(Business.POST_CODE_IS_NOT_UNIQUE, exception.getErrorCode());
        Assertions.assertDoesNotThrow(() -> postWithNewCode.checkPostCodeUnique(postService));
    }

}
