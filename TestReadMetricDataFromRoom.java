/*
 * Student Name: Dean Hunter
 * 
 * Student No: 160274565
 * 
 * Purpose: Be able to read any metric data from any room that has sensors inside the Urban Sciences Building. Also, be able to get metric data from zones in a room which contain 
 * a collection of sensors monitored by a controller.
 */

package modellingoccupancybasedonco2;

import java.io.IOException;


import com.google.gson.JsonIOException;

import com.google.gson.JsonSyntaxException;

// Type of metrics Example
// CO2
// occupied (no double quotes)
// room%20temperature
// example: "occupied+\"room%20temperature\""

public class TestReadMetricDataFromRoom {
	
	public static void main(String[] args) throws JsonIOException, JsonSyntaxException, IOException, Exception {
		
		String roomNumber = "3.015";
		
		String co2Metric = "CO2";
	
		ReadMetricDataFromRoom test = new ReadMetricDataFromRoom(roomNumber, co2Metric);
	
		System.out.println("Room " + roomNumber + "\n");
		
		System.out.println(test.getMetricDataFromRoom());
		
			
	}		
		
}