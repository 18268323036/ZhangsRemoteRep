package com.cy.driver.common.initdata;

import com.cy.driver.service.InitDataService;
import com.cy.driver.service.LocationService;
import com.cy.driver.service.VehicleCarriageService;
import com.cy.pass.service.dto.Enum.AreaLevel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统数据
 * Created by wyh on 2015/7/23.
 */
public class SystemData {

    private static final Logger LOG = LoggerFactory.getLogger(SystemData.class);

    private static final String ALL = "0";
    //必须验证的车辆类型一级参数
    private static final String[] MUST_VEHICLES = new String[]{"CL14"};
    //必须验证的车厢类型一级参数
    private static final String[] MUST_CARRIAGES = new String[]{"CX11","CX12","CX13"};
    //车辆类型
    private static Map<String, SystemTreeCode> vehicleTypeMap = new HashMap<String, SystemTreeCode>();
    //车厢类型
    private static Map<String, SystemTreeCode> carriageTypeMap = new HashMap<String, SystemTreeCode>();

    //区域初始化数据  --省
    private static Map<String, SysAreaData> provinceAreaDataMap = new HashMap<String, SysAreaData>();

    //区域初始化数据  --市
    private static Map<String, SysAreaData> cityAreaDataMap = new HashMap<String, SysAreaData>();

    //区域初始化数据  --县
    private static Map<String, SysAreaData> countyAreaDataMap = new HashMap<String, SysAreaData>();

    //区域初始化数据 --大区
    private static Map<String, SysAreaData> regionAreaDataMap = new HashMap<String, SysAreaData>();

    //德邦账号初始化数据 --大区
    private static Map<String,String> debangUserCodeDataMap = new HashMap<String,String>();

    //app的位置上传定时的时间(分钟)
    private static String appLocMinutes;

    @Resource
    private InitDataService initDataService;
    @Resource
    private VehicleCarriageService vehicleCarriageService;
    @Resource
    private LocationService locationService;

    public void execu(){
        initAreaDataMap(initDataService);
        initVehicleTypeMap(vehicleCarriageService);
        initCarriageTypeMap(vehicleCarriageService);
        initAppLocMinutes(locationService);
        initAreaDataMapByCode(initDataService);
    }

    /** 区域初始化数据 */
    /** 启动初始化 */
    public static void initAreaDataMap(InitDataService initDataService) {
        try{
            List<SysAreaData> listProvince = initDataService.getAreaTableDataLevel(AreaLevel.PROVICE.getValue());
            for(SysAreaData sysData : listProvince){
                provinceAreaDataMap.put(sysData.getAreaName(), sysData);
            }
            List<SysAreaData> listCity = initDataService.getAreaTableDataLevel(AreaLevel.CITY.getValue());
            for(SysAreaData sysData : listCity){
                cityAreaDataMap.put(sysData.getAreaName(), sysData);
            }
            List<SysAreaData> listArea = initDataService.getAreaTableDataLevel(AreaLevel.AREA.getValue());
            for(SysAreaData sysData : listArea){
                countyAreaDataMap.put(sysData.getAreaName(), sysData);
            }
            List<SysAreaData> listRegion = initDataService.getAreaTableDataLevel(AreaLevel.REGION.getValue());
            for(SysAreaData sysData : listRegion){
                regionAreaDataMap.put(sysData.getAreaName(), sysData);
            }
        }catch (Exception e)
        {
            LOG.error("初始话区域数据失败！");
        }
    }


    /** 区域初始化数据 */
    /** 启动初始化 */
    /** 根据code获得相应的省市信息*/
    public static void initAreaDataMapByCode(InitDataService initDataService) {
        try{
            List<SysAreaData> listProvince = initDataService.getAreaTableDataLevel(AreaLevel.PROVICE.getValue());
            for(SysAreaData sysData : listProvince){
                provinceAreaDataMap.put(sysData.getAreaCode(), sysData);
            }
            List<SysAreaData> listCity = initDataService.getAreaTableDataLevel(AreaLevel.CITY.getValue());
            for(SysAreaData sysData : listCity){
                cityAreaDataMap.put(sysData.getAreaCode(), sysData);
            }
            List<SysAreaData> listArea = initDataService.getAreaTableDataLevel(AreaLevel.AREA.getValue());
            for(SysAreaData sysData : listArea){
                countyAreaDataMap.put(sysData.getAreaCode(), sysData);
            }
            List<SysAreaData> listRegion = initDataService.getAreaTableDataLevel(AreaLevel.REGION.getValue());
            for(SysAreaData sysData : listRegion){
                regionAreaDataMap.put(sysData.getAreaCode(), sysData);
            }
        }catch (Exception e)
        {
            LOG.error("初始话区域数据失败！");
        }
    }


