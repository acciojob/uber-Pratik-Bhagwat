package com.driver.services.impl;

import com.driver.model.*;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Customer customer = customerRepository2.findById(customerId).get();
		customerRepository2.delete(customer);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		Customer customer = customerRepository2.findById(customerId).get();
		TripBooking bookTrip = new TripBooking();
		try{
			List<Driver> driverList = driverRepository2.findAll();
			for (Driver d: driverList) {
				if(d.getCab().isAvailable()) {
					bookTrip.setCustomer(customer);
					bookTrip.setDriver(d);
					bookTrip.setFromLocation(fromLocation);
					bookTrip.setToLocation(toLocation);
					bookTrip.setDistanceInKm(distanceInKm);
					bookTrip.setStatus(TripStatus.CONFIRMED);

					//?adding bookingList to driver's booking list
					d.getTripBookingList().add(bookTrip);
					d.getCab().setAvailable(false);
					driverRepository2.save(d);
				}
			}
			customer.getTripBookingList().add(bookTrip);
			customerRepository2.save(customer);
			return bookTrip;
		}
		catch (Exception e) {
			throw new Exception("No cab available!");
		}
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly

		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		tripBooking.setBill(0);
		tripBooking.setDistanceInKm(0);
		tripBooking.setToLocation(null);
		tripBooking.setFromLocation(null);
		tripBooking.getDriver().getCab().setAvailable(true);
		tripBooking.setStatus(TripStatus.CANCELED);

		//?saving the data to database.
		tripBookingRepository2.save(tripBooking);
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking bookTrip = tripBookingRepository2.findById(tripId).get();
		bookTrip.setStatus(TripStatus.COMPLETED);
		int bill = bookTrip.getDriver().getCab().getPerKmRate() * bookTrip.getDistanceInKm();
		bookTrip.setBill(bill);
		bookTrip.getDriver().getCab().setAvailable(true);
		tripBookingRepository2.save(bookTrip);
	}
}
