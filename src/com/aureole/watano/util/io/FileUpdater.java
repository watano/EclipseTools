package com.aureole.watano.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

public class FileUpdater {
	private String home = "";
	private String[] exculdes = null;
	private StringBuffer pathInfo = new StringBuffer();
	private Map<String, Long[]> updateinfo = null; // long[0]: -1 delete, 0-eq, 1-update

	public FileUpdater(String home, String[] exculdes) {
		super();
		this.home = home;
		this.exculdes = exculdes;
	}

	public void wirtePathInfo(){
		try {
			listDir(new File(home), 1);
			FileUtils.writeStringToFile(new File("PathInfo.txt"), pathInfo.toString());
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}

	public void update(File fPathInfo){
		try {
			updateinfo = new Hashtable<String, Long[]>();
			LineIterator lineIterator = FileUtils.lineIterator(fPathInfo);
			while(lineIterator.hasNext()){
				String line = lineIterator.nextLine();
				String name = line;
				long crc = 0;
				if(line.indexOf("----------") > 0){
					name = line.substring(0,line.indexOf("----------"));
					crc = Long.parseLong(line.substring(line.indexOf("----------")+"----------".length()));
				}
				updateinfo.put(name, new Long[]{-1l, crc});
			}
			System.out.println("updateinfo "+updateinfo.size());

			listDir(new File(home), 2);

			System.out.println("updateinfo "+updateinfo.size());

			File foUpdate = new File(home+"\\Update.zip");
			if(foUpdate.exists()){
				foUpdate.delete();
			}
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(home+"\\Update.zip"));
			StringBuffer sb = new StringBuffer();

			for(String fname : updateinfo.keySet()){
				Long[] status = updateinfo.get(fname);
				if(status[0] == -1){
					sb.append("delete " + fname+"\n");
				}else if(status[0] == 1){
					out.putNextEntry(new ZipEntry(fname));
					IOUtils.copy(new FileInputStream(fname), out);
				}
			}
			IOUtils.closeQuietly(out);
			FileUtils.writeStringToFile(new File("del.bat"), sb.toString());
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}

	private void listDir(File fdir, int mode){
		try {
			File[] fs = fdir.listFiles();
			for(File f : fs){
				String name = f.getAbsolutePath();
				if(name.startsWith(home)){
					name = name.substring(home.length());
				}
				while(name.startsWith("\\") || name.startsWith("/")){
					name = name.substring(1);
				}
				boolean exculded = false;
				for(String exculde: exculdes){
					if(exculde.equals(name) || name.endsWith("\\"+exculde)){
						exculded = true;
						break;
					}
				}
				if(exculded){
					continue;
				}
				if(f.isDirectory()){
					if(mode == 1){
						pathInfo.append(name);
						pathInfo.append("\n");
					}else if(mode  == 2){
						Long[] status = updateinfo.get(name);
						if(status != null){
							updateinfo.put(name, new Long[]{0l,0l});
						}
					}
					listDir(f, mode);
				}else if(f.isFile()){
					if(mode == 1){
						pathInfo.append(name);
						pathInfo.append("----------");
						pathInfo.append(FileUtils.checksumCRC32(f));
						pathInfo.append("\n");
					}else if(mode  == 2){
						Long[] status = updateinfo.get(name);
						if(status != null ){
							if(FileUtils.checksumCRC32(f) != status[1]){
								updateinfo.put(name, new Long[]{1l, 0l});
							}else{
								updateinfo.put(name, new Long[]{0l, 0l});
							}
						}else{
							updateinfo.put(name, new Long[]{1l,0l});
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		String[] exculdes = new String[]{
				".svn",".refactorings",".hgignore",".settings",".project",".classpath",".freemarker-ide.xml","dropins",
				"docs","log","temp","PathInfo.txt","Update.zip",
				"js\\util", "js\\dijit","js\\dojo","js\\dojox","js\\uxebu",
				"WEB-INF\\src", "WEB-INF\\lib\\debug", "WEB-INF\\lib\\src"
		};
		if(args.length == 1){
			File fhome = new File(args[0]);
			if(fhome.isDirectory() && fhome.exists()){
				FileUpdater fu = new FileUpdater(fhome.getAbsolutePath(), exculdes);
				fu.wirtePathInfo();
			}else{
				System.err.println(args[0] + " no a existing directory!");
			}
		}else if(args.length == 2){
			File fhome = new File(args[0]);
			if(!fhome.isDirectory() || !fhome.exists()){
				System.err.println(args[0] + " no a existing directory!");
				return ;
			}

			File fPathInfo = new File(args[1]);
			if(!fPathInfo.isFile() || !fPathInfo.exists()){
				System.err.println(args[1] + " no a existing file!");
				return ;
			}

			FileUpdater fu = new FileUpdater(fhome.getAbsolutePath(), exculdes);
			fu.update(fPathInfo);
		}
		System.out.println(System.currentTimeMillis() - start);
	}
}
