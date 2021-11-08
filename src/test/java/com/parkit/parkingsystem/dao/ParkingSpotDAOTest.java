package com.parkit.parkingsystem.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

class ParkingSpotDAOTest {

	private static ParkingSpotDAO parkingSpotDAO;
	private static ParkingSpot parkingSpot;
	@Test
	void testGetNextAvailableSlot() {
		parkingSpotDAO = new ParkingSpotDAO();
		assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(2);
		assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).isEqualTo(4);
	}
	@Test
	void testUpdateParking() {
		 parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		assertTrue(parkingSpotDAO.updateParking(parkingSpot));
	}
	@Test
	void testUpdate2Parking() {
		 parkingSpot = new ParkingSpot(2, ParkingType.CAR, true);
		assertTrue(parkingSpotDAO.updateParking(parkingSpot));
	}

}
