package com.and1ss.onlinechat.api.rest;

import com.and1ss.onlinechat.services.dto.AccountInfoRetrievalDTO;
import com.and1ss.onlinechat.services.UserService;
import com.and1ss.onlinechat.services.mappers.AccountInfoMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-service")
public class UsersController {
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/account")
    private AccountInfoRetrievalDTO getAccountInfo(@RequestHeader("Authorization") String token) {
        return AccountInfoMapper.toAccountInfoRetrievalDTO(userService.authorizeUserByBearerToken(token));
    }

    @GetMapping("/users")
    private List<AccountInfoRetrievalDTO> getUsers(
            @RequestHeader("Authorization") String token,
            @RequestParam(
                    value = "login_like",
                    required = false,
                    defaultValue = ""
            ) String loginLike
    ) {
        if (loginLike.isEmpty()) {
            return userService.findUsersWhoAreNotCurrentUserFriends(token);
        } else {
            return userService
                    .findUsersWhoAreNotCurrentUserFriendsAndLoginLike(token, loginLike);
        }
    }
}
