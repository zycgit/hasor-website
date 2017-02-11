/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.website.cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Cache
 * @version : 2017年2月11日
 * @author 赵永春(zyc@hasor.net)
 */
public class CacheInstance<K, V> implements Cache<K, V> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected String                              cacheName;
    protected com.google.common.cache.Cache<K, V> googleCache;
    //
    public CacheInstance(String cacheName, com.google.common.cache.Cache<K, V> googleCache) {
        this.cacheName = cacheName;
        this.googleCache = googleCache;
    }
    @Override
    public String getName() {
        return this.cacheName;
    }
    public boolean put(K key, V value) {
        this.googleCache.put(key, value);
        return true;
    }
    public boolean put(K key, V value, int timeout) {
        this.googleCache.put(key, value);
        return true;
    }
    public V get(K key) {
        if (key == null) {
            return null;
        }
        return this.googleCache.getIfPresent(key);
    }
}