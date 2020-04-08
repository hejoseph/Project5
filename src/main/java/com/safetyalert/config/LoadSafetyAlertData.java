package com.safetyalert.config;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetyalert.dao.MedicalRepository;
import com.safetyalert.dao.PersonDAO;
import com.safetyalert.dao.PersonRepository;
import com.safetyalert.dao.StationRepository;
import com.safetyalert.model.FireStation;
import com.safetyalert.model.MedicalRecord;
import com.safetyalert.model.MedicalRecordDto;
import com.safetyalert.model.Person;
import com.safetyalert.model.SafetyAlertJsonData;
import com.safetyalert.util.Util;

@Configuration
public class LoadSafetyAlertData {

	private static final Logger logger = LogManager.getLogger("LoadSafetyAlertData");

	@Autowired
	private PersonDAO personDAO;

	@Autowired
	private MedicalRepository medicalRepository;
	@Autowired
	private StationRepository stationRepository;

	@Bean
	CommandLineRunner initMedicalData(MedicalRepository medicalRepository) {
		return args -> {
			logger.info("loading data to db...");
			ObjectMapper objectMapper = new ObjectMapper();
			SafetyAlertJsonData jsonData = objectMapper.readValue(new File("data.json"), SafetyAlertJsonData.class);
			jsonData.getMedicalrecords().forEach(record -> {
				medicalRepository.save(record);
			});
			logger.info("medical records loaded");
		};
	}
	
//	public static <D> Object fromArrayToList(D[] a) {   
////	    return Arrays.stream(a).collect(Collectors.toList());
//		return null;
//	}
	
	

//	public static <D,S> D convertDto(S from, Class<D> destClass) {
//		JMapperAPI simpleApi = new JMapperAPI().add(JMapperAPI.mappedClass(destClass));
//		JMapper simpleMapper = new JMapper(destClass, from.getClass(), simpleApi);
//		return (D) simpleMapper.getDestination(from);
//	}
	
	
	
//	public static <D,S> List<D> convertDtoList(List<S> fromList, Class<D> destClass) {
//		if(fromList.size()==0) {
//			return null;
//		}
//		
//		JMapperAPI simpleApi = new JMapperAPI().add(JMapperAPI.mappedClass(destClass));
//		JMapper simpleMapper = new JMapper(destClass, fromList.get(0).getClass(), simpleApi);
//		
//		List<D> result = new ArrayList<D>();
//		
//		for(S from : fromList) {
//			result.add((D) simpleMapper.getDestination(from));
//		}
//		
//		return result;
//	}

//	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
////		JMapperAPI simpleApi = new JMapperAPI().add(JMapperAPI.mappedClass(MedicalRecordDto.class));
////		JMapper simpleMapper = new JMapper(MedicalRecordDto.class, MedicalRecord.class, simpleApi);
//		ObjectMapper objectMapper = new ObjectMapper();
//		SafetyAlertJsonData jsonData = objectMapper.readValue(new File("data.json"), SafetyAlertJsonData.class);
//
//		List<MedicalRecord> records = jsonData.getMedicalrecords();
//		List<MedicalRecordDto> recordsDto = convertDtoList(records, MedicalRecordDto.class);
//		
//		for(MedicalRecordDto dto : recordsDto) {
//			logger.info(dto);
//		}
//		
//		
//		//		jsonData.getMedicalrecords().forEach(record -> {
////			logger.info("before");
//////			MedicalRecordDto dto = (MedicalRecordDto) simpleMapper.getDestination(record);
////			MedicalRecordDto dto = convertDto(record, MedicalRecordDto.class);
////			logger.info(dto);
////		});
////		hello(MedicalRecordDto.class);
//		
//		
//		
//	}

	private static void hello(Class<MedicalRecordDto> class1) {
		// TODO Auto-generated method stub

	}

	@Bean
	CommandLineRunner initStationData(StationRepository stationRepository) {
		return args -> {
			logger.info("loading data to db...");
			ObjectMapper objectMapper = new ObjectMapper();
			SafetyAlertJsonData jsonData = objectMapper.readValue(new File("data.json"), SafetyAlertJsonData.class);
			jsonData.getFirestations().forEach(station -> {
				stationRepository.save(station);
			});
			logger.info("stations loaded");
		};
	}

	@Bean
	CommandLineRunner initPersonData(PersonRepository personRepository) {
		return args -> {
			logger.info("loading data to db...");
			ObjectMapper objectMapper = new ObjectMapper();
			SafetyAlertJsonData jsonData = objectMapper.readValue(new File("data.json"), SafetyAlertJsonData.class);
			jsonData.getPersons().forEach(pTemp -> {
				Person person = new Person();
				String firstName = pTemp.getFirstName();
				String lastName = pTemp.getLastName();
				String address = pTemp.getAddress();
				String city = pTemp.getCity();
				String email = pTemp.getEmail();
				String phone = pTemp.getPhone();
				String zip = pTemp.getZip();
				person.setFirstName(firstName);
				person.setLastName(lastName);
				person.setAddress(address);
				MedicalRecord medical = medicalRepository.findByFirstNameAndLastName(firstName, lastName);
				person.setMedicalRecord(medical);
				if (medical != null) {
					person.setAge(Util.calculteAge(medical.getBirthdate()));
				}
				FireStation station = stationRepository.findOneByAddress(address);
				person.setFireStation(station);
				person.setCity(city);
				person.setEmail(email);
				person.setPhone(phone);
				person.setZip(zip);
				personRepository.save(person);
			});
			logger.info("persons loaded");
		};
	}

	// @Bean
	// CommandLineRunner initDataFromJson() {
	// return args -> {
	// logger.info("loading data ...");
	// ObjectMapper objectMapper = new ObjectMapper();
	// //read json file and convert to customer object
	// SafetyAlertJsonData jsonData = objectMapper.readValue(new File("data.json"),
	// SafetyAlertJsonData.class);
	// associateData(jsonData);
	// personDAO.setPersons(jsonData.getPersons());
	// };
	// }

	// private void associateData(SafetyAlertJsonData jsonData) {
	// for(Person person : jsonData.getPersons()) {
	// //associate one fireStation to one person
	// for(FireStation fireStation : jsonData.getFirestations()) {
	// if(person.getAddress().equals(fireStation.getAddress())) {
	// person.setFireStation(fireStation);
	// break;
	// }
	// }
	//
	// //associate one medicalRecord to one person
	// for(MedicalRecord record : jsonData.getMedicalrecords()) {
	// String keyPerson = person.getFirstName()+person.getLastName();
	// String keyRecord = record.getFirstName()+record.getLastName();
	// if(keyPerson.equals(keyRecord)) {
	// person.setMedicalRecord(record);
	// person.setAge(PersonService.calculteAge(record.getBirthdate()));
	// }
	// }
	//
	//
	// }
	// }

}
