package com.cloud.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void pwdValidation() {
        //Minimum nine characters, at least one uppercase letter, one lowercase letter and one number,one special,no space.
        UserService userService=new UserService();
        assertFalse(userService.pwdValidation("xasdxxasxasx"));
        assertFalse(userService.pwdValidation("Xasd1"));
        assertFalse(userService.pwdValidation("111111111111111"));
        assertFalse(userService.pwdValidation("Xasdaaasdsadsadsad"));
        assertFalse(userService.pwdValidation("X12345678a"));
        assertFalse(userService.pwdValidation("X12345678a~aasdsadasdsadsadsadasdasddddddddddddddddd"));
        assertFalse(userService.pwdValidation("X12345678a~ "));
        assertTrue(userService.pwdValidation("Qw123456789~"));

    }

    @Test
    void emailVaildation() {
        UserService userService=new UserService();
        assertFalse(userService.emailVaildation("xxx@@x"));
        assertFalse(userService.emailVaildation("xxxx"));
        assertFalse(userService.emailVaildation("@"));
        assertTrue(userService.emailVaildation("asd@qq.com"));
        assertTrue(userService.emailVaildation("che.sd@husky.neu.edu"));
        assertTrue(userService.emailVaildation("Jack@gamil.com"));
    }
}