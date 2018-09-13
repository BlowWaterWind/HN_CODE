/**
 * 判断指定的对象是否为null值或未定义值。
 */
function isNull(obj) {
	return obj == null || typeof(obj) == "undefined";
}

/**
 * 判断指定的字符串是否为空。
 * 
 * @param value
 *            要验证的字段
 * @returns {Boolean} 如果字段为空，返回true，否则返回false。
 */
function isEmpty(str) {
	return isNull(str) || str.length == 0;
}

/**
 * 清空给定数组中的所有元素。
 * 
 * @param value
 *            要清空的数组
 */
function clearArray(array) {
	if(!isNull(array)) {
		array.splice(0, array.length);
	}
}