package com.safetyalert.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetyalert.dao.MedicalRepository;
import com.safetyalert.dao.PersonRepository;
import com.safetyalert.dao.StationRepository;
import com.safetyalert.exception.MedicalRecordAlreadyExists;
import com.safetyalert.exception.MedicalRecordNotFoundException;
import com.safetyalert.exception.RequestBodyException;
import com.safetyalert.exception.StationAlreadyExists;
import com.safetyalert.exception.StationNotFoundException;
import com.safetyalert.model.FireStation;
import com.safetyalert.model.MedicalRecord;
import com.safetyalert.model.Person;
import com.safetyalert.model.id.PersonId;

@Service
public class StationService {
	private static final Logger logger = LogManager.getLogger("MedicalService");
	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private PersonRepository personRepository;

	public FireStation createStation(FireStation station) throws StationAlreadyExists {
		String address = station.getAddress();
		FireStation foundStation = stationRepository.findOneByAddress(address);
		if (foundStation != null) {
			throw new StationAlreadyExists("cannot create new station, because address:" + address
					+ " is already covered by station:" + station.getStation());
		}
		return stationRepository.save(station);
	}

	public FireStation updateStation(FireStation station) throws StationNotFoundException, StationAlreadyExists {
		Long id = station.getId();
		if (id != null) {
			return updateStationWithId(station);
		} else {
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
//		return stationRepository.findById(id).map(foundStation->{
//
//			String oldAddress = foundStation.getAddress();
//			String newAddress = newStation.getAddress();
//			foundStation.setStation(newStation.getStation());
//			
//			if(newAddress.equals(oldAddress)){
//				return stationRepository.save(foundStation);
//			}				
//				
//			FireStation addressExists = stationRepository.findOneByAddress(newAddress);
//			if(addressExists!=null) { //PROBLEM...
//				throw new StationAlreadyExists("cannot update new station, because address:" + newAddress
//					+ " is already covered by station:" + addressExists.getStation());
//			}
//			
//			Person person = personRepository.findByStationId(id.toString());
//			person.setFireStation(null);
//			personRepository.save(person);
//			
//			foundStation.setAddress(newAddress);
//			return stationRepository.save(foundStation);
//			
//		}).orElseThrow(() -> new StationNotFoundException("station id not found : "+id));
		
		FireStation foundStation = stationRepository.findOneById(id);
		if(foundStation==null) {
			throw new StationNotFoundException("id not found in table station : "+id);
		}
		String oldAddress = foundStation.getAddress();
		String newAddress = newStation.getAddress();
		foundStation.setStation(newStation.getStation());
		
		if(newAddress.equals(oldAddress)){
			return stationRepository.save(foundStation);
		}				
			
		if(!canCreateStation(newAddress)) { 
			throw new StationAlreadyExists("cannot update new station, because address:" + newAddress
				+ " already covered by a station");
		}
		
		dettachStationFromPersons(id);
		
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
		
		Long foundId = foundStation.getId();
		
		dettachStationFromPersons(foundId);
		
		
		stationRepository.delete(foundStation);
		return foundStation;
	}
	
	private void dettachStationFromPersons(Long stationId) {
		List<Person> persons = personRepository.findByStationId(stationId.toString());
		for(Person person : persons) {
			person.setFireStation(null);
			personRepository.save(person);
		}
	}

}
