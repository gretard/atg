package edu.ktu.atg.common.models;

import java.io.Serializable;

public class ParamInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3178505057667420142L;
	private String methodName;
	public String getMethodName() {
		return methodName;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getNo() {
		return no;
	}

	public boolean isComplex() {
		return isComplex;
	}

	private String name;
	private String type;
	private int no;
	private boolean isComplex;

	public ParamInfo(String methodName, String name, String type, int no, boolean isComplex) {
		this.methodName = methodName;
		this.name = name;
		this.type = type;
		this.no = no;
		this.isComplex = isComplex;

	}
}
