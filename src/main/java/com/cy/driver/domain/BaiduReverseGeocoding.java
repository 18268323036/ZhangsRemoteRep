package com.cy.driver.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 类描述： 百度逆地理编码 作者 yanst 创建时间： 2015-5-6 下午2:05:35
 */
@SuppressWarnings("serial")
public class BaiduReverseGeocoding implements Serializable {
	/**
	 * 
	 * @author yanst
	 * @since 2015-5-6下午2:05:26
	 */

	private int status;//返回结果状态值， 成功返回0，其他值请查看附录。
	private Data result;

	public class Data {

		private Location location;//lat	纬度坐标 lng	经度坐标
		
		private String formatted_address;//结构化地址信息
		private String business;//所在商圈信息，如 "人民大学,中关村,苏州街"
		private AddressComponent addressComponent;
		private Object[] poiRegions;
		private String sematicDescription;
		private int cityCode;
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
			private String countryCode;//国家code
			private String province;//省名
			private String city;//城市名
			private String district;//区县名
			private String street;//街道名
			private String streetNumber;//街道门牌号
			private String direction;//和当前坐标点的方向，当有门牌号的时候返回数据
			private String distance;//和当前坐标点的距离，当有门牌号的时候返回数据

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

			public String getDirection() {
				return direction;
			}

			public void setDirection(String direction) {
				this.direction = direction;
			}

			public String getDistance() {
				return distance;
			}

			public void setDistance(String distance) {
				this.distance = distance;
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

			public String getCountryCode() {
				return countryCode;
			}

			public void setCountryCode(String countryCode) {
				this.countryCode = countryCode;
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

		public String getBusiness() {
			return business;
		}

		public void setBusiness(String business) {
			this.business = business;
		}

		public AddressComponent getAddressComponent() {
			return addressComponent;
		}

		public void setAddressComponent(AddressComponent addressComponent) {
			this.addressComponent = addressComponent;
		}

		public Object[] getPoiRegions() {
			return poiRegions;
		}

		public void setPoiRegions(Object[] poiRegions) {
			this.poiRegions = poiRegions;
		}

		public String getSematicDescription() {
			return sematicDescription;
		}

		public void setSematicDescription(String sematicDescription) {
			this.sematicDescription = sematicDescription;
		}

		public int getCityCode() {
			return cityCode;
		}

		public void setCityCode(int cityCode) {
			this.cityCode = cityCode;
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
