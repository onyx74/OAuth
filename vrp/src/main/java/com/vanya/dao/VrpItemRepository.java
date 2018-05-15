package com.vanya.dao;

import com.vanya.model.VrpEntity;
import com.vanya.model.VrpItemEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VrpItemRepository extends CrudRepository<VrpItemEntity, Long> {
    List<VrpItemEntity> findALlByVrp(VrpEntity vrpEntity);
}
