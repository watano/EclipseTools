package com.aureole.watano.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.jar.Pack200.Unpacker;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.apache.log4j.Logger;

public class FileUtil {
	private static final Logger LOG = Logger.getLogger(FileUtil.class);
	private static boolean debug = true;
	private static FileSystemManager fsm = null;

	public static void deleteFile(String path){
		LOG.debug("delete "+path);
		try {
			if(path == null){
				return;
			}
			File f = new File(path);
			if(f != null && f.exists() && f.isFile() && !debug){
				FileUtils.deleteQuietly(f);
			}
			if(f != null && f.exists() && f.isDirectory() && !debug){
				FileUtils.deleteDirectory(f);
			}
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
	}

	public static void copyFile(String src, String destDir){
		LOG.debug("copy "+src +" to "+destDir);
		File fsrc = new File(src);
		File fdest = new File(destDir);
		try {
			fdest = new File(fdest.getAbsolutePath()+File.pathSeparator+fsrc.getName());
			if(fsrc.isDirectory() && !debug){
				FileUtils.copyDirectory(fsrc, fdest);
			}else if(fsrc.isFile() && !debug){
				FileUtils.copyFile(fsrc, fdest);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String extractFile(String src, String destDir){
		LOG.debug("extract "+src +" to "+destDir);
		String outfilename = null;
		InputStream in = null;
		OutputStream out = null;
		JarOutputStream jarout = null;
		try {
			FileObject fo = getFileObject(src);
			in = fo.getContent().getInputStream();
			src = fo.getName().getURI();
			for(String ext: new String[]{"gz", "zip", "tar", "tgz", "tbz2"}){
				if(src.endsWith("."+ext) && !src.endsWith(".pack.gz") ){
					src = ext+":" + src.substring(0, src.length() - (ext.length() +1));
					src = src + "."+ext+"!" + src.substring(src.lastIndexOf("/")+1);
					return extractFile(src, destDir);
				}
			}

			outfilename = destDir;
			if(src.endsWith(".pack")){
				Unpacker unpacker = Pack200.newUnpacker();
				outfilename += fo.getName().getBaseName();
				if(outfilename.endsWith(".pack")){
					outfilename = outfilename.substring(0, outfilename.length() - 5);
				}
				out = new FileOutputStream(outfilename);
				jarout= new JarOutputStream(out);
				unpacker.unpack(in, jarout);
			}else{
				outfilename += fo.getName().getBaseName();
				out = new FileOutputStream(outfilename);
				IOUtils.copy(in, out);
			}
		} catch (FileSystemException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(jarout);
			IOUtils.closeQuietly(out);
		}
		return outfilename;
	}

	public static InputStream getContentInputStream(String filename){
		try {
			FileObject fo = getFileObject(filename);
			if(fo.exists()){
				return fo.getContent().getInputStream();
			}
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getVfsFileName(String filename){
		try {
			FileObject fo = getFileObject(filename);
			if(fo != null){
				return fo.getURL().toString();
			}
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static FileObject getFileObject(String filename){
		try {
			if(fsm == null){
				fsm = VFS.getManager();
				//				((DefaultFileSystemManager)fsm).setCacheStrategy(CacheStrategy.ON_CALL);
			}
			FileObject fo = fsm.resolveFile(filename);
			return fo;
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static InputStream getInputStream(String dirname, String filename){
		try {
			File fdir = new File(dirname);
			if(fdir.exists() && fdir.isDirectory()){
				File f = new File(dirname+File.pathSeparator+filename);
				if(f.exists() && f.isFile()){
					return new FileInputStream(f);
				}
			}
			if(fdir.exists() && fdir.isFile()){
				ZipFile zip = new ZipFile(fdir);
				return zip.getInputStream(zip.getEntry(filename));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
