package com.cy.driver.service.impl;

import com.cy.driver.domain.MyhomeInfo;
import com.cy.driver.service.DriverUserHandlerService;
import com.cy.pass.service.*;
import com.cy.pass.service.dto.*;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.pass.service.dto.base.Response;
import com.cy.pass.service.dto.driverAuth.DriverAuthParamDTO;
import com.cy.pass.service.dto.driverAuth.DriverImgTypeDTO;
import com.cy.platformpay.service.CapitalAccountService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 用户处理类 Created by mr on 2015/6/25.
 */
@Service("driverUserHandlerService")
public class DriverUserHandlerServiceImpl implements DriverUserHandlerService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private DriverUserInfoService driverUserInfoService;
	
	@Resource
	private DriverEmptyReportService driverEmptyReportService;
	
	@Resource
	private DriverOftenCitysService driverOftenCitysService;

	@Resource
	private CapitalAccountService capitalAccountService;

	@Resource
	private DriverImgService rpcDriverImgService;

	@Resource
	private DriverItemStatService driverItemStatService;

	@Resource
	private MessageRemindService messageRemindService;

	/**
	 * 获取用户信息
	 * @param dirverId 司机ID
	 * @return
	 */
	public Response<DriverUserInfoDTO> getDriverUserInfo(Long dirverId){
		return driverUserInfoService.getDriverUserInfo(dirverId);
	}

	/**
	 * 获取用户图片信息
	 */
	public Response<String> getImgInfo(Long driverId, Byte imgType){
		Response<String> findImg =rpcDriverImgService.findByType(driverId, imgType);
		return findImg;
	}



	/**
	 * 开通提现帐号业务
	 * @param dirverId
	 * @return Long
	 */
	public Long openCapitalAccount(Long dirverId){
		com.cy.platformpay.service.dto.base.Response<Long>  resultData = capitalAccountService.driverOpen(dirverId, "");
		if(resultData.getData()==null||resultData.getData().longValue()<=0){
			return 0L;
		}
		DriverUserInfoDTO driverUserInfoDTO = new DriverUserInfoDTO();
		driverUserInfoDTO.setCapitalAccountId(resultData.getData());
		driverUserInfoDTO.setId(dirverId);
		driverUserInfoService.updateDriverCapital(driverUserInfoDTO);
		return resultData.getData();
	}

	/**
	 * 获取认证信息
	 * @param driverId
	 * @return
	 */
	@Override
	public Response<List<AuthenticationInfoDTO>> getAuthenticationInfo(long driverId ) {
		 return driverUserInfoService.getAuthenticationInfo(driverId);
	}
	
	
	/**
	 * 实名认证
	 * @param driverId
	 * @param realName	真实姓名
	 * @param card	身份证号
	 * @param positiveCardMd5  身份证正面
	 * @param oppositeCardMd5 身份证反面
	 * @return
	 */
	@Override
	public Response<Boolean> authenticationRealName(long driverId , String realName, String card ,String positiveCardMd5, String oppositeCardMd5) {
		 return driverUserInfoService.authenticationRealName(driverId, realName, card, positiveCardMd5, oppositeCardMd5);
	}
	
	
	/**
	 * 驾驶证认证
	 * @param driverId
	 * @param fileMd5
	 * @return
	 */
	@Override
	public Response<Boolean> authenticationDrivingLicense(long driverId ,String fileMd5) {
		 return driverUserInfoService.authenticationDrivingLicense(driverId, fileMd5);
	}
	
	/**
	 * 车辆认证
	 * @param driverId
	 * @param carCard
	 * @param fileMd5
	 * @return
	 */
	@Override
	public Response<Boolean> authenticationCar(long driverId, String carCard ,String fileMd5) {
		 return driverUserInfoService.authenticationCar(driverId, carCard, fileMd5);
	}

	@Override
	public Boolean authenticationAllInfo(long driverId, String carNumber, String idNumber, List<DriverImgTypeDTO> driverImgTypeDTOs, String realName) {
		DriverAuthParamDTO driverAuthParamDTO = new DriverAuthParamDTO();
		driverAuthParamDTO.setCarNumber(carNumber);
		driverAuthParamDTO.setDriverId(driverId);
		driverAuthParamDTO.setIdNumber(idNumber);
		driverAuthParamDTO.setImgList(driverImgTypeDTOs);
		driverAuthParamDTO.setRealName(realName);
		Response<Boolean> response = driverUserInfoService.driverAuth(driverAuthParamDTO);
		if(response==null){
			logger.isDebugEnabled(); logger.debug("司机信息认证失败");
			return false;
		}
		if(!response.isSuccess() || !response.getData()){
			logger.isDebugEnabled(); logger.debug("司机信息认证失败,失败原因{}",response.getMessage());
			return false;
		}
		if(response.isSuccess() && response.getData()){
			return true;
		}
		return false;
	}


	/**
	 * 修改用户姓名
	 * @param driverId
	 * @param userName
	 * @return
	 */
	@Override
	public Response<Boolean> updateUserName(Long driverId, String userName) {
		return driverUserInfoService.updateUserName(driverId, userName);
	}
	
	/**
	 * 修改登录名
	 * @param diverId
	 * @param code
	 * @return
	 */
	@Override
	public Response<String> updateLoginName(Long diverId,String code) {
		return driverUserInfoService.updateLoginCode(diverId, code);
	}
	
	@Override
	public Response<MyhomeInfo> myHomeInfo(String token, Long driverId, Byte authState) {
		try {
			Response<DriverPersonInfoDTO> driverPersonInfoDTO = driverUserInfoService.getDriverPersonInfo(token);
			if(driverPersonInfoDTO.getData()==null){
				return new Response<MyhomeInfo>(CodeTable.TOKEN_EXPIRE);
			}
			Response<Integer> count = driverEmptyReportService.getEmptyCarReportCount(token);// 空车上报
			Response<Integer> count1 = driverOftenCitysService.selectByOfenCitysCount(token);// 常跑城市
			if (driverPersonInfoDTO.getCode() == CodeTable.INVALID_ARGS.getCode() || count.getCode() == CodeTable.INVALID_ARGS.getCode()
					|| count.getCode() == CodeTable.INVALID_ARGS.getCode()) {
				return new Response<MyhomeInfo>(CodeTable.INVALID_ARGS);
			}
			if (driverPersonInfoDTO.getCode() == CodeTable.EXCEPTION.getCode() || count.getCode() == CodeTable.EXCEPTION.getCode()
					|| count.getCode() == CodeTable.EXCEPTION.getCode()) {
				return new Response<MyhomeInfo>(CodeTable.EXCEPTION);
			}

			DriverPersonInfoDTO personInfoDTO = driverPersonInfoDTO.getData();
			if(driverPersonInfoDTO.isSuccess()){
                /** 刷新司机缓存 */
                refreshDriverToken(driverId, token, authState, personInfoDTO.getAuthenticationStatus());
				MyhomeInfo myhomeInfo = new MyhomeInfo();
				myhomeInfo.setAuthStatus(personInfoDTO.getAuthenticationStatus());
				myhomeInfo.setCarCard(personInfoDTO.getCarNumber());
				myhomeInfo.setEmptyCarReportNumber(String.valueOf(count.getData()));
				myhomeInfo.setPhotosAddress(personInfoDTO.getPersonPhotoMd5());
				myhomeInfo.setRegisterTime(personInfoDTO.getRegisterTime());
				myhomeInfo.setUserName(personInfoDTO.getUserName());
				myhomeInfo.setOftenRunCityNums(String.valueOf(count1.getData()));
				return new Response<MyhomeInfo>(myhomeInfo);
			}
			if(driverPersonInfoDTO.getData() == null ){
				return new Response<MyhomeInfo>(CodeTable.INVALID_CODE);
			}

		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("获取个人信息出错。", e);
			}
		}
		return new Response<MyhomeInfo>(CodeTable.EXCEPTION);
	}

    /**
     * 刷新司机缓存
     */
    @Override
    public boolean refreshDriverToken(Long driverId, String token, Byte oldAuthState, String authStateStr){
        if (StringUtils.isBlank(authStateStr)) {
            logger.error("刷新司机缓存失败，authState为空");
            return false;
        }
        try {
            Byte authState = Byte.parseByte(authStateStr);
            if (oldAuthState != null && oldAuthState.intValue() == authState.intValue()) {
                return true;
            } else {
                Response<Boolean> response = driverUserInfoService.refreshDriverRedis(driverId, token);
                if (response.isSuccess() && response.getData()) {
                    return true;
                } else {
                    logger.error("调用pass服务刷新司机缓存失败，返回信息={}", response.getMessage());
                    return false;
                }
            }
        } catch (Exception e) {
            logger.trace("刷新司机缓存出现异常", e);
            return false;
        }
    }
	
	@Override
	public Response<Boolean> updateImei(String token ,String imei) {
		return driverUserInfoService.updateImei(token, imei);
	}

	@Override
	public Response<DriverPermissionDTO> permissionValidation(String token  ,Date newdate) {
		return driverUserInfoService.permissionValidation(token, newdate);
	}
	
	@Override
	public Response<String> getAuthCode(String mobilePhone, Byte weOrApp, Byte purpose, String requestIp) {
		return driverUserInfoService.getAuthCode(mobilePhone, weOrApp, purpose, requestIp);
	}

	@Override
	public Response<Long> quickRegist(DriverUserInfoDTO userInfoDTO, String authCode) {
		return driverUserInfoService.quickRegist(userInfoDTO, authCode);
	}

	@Override
	public Response<Boolean> validAuthCode(String mobilePhone, String authCode, Byte purpose) {
		return driverUserInfoService.validAuthCode(mobilePhone, authCode, purpose);
	}

	@Override
	public Response<Long> resetPasswd(String mobilPhone, String newPassWd, String imeiNO) {
		return driverUserInfoService.resetPasswd(mobilPhone, newPassWd, imeiNO);
	}

    @Override
    public Response<CarDTO> getCarInfo(String driverId) {
        return driverUserInfoService.getCarInfo(driverId);
    }

    @Override
    public Response<Boolean> updateCarInfo(CarDTO carDTO) {
        return driverUserInfoService.updateCarInfo(carDTO);
    }

    @Override
    public Response<String> updatePassWd(String mobilePhone, String oldPassWd, String newPassWd) {
        return driverUserInfoService.updatePassWd(mobilePhone, oldPassWd, newPassWd);
    }

    @Override
    public Response<Boolean> driverQuit(String driverId) {
        return driverUserInfoService.driverQuit(driverId);
    }

    @Override
    public Response<Boolean> updateWorkState(Long driverId, Integer workState) {
        return driverUserInfoService.updateWorkState(driverId, workState);
    }

    @Override
	public Response<LoginResultInfoDTO> loginDriverUserInfo(String loginName, String apkVersion, String password, String mobileBrand, String mobilePhoneModel, String imei, Byte source, String lotuseedSid) {
		return driverUserInfoService.loginDriverUserInfo(loginName, apkVersion, password, mobileBrand, mobilePhoneModel, imei, source, lotuseedSid);
	}


	@Override
	public Response<Boolean> updateRegistrationId(String driverId, String registrationId) {
		return driverUserInfoService.updateRegistrationId(driverId, registrationId);
	}

    /**
     * 获得司机信息
     */
    @Override
    public DriverUserInfoDTO getDriverInfo(Long driverId){
        Response<DriverUserInfoDTO> result = driverUserInfoService.getDriverUserInfo(driverId);
        if (result == null) {
            logger.error("调用pass服务查询司机信息出错,driverId={}", driverId);
            return null;
        }
        if (!result.isSuccess()) {
            logger.error("调用pass服务查询司机信息失败,driverId={},返回信息={}", driverId, result.getMessage());
            return null;
        }
        if (result.getData() == null) {
            logger.error("调用pass服务查询司机信息失败,driverId={},返回司机信息为空", driverId);
            return null;
        }
        return result.getData();
    }

	/**
	 * 获取司机头像信息
	 */
	@Override
	public DriverInfoDTO getDriverInfoIncludeImg(Long driverId){
		Response<DriverInfoDTO> result = driverUserInfoService.getDriverInfo(driverId);
		if (result == null) {
			logger.error("调用pass服务查询司机信息出错,driverId={}", driverId);
			return null;
		}
		if (!result.isSuccess()) {
			logger.error("调用pass服务查询司机信息失败,driverId={},返回信息={}", driverId, result.getMessage());
			return null;
		}
		if (result.getData() == null) {
			logger.error("调用pass服务查询司机信息失败,driverId={},返回司机信息为空", driverId);
			return null;
		}
		return result.getData();
	}

	/**
	 * 获取司机业务信息
	 */
	@Override
	public DriverItemStatDTO findBusinessInfo(Long driverId){
		Response<DriverItemStatDTO> response = driverItemStatService.findByDriverId(driverId);
		if(response==null){
			logger.isDebugEnabled(); logger.debug("查询司机业务信息失败");
		}
		if(!response.isSuccess()){
			logger.isDebugEnabled(); logger.debug("查询司机业务信息失败,失败原因{}",response.getMessage());
		}
		return response.getData();
	}

	@Override
	public boolean modifyDriverInfo(DriverModifyDTO driverModifyDTO) {
		Response<Boolean> response = driverUserInfoService.modifyDriverInfo(driverModifyDTO);
		if(response==null){
			logger.isDebugEnabled(); logger.debug("更改司机车辆信息信息失败");
		}
		if(!response.isSuccess()){
			logger.isDebugEnabled(); logger.debug("更改司机车辆信息信息失败,失败原因{}",response.getMessage());
		}
		return response.getData();
	}

	@Override
	public MessageRemindDTO getLastMessage(MessageQueryDTO messageQueryDTO) {
		Response<MessageRemindDTO> response = messageRemindService.getLastActivate(messageQueryDTO);
		if(response==null){
			logger.isDebugEnabled(); logger.debug("获取司机最后一条消息失败");
		}
		if(!response.isSuccess()){
			logger.isDebugEnabled(); logger.debug("获取司机最后一条消息失败,失败原因{}",response.getMessage());
		}
		return response.getData();
	}

	@Override
	public boolean modifyMessageState(Long messageRemindId,Byte modifyState) {
		Response<Boolean> response = messageRemindService.modifyViewState(messageRemindId,modifyState);
		if(response==null){
			logger.isDebugEnabled(); logger.debug("修改消息状态失败");
		}
		if(!response.isSuccess()){
			logger.isDebugEnabled(); logger.debug("修改消息状态失败,失败原因{}",response.getMessage());
		}
		return response.getData();
	}

	@Override
	public MessageRemindDTO getBusinessInfo(Long keyId) {
		Response<MessageRemindDTO> response = messageRemindService.get(keyId);
		if(response==null){
			logger.isDebugEnabled(); logger.debug("获取业务信息失败");
		}
		if(!response.isSuccess()){
			logger.isDebugEnabled(); logger.debug("获取业务信息失败,失败原因{}",response.getMessage());
		}
		return response.getData();
	}

}
