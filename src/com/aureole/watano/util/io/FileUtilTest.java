package com.aureole.watano.util.io;

import org.junit.Assert;
import org.junit.Test;

public class FileUtilTest {

	@Test
	public void testDeleteFile() {
		//		fail("Not yet implemented");
	}

	@Test
	public void testCopyFile() {
		//		fail("Not yet implemented");
	}

	@Test
	public void testExtractFile() {
		FileUtil.extractFile("zip:file:///e:/download/JBossTools-Update-3.1.0.v201003050540R-H56-GA.zip!plugins/org.jboss.ide.eclipse.freemarker_1.1.0.v201003050540R-H56-GA.jar.pack.gz",
		"e:\\work\\devtools\\eclipse\\dropins\\");
	}

	@Test
	public void testGetContentInputStream() {
		//		fail("Not yet implemented");
	}

	@Test
	public void testGetVfsFileName() {
		Assert.assertEquals(FileUtil.getVfsFileName(
		"e:\\work\\devtools\\eclipse\\dropins\\"),
		"file:///e:/work/devtools/eclipse/dropins");
	}

}
