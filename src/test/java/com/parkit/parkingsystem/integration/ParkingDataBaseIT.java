
package com.parkit.parkingsystem.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static Ticket ticket;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticket = new Ticket();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test public void testParkingACar() throws Exception{ 
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO,ticketDAO);
		parkingService.processIncomingVehicle();
		//TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
  ticket =ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber());
  assertThat(ticket.getVehicleRegNumber()).isEqualTo("ABCDEF");
  assertThat(ticket.getInTime()).isNotNull();
  assertThat(ticket.getOutTime()).isNull();
  assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(2);
  assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).isEqualTo(4);
  
  }

	@Test
	public void testParkingLotExit() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		ticket = ticketDAO.getTicket("ABCDEF");
		parkingService.processExitingVehicle();
		// TODO: check that the fare generated and out time are populated correctly in the database

		assertThat(ticket.getPrice()).isEqualTo(0);
		assertThat(ticket.getParkingSpot().getId()).isEqualTo(1);
		assertThat(ticket.getOutTime()).isNull();
	}

	@Test

	@DisplayName("Entrance then exit of a car two times, user become a recurring user")
	public void testParkingInAndOut2TimesToBecomeRecurringUser() throws Exception {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		parkingService.processIncomingVehicle();
		parkingService.processExitingVehicle();

		parkingService.processIncomingVehicle();
		parkingService.processExitingVehicle();

		// THEN

		assertThat(ticketDAO.getNextPassage(inputReaderUtil.readVehicleRegistrationNumber())).isEqualTo(2);
	}

}
