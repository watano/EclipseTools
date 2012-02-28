package com.aureole.watano.eclipse;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.aureole.watano.util.collections.AutoIterator;
import com.aureole.watano.util.collections.MultiValueMap;

public class BundleInfo {
	private static final Logger LOG = Logger.getLogger(BundleInfo.class);

	public static final int Type_Normal = 10;
	public static final int Type_LocalFile = 20;
	public static final int Type_HttpFile = 30;
	private String ManifestVersion;
	private String BundleManifestVersion;
	private String BundleName;
	private String BundleSymbolicName;
	private String BundleVersion;
	private String BundleActivator;
	private String BundleVendor;
	private String BundleLocalization;
	private String RequireBundle;
	private String ExportPackage;
	private String EclipseAutoStart;
	private String BundleClassPath;
	private String _Localization;
	private String _Manifest;
	private String _Path;
	private List<RequireBundle> _RequireBundle;
	private int _Type = Type_Normal;

	public static BundleInfo create(File f){
		BundleInfo bundleInfo = new BundleInfo();
		bundleInfo.set_Path(f.getPath());

		Manifest m = ManifestUtil.getManifest(f);
		if(m != null){
			Attributes a = m.getMainAttributes();
			AutoIterator<Object> aiAtrrs = new AutoIterator<Object>(a);
			while(aiAtrrs.move()){
				setProperty(bundleInfo, StringUtils.uncapitalize(aiAtrrs.getValue().toString().replace("-","")), aiAtrrs.getMapValue());
			}
		}

		List<RequireBundle> requireBundles = new ArrayList<RequireBundle>();

		//_RequireBundle
		String requireBundleText = bundleInfo.getRequireBundle();
		if(StringUtils.isNotBlank(requireBundleText)){
			AutoIterator<String> aiRbTexts = new AutoIterator<String>(requireBundleText, ",");
			while(aiRbTexts.move()){
				AutoIterator<String> aiRbInfo = new AutoIterator<String>(aiRbTexts.getValue(), ";");
				RequireBundle rb = null;
				while(aiRbInfo.move()){
					String rbText = aiRbInfo.getValue().trim();
					if(aiRbInfo.getIndex() == 0){
						rb = new RequireBundle();
						rb.setName(rbText);
					}
					if(rbText.startsWith("bundle-version=")){
						rb.setVersion(rbText.substring("bundle-version=".length()));
						break;
					}
				}
				if(rb != null){
					requireBundles.add(rb);
				}
			}
			bundleInfo.setRequireBundle(requireBundleText);
		}else{
			LOG.debug(f.getName() +"没有对应的RequireBundles!");
		}

		//TODO parse plugin.xml
		//		InputStream inputPluginXml = FileUtil.getInputStream(f.getPath(), "plugin.xml");
		//		if(inputPluginXml != null){
		//			DocumentBuilder builder;
		//			try {
		//				builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		//				Document document = builder.parse(inputPluginXml);
		//				XPath xpath = XPathFactory.newInstance().newXPath();
		//				NodeListIterator nodeListIterator = XpathUtil.items(xpath, "plugin/requires/import/", document);
		//				while(nodeListIterator.hasNext()){
		//					Node node = nodeListIterator.next();
		//					RequireBundle rb = new RequireBundle();
		//					rb.setName(DomUtil.getAttribute(node, "plugin"));
		//					requireBundles.add(rb);
		//				}
		//			} catch (Exception e) {
		//				e.printStackTrace();
		//			}
		//		}
		bundleInfo.set_RequireBundle(requireBundles);

		if(StringUtils.isBlank(bundleInfo.getBundleSymbolicName())){
			if(f.isDirectory()){
				bundleInfo.setBundleSymbolicName(f.getName());
			}else if(f.isFile() && (f.getName().trim().endsWith(".jar") || f.getName().trim().endsWith(".zip"))){
				bundleInfo.setBundleSymbolicName(f.getName().trim().substring(0, f.getName().trim().length() - 4));
			}else{
				LOG.debug("不能解析"+f.getName() +"的BundleSymbolicName!");
				return null;
			}
		}
		return bundleInfo;
	}

	private static void setProperty(Object obj, String name, Object value){
		try {
			BeanUtils.setProperty(obj, name, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public String getSymbolicName(){
		if(BundleSymbolicName != null){
			if(BundleSymbolicName.indexOf(";") > 0){
				return BundleSymbolicName.substring(0, BundleSymbolicName.indexOf(";"));
			}else{
				return BundleSymbolicName;
			}
		}
		return null;
	}

	public void insert(MultiValueMap<String, BundleInfo> bundles){
		bundles.putValue(getBundleName(), this);
	}

	public String getManifestVersion() {
		return ManifestVersion;
	}

	public void setManifestVersion(String manifestVersion) {
		ManifestVersion = manifestVersion;
	}

	public String getBundleManifestVersion() {
		return BundleManifestVersion;
	}

	public void setBundleManifestVersion(String bundleManifestVersion) {
		BundleManifestVersion = bundleManifestVersion;
	}

	public String getBundleName() {
		return BundleName;
	}

	public void setBundleName(String bundleName) {
		BundleName = bundleName;
	}

	public String getBundleSymbolicName() {
		return BundleSymbolicName;
	}

	public void setBundleSymbolicName(String bundleSymbolicName) {
		BundleSymbolicName = bundleSymbolicName;
	}

	public String getBundleVersion() {
		return BundleVersion;
	}

	public void setBundleVersion(String bundleVersion) {
		BundleVersion = bundleVersion;
	}

	public String getBundleActivator() {
		return BundleActivator;
	}

	public void setBundleActivator(String bundleActivator) {
		BundleActivator = bundleActivator;
	}

	public String getBundleVendor() {
		return BundleVendor;
	}

	public void setBundleVendor(String bundleVendor) {
		BundleVendor = bundleVendor;
	}

	public String getBundleLocalization() {
		return BundleLocalization;
	}

	public void setBundleLocalization(String bundleLocalization) {
		BundleLocalization = bundleLocalization;
	}

	public String getRequireBundle() {
		return RequireBundle;
	}

	public void setRequireBundle(String requireBundle) {
		RequireBundle = requireBundle;
	}

	public String getExportPackage() {
		return ExportPackage;
	}

	public void setExportPackage(String exportPackage) {
		ExportPackage = exportPackage;
	}

	public String getEclipseAutoStart() {
		return EclipseAutoStart;
	}

	public void setEclipseAutoStart(String eclipseAutoStart) {
		EclipseAutoStart = eclipseAutoStart;
	}

	public String getBundleClassPath() {
		return BundleClassPath;
	}

	public void setBundleClassPath(String bundleClassPath) {
		BundleClassPath = bundleClassPath;
	}

	public String get_Localization() {
		return _Localization;
	}

	public void set_Localization(String _Localization) {
		this._Localization = _Localization;
	}

	public String get_Manifest() {
		return _Manifest;
	}

	public void set_Manifest(String _Manifest) {
		this._Manifest = _Manifest;
	}

	public String get_Path() {
		return _Path;
	}

	public void set_Path(String _Path) {
		this._Path = _Path;
	}

	public List<RequireBundle> get_RequireBundle() {
		return _RequireBundle;
	}

	public void set_RequireBundle(List<RequireBundle> _RequireBundle) {
		this._RequireBundle = _RequireBundle;
	}

	public int get_Type() {
		return _Type;
	}

	public void set_Type(int _Type) {
		this._Type = _Type;
	}
}
