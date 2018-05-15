package com.vanya.dao;

import com.vanya.model.VrpEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VrpRepository extends PagingAndSortingRepository<VrpEntity, Long> {
    Page<VrpEntity> findAllByOwnerAndNameLikeAndStartLocationLike(String owner,
                                                                    String name,
                                                                    String startLocation,
                                                                    Pageable pageable);
}
