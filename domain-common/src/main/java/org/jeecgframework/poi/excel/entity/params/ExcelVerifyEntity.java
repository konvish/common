package org.jeecgframework.poi.excel.entity.params;

/**
 * Excel 校验对象
 * 
 * @author JueYue
 * @date 2014年6月29日 下午4:24:59
 */
public class ExcelVerifyEntity {

	/**
	 * 接口校验
	 * 
	 * @return
	 */
	private boolean interHandler;

	/**
	 * 不允许空
	 * 
	 * @return
	 */
	private boolean notNull;

	/**
	 * 是13位移动电话
	 * 
	 * @return
	 */
	private boolean isMobile;
	/**
	 * 是座机号码
	 * 
	 * @return
	 */
	private boolean isTel;

	/**
	 * 是电子邮件
	 * 
	 * @return
	 */
	private boolean isEmail;

	/**
	 * 最小长度
	 * 
	 * @return
	 */
	private int minLength;

	/**
	 * 最大长度
	 * 
	 * @return
	 */
	private int maxLength;

	/**
	 * 正在表达式
	 * 
	 * @return
	 */
	private String regex;
	/**
	 * 正在表达式,错误提示信息
	 * 
	 * @return
	 */
	private String regexTip;

	public boolean isInterHandler() {
		return interHandler;
	}
	public void setInterHandler(boolean interHandler) {
		this.interHandler = interHandler;
	}
	public boolean isNotNull() {
		return notNull;
	}
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}
	public boolean isMobile() {
		return isMobile;
	}
	public void setMobile(boolean isMobile) {
		this.isMobile = isMobile;
	}
	public boolean isTel() {
		return isTel;
	}
	public void setTel(boolean isTel) {
		this.isTel = isTel;
	}
	public boolean isEmail() {
		return isEmail;
	}
	public void setEmail(boolean isEmail) {
		this.isEmail = isEmail;
	}
	public int getMinLength() {
		return minLength;
	}
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}
	public String getRegexTip() {
		return regexTip;
	}
	public void setRegexTip(String regexTip) {
		this.regexTip = regexTip;
	}

}
