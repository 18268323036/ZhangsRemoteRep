package com.cy.driver.action;

import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.common.util.ValidateUtil;
import com.cy.driver.domain.MyhomeInfo;
import com.cy.driver.domain.MyhomeInfoBO;
import com.cy.driver.service.*;
import com.cy.pass.service.dto.Enum.AuthCodeType;
import com.cy.pass.service.dto.Enum.DriverImgType;
import com.cy.pass.service.dto.Enum.WorkStatus;
import com.cy.pass.service.dto.base.CodeTable;
import com.cy.pass.service.dto.base.Response;
import com.cy.pass.service.dto.init.MsgCenterCountDTO;
import com.cy.platformpay.service.dto.AccountPassWdDTO;
import com.cy.platformpay.service.dto.CapitalAccountDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Scope("prototype")
@RestController("driverPersonInfoAction")
public class DriverPersonInfoAction extends BaseAction {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Resource
	private DriverImgService driverImgService;
	@Resource
	private DriverUserHandlerService driverUserHandlerService;
	@Resource
	private QueryOrderService queryOrderService;
	@Resource
	private AccountService accountService;
	@Resource
	private PointFService pointFService;

	/**
	 * 个人头像修改
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/updatePersonnelPhotos", method = RequestMethod.POST)
	@ResponseBody
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.UPDATE_PERSONNEL_PHOTOS)
	@Log(type = LogEnum.UPDATE_PERSONNEL_PHOTOS)
	public Object updatePersonnelPhotos(HttpServletRequest request, HttpServletResponse response, String personnelPhotosMd5) {
		try {
			if (StringUtils.isEmpty(personnelPhotosMd5))
				 {
				if (logger.isErrorEnabled())
					logger.error("个人头像修改：参数不能为空【不合法】。");
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
			}

			Response<Boolean> resultInfo = driverImgService.uploadDriverImg(findToken(request), personnelPhotosMd5, DriverImgType.PERSON_PHOTO.getValue());
			
			if (resultInfo.isSuccess()) {
				updRespHeadSuccess(response);
				//完善头像积分奖励-3.4版本
				pointFService.pointReward(Constants.AWARD_DRIVER,findUserId(request), Constants.CHECK_MODE_BY_EVENT,
								LogEnum.UPDATE_PERSONNEL_PHOTOS.getEventCode(),null,null,convert2InSource(),null);
				return null;
			}
			// 参数不完整
			if (resultInfo.getCode() == CodeTable.INVALID_ARGS.getCode()) {
				if (logger.isErrorEnabled())
					logger.error("个人头像修改（服务端）校验：参数不完整.");
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
			}
			if (resultInfo.getCode() == CodeTable.EXCEPTION.getCode()) {
				if (logger.isErrorEnabled())
					logger.error("个人头像修改(服务端)==>异常");
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
			}
			
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("个人头像修改 出错- " + e.getMessage());
			}
			e.printStackTrace();
		}
		updRespHeadError(response);
		return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
	}
	
	/**
	 * 3.1.14	用户姓名修改
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
	@ResponseBody
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.UPDATE_USER_NAME)
	@Log(type = LogEnum.UPDATE_USER_NAME)
	public Object updateUserName(HttpServletRequest request, HttpServletResponse response, String userNameNew) {
		try {
			if (StringUtils.isEmpty(userNameNew)) {
				if (logger.isErrorEnabled())
					logger.error("用户姓名修改：用户姓名不为空。");
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20059);
			}

			Response<Boolean> resultInfo = driverUserHandlerService.updateUserName(findUserId(request), userNameNew);
			if (resultInfo.isSuccess()) {
				updRespHeadSuccess(response);
				return null;
			}
			// 参数不完整
			if (resultInfo.getCode() == CodeTable.INVALID_ARGS.getCode()) {
				if (logger.isErrorEnabled())
					logger.error("用户姓名修改（服务端）校验：参数不完整.");
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
			}
			if (resultInfo.getCode() == CodeTable.EXCEPTION.getCode()) {
				if (logger.isErrorEnabled())
					logger.error("用户姓名修改(服务端)==>异常");
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
			}
            if(resultInfo.getCode() == CodeTable.INVALID_CODE.getCode()){
                if(logger.isErrorEnabled())
                    logger.error("用户姓名修改(服务端)==>用户不存在");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20008);
            }
            if(resultInfo.getCode() == CodeTable.AUTH_DRIVE.getCode()){
                if(logger.isErrorEnabled())
                    logger.error("用户姓名修改(服务端)==>已认证或者认证中用户不能修改姓名");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.RULE_30102);
            }
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("用户姓名修改 出错- " + e.getMessage());
			}
			e.printStackTrace();
		}
		updRespHeadError(response);
		return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
	}

    /**
     * 3.1.13	登录名修改
     * @param request
     * @param response
     * @param mobilePhoneNew 新手机号
     * @param verificationCode 新手机的验证码
     * @param oldMobilePhone 旧手机号
     * @param oldVerificationCode 旧手机的验证码
     * @return
     */
	@RequestMapping(value = "/updateLoginName", method = RequestMethod.POST)
	@ResponseBody
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.UPDATE_LOGIN_NAME)
	@Log(type = LogEnum.UPDATE_LOGIN_NAME)
	public Object updateLoginName(HttpServletRequest request, HttpServletResponse response, String mobilePhoneNew , String verificationCode , String oldMobilePhone , String oldVerificationCode) {
		try {
			if ( StringUtils.isEmpty(mobilePhoneNew)
					|| !ValidateUtil.validateTelePhone(mobilePhoneNew)) {
				if (logger.isErrorEnabled())
					logger.error("登录名修改：手机号码校验不正确。");
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20032);
			}
			if (StringUtils.isBlank(verificationCode)) {
				if (logger.isErrorEnabled())
					logger.error("登录名修改：验证码不能为空");
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20106);
			}

			//TODO ios审核使用
			if(findUserId(request)!=103759) {
				//旧手机验证码验证
				Response<Boolean> resultold = driverUserHandlerService.validAuthCode(oldMobilePhone, oldVerificationCode, AuthCodeType.UPDATE_PHONE.getValue());
				if (!resultold.isSuccess()) {
					updRespHeadError(response);
					//参数不完整
					if (resultold.getCode() == CodeTable.INVALID_ARGS.getCode()) {
						if (logger.isErrorEnabled()) logger.error("验证 验证码（服务端）校验：参数不完整.");
						return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
					}
					//验证码失效
					if (resultold.getCode() == CodeTable.LI_VALID_TIMES.getCode()) {
						if (logger.isErrorEnabled()) logger.error("验证 验证码（服务端）校验：验证码失效.");
						return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20031);
					}
					//号码未注册
					if (resultold.getCode() == CodeTable.INVALID_CODE.getCode()) {
						if (logger.isErrorEnabled()) logger.error("验证 验证码（服务端）校验：账户未注册.");
						return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20107);
					}
					return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
				}

				//新手机验证码验证
				Response<Boolean> result = driverUserHandlerService.validAuthCode(mobilePhoneNew, verificationCode, AuthCodeType.UPDATE_NEW_PHONE.getValue());

				if (!result.isSuccess()) {
					updRespHeadError(response);
					// 参数不完整
					if (result.getCode() == CodeTable.INVALID_ARGS.getCode()) {
						if (logger.isErrorEnabled())
							logger.error("登录名修改（服务端）校验：参数不完整.");
						return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
					}
					// 验证码失效
					if (result.getCode() == CodeTable.LI_VALID_TIMES.getCode()) {
						if (logger.isErrorEnabled())
							logger.error("登录名修改（服务端）校验：验证码失效.");
						return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20031);
					}
					return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
				}

			}
			Response<String> resultInfo = driverUserHandlerService.updateLoginName(findUserId(request), mobilePhoneNew);
			if (resultInfo.isSuccess()) {
				updRespHeadSuccess(response);
				Map<String ,String > resultMap = new HashMap<String ,String >();
				resultMap.put("token",resultInfo.getData());
				resultMap.put("userId",findUserIdStr(request));
				return resultMap;
			}
			// 参数不完整
			if (resultInfo.getCode() == CodeTable.INVALID_ARGS.getCode()) {
				if (logger.isErrorEnabled())
					logger.error("登录名修改（服务端）校验：参数不完整.");
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
			}
			if (resultInfo.getCode() == CodeTable.REGISTERED.getCode()) {
				if (logger.isErrorEnabled())
					logger.error("登录名修改（服务端）校验：新的登录账号已经注册。.");
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20060);
			}
			if (resultInfo.getCode() == CodeTable.EXCEPTION.getCode()) {
				if (logger.isErrorEnabled())
					logger.error("登录名修改 错误信息(服务端)==>异常");
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
			}
			
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("登录名修改出错 - " + e.getMessage());
			}
			e.printStackTrace();
		}
		updRespHeadError(response);
		return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
	}

	@Resource
	private BankCardHandlerService bankCardHandlerService;

	@Resource
	private MainBmikeceService mainBmikeceService;

	@Resource
	private MsgCenterService msgCenterService;
	
	/**
	 * 3.1.12	我的主页信息查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/myHomeInfo", method = RequestMethod.POST)
	@ResponseBody
	@ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.MY_HOME_INFO)
	@Log(type = LogEnum.MYHOME_INFO)
	public Object myHomeInfo(HttpServletRequest request, HttpServletResponse response) {
		try {

			//待收运费数量
			int freightNums = queryOrderService.collectFreightNums(findUserId(request));

			//银行卡张数
			int bankNums = bankCardHandlerService.countByAccountId(findcapitalAccountId(request));

			//获取是否有设置密码
			com.cy.platformpay.service.dto.base.Response<AccountPassWdDTO> accountPassWdDTOResponse = accountService.passWdOptions(findcapitalAccountId(request));
			Boolean isSetUpPassword = true;//默认未设置
			if(accountPassWdDTOResponse.isSuccess())
			{
				if(accountPassWdDTOResponse.getData()!=null){
                    /**
                     * 修改时间：2016-03-29 10:30
                     * 修改人：王远航
                     * 修改依据：支付密码和提现密码合成交易密码，数据库保留支付密码。兼容货主app1.1.1
                     */
					isSetUpPassword = accountPassWdDTOResponse.getData().getHasPayPassWd();
				}
			}

			//账户余额
			com.cy.platformpay.service.dto.base.Response<CapitalAccountDTO> resultTotalMoney = mainBmikeceService.queryAccountInfo(findcapitalAccountId(request));
			String totalMoney = "0.00";
			if(resultTotalMoney.isSuccess())
			{
				if(resultTotalMoney.getData()!=null){
					BigDecimal totalMoney1 = resultTotalMoney.getData().getTotalMoney() == null ? new BigDecimal("0.00") : resultTotalMoney.getData().getTotalMoney();
					BigDecimal LuckyMoney1 = resultTotalMoney.getData().getLuckyMoney() == null ? new BigDecimal("0.00") : resultTotalMoney.getData().getLuckyMoney();
					totalMoney =totalMoney1.add(LuckyMoney1)+"";
				}
			}

			int notReadTradeNum = 0;
			int notReadNoticeNum = 0;
			//未读消息数量
			MsgCenterCountDTO resultMsgcount = msgCenterService.notReadMessageCount(findUserId(request));
			if(resultMsgcount !=null ){
				notReadNoticeNum = resultMsgcount.getNotReadNoticeNum();
				notReadTradeNum = resultMsgcount.getNotReadTradeNum();
			}
			Response<MyhomeInfo> resultInfo =driverUserHandlerService.myHomeInfo(findToken(request), findUserId(request), findAuthState(request));

			//参数不完整
            if(resultInfo.getCode() == CodeTable.INVALID_ARGS.getCode()){
                if (logger.isErrorEnabled()) logger.error("我的主页信息查询（服务端）校验：参数不完整.");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
			if(resultInfo.getCode()==CodeTable.TOKEN_EXPIRE.getCode())
			{
				if(logger.isErrorEnabled())logger.error("我的主页信息查询(服务端)==>token失效");
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20095);
			}
			if(resultInfo.getCode()==CodeTable.EXCEPTION.getCode())
			{
				if(logger.isErrorEnabled())logger.error("我的主页信息查询(服务端)==>异常");
				updRespHeadError(response);
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
			}
			if(resultInfo.isSuccess())
			{

				updRespHeadSuccess(response);
				if(resultInfo.getData() == null ){
					return null;
				}
				return convertMyHomeInfo(resultInfo.getData(), totalMoney, freightNums, isSetUpPassword, bankNums, notReadTradeNum, notReadNoticeNum);
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("我的主页信息查询出错 - " + e.getMessage());
			}
			e.printStackTrace();
		}
		updRespHeadError(response);
		return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
	}

	/**
	 *
	 * @param myhomeInfo  主页信息
	 * @param amount  账户余额
	 * @param capitalFreightNum 待收运费数量
	 * @param isSetUpPassword 是否需要设置支付密码
	 * @param bankCardNum 银行卡张数
	 * @return
	 */
	public MyhomeInfoBO convertMyHomeInfo(MyhomeInfo myhomeInfo, String amount, int capitalFreightNum, Boolean isSetUpPassword, int bankCardNum, int notReadTradeNum, int notReadNoticeNum){

		MyhomeInfoBO myhomeInfoBO = new MyhomeInfoBO();
		myhomeInfoBO.setAuthStatus(myhomeInfo.getAuthStatus());
		myhomeInfoBO.setCarCard(myhomeInfo.getCarCard());
		myhomeInfoBO.setEmptyCarReportNumber(myhomeInfo.getEmptyCarReportNumber());
		myhomeInfoBO.setPhotosAddress(myhomeInfo.getPhotosAddress());
		myhomeInfoBO.setRegisterTime(myhomeInfo.getRegisterTime());
		myhomeInfoBO.setUserName(myhomeInfo.getUserName());
		myhomeInfoBO.setOftenRunCityNums(myhomeInfo.getOftenRunCityNums());
		myhomeInfoBO.setAmount(amount);
		myhomeInfoBO.setBankCardNum(bankCardNum);
		myhomeInfoBO.setIsSetUpPassword(isSetUpPassword ? "0" : "1");
		myhomeInfoBO.setCapitalFreightNum(capitalFreightNum);
		myhomeInfoBO.setNotReadTradeNum(notReadTradeNum);
		myhomeInfoBO.setNotReadNoticeNum(notReadNoticeNum);
		return myhomeInfoBO;
	}

    /**
     * 修改密码
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateDriverPassWd", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_UPDATE_PASSWD)
	@Log(type = LogEnum.UPDATE_DRIVER_PASSWD)
    public Object updateDriverPassWd(HttpServletRequest request, HttpServletResponse response, String oldPassword, String passwordNew) {
        Map<Object, Object> resultMap = new HashMap<Object, Object>();
        //校验
        if(StringUtils.isBlank(oldPassword) || StringUtils.isBlank(passwordNew)){
            if (logger.isErrorEnabled()) logger.error("修改密码校验信息：参数不完整");
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }
        //业务
        String userId = findUserIdStr(request);

        Response<String> serResponse = driverUserHandlerService.updatePassWd(userId, oldPassword, passwordNew);
        if(!serResponse.isSuccess()){
            updRespHeadError(response);
            if(serResponse.getCode() == CodeTable.ERROR.getCode()){
                if (logger.isErrorEnabled()) logger.error("修改密码(服务端)校验信息：参数不完整");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }else if(serResponse.getCode() == CodeTable.PASSWORD_ERROR.getCode()){
                if (logger.isErrorEnabled()) logger.error("修改密码(服务端)校验信息：旧密码错误");
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20102);
            }
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
		updRespHeadSuccess(response);
		resultMap.put("token",serResponse.getData());
		resultMap.put("userId",findUserIdStr(request));
        return resultMap;
    }

    @RequestMapping(value = "/answerListSwitch", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_ACCESS_ORDER_SWITCH)
	@Log(type = LogEnum.ANSWER_LIST_SWITCH)
    public Object answerListSwitch(HttpServletRequest request, HttpServletResponse response, Integer workStatus) {
        Map<Object, Object> resultMap = new HashMap<Object, Object>();
        //校验
        if(workStatus == null || StringUtils.isBlank(WorkStatus.contain(workStatus, WorkStatus.values()))){
            if (logger.isErrorEnabled()) logger.error("维护司机接单开关校验:开关数据为空或者值不正确.");
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
        }

        try{
            Long driverId = findUserId(request);
            Response<Boolean> serResponse = driverUserHandlerService.updateWorkState(driverId, workStatus);
            if(!serResponse.isSuccess()){
                updRespHeadError(response);
                if(serResponse.getCode() == CodeTable.ERROR.getCode()){
                    if (logger.isErrorEnabled()) logger.error("维护司机接单开关(服务端)校验信息：参数不完整");
                    return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
                }
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
            }
            updRespHeadSuccess(response);
            return resultMap;
        }catch (Exception e){
            logger.error("维护司机接单开关出现异常.",e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }

    /**
     * 退出
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/signOut", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.REQ_CODE_USER_QUIT)
	@Log(type = LogEnum.SIGN_OUT)
    public Object signOut(HttpServletRequest request, HttpServletResponse response) {
        Map<Object, Object> resultMap = new HashMap<Object, Object>();
		try{
			//业务
			String userId = findUserIdStr(request);

			Response<Boolean> serResponse = driverUserHandlerService.driverQuit(userId);
			if(!serResponse.isSuccess()){
				updRespHeadError(response);
				if(serResponse.getCode() == CodeTable.ERROR.getCode()){
					if (logger.isErrorEnabled()) logger.error("退出(服务端)校验信息：参数不完整");
					return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
				}
				return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
			}
			updRespHeadSuccess(response);
			return resultMap;
		}
		catch (Exception e){
			if(logger.isErrorEnabled()) logger.error("退出失败。。");
		}
      return findException(response);
    }
}
