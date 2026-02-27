package com.eventhub.eventhub_api.user.repository;

import com.eventhub.eventhub_api.user.dto.input.RegisterRequest;
import com.eventhub.eventhub_api.user.dto.output.UserResponse;
import com.eventhub.eventhub_api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);
}
