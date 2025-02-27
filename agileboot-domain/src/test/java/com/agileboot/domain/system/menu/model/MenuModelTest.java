package com.agileboot.domain.system.menu.model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.agileboot.common.exception.ApiException;
import com.agileboot.common.exception.error.ErrorCode.Business;
import com.agileboot.orm.system.service.ISysMenuService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

class MenuModelTest {

    private static final long MENU_ID = 1L;

    private final ISysMenuService menuService = mock(ISysMenuService.class);

    @Test
    void testCheckMenuNameUnique() {
        MenuModel menuModel = new MenuModel();
        menuModel.setMenuName("menu 1");
        when(
            menuService.isMenuNameDuplicated(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any())).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> menuModel.checkMenuNameUnique(menuService));
        Assertions.assertEquals(Business.MENU_NAME_IS_NOT_UNIQUE, exception.getErrorCode());
    }

    @Test
    void testCheckExternalLinkWhenSuccessful() {
        MenuModel notExternalButWithoutHttpPrefix = new MenuModel();
        notExternalButWithoutHttpPrefix.setIsExternal(false);
        notExternalButWithoutHttpPrefix.setPath("www.baidu.com");

        MenuModel isExternalWithHttpPrefix = new MenuModel();
        isExternalWithHttpPrefix.setIsExternal(true);
        isExternalWithHttpPrefix.setPath("http://www.baidu.com");

        Assertions.assertDoesNotThrow(()->{
            notExternalButWithoutHttpPrefix.checkExternalLink();
            isExternalWithHttpPrefix.checkExternalLink();
        });
    }

    @Test
    void testCheckExternalLinkWhenFailed() {
        MenuModel externalWithoutHttpPrefix = new MenuModel();
        externalWithoutHttpPrefix.setIsExternal(true);
        externalWithoutHttpPrefix.setPath("www.baidu.com");

        ApiException exception = assertThrows(ApiException.class, externalWithoutHttpPrefix::checkExternalLink);

        Assertions.assertEquals(Business.MENU_EXTERNAL_LINK_MUST_BE_HTTP, exception.getErrorCode());
    }



    @Test
    void testCheckParentIdConflict() {
        MenuModel menuModel = new MenuModel();
        menuModel.setMenuId(MENU_ID);
        menuModel.setParentId(MENU_ID);

        ApiException exception = assertThrows(ApiException.class, menuModel::checkParentIdConflict);

        Assertions.assertEquals(Business.MENU_PARENT_ID_NOT_ALLOW_SELF, exception.getErrorCode());
    }

    @Test
    void testCheckHasChildMenus() {
        MenuModel menuModel = new MenuModel();
        menuModel.setMenuId(MENU_ID);
        when(menuService.hasChildrenMenu(eq(MENU_ID))).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> menuModel.checkHasChildMenus(menuService));

        Assertions.assertEquals(Business.MENU_EXIST_CHILD_MENU_NOT_ALLOW_DELETE, exception.getErrorCode());

    }

    @Test
    void testCheckMenuAlreadyAssignToRole() {
        MenuModel menuModel = new MenuModel();
        menuModel.setMenuId(MENU_ID);
        when(menuService.isMenuAssignToRoles(eq(MENU_ID))).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class,
            () -> menuModel.checkMenuAlreadyAssignToRole(menuService));

        Assertions.assertEquals(Business.MENU_ALREADY_ASSIGN_TO_ROLE_NOT_ALLOW_DELETE, exception.getErrorCode());
    }
}
