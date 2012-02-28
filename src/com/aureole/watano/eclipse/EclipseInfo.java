package com.aureole.watano.eclipse;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.log4j.Logger;

import com.aureole.watano.util.collections.MultiValueMap;
import com.aureole.watano.util.io.FileIterator;
import com.aureole.watano.util.io.FileUtil;
import com.aureole.watano.util.xml.SimpleXMLReader2;

public class EclipseInfo {
	private static final Logger LOG = Logger.getLogger(EclipseInfo.class);
	public static final String[] BuildinBundles = new String[]{"system.bundle", "org.eclipse.equinox.p2.operations"};

	private MultiValueMap<String, BundleInfo> bundleInfos = null;

	public MultiValueMap<String, BundleInfo> getBundleInfos() {
		if(bundleInfos == null){
			bundleInfos = new MultiValueMap<String, BundleInfo>();
		}
		return bundleInfos;
	}

	protected void setBundleInfos(MultiValueMap<String, BundleInfo> bundleInfos) {
		this.bundleInfos = bundleInfos;
	}

	public EclipseInfo sortByVersion(){
		//sort BundleInfo by version
		getBundleInfos().sortValues(new BundleInfoVersionComparator(), false);
		return this;
	}

	public EclipseInfo parseHome(String home){
		long start = System.currentTimeMillis();
		File fEclipseHome = new File(home);
		if(fEclipseHome.exists() && fEclipseHome.isDirectory()){
			parseDir(new File(fEclipseHome.getPath()+File.separator+"dropins"), true);
			//			parseDir(new File(fEclipseHome.getPath()+File.separator+"features"), false);
			parseDir(new File(fEclipseHome.getPath()+File.separator+"plugins"), false);
		}

		LOG.debug("parse all Bundle["+getBundleInfos().size()+"] Info cost:"+(System.currentTimeMillis()-start)+"ms");
		return this;
	}

	public EclipseInfo parseDir(File dir, boolean recursiveSubDir){
		FileIterator fileIterator = new FileIterator(dir, recursiveSubDir);
		IteratorUtils.transformedIterator(fileIterator, new Transformer(){
			@Override public Object transform(Object input) {
				BundleInfo currBundle = BundleInfo.create((File) input);
				if(currBundle != null){
					currBundle.set_Type(BundleInfo.Type_Normal);
					return currBundle;
				}
				return null;
			}});
		while(fileIterator.hasNext()){
			File f = fileIterator.next();
			BundleInfo currBundle = BundleInfo.create(f);
			if(currBundle != null){
				currBundle.set_Type(BundleInfo.Type_Normal);
				currBundle.insert(getBundleInfos());
			}
		}
		return this;
	}

	public EclipseInfo parseURL(String... urls){
		for(String url: urls){
			InputStream is = null;
			try {
				if(!url.endsWith("/")){
					url = url +"/";
				}
				Hashtable<String,String> valueMap = new Hashtable<String,String>();
				valueMap.put("repoUrl", url);
				valueMap.put("_Type", String.valueOf(BundleInfo.Type_HttpFile));
				is = FileUtil.getContentInputStream("jar:"+url+"artifacts.jar!artifacts.xml");
				parseArtifactsFile(is, valueMap);
			} catch (Throwable e) {
				e.printStackTrace();
			}finally{
				IOUtils.closeQuietly(is);
			}
		}
		return this;
	}

	public EclipseInfo parseUpdateSite(String... files){
		for(String file: files){
			InputStream is = null;
			try {
				file = FileUtil.getVfsFileName(file);
				Hashtable<String,String> valueMap = new Hashtable<String,String>();
				valueMap.put("repoUrl", "zip:"+file+"!");
				valueMap.put("_Type", String.valueOf(BundleInfo.Type_LocalFile));

				is = FileUtil.getContentInputStream("jar:zip:"+file+"!artifacts.jar!artifacts.xml");
				parseArtifactsFile(is, valueMap);
			} catch (Throwable e) {
				e.printStackTrace();
			}finally{
				IOUtils.closeQuietly(is);
			}
		}
		return this;
	}

	private void parseArtifactsFile(InputStream is, Hashtable<String,String> valueMap){
		try {
			long start = System.currentTimeMillis();
			new SimpleXMLReader2<MultiValueMap<String, BundleInfo>, Hashtable<String,String>>(getBundleInfos(), valueMap){
				private List<String[]> rules = new ArrayList<String[]>();
				private BundleInfo currBundle = null;

				@Override
				public void doStartElement(StartElement startElement, String tag) {
					if("property".equals(tag)){
						param.b.put(attrByName(startElement, "name"), attrByName(startElement, "value"));
					}else if("rule".equals(tag)){
						rules.add(new String[]{attrByName(startElement, "filter") , attrByName(startElement, "output")});
					}else if("artifact".equals(tag)){
						currBundle = new BundleInfo();
						currBundle.setBundleSymbolicName(attrByName(startElement, "id"));
						currBundle.setBundleVersion(attrByName(startElement, "version"));

						param.b.put("id", attrByName(startElement, "id"));
						param.b.put("version", attrByName(startElement, "version"));
						param.b.put("classifier", attrByName(startElement, "classifier"));
					}else if("rule".equals(tag)){
						rules.add(new String[]{attrByName(startElement, "filter") , attrByName(startElement, "output")});
					}else if("rule".equals(tag)){
						rules.add(new String[]{attrByName(startElement, "filter") , attrByName(startElement, "output")});
					}else if("rule".equals(tag)){
						rules.add(new String[]{attrByName(startElement, "filter") , attrByName(startElement, "output")});
					}
				}

				@Override
				public void doEndElement(EndElement endElement, String tag) {
					if("property".equals(tag)){
					}else if("rule".equals(tag)){
					}else if("artifact".equals(tag)){
						currBundle = new BundleInfo();
						for(String[] rule:rules){
							if(rule[0].indexOf("(classifier="+param.b.get("classifier")+")") > 0 &&
									rule[0].indexOf("(format="+param.b.get("format")+")") > 0 ){
								String out = StringUtils.replace(rule[1], "${repoUrl}/", "${repoUrl}");
								out = StrSubstitutor.replace(out, param.b);
								currBundle.setBundleSymbolicName(param.b.get("id"));
								currBundle.setBundleVersion(param.b.get("version"));
								currBundle.set_Path(out);
								currBundle.set_Type(Integer.parseInt(param.b.get("_Type")));
								//								System.err.println(param.b.get("id")+"--"+param.b.get("version")+"["+param.b.get("classifier")+"]["+param.b.get("format")+"]--"+out);
								param.b.remove("id");
								param.b.remove("version");
								param.b.remove("classifier");
								param.b.remove("format");
								param.b.remove("download.size");
							}
						}
						currBundle.insert(param.a);
					}
				}
			}.parse(is);

			LOG.debug(""+(System.currentTimeMillis()-start)+"ms");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
