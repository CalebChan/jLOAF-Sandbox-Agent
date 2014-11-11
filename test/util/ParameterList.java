package util;

import java.util.HashMap;

public class ParameterList {
	private HashMap<String, Object> parameterMap;
	
	public ParameterList(){
		this.parameterMap = new HashMap<String, Object>();
	}
	
	public void addParameter(String tag, Object value){
		this.parameterMap.put(tag, value);
	}
	
	public String getStringParam(String tag){
		Object o = this.parameterMap.get(tag);
		if (o instanceof String){
			return (String)o;
		}
		return null;
	}
	
	public int getIntParam(String tag){
		Object o = this.parameterMap.get(tag);
		if (o instanceof Integer){
			return ((Integer)o).intValue();
		}
		return Integer.MIN_VALUE;
	}
	
	public boolean getBoolParam(String tag){
		Object o = this.parameterMap.get(tag);
		if (o instanceof Boolean){
			return ((Boolean)o).booleanValue();
		}
		return false;
	}
}