    /** 初始化车辆类型 */
    /** 启动初始化 */
    public static void initVehicleTypeMap(VehicleCarriageService vehicleCarriageService){
        List<SystemTreeCode> list = vehicleCarriageService.queryVehicleList();
        for(SystemTreeCode sysTree : list){
            vehicleTypeMap.put(sysTree.getCode(), sysTree);
        }
    }

    /** 初始化车厢类型 */
    /** 启动初始化 */
    public static void initCarriageTypeMap(VehicleCarriageService vehicleCarriageService){
        List<SystemTreeCode> list = vehicleCarriageService.queryCarriageList();
        for(SystemTreeCode sysTree : list){
            carriageTypeMap.put(sysTree.getCode(), sysTree);
        }
    }

    /** 初始化app的位置上传定时的时间 */
    public static void initAppLocMinutes(LocationService locationService){
        String value = locationService.findInitTimeValue();
        if(StringUtils.isBlank(value)){
            appLocMinutes = "15";//默认30分钟
        }else{
            appLocMinutes = value;
        }
    }

    /** 获得app的位置上传定时的时间 */
    public static String getAppLocMinutes(){
        return appLocMinutes;
    }

    /**
     * 比较车辆类型
     * @param code
     * @return
     */
    public static String containVehicleType(String code){
        SystemTreeCode item = vehicleTypeMap.get(code);
        if(item != null){
            if(item.haveParent()){
                //存在二级
                return item.getName();
            }else{
                //一级则判断是否需要必填二级
                for(String mustValue : MUST_VEHICLES){
                    if(mustValue.equals(item.getCode())){
                        return "";
                    }
                }
                return item.getName();
            }
        }else{
            return "";
        }
    }

    /**
     * 查找车辆类型二级编号
     * @param code
     * @return
     */
    public static List<String> findVehicleTypeCode(String code){
        List<String> list = new ArrayList<String>(2);
        SystemTreeCode item = vehicleTypeMap.get(code);
        if(item != null){
            if(item.haveParent()){
                SystemTreeCode itemParent = vehicleTypeMap.get(item.getParentCode());
                if(itemParent != null){
                    list.add(itemParent.getCode());
                    list.add(item.getCode());
                }else{
                    list.add(item.getCode());
                    list.add("");
                }
            }else{
                list.add(item.getCode());
                list.add("");
            }
        }else{
            list.add("");
            list.add("");
        }
        return list;
    }

    /**
     * 查找车辆类型名称
     * @param code
     * @return
     */
    public static String findVehicleTypeName(String code){
        if(ALL.equals(code)){
            return "全车辆";
        }
        SystemTreeCode item = vehicleTypeMap.get(code);
        if(item != null){
            return item.getName();
        }else{
            return "";
        }
    }

    /**
     * 获得查询车辆类型数组
     * @param code
     * @return
     */
    public static List<String> findQueryVehicleTypes(String code){
        if(StringUtils.isBlank(code)){
            return null;
        }
        List<String> list = null;
        SystemTreeCode item = vehicleTypeMap.get(code);
        if(item != null){
            list = new ArrayList<String>();
            list.add(ALL);
            list.add(item.getCode());
            if(item.haveParent()){
                item = vehicleTypeMap.get(item.getParentCode());
                if(item != null){
                    list.add(item.getCode());
                }
            }
        }
        return list;
    }

    /**
     * 比较车厢类型
     * @param code
     * @return
     */
    public static String containCarriageType(String code){
        SystemTreeCode item = carriageTypeMap.get(code);
        if(item != null){
            if(item.haveParent()){
                //存在二级
                return item.getName();
            }else{
                //一级则判断是否需要必填二级
                for(String mustValue : MUST_CARRIAGES){
                    if(mustValue.equals(item.getCode())){
                        return "";
                    }
                }
                return item.getName();
            }
        }else{
            return null;
        }
    }

