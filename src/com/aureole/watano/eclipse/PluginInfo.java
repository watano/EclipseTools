package com.aureole.watano.eclipse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;

import org.apache.commons.io.IOUtils;

import com.aureole.watano.util.xml.SimpleXMLReader;

public class PluginInfo {
	private String schemaVersion;
	private String pluginId;
	private String version;
	private String vendor;

	// an ordered list of library path names.
	private ArrayList<?> libraryPaths;
	// TODO Should get rid of the libraries map and just have a
	// list of library export statements instead.  Library paths must
	// preserve order.
	private Map<?,?> libraries; //represent the libraries and their export statement
	private ArrayList<?> requires;
	private boolean requiresExpanded = false; //indicates if the requires have been processed.
	private boolean compatibilityFound = false; //set to true is the requirement list contain compatilibity
	private String pluginClass;
	private String masterPluginId;
	private String masterVersion;
	private String masterMatch;
	private Set<?> filters;
	private String pluginName;
	private boolean singleton;
	private boolean fragment;
	private boolean hasExtensionExtensionPoints = false;

	@SuppressWarnings("unchecked")
	public static PluginInfo parsePluginXml(InputStream input){
		PluginInfo pluginInfo = new PluginInfo();
		new SimpleXMLReader<PluginInfo>(pluginInfo){
			@Override
			public void doStartElement(StartElement startElement, String tag) {
				System.out.print(tag+"\t");
				Iterator<Attribute> as = startElement.getAttributes();
				param.setFilters(null);
				while(as.hasNext()){
					Attribute a = as.next();
					if("extension".equals(tag) && "id".equals(a.getName())){

					}
					if("extension".equals(tag) && "point".equals(a.getName())){

					}
					System.out.print(a.getName() +"="+ a.getValue());
				}

				System.out.println("");
			}

			@Override
			public void doCharacters(Characters text) {
				if (!text.isWhiteSpace()) {
					//						System.out.println("\t" + text.getData());
				}
			}
		}.parse(input);

		return pluginInfo;
	}

	public static void main(String[] args) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("d:\\temp\\plugin.xml");
			PluginInfo.parsePluginXml(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(fis);
		}
	}

	@Override
	public String toString() {
		return "plugin-id: " + pluginId + "  version: " + version + " libraries: " + libraries + " class:" + pluginClass + " master: " + masterPluginId + " master-version: " + masterVersion + " requires: " + requires + " singleton: " + singleton; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
	}


	public String getSchemaVersion() {
		return schemaVersion;
	}


	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}


	public String getPluginId() {
		return pluginId;
	}


	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public String getVendor() {
		return vendor;
	}


	public void setVendor(String vendor) {
		this.vendor = vendor;
	}


	public ArrayList<?> getLibraryPaths() {
		return libraryPaths;
	}


	public void setLibraryPaths(ArrayList<?> libraryPaths) {
		this.libraryPaths = libraryPaths;
	}


	public Map<?, ?> getLibraries() {
		return libraries;
	}


	public void setLibraries(Map<?, ?> libraries) {
		this.libraries = libraries;
	}


	public ArrayList<?> getRequires() {
		return requires;
	}


	public void setRequires(ArrayList<?> requires) {
		this.requires = requires;
	}


	public boolean isRequiresExpanded() {
		return requiresExpanded;
	}


	public void setRequiresExpanded(boolean requiresExpanded) {
		this.requiresExpanded = requiresExpanded;
	}


	public boolean isCompatibilityFound() {
		return compatibilityFound;
	}


	public void setCompatibilityFound(boolean compatibilityFound) {
		this.compatibilityFound = compatibilityFound;
	}


	public String getPluginClass() {
		return pluginClass;
	}


	public void setPluginClass(String pluginClass) {
		this.pluginClass = pluginClass;
	}


	public String getMasterPluginId() {
		return masterPluginId;
	}


	public void setMasterPluginId(String masterPluginId) {
		this.masterPluginId = masterPluginId;
	}


	public String getMasterVersion() {
		return masterVersion;
	}


	public void setMasterVersion(String masterVersion) {
		this.masterVersion = masterVersion;
	}


	public String getMasterMatch() {
		return masterMatch;
	}


	public void setMasterMatch(String masterMatch) {
		this.masterMatch = masterMatch;
	}


	public Set<?> getFilters() {
		return filters;
	}


	public void setFilters(Set<?> filters) {
		this.filters = filters;
	}


	public String getPluginName() {
		return pluginName;
	}


	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}


	public boolean isSingleton() {
		return singleton;
	}


	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}


	public boolean isFragment() {
		return fragment;
	}


	public void setFragment(boolean fragment) {
		this.fragment = fragment;
	}


	public boolean isHasExtensionExtensionPoints() {
		return hasExtensionExtensionPoints;
	}


	public void setHasExtensionExtensionPoints(boolean hasExtensionExtensionPoints) {
		this.hasExtensionExtensionPoints = hasExtensionExtensionPoints;
	}
}
