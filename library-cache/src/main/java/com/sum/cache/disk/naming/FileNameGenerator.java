
package com.sum.cache.disk.naming;

public interface FileNameGenerator {

	/** Generates unique file name for image defined by URI */
	String generate(String imageUri);
}
