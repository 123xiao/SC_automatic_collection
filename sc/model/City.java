package com.sc.sc.model;

public class City {
	private String id;

	private String citynameorg;

	private String citycodeorg;

	private String citynamedes;

	private String citycodedes;

	public String getCitycodedes() {
		return citycodedes;
	}

	public String getCitycodeorg() {
		return citycodeorg;
	}

	public String getCitynamedes() {
		return citynamedes;
	}

	public String getCitynameorg() {
		return citynameorg;
	}

	public String getId() {
		return id;
	}

	public void setCitycodedes(String citycodedes) {
		this.citycodedes = citycodedes == null ? null : citycodedes.trim();
	}

	public void setCitycodeorg(String citycodeorg) {
		this.citycodeorg = citycodeorg == null ? null : citycodeorg.trim();
	}

	public void setCitynamedes(String citynamedes) {
		this.citynamedes = citynamedes == null ? null : citynamedes.trim();
	}

	public void setCitynameorg(String citynameorg) {
		this.citynameorg = citynameorg == null ? null : citynameorg.trim();
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", citynameorg=" + citynameorg + ", citycodeorg=" + citycodeorg + ", citynamedes="
				+ citynamedes + ", citycodedes=" + citycodedes + "]";
	}
}