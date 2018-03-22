package config;

import io.univ.rouen.m2gil.smartclass.core.user.Role;
import io.univ.rouen.m2gil.smartclass.core.user.User;
import io.univ.rouen.m2gil.smartclass.core.user.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyUserRepository extends UserRepository<User> {
    List<User> findByRole(Role role);
}
