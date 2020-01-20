
package com.sum.cache.memory;


import java.util.Collection;
import java.util.Map;

import com.sum.cache.Cache;

public interface MemoryCache extends Cache {

	/** Returns all keys of cache */
	Collection<String> keys();
	
	/** Returns all Map of cache 
	 */
	Map<String, ?> snapshot();
	
}
