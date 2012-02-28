package com.aureole.watano.eclipse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

public class VersionComparator  implements Comparator<String>{
	private static final Logger LOG = Logger.getLogger(VersionComparator.class);
	@Override
	public int compare(String v1, String v2) {
		return c(v1, v2);

	}
	public static int c(String v1, String v2) {
		long[] v1s = getNumbers(v1);
		long[] v2s = getNumbers(v2);
		for(int i=0; i<v1s.length && i<v2s.length; i++){
			if(v1s[i] != v2s[i]){
				return (int) (v1s[i] - v2s[i]);
			}
			if(i+1 == v1s.length && i+1 < v2s.length){
				return -1;
			}
			if(i+1 == v2s.length && i+1 < v1s.length){
				return 1;
			}
		}
		return 0;
	}

	public static int in(String versionRange, String version){
		if(StringUtils.isBlank(versionRange)){
			return 1;
		}
		String range = versionRange.trim();
		while(range.startsWith("\"") || range.startsWith("'")){
			range = range.substring(1);
		}
		while(range.endsWith("\"") || range.endsWith("'")){
			range = range.substring(0, range.length()-1);
		}
		if(range.startsWith("[") || range.startsWith("(") || range.endsWith(")") || range.endsWith("]")){
			LOG.debug("version Range---------<"+versionRange+">");
		}
		String[] rangs = StringUtils.split(range, ",");
		if(rangs != null && rangs.length > 0){
			if(StringUtils.isNotBlank(rangs[0])){
				if(c(rangs[0].trim(), version) > 0){
					return -1;
				}
			}
		}
		if(rangs != null && rangs.length > 1){
			if(StringUtils.isNotBlank(rangs[1])){
				if(c(version, rangs[1].trim()) > 0){
					return 1;
				}
			}
		}

		return 0;
	}

	private static long[] getNumbers(String text){
		String newtext = "";
		for(char ch:text.toCharArray()){
			if(ch < '0' || ch > '9'){
				newtext += "|";
			}else{
				newtext += ch;
			}
		}
		String[] v1ints = StringUtils.split(newtext, "|");
		long[] numbers = new long[v1ints.length];
		int i=0;
		for(String v: v1ints){
			try{
				numbers[i++] = Long.parseLong(v);
			}catch(Throwable t){
			}
		}
		return numbers;
	}

	@Test
	public void testCompare() {
		assertTrue(VersionComparator.c("1.0.300.v201003121448", "1.0.400.v201002111343")<0);
		assertTrue(VersionComparator.c("1.0.100.v20100309-0005", "1.0.101.R35x_v20090721")<0);
		assertTrue(VersionComparator.c("1.0.100.v_A39", "1.0.100.v_981_R35x")<0);
		assertTrue(VersionComparator.c("1.1.2.v201001222130", "1.1.2.v201001222130")==0);
		assertTrue(VersionComparator.c("2.1.2.v201001222130", "1.1.2.v201001222130")>0);

		assertEquals(VersionComparator.in("", "1.1.2.v201001222130"), 1);
		assertEquals(VersionComparator.in(null, "1.1.2.v201001222130"), 1);
		assertEquals(VersionComparator.in("2.1.2.v201001222130", "1.1.2.v201001222130"), -1);
		assertEquals(VersionComparator.in("0.1.2.v201001222130", "1.1.2.v201001222130"), 0);
		assertEquals(VersionComparator.in("0.1.2.v201001222130,2.1.2.v201001222130", "1.1.2.v201001222130"), 0);
		assertEquals(VersionComparator.in("0.1.2.v201001222130,2.1.2.v201001222130", "0.0.2.v201001222130"), -1);
		assertEquals(VersionComparator.in("1.1.2.v201001222130,2.1.2.v201001222130", "1.1.2.v201001222130"), 0);
		assertEquals(VersionComparator.in("1.1.2.v201001222130,2.1.2.v201001222130", "3.1.2.v201001222130"), 1);
	}
}
