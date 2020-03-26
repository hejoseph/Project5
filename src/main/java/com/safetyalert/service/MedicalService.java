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
import com.safetyalert.model.Person;
import com.safetyalert.model.id.PersonId;

@Service
public class MedicalService {
	private static final Logger logger = LogManager.getLogger("MedicalService");
	@Autowired
	private MedicalRepository medicalRepository;

	@Autowired
	private PersonRepository personRepository;

	// public MedicalService(MedicalRepository medicalRepository) {
	// this.medicalRepository = medicalRepository;
	// }

	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) throws MedicalRecordAlreadyExists {
		String firstName = medicalRecord.getFirstName();
		String lastName = medicalRecord.getLastName();
		if (!canCreateMedical(firstName, lastName)) {
			throw new MedicalRecordAlreadyExists(
					"cannot create new medical record, because medical already exists, for firstName and lastName " + medicalRecord);
		}
		
		Person person = personRepository.findByFirstNameAndLastName(firstName, lastName);
		person.setMedicalRecord(medicalRecord);
		personRepository.save(person);
		
		return medicalRepository.save(medicalRecord);
	}

	private boolean canCreateMedical(String firstName, String lastName) {
		MedicalRecord exists = medicalRepository.findByFirstNameAndLastName(firstName, lastName);
		return exists==null;
	}
	
	private MedicalRecord updateMedicalRecordWithId(MedicalRecord newRecord) throws MedicalRecordNotFoundException, MedicalRecordAlreadyExists {
		Long id = newRecord.getId();
//		return medicalRepository.findById(id).map(foundRecord -> {
//			foundRecord.setAllergies(newRecord.getAllergies());
//			foundRecord.setMedications(newRecord.getMedications());
//			foundRecord.setBirthdate(newRecord.getBirthdate());
//
//			String newId = newRecord.getFirstName() + newRecord.getLastName();
//			String foundId = foundRecord.getFirstName() + foundRecord.getLastName();
//
//			if (newId.equals(foundId)) {
//				return medicalRepository.save(foundRecord);
//			} else {
//				String oldFirstName = foundRecord.getFirstName();
//				String oldLastName = foundRecord.getLastName();
//
//				String newFirstName = newRecord.getFirstName();
//				String newLastName = newRecord.getLastName();
//				
//				if(!canCreateMedical(newFirstName, newLastName)) {
//					//throw exception
//				}
//				
//				// medical name changed, so detached object medical from person
//				Person person = personRepository.findByFirstNameAndLastName(oldFirstName, oldLastName);
//				person.setMedicalRecord(null);
//				personRepository.save(person);
//
//				foundRecord.setFirstName(newRecord.getFirstName());
//				foundRecord.setLastName(newRecord.getLastName());
//
//				return medicalRepository.save(foundRecord);
//			}
//		}).orElseThrow(() -> {
//			logger.info("id=" + id + " not found");
//			return new MedicalRecordNotFoundException("id=" + id + " not found");
//		});
		
		MedicalRecord foundRecord = medicalRepository.findOneById(id);
		if(foundRecord==null) {
			throw new MedicalRecordNotFoundException("id{"+id+"} not found in table MedicalRecord");
		}
		
		foundRecord.setAllergies(newRecord.getAllergies());
		foundRecord.setMedications(newRecord.getMedications());
		foundRecord.setBirthdate(newRecord.getBirthdate());
		String newId = newRecord.getFirstName() + newRecord.getLastName();
		String foundId = foundRecord.getFirstName() + foundRecord.getLastName();

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
			Person person = personRepository.findByFirstNameAndLastName(oldFirstName, oldLastName);
			person.setMedicalRecord(null);
			personRepository.save(person);

			foundRecord.setFirstName(newRecord.getFirstName());
			foundRecord.setLastName(newRecord.getLastName());

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

	public MedicalRecord deleteMedicalRecord(MedicalRecord record) throws MedicalRecordNotFoundException {
		Long id = record.getId();
		if (id != null) {
			dettachMedicalFromPerson(id);
			medicalRepository.deleteById(id);
		} else {
			checkFirstNameAndLastName(record);
			MedicalRecord foundRecord = medicalRepository.findByFirstNameAndLastName(record.getFirstName(),
					record.getLastName());
			dettachMedicalFromPerson(foundRecord.getId());
			medicalRepository.delete(foundRecord);
		}
		return record;
	}
	
	private void dettachMedicalFromPerson(Long medicalId) {
		Person person = personRepository.findByMedicalId(medicalId.toString());
		person.setMedicalRecord(null);
		personRepository.save(person);
	}

}
