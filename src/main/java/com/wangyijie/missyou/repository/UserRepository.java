package com.wangyijie.missyou.repository;

import com.wangyijie.missyou.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByOpenid(String openId);

    User findFirstById(Long id);
}
