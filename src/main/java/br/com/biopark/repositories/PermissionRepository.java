package br.com.biopark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.biopark.models.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long>{}
