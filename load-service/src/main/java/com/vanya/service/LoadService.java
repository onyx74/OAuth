package com.vanya.service;

import com.google.maps.model.LatLng;
import com.vanya.dao.ArchivedLoadRepository;
import com.vanya.dao.LoadRepository;
import com.vanya.dto.LoadDTO;
import com.vanya.enums.LoadStatus;
import com.vanya.model.entity.ArchivedLoadEntity;
import com.vanya.model.entity.LoadEntity;
import com.vanya.model.event.NewLoadEvent;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class LoadService {
    @Autowired
    private LoadRepository loadRepository;

    @Autowired
    private ArchivedLoadRepository archivedLoadRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ModelMapper mapper;

    public void updateLoadCoordinates(Long loadId,
                                      LatLng startLocation,
                                      LatLng finishLocation,
                                      long distance) {
        loadRepository.updateLoadCoordinates(loadId,
                startLocation.lat,
                startLocation.lng,
                finishLocation.lat,
                finishLocation.lng,
                distance);
    }

    public LoadDTO addNewLoad(LoadDTO loadDTO) {
        final LoadEntity loadEntity = getLoadEntity(loadDTO);

        loadRepository.save(loadEntity);
        eventPublisher.publishEvent(new NewLoadEvent(loadEntity));

        return mapper.map(loadEntity, LoadDTO.class);
    }

    private LoadEntity getLoadEntity(LoadDTO loadDTO) {
        LoadEntity loadEntity = mapper.map(loadDTO, LoadEntity.class);
        loadEntity.setCreateDate(new Date(System.currentTimeMillis()));
        loadEntity.setLoadStatus(LoadStatus.ACTIVE);
        return loadEntity;
    }

    public Page<LoadDTO> findAllLoads(String username, int evalPage, int evalPageSize) {
        final PageRequest pageRequest = new PageRequest(evalPage, evalPageSize);
        return loadRepository.findAllByUsername(username, pageRequest).map(load -> mapper.map(load, LoadDTO.class));
    }

    public void removeLoad(long loadId) {
        LoadEntity load = loadRepository.findOne(loadId);
        loadRepository.delete(loadId);
        ArchivedLoadEntity archivedLoad = mapper.map(load, ArchivedLoadEntity.class);
        archivedLoad.setDeletedDate(new Date(System.currentTimeMillis()));
        archivedLoadRepository.save(archivedLoad);

    }

    public LoadDTO getLoad(Long loadId) {
        return mapper.map(loadRepository.findOne(loadId), LoadDTO.class);
    }
}
