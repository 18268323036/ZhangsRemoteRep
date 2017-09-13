package com.cy.driver.service.impl;

import com.cy.driver.common.initdata.SysAreaData;
import com.cy.driver.common.util.ListObjConverter;
import com.cy.driver.service.InitDataService;
import com.cy.pass.service.BankCardBinVersionService;
import com.cy.pass.service.InitializationDataService;
import com.cy.pass.service.dto.BankCardBinVersionDTO;
import com.cy.pass.service.dto.CarBasicDataEntity;
import com.cy.pass.service.dto.SystemAreaDivisionDTO;
import com.cy.pass.service.dto.base.Response;
import com.cy.pass.service.dto.init.Tree;
import com.cy.platformpay.service.BankWyhlCardbinService;
import com.cy.platformpay.service.dto.BankCardbinDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("initDataService")
public class InitDataServiceImpl implements InitDataService{

    @Resource
	private InitializationDataService initializationDataService;

    @Resource
    private BankWyhlCardbinService bankWyhlCardbinService;

    @Resource
    private BankCardBinVersionService bankCardBinVersionService;

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public com.cy.platformpay.service.dto.base.Response<List<BankCardbinDTO>> queryCardbinList() {
        return bankWyhlCardbinService.queryCardbinList();
    }

    /**
     * 根据区域等级获取 对应等级的所有信息
     * @param level
     * @return
     * @throws Exception
     */
    @Override
    public List<SysAreaData> getAreaTableDataLevel(int level) throws Exception{
        List<SystemAreaDivisionDTO> list = initializationDataService.getAreaTableDataLevel(level);
        List<SysAreaData> areaDataList = ListObjConverter.convert(list, SysAreaData.class);
        return areaDataList;
    }

    @Override
	public Response<Tree> getInitializationDataTree(){
		return initializationDataService.getInitializationDataTree();
	}

    @Override
	public Response<Map<String, Object>> getInitializationDataCarBasicData(){
		return initializationDataService.getInitializationDataCarBasicData();
	}

    /**
     * 获取车厢类型
     * @param code
     * @param codeTwo
     * @return
     */
    @Override
    public String  getCarrriageTypes(int code ,int codeTwo){
        if(code == 0 || codeTwo == 0){
            return null;
        }
        Response<Map<String, Object>> resultdata = initializationDataService.getInitializationDataCarBasicData();
        if (resultdata.isSuccess()) {
            Map<String, Object> resultMap = resultdata.getData();
            List<CarBasicDataEntity> list = (List<CarBasicDataEntity>) resultMap.get("carrriageTypes");
            //遍历车厢类型父节点
            for (CarBasicDataEntity carBasicDataEntity : list) {
                //匹配父节点
                if (Integer.valueOf(carBasicDataEntity.getCode()) == code) {
                    //获取子节点集合
                    List<CarBasicDataEntity> lsit1 = (List<CarBasicDataEntity>) carBasicDataEntity.getChildren();
//					if(lsit1==null || lsit1.size()==0){
//						return carBasicDataEntity.getValue();
//
//					}else
//					{
                    //遍历车厢类型子节点
                    for (CarBasicDataEntity carBasicDataEntity1 : lsit1) {
                        if (Integer.valueOf(carBasicDataEntity1.getCode()) == codeTwo) {
                            return carBasicDataEntity1.getValue();
                        }
                    }
//					}
                }
            }

        }
        return null;
    }

    @Override
    public String getBankTableVersion() {
        Response<BankCardBinVersionDTO> response = bankCardBinVersionService.getByCardBinType((byte)1);
        if(response==null){
            LOG.isDebugEnabled(); LOG.debug("调用bankCardBinVersionService.getByCardBinType查询卡bin信息失败,失败原因response为null");
            return "";
        }
        if(!response.isSuccess()){
            LOG.isDebugEnabled(); LOG.debug("调用bankCardBinVersionService.getByCardBinType查询卡bin信息失败,失败原因={}",response.getMessage());
            return "";
        }
        return response.getData().getVersionNumber();
    }

}
