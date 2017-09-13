package com.cy.driver.service;

import com.cy.driver.domain.MyhomeInfo;
import com.cy.pass.service.dto.*;
import com.cy.pass.service.dto.base.Response;
import com.cy.pass.service.dto.driverAuth.DriverImgTypeDTO;

import java.util.Date;
import java.util.List;

/**
 * 司机用户业务 Created by mr on 2015/6/25.
 */
public interface DriverUserHandlerService {

	/**
	 * 获取用户信息
	 * @param dirverId 司机ID
	 * @return
	 */
	Response<DriverUserInfoDTO> getDriverUserInfo(Long dirverId);

	/**
	 * 获取司机图片信息
	 * @param driverId
	 * @param imgType
     * @return
     */
	Response<String> getImgInfo(Long driverId, Byte imgType);

	/**
	 * 开通提现帐号业务
	 * @param dirverId
	 * @return
	 */
	public Long openCapitalAccount(Long dirverId);

	/**
	 * 获取认证信息
	 * @param  driverId
	 * @return
	 */
	public Response<List<AuthenticationInfoDTO>> getAuthenticationInfo(long driverId);
	
	/**
	 * 实名认证
	 * @param  driverId
	 * @param realName	真实姓名
	 * @param card	身份证号
	 * @param positiveCardMd5  身份证正面
	 * @param oppositeCardMd5 身份证反面
	 * @return
	 */
	public Response<Boolean> authenticationRealName(long driverId, String realName, String card, String positiveCardMd5, String oppositeCardMd5);
	/**
	 * 驾驶证认证
	 * @param  driverId
	 * @param fileMd5
	 * @return
	 */
	public Response<Boolean> authenticationDrivingLicense(long driverId, String fileMd5) ;

	/**
	 * 车辆认证
	 * @param driverId
	 * @param carCard
	 * @param fileMd5
	 * @return
	 */
	public Response<Boolean> authenticationCar(long driverId, String carCard, String fileMd5);

	Boolean authenticationAllInfo(long driverId, String carNumber, String idNumber, List<DriverImgTypeDTO> driverImgTypeDTOs, String realName);
	
	/**
	 * 修改用户姓名
	 * @param driverId
	 * @param userName
	 * @return
	 */
	public Response<Boolean> updateUserName(Long driverId, String userName);
	
	/**
	 * 修改登录名
	 * @param driverId
	 * @param code
	 * @return
	 */
	public Response<String> updateLoginName(Long driverId, String code);
	
	/**
	 * 获取我的主页信息
	 * @param token
	 * @param driverId
	 * @param authState
	 * @return
	 */
	public Response<MyhomeInfo> myHomeInfo(String token, Long driverId, Byte authState);
	
	/**
	 * 修改imei
	 * @param token 
	 * @param imei
	 * @return
	 */
	public Response<Boolean> updateImei(String token, String imei);

	/**
	 * 权限验证
	 * @param token
	 * @param newdate
	 * @return
	 */
	public Response<DriverPermissionDTO> permissionValidation(String token, Date newdate);
	
	/**
	 * app司机登录
	 * 
	 * @param loginName
	 *            登录名称
	 * @param password
	 *            登录密码 md5
	 * @param mobileBrand
	 *            手机品牌
	 * @param mobilePhoneModel
	 *            手机型号
	 * @param imei
	 * @return Response<LoginResultInfoDTO>
	 */
	public Response<LoginResultInfoDTO> loginDriverUserInfo(String loginName, String apkVersion, String password, String mobileBrand, String mobilePhoneModel, String imei, Byte source, String lotuseedSid);

	/**
	 * 获取验证码
	 * 
	 * @param mobilePhone
	 *            (必填)
	 * @param weOrApp
	 *            web 0,app 1 (必填)
	 * @param purpose
	 *            0 注册 1修改密码 2忘记密码 3 修改手机号 -1 老用户登录补充手机号(必填)
	 * @param requestIp
	 *            客户ip (必填)
	 * @return 返回最新一次验证码记录
	 */
	public Response<String> getAuthCode(String mobilePhone, Byte weOrApp, Byte purpose, String requestIp);

	/**
	 * 快速注册
	 * 
	 * @param userInfoDTO
	 * @param authCode
	 *            验证码
	 * @return userId
	 */
	public Response<Long> quickRegist(DriverUserInfoDTO userInfoDTO, String authCode);

	/**
	 * 检查 验证码是否正确
	 * 
	 * @param mobilePhone
	 * @param authCode
	 * @param purpose
	 * @return
	 */
	public Response<Boolean> validAuthCode(String mobilePhone, String authCode, Byte purpose);

	/**
	 * 保存新密码
	 * 
	 * @param mobilPhone
	 *            账户
	 * @param newPassWd
	 *            新密码
	 * @return userId
	 */
	public Response<Long> resetPasswd(String mobilPhone, String newPassWd, String imeiNO);


    /**
     * 获取车辆信息
     * @param driverId
     * @return
     */
    public Response<CarDTO> getCarInfo(String driverId);

    /**
     * 修改车辆
     * @param carDTO
     * @return
     */
    public Response<Boolean> updateCarInfo(CarDTO carDTO);

    /**
     * 修改密码
     * @param mobilePhone
     * @param oldPassWd
     * @param newPassWd
     * @return
     */
    public Response<String> updatePassWd(String mobilePhone, String oldPassWd, String newPassWd);

    /**
     * 用户退出
     * @param driverId
     * @return
     */
    public Response<Boolean> driverQuit(String driverId);

    /**
     * 修改司机工作状态
     * @param driverId
     * @param workState 0:拒单 1:接单
     * @return
     */
    public Response<Boolean> updateWorkState(Long driverId, Integer workState);

	/**
	 * 修改 极光推送绑定id
	 * @param driverId
	 * @param registrationId 极光推送绑定id
	 * @return
	 */
	public Response<Boolean> updateRegistrationId(String driverId, String registrationId);

    /**
     * 获得司机信息
     * @param driverId 司机id
     * @return
     * @author wyh
     */
    DriverUserInfoDTO getDriverInfo(Long driverId);

    /**
     * 刷新司机缓存
     * @param driverId
     * @param token
     * @param oldAuthState
     * @param authStateStr
     * @return
     * @author wyh
     */
    boolean refreshDriverToken(Long driverId, String token, Byte oldAuthState, String authStateStr);

	/**
	 * 获取司机信息带头像,认证信息等
	 * @param driverId
	 * @return
     */
	DriverInfoDTO getDriverInfoIncludeImg(Long driverId);

	DriverItemStatDTO findBusinessInfo(Long driverId);

	boolean modifyDriverInfo(DriverModifyDTO driverModifyDTO);

	/**
	 * 3.5.1版本  获取最新的一条消息提醒记录
	 * @param messageQueryDTO
	 * @return
     */
	MessageRemindDTO getLastMessage(MessageQueryDTO messageQueryDTO);

	/**
	 * 修改消息查看状态
	 * @param messageRemindId
	 * @param modifyState
     * @return
     */
	boolean modifyMessageState(Long messageRemindId, Byte modifyState);

	/**
	 * 获取业务信息
	 * @param keyId
	 * @return
     */
	MessageRemindDTO getBusinessInfo(Long keyId) ;
}