    /**
     * 查找车厢类型二级编号
     * @param code
     * @return
     */
    public static List<String> findCarriageTypeCode(String code){
        List<String> list = new ArrayList<String>(2);
        SystemTreeCode item = carriageTypeMap.get(code);
        if(item != null){
            if(item.haveParent()){
                SystemTreeCode itemParent = carriageTypeMap.get(item.getParentCode());
                if(itemParent != null){
                    list.add(itemParent.getCode());
                    list.add(item.getCode());
                }else{
                    list.add(item.getCode());
                    list.add("");
                }
            }else{
                list.add(item.getCode());
                list.add("");
            }
        }else{
            list.add("");
            list.add("");
        }
        return list;
    }

    /**
     * 查找车厢类型名称
     * @param code
     * @return
     */
    public static String findCarriageTypeName(String code){
        if(ALL.equals(code)){
            return "全车厢";
        }
        SystemTreeCode item = carriageTypeMap.get(code);
        if(item != null){
            return item.getName();
        }else{
            return "";
        }
    }

    /**
     * 获得查询车厢类型数组
     * @param code
     * @return
     */
    public static List<String> findQueryCarriageTypes(String code){
        if(StringUtils.isBlank(code)){
            return null;
        }
        List<String> list = null;
        SystemTreeCode item = carriageTypeMap.get(code);
        if(item != null){
            list = new ArrayList<String>();
            list.add(ALL);
            list.add(item.getCode());
            if(item.haveParent()){
                item = carriageTypeMap.get(item.getParentCode());
                if(item != null){
                    list.add(item.getCode());
                }
            }
        }
        return list;
    }

    /**
     * 根据省全名  获取省信息
     * @param value 省全名
     * @return  SysAreaData 区域对象信息
     */
    public static SysAreaData getProvinceInfoByVal(String value){
        if(StringUtils.isBlank(value)){
            return null;
        }
        List<String> list = null;
        SysAreaData item = provinceAreaDataMap.get(value);
        if(item != null){
            return item;
        }
        return null;
    }

    /**
     * 根据市全名  获取市信息
     * @param value 市全名
     * @return SysAreaData 区域对象信息
     */
    public static SysAreaData getCityInfoByVal(String value){
        if(StringUtils.isBlank(value)){
            return null;
        }
        List<String> list = null;
        SysAreaData item = cityAreaDataMap.get(value);
        if(item != null){
            return item;
        }
        return null;
    }

    /**
     * 根据县(区)全名  获取区信息
     * @param value 县(区)全名
     * @return SysAreaData 区域对象信息
     */
    public static SysAreaData getAreaInfoByVal(String value){
        if(StringUtils.isBlank(value)){
            return null;
        }
        List<String> list = null;
        SysAreaData item = countyAreaDataMap.get(value);
        if(item != null){
            return item;
        }
        return null;
    }

    /**
     * 根据大区全名  获取大区信息
     * @param value 大区全名
     * @return SysAreaData 区域对象信息
     */
    public static SysAreaData getRegionInfoByVal(String value){
        if(StringUtils.isBlank(value)){
            return null;
        }
        List<String> list = null;
        SysAreaData item = regionAreaDataMap.get(value);
        if(item != null){
            return item;
        }
        return null;
    }

    /**
     * 根据省编号  获取省信息
     * @param code 省编号
     * @return SysAreaData 区域对象信息
     */
    public static SysAreaData getProvinceInfoByCode(String code){
        if(StringUtils.isBlank(code)){
            return null;
        }
        List<String> list = null;
        SysAreaData item = provinceAreaDataMap.get(code);
        if(item != null){
            return item;
        }
        return null;
    }

    /**
     * 根据市编号  获取市信息
     * @param code 市编号
     * @return SysAreaData 区域对象信息
     */
    public static SysAreaData getCityInfoByCode(String code){
        if(StringUtils.isBlank(code)){
            return null;
        }
        List<String> list = null;
        SysAreaData item = cityAreaDataMap.get(code);
        if(item != null){
            return item;
        }
        return null;
    }

}
