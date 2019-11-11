/**
 * @工程名称：datapot-es
 * @程序包名：com.datapot.es.domain
 * @程序类名：QueryTimeRange.java
 * @创建日期：2017年8月31日
 */
package com.wyj.javautil.es;

public class TimeRangeQ {

	// 开始时间的Long类型值
	private Long beganTime;
	// 结束时间的Long类型值
	private Long endTime;

	public Long getBeganTime() {
		return beganTime;
	}

	public void setBeganTime(Long beganTime) {
		this.beganTime = beganTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public TimeRangeQ(Long beganTime, Long endTime) {
		this.beganTime = beganTime;
		this.endTime = endTime;
	}

	public TimeRangeQ() {
	}
}
