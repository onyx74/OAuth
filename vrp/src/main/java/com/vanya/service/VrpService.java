package com.vanya.service;

import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.google.maps.model.LatLng;
import com.vanya.calculator.vrp.VRPCalculator;
import com.vanya.client.LoadClient;
import com.vanya.dao.VrpItemRepository;
import com.vanya.dao.VrpRepository;
import com.vanya.dto.LoadDTO;
import com.vanya.dto.VrpDTO;
import com.vanya.dto.VrpItemDTO;
import com.vanya.model.VrpEntity;
import com.vanya.model.VrpItemEntity;
import com.vanya.provider.DistanceProvider;
import com.vanya.utils.GoogleApiUtils;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VrpService {
    @Autowired
    private VrpRepository vrpRepository;

    @Autowired
    private GoogleApiUtils googleApiUtils;

    @Autowired
    private LoadClient loadClient;

    @Autowired
    @Qualifier("GoogleDistanceProvider")
    private DistanceProvider distanceProvider;

    @Autowired
    private VRPCalculator vrpCalculator;

    public VrpDTO calculateOptimalWay(List<Long> loadsId, String startPosition, String name) {
        LatLng geocod = googleApiUtils.geocod(startPosition);
        List<LoadDTO> loads = loadClient.getLoads(loadsId);
        Pair<Double, Double> startCoordinate = new Pair<>(geocod.lat, geocod.lng);
        Table<Long, Long, Double> table = distanceProvider.calculateDistance(startCoordinate, loads);
        Map<Long, LoadDTO> loadDTOMap = loads.stream()
                .collect(Collectors.toMap(LoadDTO::getLoadId, load -> load,
                        (oldValue, newValue) -> oldValue));
        log.info("Start calculation");
        Multimap<Long, Long> result = vrpCalculator.clarckSolver(table);
        VrpEntity vrpEntity = convertToVrpEntity(result, loadDTOMap, geocod);
        vrpEntity.setName(name);
        vrpEntity.setTotalDistance(distanceProvider.calculateDistance(startCoordinate, result, loadDTOMap));
        vrpEntity.setStartLocation(startPosition);
        vrpRepository.save(vrpEntity);
        return convertToVrpEntityDTO(vrpEntity);
    }


    public Page<VrpDTO> findAllVrp(String username,
                                   String startPosition,
                                   String name,
                                   int evalPage,
                                   int evalPageSize) {
        final PageRequest pageRequest = new PageRequest(evalPage, evalPageSize);
        return vrpRepository.findAllByOwnerAndNameLikeAndStartLocationLike(username,
                "%" + name + "%",
                "%" + startPosition + "%",
                pageRequest)
                .map(this::convertToVrpEntityDTO);
    }

    public VrpDTO getVrp(long vrpId) {
        VrpEntity vrpEntity = vrpRepository.findOne(vrpId);
        return convertToVrpEntityDTO(vrpEntity);
    }

    private VrpEntity convertToVrpEntity(Multimap<Long, Long> result, Map<Long, LoadDTO> loadDTOMap, LatLng startPosition) {
        VrpEntity vrpEntity = createVrpEntity(startPosition);

        Set<VrpItemEntity> vrpItemEntities = new HashSet<>();

        for (Long solutionId : result.keySet()) {
            int position = 0;
            for (Long loadId : result.get(solutionId)) {
                VrpItemEntity vrpItemEntity = createVrpEntity(loadDTOMap.get(loadId), solutionId, position, vrpEntity);
                vrpItemEntities.add(vrpItemEntity);
                position++;
            }
        }

        vrpEntity.setVrpItemEntities(vrpItemEntities);
        return vrpEntity;
    }

    private VrpItemEntity createVrpEntity(LoadDTO loadDTO, Long solutionId, int position, VrpEntity vrpEntity) {
        VrpItemEntity vrpItemEntity = new VrpItemEntity();
        vrpItemEntity.setStartLatitude(loadDTO.getStartLatitude());
        vrpItemEntity.setStartLongitude(loadDTO.getStartLongitude());

        vrpItemEntity.setFinishLatitude(loadDTO.getFinishLatitude());
        vrpItemEntity.setFinishLongitude(loadDTO.getFinishLongitude());
        vrpItemEntity.setStartLocation(loadDTO.getStartAddress());
        vrpItemEntity.setFinishLocation(loadDTO.getFinishAddress());
        vrpItemEntity.setLoadId(loadDTO.getLoadId());
        vrpItemEntity.setSolutionId(solutionId);
        vrpItemEntity.setPosition(position);
        vrpItemEntity.setVrp(vrpEntity);
        return vrpItemEntity;
    }

    private VrpEntity createVrpEntity(LatLng startPosition) {
        VrpEntity vrpEntity = new VrpEntity();
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        vrpEntity.setOwner(currentUserName);
        vrpEntity.setCreateDate(new Date(System.currentTimeMillis()));
        vrpEntity.setStartLatitude(startPosition.lat);
        vrpEntity.setStartLongitude(startPosition.lng);
        return vrpEntity;
    }

    private VrpDTO convertToVrpEntityDTO(VrpEntity vrpEntity) {
        VrpDTO vrpDTO = new VrpDTO();
        vrpDTO.setCreateDate(vrpEntity.getCreateDate());
        vrpDTO.setOwner(vrpEntity.getOwner());
        vrpDTO.setStartLatitude(vrpEntity.getStartLatitude());
        vrpDTO.setStartLongitude(vrpEntity.getStartLongitude());
        vrpDTO.setVrpItemEntities(vrpEntity.getVrpItemEntities().stream()
                .map(this::convertToVrpItemDTO)
                .collect(Collectors.toList())
        );
        vrpDTO.setVrpId(vrpEntity.getVrpId());
        vrpDTO.setName(vrpEntity.getName());
        vrpDTO.setTotalDistance(vrpEntity.getTotalDistance());
        vrpDTO.setStartLocation(vrpEntity.getStartLocation());
        return vrpDTO;
    }

    private VrpItemDTO convertToVrpItemDTO(VrpItemEntity vrpItemEntity) {
        VrpItemDTO vrpItemDTO = new VrpItemDTO();
        vrpItemDTO.setLoadId(vrpItemEntity.getLoadId());
        vrpItemDTO.setSolutionId(vrpItemEntity.getSolutionId());
        vrpItemDTO.setVrpItemId(vrpItemEntity.getVrpItemId());
        vrpItemDTO.setStartLocation(vrpItemEntity.getStartLocation());
        vrpItemDTO.setFinishLocation(vrpItemEntity.getFinishLocation());
        vrpItemDTO.setStartLatitude(vrpItemEntity.getStartLatitude());
        vrpItemDTO.setStartLongitude(vrpItemEntity.getStartLongitude());
        vrpItemDTO.setFinishLatitude(vrpItemEntity.getFinishLatitude());
        vrpItemDTO.setFinishLongitude(vrpItemEntity.getFinishLongitude());
        vrpItemDTO.setPosition(vrpItemEntity.getPosition());
        return vrpItemDTO;
    }
}
