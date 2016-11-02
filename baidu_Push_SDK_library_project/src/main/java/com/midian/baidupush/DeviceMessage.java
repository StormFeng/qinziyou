package com.midian.baidupush;

import com.midian.fastdevelop.afinal.annotation.sqlite.Id;
import com.midian.fastdevelop.afinal.annotation.sqlite.Table;

import org.json.JSONObject;

/**
 * 预约推送
 * 
 * @author chu
 * 
 */
@Table(name = "DeviceMessage")
public class DeviceMessage extends MessageBase {

	@Id(column = "id")
	private int id; // 表id
	private String page;// 预约时间
	private String noticeCode;// 标题
	private String roletype;// 内容
	private String notice;
	private JSONObject data;

	public DeviceMessage() {
		super();
	}

	public DeviceMessage(String page, String noticeCode, String roletype, String notice, JSONObject data) {
		this.page = page;
		this.noticeCode = noticeCode;
		this.roletype = roletype;
		this.notice = notice;
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getNoticeCode() {
		return noticeCode;
	}

	public void setNoticeCode(String noticeCode) {
		this.noticeCode = noticeCode;
	}

	public String getRoletype() {
		return roletype;
	}

	public void setRoletype(String roletype) {
		this.roletype = roletype;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}
}
