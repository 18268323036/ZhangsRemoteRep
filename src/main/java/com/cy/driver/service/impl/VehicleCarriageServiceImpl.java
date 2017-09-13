package com.cy.driver.service.impl;

import com.cy.driver.common.constants.Constants;
import com.cy.driver.common.initdata.SystemTreeCode;
import com.cy.driver.service.VehicleCarriageService;
import com.cy.pass.service.SystemTreeCodeService;
import com.cy.pass.service.dto.SystemTreeCodeDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 车辆类型和车厢类型
 * Created by wyh on 2015/7/23.
 */
@Service("vehicleCarriageService")
public class VehicleCarriageServiceImpl implements VehicleCarriageService {

    @Resource
    private SystemTreeCodeService systemTreeCodeService;

    @Override
    public List<SystemTreeCode> queryVehicleList() {
        List<SystemTreeCodeDTO> list = systemTreeCodeService.queryListByType(Constants.SYS_TREE_CODE_VEHICLE);
        return copyList(list);
    }

    @Override
    public List<SystemTreeCode> queryCarriageList() {
        List<SystemTreeCodeDTO> list = systemTreeCodeService.queryListByType(Constants.SYS_TREE_CODE_CARRIAGE);
        return copyList(list);
    }

    private List<SystemTreeCode> copyList(List<SystemTreeCodeDTO> list){
        List<SystemTreeCode> treeList = new ArrayList<SystemTreeCode>();
        SystemTreeCode sysTree = null;
        for(SystemTreeCodeDTO item : list){
            sysTree = new SystemTreeCode();
            sysTree.setId(item.getId());
            sysTree.setCode(item.getCode());
            sysTree.setName(item.getName());
            sysTree.setParentCode(item.getParentCode());
            treeList.add(sysTree);
        }
        return treeList;
    }
}
