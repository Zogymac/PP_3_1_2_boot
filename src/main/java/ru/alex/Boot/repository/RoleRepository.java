package ru.alex.Boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex.Boot.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
