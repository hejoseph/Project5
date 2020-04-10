package com.safetyalert.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetyalert.exception.StationAlreadyExists;
import com.safetyalert.exception.StationNotFoundException;
import com.safetyalert.model.FireStation;
import com.safetyalert.service.IStationService;

@RestController
@RequestMapping(consumes = "application/json", produces = "application/json")
public class StationController {

	private static final Logger logger = LogManager.getLogger("StationController");

	@Autowired
	private IStationService stationService;

	@GetMapping("/station")
	public FireStation getStationById(@RequestParam Long id) {
		return stationService.getStationById(id);
	}

	@PostMapping("/firestation")
	public FireStation createStation(@RequestBody FireStation station) throws StationAlreadyExists{
		logger.info("Post Request : \n /firestation\n"+station);
		FireStation result = stationService.createStation(station);
		logger.info("Response : \n "+result);
		return result;
	}
	
	@PutMapping("/firestation")
	public FireStation updateStation(@RequestBody FireStation station) throws StationNotFoundException, StationAlreadyExists{
		logger.info("Put Request : \n /firestation\n"+station);
		FireStation result = stationService.updateStation(station);
		logger.info("Response : \n "+result);
		return result;
	}
	
	@DeleteMapping("/firestation")
	public String deleteStation(@RequestBody FireStation station) throws StationNotFoundException{
		logger.info("Delete Request : \n /firestation\n"+station);
		FireStation deleted = stationService.deleteStation(station);
		String msg = "station deleted : "+deleted;
		logger.info("Response : \n "+msg);
		return msg;
	}

}
