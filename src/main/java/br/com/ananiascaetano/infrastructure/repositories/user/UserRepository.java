package br.com.ananiascaetano.infrastructure.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.ananiascaetano.domain.entities.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
	UserDetails findByUsername(String username);
}
