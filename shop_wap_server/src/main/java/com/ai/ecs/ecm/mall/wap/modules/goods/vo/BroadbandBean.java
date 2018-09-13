package com.ai.ecs.ecm.mall.wap.modules.goods.vo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ai.ecs.goods.entity.goods.TfGoodsInfo;

public class BroadbandBean {
	
	//将list装进本bean内
	public void initBean(Object[] obj){
		Field[] preName=this.getClass().getDeclaredFields();
		if(preName.length==obj.length){			
			for(int i=0;i<obj.length;i++){
				setValueByPropName(preName[i].getName(),obj[i]);				
			}
		}else{
			System.out.println("属性与字段不吻合:结果集与类熟悉长度不一致");
		}		
	}	
	
	public void initBean(String[] parm,Object[] obj){		
		if(parm.length==obj.length){			
			for(int i=0;i<obj.length;i++){
				setValueByPropName(parm[i],obj[i]);
			}
		}else{
			System.out.println("属性与字段不吻合");
		}		
	}	
	
	public void initBean(String parm,Object obj){		
		setValueByPropName(parm,obj);				
	}	
	
	public void initBean(Map<String, Object> obj){		
		Field[] fields=this.getClass().getDeclaredFields();
		for(Field field:fields){
			if(obj.containsKey(field.getName())){
				setValueByPropName(field.getName(),obj.get(field.getName()));
			}
		}
	}	
	
	public void setValueByPropName(String propName,Object obj){
		try {			
			//获得此参数的引用类型CLASS
			Class clazz = getSetterType(propName);			
			
			//反射继承了本类的某个实体中某个属性set方法 一个参数是setXxx的方法名，一个参数是传入此方法的参数类型CLASS		
//			System.out.println(getSetterName(propName));
			
			Method method = this.getClass().getDeclaredMethod(getSetterName(propName), clazz);
			
			Constructor con = null;
			
			if(clazz == Date.class){				
				method.invoke(this,obj == null?null:obj);
			}else{		
				//取得传入参数类型根据字符串实例化的构造函数
				con = clazz.getDeclaredConstructor(String.class);
				
				//加入传入的是空,则传入空,否则传入实例化了的值
				method.invoke(this,obj == null?null:con.newInstance(obj.toString()));	
			}
		} catch (SecurityException e) {			
			e.printStackTrace();
		} catch (NoSuchMethodException e) {			
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {			
			e.printStackTrace();
		} catch (InvocationTargetException e) {			
			e.printStackTrace();
		} catch (InstantiationException e) {						
			e.printStackTrace();
		}
	}
	
	public String getGetterName(String propName) {
		return "get" + propName.substring(0, 1).toUpperCase()
				+ propName.substring(1, propName.length());
	}
	
	public String getSetterName(String propName) {
		return "set" + propName.substring(0, 1).toUpperCase()
				+ propName.substring(1, propName.length());
	}
	
	public Class getSetterType(String propName) {
		Class clazz=null;
		try {
//			System.out.println(propName);
			clazz = this.getClass().getDeclaredField(propName).getType();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		 return clazz;
	}
	
	//继承此类将具有格式化属性能力
	public static String getPreName(Class clazz){		
		String str="";
		Field[] preName=clazz.getDeclaredFields();
		for(Field fie:preName){
			str += fie.getName()+",";
		}		
		return str.substring(0, str.length()-1)+" ";
	}
}

