package com.jxm.yiti.service;

import com.jxm.yiti.domain.User;
import com.jxm.yiti.domain.UserExample;
import com.jxm.yiti.mapper.UserMapper;
import com.jxm.yiti.resp.CommonResp;
import jakarta.annotation.Resource;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public AuthRequest getAuthRequest() {
        return new AuthGithubRequest(AuthConfig.builder()
                .clientId("7f64b8527592ce2d4d7c")
                .clientSecret("bc5d42bee691771f547351fec23f445676aeb10b")
                .redirectUri("http://124.223.184.187:8111/user/github/callback")
                .build());
    }

    public int signInOrUp(AuthUser authUser) {
        int res = -1;
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andGithubIdEqualTo(authUser.getUuid());

        List<User> resList = userMapper.selectByExample(userExample);
        if (resList.isEmpty()) {
            // 为新用户
            User user = new User();
            user.setGithubId(authUser.getUuid());
            user.setEmail(authUser.getEmail());
            user.setBalance(0L);                 // 余额设为 0
            res = userMapper.insert(user);
        }
        return res;
    }
}
