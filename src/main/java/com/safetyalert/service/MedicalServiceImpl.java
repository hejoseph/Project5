package com.safetyalert.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetyalert.dao.MedicalRepository;
import com.safetyalert.dao.PersonRepository;
import com.safetyalert.exception.MedicalRecordAlreadyExists;
import com.safetyalert.exception.MedicalRecordNotFoundException;
import com.safetyalert.model.MedicalRecord;
import com.safetyalert.model.MedicalRecordDto;
import com.safetyalert.model.Person;
import com.safetyalert.util.Util;

@Service
public class MedicalServiceImpl implements IMedicalService{
	private static final Logger logger = LogManager.getLogger("MedicalServiceImpl");
	@Autowired
	private MedicalRepository medicalRepository;

	@Autowired
	private PersonRepository personRepository;

	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) throws MedicalRecordAlreadyExists {
		String firstName = medicalRecord.getFirstName();
		String lastName = medicalRecord.getLastName();
		if (!canCreateMedical(firstName, lastName)) {
			throw new MedicalRecordAlreadyExists(
					"cannot create new medical record, because medical already exists, for firstName and lastName " + medicalRecord);
		}
		
		MedicalRecord saved = medicalRepository.save(medicalRecord);
		attachMedicalToPerson(saved);
		
		return saved;
	}
	
	private void attachMedicalToPerson(MedicalRecord medicalRecord) {
		String firstName = medicalRecord.getFirstName();
		String lastName = medicalRecord.getLastName();
		Person person = personRepository.findByFirstNameAndLastName(firstName, lastName);
		if(person!=null) {
			person.setMedicalRecord(medicalRecord);
			personRepository.save(person);
		}
	}

	private boolean canCreateMedical(String firstName, String lastName) {
		MedicalRecord exists = medicalRepository.findByFirstNameAndLastName(firstName, lastName);
		return exists==null;
	}
	
	private MedicalRecord updateMedicalRecordWithId(MedicalRecord newRecord) throws MedicalRecordNotFoundException, MedicalRecordAlreadyExists {
		Long id = newRecord.getId();
		MedicalRecord foundRecord = medicalRepository.findOneById(id);
		if(foundRecord==null) {
			throw new MedicalRecordNotFoundException("id{"+id+"} not found in table MedicalRecord");
		}
		
		foundRecord.setAllergies(newRecord.getAllergies());
		foundRecord.setMedications(newRecord.getMedications());
		foundRecord.setBirthdate(newRecord.getBirthdate());
		String newId = newRecord.getId()+newRecord.getFirstName() + newRecord.getLastName();
		String foundId = foundRecord.getId()+foundRecord.getFirstName() + foundRecord.getLastName();

		if (newId.equals(foundId)) {
			return medicalRepository.save(foundRecord);
		} else {
			String oldFirstName = foundRecord.getFirstName();
			String oldLastName = foundRecord.getLastName();

			String newFirstName = newRecord.getFirstName();
			String newLastName = newRecord.getLastName();
			
			if(!canCreateMedical(newFirstName, newLastName)) {
				throw new MedicalRecordAlreadyExists("cannot update medical table with firstName="+newFirstName+" ; lastName="+newLastName+ " because already exists");
			}
			
			// medical name changed, so detached object medical from person
			dettachMedicalFromPerson(foundRecord);

			foundRecord.setFirstName(newRecord.getFirstName());
			foundRecord.setLastName(newRecord.getLastName());
			
			attachMedicalToPerson(foundRecord);

			return medicalRepository.save(foundRecord);
		}
		
	}

	private MedicalRecord updateMedicalRecordWithFirstAndLastName(MedicalRecord newRecord) {
		String firstName = newRecord.getFirstName();
		String lastName = newRecord.getLastName();
		MedicalRecord medical = medicalRepository.findByFirstNameAndLastName(firstName, lastName);
		medical.setBirthdate(newRecord.getBirthdate());
		medical.setAllergies(newRecord.getAllergies());
		medical.setMedications(newRecord.getMedications());
		return medicalRepository.save(medical);
	}

	private void checkFirstNameAndLastName(MedicalRecord newRecord) throws MedicalRecordNotFoundException {
		String firstName = newRecord.getFirstName();
		String lastName = newRecord.getLastName();

		if (firstName == null || firstName.equals("") || lastName == null || lastName.equals("")) {
			throw new MedicalRecordNotFoundException("firstName and lastName must not be null or empty");
		}
	}

	public MedicalRecord updateMedicalRecord(MedicalRecord newRecord) throws MedicalRecordNotFoundException, MedicalRecordAlreadyExists {
		Long id = newRecord.getId();
		if (id != null) {
			return updateMedicalRecordWithId(newRecord);
		}

		checkFirstNameAndLastName(newRecord);

		return updateMedicalRecordWithFirstAndLastName(newRecord);
	}

	public MedicalRecordDto deleteMedicalRecord(MedicalRecord record) throws MedicalRecordNotFoundException {
		Long id = record.getId();
		MedicalRecordDto result = null;
		MedicalRecord temp = null;
		if (id != null) {
			temp = medicalRepository.findOneById(id);
			if(temp!=null) {
				System.out.println("Debug");
				System.out.println("Debug");
				System.out.println("Debug");
				logger.info(temp.getBirthdate());
				
				logger.info(temp.getAllergies());
				
				dettachMedicalFromPerson(temp);
				result = Util.copyObject(temp, MedicalRecordDto.class);
				medicalRepository.delete(temp);
			}
		} else {
			checkFirstNameAndLastName(record);
			MedicalRecord foundRecord = medicalRepository.findByFirstNameAndLastName(record.getFirstName(),
					record.getLastName());
			if(foundRecord!=null) {
				dettachMedicalFromPerson(foundRecord);
				result=Util.copyObject(foundRecord, MedicalRecordDto.class);
				medicalRepository.delete(foundRecord);
			}
		}
		return result;
	}
	
	private boolean dettachMedicalFromPerson(MedicalRecord record) {
		Person person = personRepository.findByMedicalId(record.getId().toString());
		if(person!=null) {
			person.setMedicalRecord(null);
			personRepository.save(person);
			return true;
		}
		return false;
//		record.getPerson().setMedicalRecord(null);
	}

	@Override
	public MedicalRecord getMedicalById(Long id) {
		return medicalRepository.findOneById(id);
	}

}
