package com.vanya.dao;

import com.vanya.model.TruckEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TruckRepository extends PagingAndSortingRepository<TruckEntity, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE TruckEntity t " +
            "SET t.latitude=?2, t.longitude=?3  " +
            "where t.truckId=?1")
    int updateTruckCoordinates(long truckId,
                               double latitude,
                               double longitude);

    Page<TruckEntity> findAllByOwnernameAndCarModelLikeAndCurrentPossitionLike(String ownername,
                                                                               String carModel,
                                                                               String position,
                                                                               Pageable pageable);
}

