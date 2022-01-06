package org.example.controller;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2021/12/18 18:59
 */

import com.alibaba.dubbo.config.annotation.Reference;
import org.example.constant.MessageConstant;
import org.example.entity.Result;
import org.example.pojo.OrderSetting;
import org.example.service.OrderSettingService;
import org.example.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 后台OrderSettingController处理流程
 * 1.接收一个文件类型的变量MultipartFile excelFile
 * 2.List<String[]> list = POIUtils.readExcel(excelFile);//使用POI解析表格数据得到一个List集合，集合里面存储的是一个一个的数组
 * 3.通过遍历List集合将String[]数组转换成OrderSetting，也就是说List<String[]>---->List<OrderSetting>    [[2020/6/17,400],[2020/6/18,
 * 400]]--->[OrderSetting{orderDate=2020/6/17,number=400},OrderSetting{orderDate=2020/6/18,number=400}]
 * 4.通过dubbo远程调用服务实现数据批量导入到数据库
 */

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result uploadFromOrderSetting(@RequestParam("excelFile") MultipartFile excelFile){
        try {
            // 使用POI解析表格数据
            List<String[]> list = POIUtils.readExcel(excelFile);
            List<OrderSetting> data = new ArrayList<>();
            for (String[] strings :
                    list) {
                String date = strings[0];
                String number = strings[1];
//                OrderSetting orderSetting = new OrderSetting(new SimpleDateFormat("yyyy/MM/dd").parse(date), Integer.parseInt(number));
                OrderSetting orderSetting = new OrderSetting(new Date(date), Integer.parseInt(number));
                data.add(orderSetting);
            }
            // 通过dubbo远程调用服务实现数据批量导入到数据库
            orderSettingService.addFileData(data);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){  // date格式为:yyyy-MM
        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
