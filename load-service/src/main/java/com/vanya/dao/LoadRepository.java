package com.vanya.dao;

import com.vanya.model.entity.LoadEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LoadRepository extends PagingAndSortingRepository<LoadEntity, Long> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE LoadEntity l " +
            "SET l.startLatitude= ?2,l.startLongitude =?3, l.finishLatitude = ?4,l.finishLongitude=?5,l.distance=?6 " +
            "where l.loadId=?1")
    int updateLoadCoordinates(long loadId,
                              double startLatitude,
                              double startLongitude,
                              double finishLatitude,
                              double finishLongitude,
                              long distance);

    Page<LoadEntity> findAllByUsernameAndStartAddressLikeAndFinishAddressLike(String username,
                                                                              String startAddress,
                                                                              String finishAddress,
                                                                              Pageable pageable);
}
