package com.safetyalert.service;

import com.safetyalert.exception.StationAlreadyExists;
import com.safetyalert.exception.StationNotFoundException;
import com.safetyalert.model.FireStation;

public interface IStationService {
	public FireStation getStationById(Long id);
	public FireStation createStation(FireStation station) throws StationAlreadyExists;
	public FireStation updateStation(FireStation station) throws StationNotFoundException, StationAlreadyExists;
	public FireStation deleteStation(FireStation station) throws StationNotFoundException;
}
