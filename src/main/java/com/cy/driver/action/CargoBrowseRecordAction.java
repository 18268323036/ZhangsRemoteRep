package com.cy.driver.action;

import com.cy.driver.action.convert.CargoBrowseRecordConvert;
import com.cy.driver.common.annotate.ApiResultCodeEnum;
import com.cy.driver.common.annotate.JSonResponse;
import com.cy.driver.common.annotate.ReqRespHeadCode;
import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.enumer.ApiReqCodeEnum;
import com.cy.driver.common.syslog.Log;
import com.cy.driver.common.syslog.LogEnum;
import com.cy.driver.domain.CargoListBO;
import com.cy.driver.domain.PageBase;
import com.cy.driver.service.CargoBrowseRecordService;
import com.cy.search.service.dto.request.IdSourceDTO;
import com.cy.search.service.dto.response.CargoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * 浏览记录
 * @author yanst 2016/4/20 11:09
 */
@Scope("prototype")
@RestController("cargoBrowseRecordAction")
public class CargoBrowseRecordAction extends BaseAction {

    private Logger LOG = LoggerFactory.getLogger(CargoBrowseRecordAction.class);

    @Resource
    private CargoBrowseRecordService cargoBrowseRecordService;

    /**
     * 添加浏览记录
     * @param cargoId 货源id
     * @return
     */
    @RequestMapping(value = "/addCargoBrowseRecord", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.ADD_BROWSE_RECORD)
    @Log(type = LogEnum.ADD_BROWSE_RECORD)
    public Object queryNearByCargo(HttpServletResponse response, Long cargoId, Integer siteSource) {
        try {
            if(LOG.isDebugEnabled())LOG.debug("添加浏览记录接口入参：cargoId={},siteSource={}", cargoId,siteSource);
            if (cargoId == null) {
                if (LOG.isErrorEnabled())
                    LOG.error("添加浏览记录参数校验：货源id【不为空】。");
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20062);
            }
            //查看货源详情增加浏览记录
            Boolean result = cargoBrowseRecordService.add(findUserId(), cargoId, Constants.DRIVER, siteSource);
            if(result == null || !result.booleanValue()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20201);
            }else {
                updRespHeadSuccess(response);
                return null;
            }
        } catch (Exception e) {
            LOG.error("添加浏览记录出错。", e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }

    /**
     * 批量删除货源浏览记录
     * @return
     */
    @RequestMapping(value = "/deletesCargoBrowseRecord", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.DELETES_CARGO_BROWSE_RECORD)
    @Log(type = LogEnum.DELETES_CARGO_BROWSE_RECORD)
    public Object deletesCargoBrowseRecord(HttpServletResponse response, String cargoIds, String cargoSources) {
        try {
            if(LOG.isDebugEnabled())LOG.debug("调用批量删除货源浏览记录接口,cargoIds={}", cargoIds);
            if(StringUtils.isEmpty(cargoIds)){
                if(LOG.isDebugEnabled())LOG.debug("调用批量删除货源浏览记录,货源id不正确，cargoIds={}", cargoIds);
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }

            //常跑城市  去掉结尾,
            if(cargoIds.endsWith(",")){
                cargoIds = cargoIds.substring(0,cargoIds.length()-1);
            }
            String[] listCargoIdStr = cargoIds.split(",");
            String[] listCargoSourceStr = cargoSources.split(",");
            List<IdSourceDTO> idSourceDTOs = new ArrayList<>();
            for(int i=0 ; i<listCargoIdStr.length ; i++) {
                IdSourceDTO idSourceDTO = new IdSourceDTO();
                idSourceDTO.setId(Long.valueOf(listCargoIdStr[i]));
                idSourceDTO.setSource(Integer.valueOf(listCargoSourceStr[i]));
                idSourceDTOs.add(idSourceDTO);
            }
            Boolean result = cargoBrowseRecordService.deletes(idSourceDTOs, findUserId());
            if(result == null || !result.booleanValue()){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20202);
            }else {
                updRespHeadSuccess(response);
                return null;
            }
        } catch (Exception e) {
            LOG.error("批量删除货源浏览记录出错。", e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }

    /**
     * 浏览记录
     * @return
     */
    @RequestMapping(value = "/pageForCargoBrowseRecord", method = RequestMethod.POST)
    @ResponseBody
    @ReqRespHeadCode(reqHeadCode = ApiReqCodeEnum.PAGE_FOR_CARGO_BROWSERECORD)
    @Log(type = LogEnum.PAGE_FOR_CARGO_BROWSERECORD)
    public Object pageForCargoBrowseRecord(HttpServletResponse response, Integer page) {
        try {
            if(LOG.isDebugEnabled())LOG.debug("调用浏览记录列表接口,page={}", page);
            if(page == null || page ==0){
                if(LOG.isDebugEnabled())LOG.debug("调用浏览记录列表接口,页码信息不正确，page={}", page);
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20001);
            }
            PageBase<CargoDTO> pageBase = cargoBrowseRecordService.pageList(findUserId(), page);
            if(pageBase == null){
                updRespHeadError(response);
                return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SER_20113);
            }

            PageBase<CargoListBO> pageBase1 = CargoBrowseRecordConvert.ConvertCargoBrowseRecordInfo(pageBase);
            updRespHeadSuccess(response);
            return pageBase1;
        } catch (Exception e) {
            LOG.error("添加浏览记录出错。", e);
            updRespHeadError(response);
            return JSonResponse.makeHasContentJSonRespone(ApiResultCodeEnum.SYS_10001);
        }
    }


}
