package com.parkit.parkingsystem.service;

import java.util.Date;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        Date inHour = ticket.getInTime();
        Date outHour = ticket.getOutTime();
        long diff = outHour.getTime() - inHour.getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        double duration;
        double d_minutes = (int) (diff / (1000*60));
        //30 minutes gratuites
       
         if (d_minutes <30) {
			duration =0;
		}
         else {
        	 duration = d_minutes/60;
         }
      

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}