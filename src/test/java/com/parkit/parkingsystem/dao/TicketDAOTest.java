package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

class TicketDAOTest {

	private static Ticket ticket;
	 private static TicketDAO ticketDAO;
	 private static ParkingSpot parkingSpot;
	 @BeforeAll
	  public static void TestsetUpTicket() {
		 ticketDAO = new TicketDAO();
		 ticket = new Ticket();
		 parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
	 }
		@Test
		void testSaveTicket() {
			
			ticket.setId(1);
			ticket.setParkingSpot(parkingSpot);
			ticket.setVehicleRegNumber("v1");
			
			Date inTime = new Date();
			ticket.setInTime(inTime);
			
			Date outTime = null;
			ticket.setOutTime(outTime);
			double price=0.0;
			ticket.setPrice(price);
			
			assertFalse(ticketDAO.saveTicket(ticket));
		}

}
