package com.cunzheng.configuration.servie;

import com.cunzheng.entity.UserBean;
import com.cunzheng.entity.UserResult;
import com.cunzheng.entity.UserRole;
import com.cunzheng.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean hasUserNameCreated(String userName) {
        return userRepository.findByUserName(userName) != null;
    }

    public UserResult verifyUserNameAndPassWord(String userName, String encodePassword) {
        UserResult result;
        UserBean userBean = userRepository.findByUserName(userName);
        if (userBean == null) {
            result = UserResult.INVALID_USERNAME;
        } else if (!userBean.getPassword().equals(encodePassword)) {
            result = UserResult.INVALID_PASSWORD;
        } else {
            result = UserResult.SUCCESS;
        }
        return result;
    }

    public void saveUser(String userName, String password, UserRole userRole, String accountJson) {
        String encodePwd = new String(DigestUtils.md5Digest(password.getBytes()));
        UserBean userBean = new UserBean(userName, encodePwd, userRole, accountJson);
        userRepository.save(userBean);
    }
}
