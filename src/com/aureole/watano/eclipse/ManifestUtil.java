package com.aureole.watano.eclipse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ManifestUtil {
	public static Manifest getManifest(File f){
		try {
			if(f.exists() && f.isFile() && (f.getName().endsWith(".jar") || f.getName().endsWith(".zip"))){
				JarFile jarFile = new JarFile(f);
				return jarFile.getManifest();
			}else if(f.exists() && f.isDirectory()){
				File mf = new File(f.getPath()+File.separator+"META-INF"+File.separator+"MANIFEST.MF");
				if(mf.exists() && mf.isFile()){
					return new Manifest(new FileInputStream(mf));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
