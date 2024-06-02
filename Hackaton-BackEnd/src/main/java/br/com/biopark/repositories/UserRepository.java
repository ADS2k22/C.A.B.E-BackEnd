package br.com.biopark.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.biopark.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query(nativeQuery = true, value = "SELECT * FROM user WHERE username = :username")
	User findByUsername(@Param("username") String username);
	
	@Query(nativeQuery = true, value = "SELECT * FROM user ORDER BY id ASC LIMIT 10 OFFSET :page")
	List<User> findAllPaged(@Param("page") int page);
	
	@Query(nativeQuery = true, value = "SELECT COUNT(id) FROM user WHERE username = :username")
	int existsByUsername(@Param(value = "username") String numero);
}
