package com.aureole.watano.eclipse;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.eclipse.internal.provisional.equinox.p2.jarprocessor.JarProcessorExecutor;

import com.aureole.watano.util.collections.AutoIterator;
import com.aureole.watano.util.collections.MultiValueMap;
import com.aureole.watano.util.io.FileUtil;


public class EclipseUtil {
	private static final Logger LOG = Logger.getLogger(EclipseUtil.class);

	protected static void addNewBundle(MultiValueMap<String, BundleInfo> oldBIs, MultiValueMap<String, BundleInfo> newBIs, MultiValueMap<String, BundleInfo> pendBIs, String bname){
		BundleInfo oldb = oldBIs.getValue(bname, 0);
		BundleInfo newb = newBIs.getValue(bname, 0);
		if(newb != null && oldb != null &&  new BundleInfoVersionComparator().compare(newb, oldb)>0){
			LOG.debug("update "+oldb.getSymbolicName() +" from "+oldb.getBundleVersion() +" to "+newb.getBundleVersion());
			newb.insert(pendBIs);
			if(CollectionUtils.isNotEmpty(newb.get_RequireBundle())){
				for(RequireBundle rb: newb.get_RequireBundle()){
					addNewBundle(oldBIs, newBIs, pendBIs, rb.getName());
				}
			}
		}
	}
	public static void update(EclipseInfo oldEclipse, EclipseInfo newEclipse, String copyDir){
		MultiValueMap<String, BundleInfo> oldBIs = oldEclipse.getBundleInfos();
		MultiValueMap<String, BundleInfo> newBIs = newEclipse.getBundleInfos();
		MultiValueMap<String, BundleInfo> pendBIs = new MultiValueMap<String, BundleInfo>();
		for(Object bname: oldBIs.keySet()){
			addNewBundle(oldBIs, newBIs, pendBIs, (String)bname);
		}

		pendBIs.sortValues(new BundleInfoVersionComparator(), true);
		for(String bname: pendBIs.keySet()){
			BundleInfo bundle = pendBIs.getValue(bname, 0);
			switch(bundle.get_Type()){
			case BundleInfo.Type_HttpFile:
			case BundleInfo.Type_LocalFile:
				String outFileName = FileUtil.extractFile(bundle.get_Path(), copyDir);
				if(bundle.get_Path().endsWith(".pack.gz") || bundle.get_Path().endsWith(".pack")){
					JarProcessorExecutor.Options options = new JarProcessorExecutor.Options();
					options.unpack = true;
					options.outputDir = copyDir;
					options.input = new File(outFileName);
					new JarProcessorExecutor().runJarProcessor(options);
					FileUtils.deleteQuietly(options.input);
				}
				break;
			case BundleInfo.Type_Normal:
			default:
				FileUtil.copyFile(bundle.get_Path(), copyDir);
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void delDupPlugins(EclipseInfo eclipseinfo, String[] excludes){
		String[] _excludes = excludes;
		if(_excludes == null){
			_excludes = new String[]{};
		}
		MultiValueMap<String, BundleInfo> mapbi = eclipseinfo.getBundleInfos();
		AutoIterator<String> aimapbi = new AutoIterator<String>(mapbi);
		while(aimapbi.move() && !ArrayUtils.contains(_excludes, aimapbi.getValue())){
			AutoIterator<BundleInfo> aiarrBI = new AutoIterator<BundleInfo>((ArrayList<BundleInfo>)aimapbi.getMapValue());
			while(aiarrBI.move() && aiarrBI.getIndex() > 1){
				FileUtil.deleteFile(aiarrBI.getValue().get_Path());
			}
		}
		eclipseinfo.setBundleInfos(mapbi);
	}

	public static void check(EclipseInfo eclipseinfo){
		eclipseinfo.sortByVersion();
		MultiValueMap<String, BundleInfo> mapbi = eclipseinfo.getBundleInfos();
		for(String bname: mapbi.keySet()){
			BundleInfo b = mapbi.getValue(bname,0);
			if(b != null && b.get_RequireBundle() != null && b.get_RequireBundle().size() > 0){
				for(RequireBundle rb:b.get_RequireBundle()){
					BundleInfo requireBundle = mapbi.getValue(rb.getName(), 0);
					if(requireBundle != null && rb.compareVersion(requireBundle.getBundleVersion())){

					}else{
						System.err.println("Require Bundle:"+rb.getName()+"-->"+rb.getVersion());
					}
				}
			}
		}
	}

	public static void findIsolated(EclipseInfo eclipseinfo){
		eclipseinfo.sortByVersion();
		MultiValueMap<String, BundleInfo> used = new MultiValueMap<String, BundleInfo>();
		MultiValueMap<String, BundleInfo> mapbi = eclipseinfo.getBundleInfos();
		for(String bname: mapbi.keySet()){
			BundleInfo bundle = mapbi.getValue(bname, 0);
			if(bundle.get_RequireBundle() != null && bundle.get_RequireBundle().size() > 0){
				for(RequireBundle rb:bundle.get_RequireBundle()){
					ArrayList<BundleInfo> requireBundles = mapbi.get(rb.getName());
					if(requireBundles != null && requireBundles.size() > 0){
						for(BundleInfo usedrb:requireBundles){
							usedrb.insert(used);
						}
					}
				}
			}
		}

		for(Object bname: mapbi.keySet()){
			if(used.get(bname) == null){
				System.err.println(bname);
			}
		}
	}

	public static void main(String[] args) {
		try {
			//			EclipseInfo lastest = new EclipseInfo();
			//			lastest.parseHome("e:\\work\\devtools\\springsource\\sts-2.3.1.RELEASE");
			//			lastest.parseHome("e:\\work\\devtools\\jee_eclipse\\eclipse\\");
			//			lastest.parseUpdateSite("e:\\download\\JBossTools-Update-3.1.0.v201003050540R-H56-GA.zip");
			//			List<String> urls = FileUtils.readLines(new File("updatesites.txt"), "utf-8");
			//			for(String url: urls){
			//				lastest.parseURL(url);
			//			}

			EclipseInfo jdt = new EclipseInfo()
			.parseHome("e:\\work\\devtools\\eclipse");
			//			EclipseUtil.check(jdt);
			EclipseUtil.findIsolated(jdt);
			//			EclipseUtil.update(jdt, lastest, "e:\\work\\devtools\\eclipse\\dropins\\");
			//			EclipseUtil.delDupPlugins(jdt, new String[]{});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
