package com.hacker.repository;


import com.hacker.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Boolean existsByEmail(String email); // wenn email existiert true, sonst false

    @EntityGraph(attributePaths = "roles") // Defaultta Lazy olan Role bilgilerini EAGER yapiyor
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    List<User> findAll();                    // Ohne EntityGraph --> 4 Query bei Lazy
                                             // Mit  EntityGraph --> 2 Query bei Eager

    @EntityGraph(attributePaths = "roles")
    Page<User> findAll(Pageable pageable);   // Ohne EntityGraph --> 4 Query bei Lazy
                                             // Mit  EntityGraph --> 2 Query bei Eager

    @EntityGraph(attributePaths = "roles")
    Optional<User> findById(Integer id);        // Ohne EntityGraph --> 4 Query bei Lazy
                                             // Mit  EntityGraph --> 2 Query bei Eager

    @EntityGraph(attributePaths = "roles")
    Optional<User> findUserById(Integer id);    // Ohne EntityGraph --> 4 Query bei Lazy
                                             // Mit  EntityGraph --> 2 Query bei Eager

    //Native Query
    @Modifying //JpaRepository icinde custom bir query ile DML operasyonlari yapiliyor ile @Modifying yazilir
    @Query("UPDATE User u SET u.firstName=:firstName, u.lastName=:lastName, u.username=:username, u.email=:email WHERE u.id=:id")
    void update(@Param("id") Integer id,
                @Param("firstName") String firstName,
                @Param("lastName") String lastName,
                @Param("username") String username,
                @Param("email") String email);
}
