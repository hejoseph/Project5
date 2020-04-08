package com.safetyalert.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetyalert.dao.PersonRepository;
import com.safetyalert.dao.StationRepository;
import com.safetyalert.exception.RequestBodyException;
import com.safetyalert.exception.StationAlreadyExists;
import com.safetyalert.exception.StationNotFoundException;
import com.safetyalert.model.FireStation;
import com.safetyalert.model.Person;

@Service
public class StationServiceImpl implements IStationService{
	private static final Logger logger = LogManager.getLogger("StationServiceImpl");
	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private PersonRepository personRepository;

	public FireStation createStation(FireStation station) throws StationAlreadyExists {
		String address = station.getAddress();
		if (isAddressCovered(address)) {
			throw new StationAlreadyExists("cannot create new station, because address:" + address
					+ " is already covered by a station");
		}
		return stationRepository.save(station);
	}
	
	
	private boolean isAddressCovered(String address) {
		FireStation foundStation = stationRepository.findOneByAddress(address);
		return foundStation!=null;
	}

	public FireStation updateStation(FireStation station) throws StationNotFoundException, StationAlreadyExists {
		Long id = station.getId();
		if (id != null) {
			logger.info("idfound");
			return updateStationWithId(station);
		} else {
			logger.info("noidfound");
			return updateStationWithAddress(station);
		}
	}

	private boolean canCreateStation(String address) {
		FireStation exists = stationRepository.findOneByAddress(address);
		return exists==null;
	}
	
	private FireStation updateStationWithAddress(FireStation station) throws StationNotFoundException {
		String address = station.getAddress();
		FireStation foundStation = stationRepository.findOneByAddress(address);
		if(foundStation==null) {
			throw new StationNotFoundException("address does not exist in table station "+station);
		}
		foundStation.setStation(station.getStation());
		return stationRepository.save(foundStation);
	}

	private FireStation updateStationWithId(FireStation newStation) throws StationNotFoundException, StationAlreadyExists{
		Long id = newStation.getId();
		FireStation foundStation = stationRepository.findOneById(id);
		if(foundStation==null) {
			throw new StationNotFoundException("id not found in table station : "+id);
		}
		String oldId = foundStation.getId()+foundStation.getAddress();
		String newId = newStation.getId()+newStation.getAddress();
		foundStation.setStation(newStation.getStation());
		
		if(newId.equals(oldId)){
			return stationRepository.save(foundStation);
		}				
		
		String newAddress = newStation.getAddress();
		if(!canCreateStation(newAddress)) { 
			throw new StationAlreadyExists("cannot update new station, because address:" + newAddress
				+ " already covered by a station");
		}
		
		dettachStationFromPersons(foundStation);
		
		foundStation.setAddress(newAddress);
		return stationRepository.save(foundStation);
	}
	
	private void attributeAddressMustExists(FireStation station) throws RequestBodyException {
		if(station.getAddress()==null) {
			throw new RequestBodyException("attribute 'address' is missing in your json request");
		}
	}

	public FireStation deleteStation(FireStation station) throws StationNotFoundException {
		Long id = station.getId();
		FireStation foundStation = null;
		if(id!=null) {
			foundStation = stationRepository.findOneById(id);
		}else{
			String address = station.getAddress();
			foundStation = stationRepository.findOneByAddress(address);
		}
		
		if(foundStation==null) {
			throw new StationNotFoundException("cannot delete, no station found "+station);
		}
		
		dettachStationFromPersons(foundStation);
		
		stationRepository.delete(foundStation);
		return foundStation;
	}
	
	private void dettachStationFromPersons(FireStation foundStation) {
		List<Person> persons = foundStation.getPerson();
		for(Person person : persons) {
			person.setFireStation(null);
		}
	}


	public FireStation getStationById(Long id) {
		return stationRepository.findOneById(id);
	}

}
