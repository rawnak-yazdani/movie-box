package io.welldev.model.repository;

import io.welldev.model.entity.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CredentialsRepo extends JpaRepository<Credentials,Long> {
    Credentials findByUsername(String username);

//    @Modifying
//    @Query(
//            value =
//                "insert into Credentials (id, password, role, username, admin_id, cinephile_id)" +
//                "values (:id, :password, :role, :username, :admin_id, :cinephile_id)",
//            nativeQuery = true
//    )
//    void insertUser(@Param("id") Long id, @Param("password") String password,
//                    @Param("role") String role, @Param("username") String username,
//                    @Param("admin_id") Long admin_id, @Param("cinephile_id") Long cinephile_id);
}
