package com.vanya.dao;

import com.vanya.model.VrpEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VrpRepository extends PagingAndSortingRepository<VrpEntity, Long> {
}
