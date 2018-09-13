package com.ai.ecs.ecm.mall.wap.platform.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import com.ai.ecs.common.utils.ObjectUtils;
import com.ai.ecs.common.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * jedisCluster Cache 宸ュ叿绫�
 * 
 * @author ThinkGem
 * @version 2014-6-29
 */
public class JedisClusterShopUtils {

	private static Logger logger = LoggerFactory
			.getLogger(JedisClusterShopUtils.class);

	private static JedisCluster jedisCluster = SpringContextHolder
			.getBean(JedisCluster.class);

	private static final String CHARSET_NAME = "UTF-8";

	private static int getExpires(int cacheSeconds){
	    int value = cacheSeconds;
	    //涓嶈缃湁鏁堟湡锛岄粯璁�0鍒嗛挓
	    if(cacheSeconds <=0){
	        value = 30 * 60;//榛樿30F
	    }
	    //瓒呰繃涓�ぉ锛屽彧缁欎竴澶╃殑鏈夋晥鏈�
	    if(cacheSeconds > 86400){
	        value = 86400;
	    }
	    return value;
	}
	public static void expires(String key, int cacheSeconds){
	    jedisCluster.expire(key, cacheSeconds);
	}
	public static void expires(Object key, int cacheSeconds){
	    byte[] keyByte =getBytesKey(key);
	    jedisCluster.expire(keyByte, cacheSeconds);
    }
	
	
	public static String hget(String key,String field) {
		String value = null;

		try {

			if (jedisCluster.exists(key)) {
				value = jedisCluster.hget(key, field);
				value = StringUtils.isNotBlank(value)
						&& !"nil".equalsIgnoreCase(value) ? value : null;
				logger.debug("hget {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("hget {} = {}", key, value, e);
		} finally {

		}
		return value;
	}
	
	public static Long hdel(String key,String field) {
		Long value = null;

		try {

			if (jedisCluster.exists(key)) {
				value = jedisCluster.hdel(key, field);
				logger.debug("hdel {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("hdel {} = {}", key, value, e);
		} finally {

		}
		return value;
	}
	
	/**
	 * 鑾峰彇缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @return 鍊�
	 */
	public static String get(String key) {
		String value = null;

		try {

			if (jedisCluster.exists(key)) {
				value = jedisCluster.get(key);
				value = StringUtils.isNotBlank(value)
						&& !"nil".equalsIgnoreCase(value) ? value : null;
				logger.debug("get {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("get {} = {}", key, value, e);
		} finally {

		}
		return value;
	}

	/**
	 * 鑾峰彇缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @return 鍊�
	 */
	public static Object getObject(Object key) {
		Object value = null;

		try {

			if (jedisCluster.exists(getBytesKey(key))) {
				value = toObject(jedisCluster.get(getBytesKey(key)));
				logger.debug("getObject {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObject {} = {}", key, value, e);
		} finally {

		}
		return value;
	}

	/**
	 * 璁剧疆缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @param value
	 *            鍊�
	 * @param cacheSeconds
	 *            瓒呮椂鏃堕棿锛�涓轰笉瓒呮椂
	 * @return
	 */
	public static String set(String key, String value, int cacheSeconds) {
		String result = null;
		cacheSeconds = getExpires(cacheSeconds);
		try {

			result = jedisCluster.set(key, value);
			if (cacheSeconds != 0) {
				jedisCluster.expire(key, cacheSeconds);
			}
			logger.debug("set {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("set {} = {}", key, value, e);
		} finally {

		}
		return result;
	}

	/**
	 * 璁剧疆缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @param value
	 *            鍊�
	 * @param cacheSeconds
	 *            瓒呮椂鏃堕棿锛�涓轰笉瓒呮椂
	 * @return
	 */
	public static String setObject(Object key, Object value, int cacheSeconds) {
		String result = null;
		cacheSeconds = getExpires(cacheSeconds);
		try {
			byte[] keyByte =getBytesKey(key) ;
			result = jedisCluster.set(keyByte,
					JedisClusterShopUtils.toBytes(value));
			if (cacheSeconds != 0) {
				jedisCluster.expire(keyByte, cacheSeconds);
			}
			// logger.debug("setObject {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObject {} = {}", key, value, e);
		} finally {

		}
		return result;
	}

	/**
	 * 鑾峰彇List缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @return 鍊�
	 */
	public static List<String> getList(String key) {
		List<String> value = null;
		try {
			if (jedisCluster.exists(key)) {
				value = jedisCluster.lrange(key, 0, -1);
				logger.debug("getList {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getList {} = {}", key, value, e);
		} finally {

		}
		return value;
	}

	/**
	 * 鑾峰彇List缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @return 鍊�
	 */
	public static List getObjectList(Object key) {
		List value = null;
		try {
			byte[] keyByte =getBytesKey(key);
			if (jedisCluster.exists(keyByte)) {
				List<byte[]> list = jedisCluster.lrange(keyByte, 0, -1);
				value = Lists.newArrayList();
				for (byte[] bs : list) {
					value.add(toObject(bs));
				}
				logger.debug("getObjectList {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectList {} = {}", key, value, e);
		} finally {

		}
		return value;
	}

	/**
	 * 璁剧疆List缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @param value
	 *            鍊�
	 * @param cacheSeconds
	 *            瓒呮椂鏃堕棿锛�涓轰笉瓒呮椂
	 * @return
	 */
	public static long setList(String key, List<String> value,
			int cacheSeconds) {
		long result = 0;
		cacheSeconds = getExpires(cacheSeconds);
		try {
			if (jedisCluster.exists(key)) {
				jedisCluster.del(key);
			}
			result = jedisCluster.rpush(key, (String[]) value.toArray());
			if (cacheSeconds != 0) {
				jedisCluster.expire(key, cacheSeconds);
			}
			logger.debug("setList {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setList {} = {}", key, value, e);
		} finally {

		}
		return result;
	}

	/**
	 * 璁剧疆List缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @param value
	 *            鍊�
	 * @param cacheSeconds
	 *            瓒呮椂鏃堕棿锛�涓轰笉瓒呮椂
	 * @return
	 */
	public static long setObjectList(Object key, List value,
			int cacheSeconds) {
		long result = 0;
		cacheSeconds = getExpires(cacheSeconds);
		if(null == value || 0 == value.size()){
		    logger.info("setObjectList {} = {} , {}", key, value, "input param:value is null or size = 0.");
		    return result;
		}
		try {
			byte[] keyByte =getBytesKey(key);
			if (jedisCluster.exists(keyByte)) {
				jedisCluster.del(keyByte);
			}
			List<byte[]> list = Lists.newArrayList();
			for (Object o : value) {
				list.add(toBytes(o));
			}
			byte[][] blists = new byte[list.size()][];
			list.toArray(blists);
			result = jedisCluster.rpush(keyByte, blists);
			if (cacheSeconds != 0) {
				jedisCluster.expire(keyByte, cacheSeconds);
			}
			logger.debug("setObjectList {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectList {} = {}", key, value, e);
		} finally {

		}
		return result;
	}

	/**
	 * 鍚慙ist缂撳瓨涓坊鍔犲�
	 * 
	 * @param key
	 *            閿�
	 * @param value
	 *            鍊�
	 * @return
	 */
	public static long listAdd(String key, String... value) {
		long result = 0;
		try {
			result = jedisCluster.rpush(key, value);
			logger.debug("listAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("listAdd {} = {}", key, value, e);
		} finally {

		}
		return result;
	}

	/**
	 * 鍚慙ist缂撳瓨涓坊鍔犲�
	 * 
	 * @param key
	 *            閿�
	 * @param value
	 *            鍊�
	 * @return
	 */
	public static long listObjectAdd(Object key, Object... value) {
		long result = 0;
		try {
			byte[] keyByte =getBytesKey(key);
			List<byte[]> list = Lists.newArrayList();
			for (Object o : value) {
				list.add(toBytes(o));
			}
			byte[][] blists = new byte[list.size()][];
			list.toArray(blists);
			result = jedisCluster.rpush(keyByte, blists);
			logger.debug("listObjectAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("listObjectAdd {} = {}", key, value, e);
		} finally {

		}
		return result;
	}

	/**
	 * 鑾峰彇缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @return 鍊�
	 */
	public static Set<String> getSet(String key) {
		Set<String> value = null;
		try {
			if (jedisCluster.exists(key)) {
				value = jedisCluster.smembers(key);
				logger.debug("getSet {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getSet {} = {}", key, value, e);
		} finally {

		}
		return value;
	}

	/**
	 * 鑾峰彇缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @return 鍊�
	 */
	public static Set getObjectSet(Object key) {
		Set value = null;
		try {
			byte[] keyByte =getBytesKey(key);
			if (jedisCluster.exists(keyByte)) {
				value = Sets.newHashSet();
				Set<byte[]> set = jedisCluster.smembers(keyByte);
				for (byte[] bs : set) {
					value.add(toObject(bs));
				}
				logger.debug("getObjectSet {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectSet {} = {}", key, value, e);
		} finally {

		}
		return value;
	}

	/**
	 * 璁剧疆Set缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @param value
	 *            鍊�
	 * @param cacheSeconds
	 *            瓒呮椂鏃堕棿锛�涓轰笉瓒呮椂
	 * @return
	 */
	public static long setSet(String key, Set<String> value, int cacheSeconds) {
		long result = 0;
		cacheSeconds = getExpires(cacheSeconds);
		try {
			if (jedisCluster.exists(key)) {
				jedisCluster.del(key);
			}
			result = jedisCluster.sadd(key, (String[]) value.toArray());
			if (cacheSeconds != 0) {
				jedisCluster.expire(key, cacheSeconds);
			}
			logger.debug("setSet {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setSet {} = {}", key, value, e);
		} finally {

		}
		return result;
	}

	/**
	 * 璁剧疆Set缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @param value
	 *            鍊�
	 * @param cacheSeconds
	 *            瓒呮椂鏃堕棿锛�涓轰笉瓒呮椂
	 * @return
	 */
	public static long setObjectSet(Object key, Set value,
			int cacheSeconds) {
		long result = 0;
		cacheSeconds = getExpires(cacheSeconds);
		try {
			byte[] keyByte =getBytesKey(key);
			if (jedisCluster.exists(keyByte)) {
				jedisCluster.del(keyByte);
			}
			Set<byte[]> set = Sets.newHashSet();
			for (Object o : value) {
				set.add(toBytes(o));
			}
			byte[][] bsets = new byte[set.size()][];
			set.toArray(bsets);
			result = jedisCluster.sadd(keyByte, bsets);
			if (cacheSeconds != 0) {
				jedisCluster.expire(keyByte, cacheSeconds);
			}
			logger.debug("setObjectSet {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectSet {} = {}", key, value, e);
		} finally {

		}
		return result;
	}

	/**
	 * 鍚慡et缂撳瓨涓坊鍔犲�
	 * 
	 * @param key
	 *            閿�
	 * @param value
	 *            鍊�
	 * @return
	 */
	public static long setSetAdd(String key, String... value) {
		long result = 0;
		try {
			result = jedisCluster.sadd(key, value);
			logger.debug("setSetAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setSetAdd {} = {}", key, value, e);
		} finally {

		}
		return result;
	}

	/**
	 * 鍚慡et缂撳瓨涓坊鍔犲�
	 * 
	 * @param key
	 *            閿�
	 * @param value
	 *            鍊�
	 * @return
	 */
	public static long setSetObjectAdd(Object key, Object... value) {
		long result = 0;
		try {
			byte[] keyByte =getBytesKey(key);
			Set<byte[]> set = Sets.newHashSet();
			for (Object o : value) {
				set.add(toBytes(o));
			}
			byte[][] bsets = new byte[set.size()][];
			set.toArray(bsets);
			result = jedisCluster.rpush(keyByte, bsets);
			logger.debug("setSetObjectAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setSetObjectAdd {} = {}", key, value, e);
		} finally {

		}
		return result;
	}

	/**
	 * 鑾峰彇Map缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @return 鍊�
	 */
	public static Map<String, String> getMap(String key) {
		Map<String, String> value = null;
		try {
			if (jedisCluster.exists(key)) {
				value = jedisCluster.hgetAll(key);
				logger.debug("getMap {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getMap {} = {}", key, value, e);
		} finally {

		}
		return value;
	}

	/**
	 * 鑾峰彇Map缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @return 鍊�
	 */
	public static Map<Object, Object> getObjectMap(Object key) {
		Map<Object, Object> value = null;
		try {
			byte[] keyByte =getBytesKey(key);
			if (jedisCluster.exists(keyByte)) {
				value = Maps.newHashMap();
				Map<byte[], byte[]> map = jedisCluster.hgetAll(keyByte);
				for (Map.Entry<byte[], byte[]> e : map.entrySet()) {
					value.put(toObject(e.getKey()), toObject(e.getValue()));
				}
				logger.debug("getObjectMap {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectMap {} = {}", key, value, e);
		} finally {

		}
		return value;
	}

	/**
	 * 璁剧疆Map缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @param value
	 *            鍊�
	 * @param cacheSeconds
	 *            瓒呮椂鏃堕棿锛�涓轰笉瓒呮椂
	 * @return
	 */
	public static String setMap(String key, Map<String, String> value,
			int cacheSeconds) {
		String result = null;
		cacheSeconds = getExpires(cacheSeconds);
		try {
			if (jedisCluster.exists(key)) {
				jedisCluster.del(key);
			}
			result = jedisCluster.hmset(key, value);
			if (cacheSeconds != 0) {
				jedisCluster.expire(key, cacheSeconds);
			}
			logger.debug("setMap {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setMap {} = {}", key, value, e);
		} finally {

		}
		return result;
	}

	/**
	 * 璁剧疆Map缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @param value
	 *            鍊�
	 * @param cacheSeconds
	 *            瓒呮椂鏃堕棿锛�涓轰笉瓒呮椂
	 * @return
	 */
	public static String setObjectMap(String key, Map<Object, Object> value,
			int cacheSeconds) {
		String result = null;
		cacheSeconds = getExpires(cacheSeconds);
		try {
			byte[] keyByte =getBytesKey(key);
			if (jedisCluster.exists(keyByte)) {
				jedisCluster.del(key);
			}
			Map<byte[], byte[]> map = Maps.newHashMap();
			for (Map.Entry<Object, Object> e : value.entrySet()) {
				map.put(toBytes(e.getKey()), toBytes(e.getValue()));
			}
			result = jedisCluster.hmset(keyByte, map);
			if (cacheSeconds != 0) {
				jedisCluster.expire(keyByte, cacheSeconds);
			}
			logger.debug("setObjectMap {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectMap {} = {}", key, value, e);
		} finally {

		}
		return result;
	}

	/**
	 * 鍚慚ap缂撳瓨涓坊鍔犲�
	 * 
	 * @param key
	 *            閿�
	 * @param value
	 *            鍊�
	 * @return
	 */
	public static String mapPut(String key, Map<String, String> value) {
		String result = null;

		try {

			result = jedisCluster.hmset(key, value);
			logger.debug("mapPut {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("mapPut {} = {}", key, value, e);
		} finally {

		}
		return result;
	}

	/**
	 * 鍚慚ap缂撳瓨涓坊鍔犲�
	 * 
	 * @param key
	 *            閿�
	 * @param value
	 *            鍊�
	 * @return
	 */
	public static String mapObjectPut(Object key, Map<Object, Object> value) {
		String result = null;
		try {
			byte[] keyByte =getBytesKey(key);
			Map<byte[], byte[]> map = Maps.newHashMap();
			for (Map.Entry<Object, Object> e : value.entrySet()) {
				map.put(toBytes(e.getKey()), toBytes(e.getValue()));
			}
			result = jedisCluster.hmset(keyByte,  map);
			logger.debug("mapObjectPut {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("mapObjectPut {} = {}", key, value, e);
		} finally {

		}
		return result;
	}

	/**
	 * 绉婚櫎Map缂撳瓨涓殑鍊�
	 * 
	 * @param key
	 *            閿�
	 * @param mapKey
	 *            鍊奸敭
	 * @return
	 */
	public static long mapRemove(String key, String mapKey) {
		long result = 0;
		try {
			result = jedisCluster.hdel(key, mapKey);
			logger.debug("mapRemove {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapRemove {}  {}", key, mapKey, e);
		} finally {

		}
		return result;
	}

	/**
	 * 绉婚櫎Map缂撳瓨涓殑鍊�
	 * 
	 * @param key
	 *            閿�
	 * @param mapKey
	 *            鍊奸敭
	 * @return
	 */
	public static long mapObjectRemove(Object key, Object mapKey) {
		long result = 0;
		try {
			byte[] keyByte =getBytesKey(key);
			result = jedisCluster.hdel(keyByte, toBytes(mapKey));
			logger.debug("mapObjectRemove {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapObjectRemove {}  {}", key, mapKey, e);
		} finally {

		}
		return result;
	}


	public static Object mapObjectGet(Object key, Object mapKey) {
		Object result = 0;
		try {
			byte[] keyByte =getBytesKey(key);
			result = toObject(jedisCluster.hget(keyByte, toBytes(mapKey)));
			logger.debug("mapObjectGet {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapObjectGet {}  {}", key, mapKey, e);
		} finally {

		}
		return result;
	}

    public static long mapObjectSet(Object key, Object mapKey, Object value) {
        long result = 0;
        try {
            byte[] keyByte =getBytesKey(key);
            result = jedisCluster.hset(keyByte, toBytes(mapKey), toBytes(value));
            logger.debug("mapObjectSet {}  {}  {}", key, mapKey,value);
        } catch (Exception e) {
            logger.warn("mapObjectGet {}  {}  {}", key, mapKey,value, e);
        } finally {

        }
        return result;
    }
    

    
	/**
	 * 鍒ゆ柇Map缂撳瓨涓殑Key鏄惁瀛樺湪
	 * 
	 * @param key
	 *            閿�
	 * @param mapKey
	 *            鍊奸敭
	 * @return
	 */
	public static boolean mapExists(String key, String mapKey) {
		boolean result = false;
		try {
			result = jedisCluster.hexists(key, mapKey);
			logger.debug("mapExists {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapExists {}  {}", key, mapKey, e);
		} finally {

		}
		return result;
	}

	/**
	 * 鍒ゆ柇Map缂撳瓨涓殑Key鏄惁瀛樺湪
	 * 
	 * @param key
	 *            閿�
	 * @param mapKey
	 *            鍊�
	 * @return
	 */
	public static boolean mapObjectExists(Object key, Object mapKey) {
		boolean result = false;
		try {
			byte[] keyByte =getBytesKey(key);
			result = jedisCluster.hexists(keyByte, toBytes(mapKey));
			logger.debug("mapObjectExists {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapObjectExists {}  {}", key, mapKey, e);
		} finally {

		}
		return result;
	}

	/**
	 * 鍒犻櫎缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @return
	 */
	public static long del(String key) {
		long result = 0;
		try {
			if (jedisCluster.exists(key)) {
				result = jedisCluster.del(key);
				logger.debug("del {}", key);
			} else {
				logger.debug("del {} not exists", key);
			}
		} catch (Exception e) {
			logger.warn("del {}", key, e);
		} finally {

		}
		return result;
	}

	/**
	 * 鍒犻櫎缂撳瓨
	 * 
	 * @param key
	 *            閿�
	 * @return
	 */
	public static long delObject(Object key) {
		long result = 0;
		try {
			byte[] keyByte =getBytesKey(key);
			if (jedisCluster.exists(keyByte)) {
				result = jedisCluster.del(keyByte);
				logger.debug("delObject {}", key);
			} else {
				logger.debug("delObject {} not exists", key);
			}
		} catch (Exception e) {
			logger.warn("delObject {}", key, e);
		} finally {

		}
		return result;
	}

	/**
	 * 缂撳瓨鏄惁瀛樺湪
	 * 
	 * @param key
	 *            閿�
	 * @return
	 */
	public static boolean exists(String key) {
		boolean result = false;
		try {
			result = jedisCluster.exists(key);
			logger.debug("exists {}", key);
		} catch (Exception e) {
			logger.warn("exists {}", key, e);
		} finally {

		}
		return result;
	}

	/**
	 * 缂撳瓨鏄惁瀛樺湪
	 * 
	 * @param key
	 *            閿�
	 * @return
	 */
	public static boolean existsObject(Object key) {
		boolean result = false;
		try {
			byte[] keyByte =getBytesKey(key);
			result = jedisCluster.exists(keyByte);
			logger.debug("existsObject {}", key);
		} catch (Exception e) {
			logger.warn("existsObject {}", key, e);
		} finally {

		}
		return result;
	}

	/**
	 * 鑾峰彇byte[]绫诲瀷Key
	 * 
	 * @param object
	 * @return
	 */
	public static byte[] getBytesKey(Object object) {
		if (object instanceof String) {
			return StringUtils.getBytes((String) object);
		} else {
			return ObjectUtils.serialize(object);
		}
	}

	/**
	 * Object杞崲byte[]绫诲瀷
	 * 
	 * @param object
	 * @return
	 */
	public static byte[] toBytes(Object object) {
		return ObjectUtils.serialize(object);
	}



	/**
	 * byte[]鍨嬭浆鎹bject
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object toObject(byte[] bytes) {
		return ObjectUtils.unserialize(bytes);
	}


	/**
	 * 妯＄硦鍖归厤鑾峰彇key闆嗗悎
	 * 
	 * @param pattern
	 * @return
	 */
//	public static List<String> keys(String pattern) {
//		logger.debug("Start getting keys...");
//		List<String> keys = Lists.newArrayList();
//		Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
//		for (String k : clusterNodes.keySet()) {
//			logger.debug("Getting keys from: {}", k);
//			JedisPool jp = clusterNodes.get(k);
//			Jedis connection = jp.getResource();
//			try {
//				keys.addAll(connection.keys(pattern));
//			} catch (Exception e) {
//				logger.error("Getting keys error: {}", e);
//			} finally {
//				logger.debug("Connection closed.");
//				connection.close();// 鐢ㄥ畬涓�畾瑕乧lose杩欎釜閾炬帴锛侊紒锛�
//			}
//		}
//		logger.debug("Keys gotten!");
//		return keys;
//	}

	/**
	 * 妯＄硦鍖归厤鑾峰彇key闆嗗悎
	 *
	 * @param pattern
	 * @return
	 */
	public static List<String> keys(String pattern) {
		logger.debug("Start getting keys...");
		List<String> keys = Lists.newArrayList();
		Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
		for (String k : clusterNodes.keySet()) {
			logger.debug("Getting keys from: {}", k);
			JedisPool jp = clusterNodes.get(k);
			Jedis connection = jp.getResource();
			try {
				keys.addAll(connection.keys(pattern));
			} catch (Exception e) {
				logger.error("Getting keys error: {}", e);
			} finally {
				logger.debug("Connection closed.");
				connection.close();// 鐢ㄥ畬涓�畾瑕乧lose杩欎釜閾炬帴锛侊紒锛�
			}
		}
		logger.debug("Keys gotten!");
//		return keys;
		
		//鍘婚噸
		return new ArrayList<String>(new HashSet<String>(keys));
	}

}
