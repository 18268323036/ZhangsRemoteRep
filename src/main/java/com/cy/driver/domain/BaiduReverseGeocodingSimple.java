package com.cy.driver.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 类描述： 百度逆地理编码 作者 yanst 创建时间： 2015-5-6 下午2:05:35
 */
@SuppressWarnings("serial")
public class BaiduReverseGeocodingSimple implements Serializable {
	private int status;//返回结果状态值， 成功返回0，其他值请查看附录。
	private Data result;

	public class Data {
		private Location location;//lat	纬度坐标 lng	经度坐标
		private String formatted_address;//结构化地址信息
		private AddressComponent addressComponent;
		@SuppressWarnings("unused")
		private Date date;//获取信息时间

		public	class Location {

			private double lng;//纬度坐标 	经度坐标
			private double lat;//	经度坐标

			public double getLng() {
				return lng;
			}
			public void setLng(double lng) {
				this.lng = lng;
			}
			public double getLat() {
				return lat;
			}
			public void setLat(double lat) {
				this.lat = lat;
			}
		}
		public class AddressComponent {
			private String country;//国家
			private String province;//省名
			private String city;//城市名
			private String district;//区县名
			private String street;//街道名
			private String streetNumber;//街道门牌号
			public String getCity() {
				return city;
			}
			public void setCity(String city) {
				this.city = city;
			}
			public String getCountry() {
				return country;
			}
			public void setCountry(String country) {
				this.country = country;
			}
			public String getDistrict() {
				return district;
			}
			public void setDistrict(String district) {
				this.district = district;
			}
			public String getProvince() {
				return province;
			}
			public void setProvince(String province) {
				this.province = province;
			}
			public String getStreet() {
				return street;
			}
			public void setStreet(String street) {
				this.street = street;
			}
			public String getStreetNumber() {
				return streetNumber;
			}
			public void setStreetNumber(String streetNumber) {
				this.streetNumber = streetNumber;
			}
		}
		public Date getDate() {
			return new Date();
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public Location getLocation() {
			return location;
		}
		public void setLocation(Location location) {
			this.location = location;
		}
		public String getFormatted_address() {
			return formatted_address;
		}
		public void setFormatted_address(String formatted_address) {
			this.formatted_address = formatted_address;
		}
		public AddressComponent getAddressComponent() {
			return addressComponent;
		}
		public void setAddressComponent(AddressComponent addressComponent) {
			this.addressComponent = addressComponent;
		}
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Data getResult() {
		return result;
	}
	public void setResult(Data result) {
		this.result = result;
	}
}
