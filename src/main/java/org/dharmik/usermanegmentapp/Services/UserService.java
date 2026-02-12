package org.dharmik.usermanegmentapp.Services;

import org.dharmik.usermanegmentapp.Entity.User;
import org.dharmik.usermanegmentapp.Repositary.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    public void saveEntry(User userEntry) {
        repo.save(userEntry);
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return repo.findById(Long.valueOf(id));
    }

    public User deleteEntry(Integer id) {
        User userToDelete = repo.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        repo.delete(userToDelete);
        return userToDelete;
    }
}