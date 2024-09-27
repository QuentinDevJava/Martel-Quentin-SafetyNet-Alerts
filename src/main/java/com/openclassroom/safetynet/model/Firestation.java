package com.openclassroom.safetynet.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "address", "station" })

public class Firestation {

	@JsonProperty("address")
	private String address;
	@JsonProperty("station")
	private String station;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Firestation that = (Firestation) o;
		return Objects.equals(address, that.address) && Objects.equals(station, that.station);
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, station);
	}

	@Override
	public String toString() {
		return "Firestation{" + "address='" + address + '\'' + ", station='" + station + '\'' + '}';
	}
}
