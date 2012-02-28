package com.aureole.watano.eclipse;


public class RequireBundle {
	private String name;
	private String version;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public boolean compareVersion(String bundleVersion) {
		return VersionComparator.in(null, bundleVersion) == 0;
	}
}
