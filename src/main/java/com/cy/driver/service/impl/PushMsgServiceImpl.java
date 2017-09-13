package com.cy.driver.service.impl;

import com.cy.driver.common.util.HttpPostUtil;
import com.cy.driver.service.PushMsgService;
import com.cy.pass.service.MessageCenterService;
import com.cy.pass.service.SysPushCounterService;
import com.cy.pass.service.dto.Enum.SysPushCounterEnum;
import com.cy.pass.service.dto.PushSendRecordDTO;
import com.cy.pass.service.dto.SysPushCounterDTO;
import com.cy.pass.service.dto.SysPushCounterParamDTO;
import com.cy.pass.service.dto.base.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanst on 2015/11/3.
 */
@Service("pushMsgService")
public class PushMsgServiceImpl implements PushMsgService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SysPushCounterService sysPushCounterService;

    @Resource
    private MessageCenterService messageCenterService;

    private String sendPushSDKURL;//推送接口地址

    private String pushTitle;//推送接口标题

    private String pushContent;//推送接口内容

    public void findPushCounter(SysPushCounterParamDTO sysPushCounterParamDTO)
    {
        Response<SysPushCounterDTO> response = sysPushCounterService.findPushCounter(sysPushCounterParamDTO);
        if(response.isSuccess()){
            if(response.getData() == null){
                boolean flag = push(String.valueOf(sysPushCounterParamDTO.getUserId()));
                if(flag){
                    SysPushCounterParamDTO sysPushCounterParamDTO1 = new SysPushCounterParamDTO();
                    sysPushCounterParamDTO1.setCount(sysPushCounterParamDTO.getCount());
                    sysPushCounterParamDTO1.setPushType(SysPushCounterEnum.PushType.REG_RED_PAC.getCode());
                    sysPushCounterParamDTO1.setUserKind(SysPushCounterEnum.UserKind.DRIVER_USER.getCode());
                    sysPushCounterParamDTO1.setUserId(sysPushCounterParamDTO.getUserId());
                    Response<Boolean> response1 = sysPushCounterService.insert(sysPushCounterParamDTO1);
                }
            }else
            {
                if(sysPushCounterParamDTO.getCount().intValue() == 0){
                    boolean flag = push(String.valueOf(sysPushCounterParamDTO.getUserId()));
                    if(flag) {
                        sysPushCounterParamDTO.setCount(1);
                        Response<Boolean> response1 = sysPushCounterService.update(sysPushCounterParamDTO);
                    }
                }
            }
        }
    }

    @Value("${inside.push.api.sendPushSDKURL}")
    public void setSendPushSDKURL(String sendPushSDKURL) {
        this.sendPushSDKURL = sendPushSDKURL;
    }

    @Value("${dctms.download.sendPushSDKURL.pushTitle}")
    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    @Value("${dctms.download.sendPushSDKURL.pushContent}")
    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    public boolean insert(Long driverId, Long logId, byte returnStatus){
        PushSendRecordDTO pushSendRecordDTO = new PushSendRecordDTO();
        pushSendRecordDTO.setEventFrom((byte) 3);
        pushSendRecordDTO.setDriverId(driverId);
        pushSendRecordDTO.setPushId(logId);
        pushSendRecordDTO.setPushKind((byte) 1);
        pushSendRecordDTO.setPushTitle(pushTitle);
        pushSendRecordDTO.setPushContent(pushContent);
        pushSendRecordDTO.setReturnStatus(returnStatus);
        pushSendRecordDTO.setJumpType("2");
       return  messageCenterService.insert(pushSendRecordDTO).getData();
    }

    /**
     *
     */
    public Boolean push(String driverId){
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("pushChannel", "1");
        paramMap.put("type", "4");
        paramMap.put("driverId", driverId);
        paramMap.put("pushTitle", pushTitle);
        paramMap.put("pushContent", pushContent);
        paramMap.put("eventFrom", "3");
        paramMap.put("jumpType", "2");
        String jsonString =  HttpPostUtil.postXml(sendPushSDKURL, paramMap);

        ObjectMapper mapper = new ObjectMapper();
        int code = 0;//0失败
        String message = "";
        String data = "";
        try {
            JsonNode jsonNode = mapper.readTree(jsonString);
            //推送接口发送返回状态 0失败，1成功
            code = jsonNode.path("errorCode") != null ? jsonNode.path("errorCode").asInt() : 0;
            message = jsonNode.path("errorMsg") != null ? jsonNode.path("errorMsg").asText() : "失败！";//pk-t_push_log_info.id
            data = jsonNode.path("object") != null ? jsonNode.path("object").asText() : "";
            insert(Long.valueOf(driverId), Long.valueOf(data==""?"0":data),(byte)(code == 1?0:1));
        } catch (IOException e) {
            message = "解析返回json报错！";
            logger.debug("解析返回json报错！");
            return false;
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug(e.getMessage());
            return false;
        }
        if(code == 0){
            logger.debug("新注册用户发送红包消息推送失败！"+message);
            return false;
        }
        return true;
    }

}
