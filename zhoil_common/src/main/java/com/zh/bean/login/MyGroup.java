package com.zh.bean.login;

import java.util.ArrayList;

/**
 * 分组表
 * 
 */
public class MyGroup extends BaseBean{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 分组
     */
    private ArrayList<MyStaffVehiTask> updateStaffArr;

	/**
	 * @return the updateStaffArr
	 */
	public ArrayList<MyStaffVehiTask> getUpdateStaffArr() {
		return updateStaffArr;
	}

	/**
	 * @param updateStaffArr the updateStaffArr to set
	 */
	public void setUpdateStaffArr(ArrayList<MyStaffVehiTask> updateStaffArr) {
		this.updateStaffArr = updateStaffArr;
	}
  
}