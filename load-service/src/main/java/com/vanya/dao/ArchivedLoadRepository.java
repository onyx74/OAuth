package com.vanya.dao;

import com.vanya.model.entity.ArchivedLoadEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivedLoadRepository extends CrudRepository<ArchivedLoadEntity, Long> {
}
