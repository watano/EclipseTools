package com.aureole.watano.eclipse;

import java.util.Comparator;

public class BundleInfoVersionComparator implements Comparator<BundleInfo>{
	@Override
	public int compare(BundleInfo o1, BundleInfo o2) {
		return new VersionComparator().compare(o1.getBundleVersion(), o2.getBundleVersion());
	}
}
