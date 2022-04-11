package com.jira.kitsjiraclient.repository;

import com.jira.kitsjiraclient.entities.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepo extends JpaRepository<ConfigEntity,Long> {
}