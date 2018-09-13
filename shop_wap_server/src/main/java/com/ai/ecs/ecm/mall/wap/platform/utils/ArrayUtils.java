/**
 * @authorDeng.yy
 * @createDate 2014年1月6日
 */
package com.ai.ecs.ecm.mall.wap.platform.utils;

/**
 * 数组操作工具类。
 * 
 */
public class ArrayUtils extends org.apache.commons.lang3.ArrayUtils {

	/**
	 * 从给定的数组中根据索引寻找对应的元素，如果不存在对应索引的元素，则返回null。
	 * 
	 * @param array 数组
	 * @param index 元素索引
	 * @return 索引对应的元素，如果不存在对应索引的元素，则返回null。
	 * 
	 * @authorDeng.yy
	 * @createDate 2013年11月4日
	 */
	public static <T> T get(T[] array, int index) {
		if (array != null && array.length > index) {
			return array[index];
		} else {
			return null;
		}
	}

	/**
	 * 从给定的数组中寻找第一个元素，如果数组为null或者空数组，则返回null。
	 * 
	 * @param array 数组
	 * @return 第一个元素，如果数组为null或者空数组，则返回null。
	 * 
	 * @authorDeng.yy
	 * @createDate 2013年11月4日
	 */
	public static <T> T getFirst(T[] array) {
		return get(array, 0);
	}

	/**
	 * 从给定的数组中寻找最后一个元素，如果数组为null或者空数组，则返回null。
	 * 
	 * @param array 数组
	 * @return 最后一个元素，如果数组为null或者空数组，则返回null。
	 * 
	 * @authorDeng.yy
	 * @createDate 2013年11月4日
	 */
	public static <T> T getLast(T[] array) {
		if (array != null && array.length > 0) {
			return array[array.length - 1];
		} else {
			return null;
		}
	}

	/**
	 * 从给定的数组中寻找是否包含<code>null</code>元素。
	 * 
	 * @param array 数组
	 * @return 如果数组非空且包含<code>null</code>元素，则返回true，否则返回false。
	 * 
	 * @authorDeng.yy
	 * @createDate 2014年2月20日
	 */
	public static boolean containsNull(Object... array) {
		if (isNotEmpty(array)) {
			for (int i = 0; i < array.length; ++i) {
				if (array[i] == null) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 从给定的数组中寻找是否不包含<code>null</code>元素。
	 * 
	 * @param array 数组
	 * @return 如果数组非空且不包含<code>null</code>元素，则返回true，否则返回false。
	 * 
	 * @authorDeng.yy
	 * @createDate 2014年2月20日
	 */
	public static boolean notContainsNull(Object... array) {
		if (isNotEmpty(array)) {
			for (int i = 0; i < array.length; ++i) {
				if (array[i] == null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 从给定的数组中寻找第一个为null的元素的索引。
	 * 
	 * @param array 数组
	 * @return 数组中第一个为null的元素的索引，如果数组为空或数组中不存在为null的元素，则返回-1。
	 * 
	 * @authorDeng.yy
	 * @createDate 2014年2月20日
	 */
	public static int indexOfNull(Object... array) {
		if (isNotEmpty(array)) {
			for (int i = 0; i < array.length; ++i) {
				if (array[i] == null) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 从给定的数组中寻找第一个不为null的元素的索引。
	 * 
	 * @param array 数组
	 * @return 数组中第一个不为null的元素的索引，如果数组为空或数组中不存在不为null的元素，则返回-1。
	 * 
	 * @authorDeng.yy
	 * @createDate 2014年2月20日
	 */
	public static int indexOfNotNull(Object... array) {
		if (isNotEmpty(array)) {
			for (int i = 0; i < array.length; ++i) {
				if (array[i] != null) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 从给定的数组中寻找最后一个为null的元素的索引。
	 * 
	 * @param array 数组
	 * @return 数组中最后一个为null的元素的索引，如果数组为空或数组中不存在为null的元素，则返回-1。
	 * 
	 * @authorDeng.yy
	 * @createDate 2014年2月20日
	 */
	public static int lastIndexOfNull(Object... array) {
		if (isNotEmpty(array)) {
			for (int i = array.length - 1; i >= 0; --i) {
				if (array[i] == null) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 从给定的数组中寻找最后一个不为null的元素的索引。
	 * 
	 * @param array 数组
	 * @return 数组中最后一个不为null的元素的索引，如果数组为空或数组中不存在不为null的元素，则返回-1。
	 * 
	 * @authorDeng.yy
	 * @createDate 2014年2月20日
	 */
	public static int lastIndexOfNotNull(Object... array) {
		if (isNotEmpty(array)) {
			for (int i = array.length - 1; i >= 0; --i) {
				if (array[i] != null) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 从给定的数组中查找第一个不为null的元素并返回该元素。
	 * 
	 * @param array 数组
	 * @return 数组中第一个不为null的元素，如果数组为空或数组中不存在不为null的元素，则返回null。
	 * 
	 * @authorDeng.yy
	 * @createDate 2014年2月20日
	 */
	public static <T> T firstNotNull(T[] array) {
		if (isNotEmpty(array)) {
			T o;
			for (int i = 0; i < array.length; ++i) {
				o = array[i];
				if (o != null) {
					return o;
				}
			}
		}
		return null;
	}

	/**
	 * 从给定的数组中查找最后一个不为null的元素并返回该元素。
	 * 
	 * @param array 数组
	 * @return 数组中最后一个不为null的元素，如果数组为空或数组中不存在不为null的元素，则返回null。
	 * 
	 * @authorDeng.yy
	 * @createDate 2014年2月20日
	 */
	public static <T> T lastNotNull(T[] array) {
		if (isNotEmpty(array)) {
			T o;
			for (int i = array.length - 1; i >= 0; --i) {
				o = array[i];
				if (o != null) {
					return o;
				}
			}
		}
		return null;
	}

}
