package com.vanya.service;

import com.google.maps.model.LatLng;
import com.vanya.dao.TruckRepository;
import com.vanya.dto.TruckDTO;
import com.vanya.model.TruckEntity;
import com.vanya.model.event.NewTruckEvent;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class TruckService {
    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ModelMapper mapper;

    public void updateTruckCoordinates(Long truckId,
                                       LatLng startLocation) {
        truckRepository.updateTruckCoordinates(truckId,
                startLocation.lat,
                startLocation.lng);
    }

    public TruckDTO addNewTruck(TruckDTO truckDTO) {
        final TruckEntity truckEntity = getTruckEntity(truckDTO);

        truckRepository.save(truckEntity);
        eventPublisher.publishEvent(new NewTruckEvent(truckEntity));

        return mapper.map(truckEntity, TruckDTO.class);
    }

    private TruckEntity getTruckEntity(TruckDTO truckDTO) {
        TruckEntity truckEntity = mapper.map(truckDTO, TruckEntity.class);
        truckEntity.setCreateDate(new Date(System.currentTimeMillis()));
        return truckEntity;
    }

    public Page<TruckDTO> findAllTrucks(String username, String carModel, String position, int evalPage, int evalPageSize) {
        final PageRequest pageRequest = new PageRequest(evalPage, evalPageSize);
        return truckRepository.findAllByOwnernameAndCarModelLikeAndCurrentPossitionLike(username,
                "%" + carModel + "%",
                "%" + position + "%",
                pageRequest)
                .map(truck -> mapper.map(truck, TruckDTO.class));
    }

    public void removeTruck(long truckId) {
        truckRepository.delete(truckId);

    }

    public TruckDTO getTruck(Long truckId) {
        return mapper.map(truckRepository.findOne(truckId), TruckDTO.class);
    }
}
