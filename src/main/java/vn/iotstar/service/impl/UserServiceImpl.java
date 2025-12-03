package vn.iotstar.service.impl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired UserRepository userRepository;

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    @Override
    public Page<User> search(String keyword, Pageable pageable) {
        return userRepository.findByUsernameContainingOrFullnameContaining(keyword, keyword, pageable);
    }
    @Override
    public Optional<User> findById(String username) {
        return userRepository.findById(username);
    }
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
    @Override
    public void deleteById(String username) {
        userRepository.deleteById(username);
    }
}