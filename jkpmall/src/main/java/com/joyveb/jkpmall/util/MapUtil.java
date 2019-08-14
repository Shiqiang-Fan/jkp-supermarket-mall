package com.joyveb.jkpmall.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chenjia on 2018/7/10
 */
@Slf4j
public class MapUtil {


	@SuppressWarnings({"rawtypes", "unchecked"})
	public static Map bean2Map(Object javaBean) {
		Map<Object, Object> map = new TreeMap(
			new Comparator<String>() {
				@Override
				public int compare(String obj1, String obj2) {
					// 升序排序
					return obj1.compareTo(obj2);
				}
			});
		try {
			// 获取javaBean属性
			BeanInfo beanInfo = Introspector.getBeanInfo(javaBean.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			if (propertyDescriptors != null && propertyDescriptors.length > 0) {
				// javaBean属性名
				String propertyName = null;
				// javaBean属性值
				Object propertyValue = null;
				for (PropertyDescriptor pd : propertyDescriptors) {
					propertyName = pd.getName();
					if (!"class".equals(propertyName)) {
						Method readMethod = pd.getReadMethod();
						propertyValue = readMethod.invoke(javaBean, new Object[0]);
						if (null != propertyValue) {
							String value = propertyValue.toString();
							if (StringUtils.isNotBlank(value)) {
								map.put(propertyName, value);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.warn("java bean 转 map 异常", e);
		}

		return map;
	}


}


