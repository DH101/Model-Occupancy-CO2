/*
 * Student Name: Dean Hunter
 * 
 * Student No: 160274565
 * 
 * Purpose: Be able to read any metric data from any room that has sensors inside the Urban Sciences Building. Also, be able to get metric data from zones in a room which contain 
 * a collection of sensors monitored by a controller.
 */

package modellingoccupancybasedonco2;

import java.io.BufferedReader;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class ReadMetricDataFromRoom {
	
	private String roomNumber;
	
	private String metric;
	
	private String roomZone;
	
	private String apiKey;
	
	public ReadMetricDataFromRoom() {
		
		
		
	}
	
	public ReadMetricDataFromRoom(String roomNumber, String metric) {
		
		this.setRoomNumber(roomNumber);
		
		this.setMetric(metric);
		
	}
	
	public ReadMetricDataFromRoom(String roomNumber, String metric, String apiKeyOrRoomZone, boolean chooseApiKeyOrRoomZone) {
		
		this.setRoomNumber(roomNumber);
		
		this.setMetric(metric);
		
		if(chooseApiKeyOrRoomZone == true) {
			
			this.setApiKey(apiKeyOrRoomZone);
			
		} else {
			
			this.setRoomZone(apiKeyOrRoomZone);
			
		}
		
	}
	
	public ReadMetricDataFromRoom(String roomNumber, String metric, String roomZone, String apiKey) {
		
		this.setRoomNumber(roomNumber);
		
		this.setMetric(metric);
	
		this.setRoomZone(roomZone);
		
		this.setApiKey(apiKey);
		
	}

	public String getRoomNumber() {
		
		return roomNumber;
		
	}

	public void setRoomNumber(String roomNumber) {
		
		this.roomNumber = roomNumber;
		
	}

	public String getMetric() {
		
		return metric;
		
	}

	public void setMetric(String metric) {
		
		this.metric = metric;
		
	}

	public String getRoomZone() {
		
		return roomZone;
		
	}

	public void setRoomZone(String roomZone) {
		
		this.roomZone = roomZone;
		
	}
	
	public String getApiKey() {
		
		return apiKey;
		
	}

	public void setApiKey(String apiKey) {
		
		this.apiKey = apiKey;
		
	}
	
	// String is the metric, Double is metric value
	public HashMap<String, Double> getMetricDataFromRoom() throws MalformedURLException, IOException {
		
		return getParseUrbanSciencesBuildingJSONForAverageOfMetricsForRoom(this.getURLForApiForRoom());
		
	}
	
	private HashMap<String, Double> getParseUrbanSciencesBuildingJSONForAverageOfMetricsForRoom(String api) throws MalformedURLException, IOException {
		
		return parseUrbanSciencesBuildingJSONForAverageOfMetricsForRoom(createAndReadURL(api));
		
	}
	
	private HashMap<String, Double> parseUrbanSciencesBuildingJSONForAverageOfMetricsForRoom(BufferedReader dataFromApi) {
		
		HashMap<String, HashMap<String, Double>> roomZones = this.parseUrbanSciencesBuildingJSONForMetrics(dataFromApi);
		
		HashMap<String, Double> room = new HashMap<String, Double>();
		
		HashMap<String, ArrayList<Double>> allRoomData = new HashMap<String, ArrayList<Double>>();
		
		Iterator<Entry<String, HashMap<String, Double>>> iterateThroughRoomZones = roomZones.entrySet().iterator();
		
		while(iterateThroughRoomZones.hasNext()) {
			
			Entry<String, HashMap<String, Double>> zoneAndMetric = iterateThroughRoomZones.next();
			
			HashMap<String, Double> metrics = zoneAndMetric.getValue();
			
			Iterator<Entry<String, Double>> iterateThroughMetrics = metrics.entrySet().iterator();
			
			while(iterateThroughMetrics.hasNext()) {
				
				Entry<String, Double> metricData = iterateThroughMetrics.next();
				
				String metric = metricData.getKey();
				
				Double value = metricData.getValue();
				
				this.addToMetricListOfMetric(allRoomData, metric, value);
				
			}
			
		}
		
		room = this.getAveragesForMetric(allRoomData);
		
		return room;
		
	}
	
	// String is room zone, then the HashMap is the metric, like above.
	public HashMap<String, HashMap<String, Double>> getMetricDataFromAllRoomZones() throws MalformedURLException, IOException {
		
		return getParseUrbanSciencesBuildingJSONForMetrics(this.getURLForApiForRoom());
		
	}
	
	private String getURLForApiForRoom() {
		
		return "https://api.usb.urbanobservatory.ac.uk/api/v2/sensors/entity?meta:roomNumber=" + this.getRoomNumber() + "&metric=" + this.getMetric();
		
	}
	
	public HashMap<String, HashMap<String, Double>> getMetricDataFromRoomThatNeedsApiKey() throws MalformedURLException, IOException{
		
		return getParseUrbanSciencesBuildingJSONForMetrics(this.getURLForApiForRoomThatNeedsKey());
		
	}
	
	private String getURLForApiForRoomThatNeedsKey() {
		
		return "https://api.usb.urbanobservatory.ac.uk/api/v2/sensors/entity?meta:roomNumber=" + this.getRoomNumber() + "&metric=" + this.getMetric() + "&apiKey=" + this.getApiKey();
		
	}
	
	public HashMap<String, HashMap<String, Double>> getMetricDataFromRoomZone() throws MalformedURLException, IOException {
		
		return getParseUrbanSciencesBuildingJSONForMetrics(this.getURLForApiForRoomZone());
		
	}
	
	private String getURLForApiForRoomZone() {
		
		return "https://api.usb.urbanobservatory.ac.uk/api/v2/sensors/entity?meta:roomNumber=" + this.getRoomNumber() + "&meta:roomZone=" + this.getRoomZone() + "&metric=" +  this.getMetric();
		
	}
	
	public HashMap<String, HashMap<String, Double>> getMetricDataFromRoomZoneThatNeedsApiKey() throws MalformedURLException, IOException{
		
		return getParseUrbanSciencesBuildingJSONForMetrics(this.getURLForApiForRoomZoneThatNeedsKey());
		
	}
	
	private String getURLForApiForRoomZoneThatNeedsKey() {
		
		return "https://api.usb.urbanobservatory.ac.uk/api/v2/sensors/entity?meta:roomNumber=" + this.getRoomNumber() + "&meta:roomZone=" + this.getRoomZone() + "&metric=" + this.getMetric() + "&apiKey=" + this.getApiKey();
		
	}
	
	private HashMap<String, HashMap<String, Double>> getParseUrbanSciencesBuildingJSONForMetrics(String api) throws MalformedURLException, IOException{
		
		return parseUrbanSciencesBuildingJSONForMetrics(createAndReadURL(api));
		
	}
	
	private BufferedReader createAndReadURL(String api) throws MalformedURLException, IOException {
		
		URL urlAPI = new URL(api);
		
		BufferedReader dataFromAPI = new BufferedReader(new InputStreamReader(urlAPI.openStream()));
		
		return dataFromAPI;
		
	}
	
	
	private HashMap<String, HashMap<String, Double>> parseUrbanSciencesBuildingJSONForMetrics(BufferedReader dataFromApi) {
		
		HashMap<String, HashMap<String, Double>> roomZonesData = new HashMap<String, HashMap<String, Double>>();
		
		JsonObject jsonOfRoomData = new JsonParser().parse(dataFromApi).getAsJsonObject();
		
		JsonArray items = jsonOfRoomData.get("items").getAsJsonArray();
		
		if(items.isJsonArray()) {
			
			for(int i = 0; i < items.size(); i++) {
				
				JsonObject item = items.get(i).getAsJsonObject();
				
				JsonObject meta = item.getAsJsonObject("meta");
				
				JsonElement elementRoomZone = meta.get("roomZone");
				
				String roomZone = elementRoomZone == null ? "does not exist or no number for zone" : elementRoomZone.getAsString();
				
				JsonArray feedsArray = item.get("feed").getAsJsonArray();
				
				HashMap<String, ArrayList<Double>> roomZoneDuplicateData = new HashMap<String, ArrayList<Double>>();
	    		
				if(feedsArray.isJsonArray()) {
					
					for(int j = 0; j < feedsArray.size(); j++) {
					
						JsonObject feeds = feedsArray.get(j).getAsJsonObject();
						
						JsonElement elementMetric = feeds.get("metric");
						
						String metric = elementMetric.getAsString();
						
						JsonArray timeSeriesArray = feeds.get("timeseries").getAsJsonArray();
						
						if(timeSeriesArray.isJsonArray()) {
							
							for(int k = 0; k < timeSeriesArray.size(); k++) {
								
								JsonObject timeSeries = timeSeriesArray.get(k).getAsJsonObject();
								
								JsonElement latest = timeSeries.get("latest");
								
								if(latest.isJsonObject()) {
									
									JsonObject latestObject = latest.getAsJsonObject();
									
									JsonElement elementValue = latestObject.get("value");
									
									Double value = elementValue.getAsDouble();
									
									this.addToMetricListOfMetric(roomZoneDuplicateData, metric, value);
									
								}
								
							}
							
						}
					
					}
					
				}
				
				roomZonesData.put("Zone " + roomZone, this.getAveragesForMetric(roomZoneDuplicateData));
				
			}
			
		}
		
		return roomZonesData;
		
	}
	
	private synchronized void addToMetricListOfMetric(HashMap<String, ArrayList<Double>> roomZoneData, String metric, Double value ) {
		
		ArrayList<Double> metricList = roomZoneData.get(metric);
		
		if(metricList == null) {
			
			metricList = new ArrayList<Double>();
			
			metricList.add(value);
			
			roomZoneData.put(metric, metricList);
			
		} else {
			
			metricList.add(value);
			
		}
		
	}
	
	private HashMap<String, Double> getAveragesForMetric(HashMap<String, ArrayList<Double>> roomZone) {
		
		HashMap<String, Double> metric = new HashMap<String, Double>();
		
		Iterator<Entry<String, ArrayList<Double>>> iterateThroughRoomZone = roomZone.entrySet().iterator();
		
		while(iterateThroughRoomZone.hasNext()) {
			
			Entry<String, ArrayList<Double>> metricAndItsValues = (Entry<String, ArrayList<Double>>) iterateThroughRoomZone.next();
			
			Double averageOfMetric = calculateAverage((List<Double>) metricAndItsValues.getValue());
			
			metric.put((String) metricAndItsValues.getKey(), averageOfMetric);
			
		}
		
		return metric;
		
	}
	
	private double calculateAverage(List<Double> listOfNumbers) {
		
		Double sum = 0.0;
		
		if(!listOfNumbers.isEmpty()) {
			
			for(int l = 0; l < listOfNumbers.size(); l++) {
				
				sum += listOfNumbers.get(l);
			}
			
			return sum / listOfNumbers.size();
			
		} else if (listOfNumbers.size() == 1) {
			
			sum = listOfNumbers.get(0);
			
		}
		
		return sum;
		
	}
	
}
