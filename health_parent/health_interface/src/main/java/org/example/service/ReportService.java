package org.example.service;

import java.util.Map;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/6 11:17
 */
public interface ReportService {
    /**
     *  * Map数据格式：
     *      *      todayNewMember -> number
     *      *      totalMember -> number
     *      *      thisWeekNewMember -> number
     *      *      thisMonthNewMember -> number
     *      *      todayOrderNumber -> number
     *      *      todayVisitsNumber -> number
     *      *      thisWeekOrderNumber -> number
     *      *      thisWeekVisitsNumber -> number
     *      *      thisMonthOrderNumber -> number
     *      *      thisMonthVisitsNumber -> number
     *      *      hotSetmeals -> List<Setmeal>
     * @return
     */
    Map<String, Object> getBusinessReport() throws Exception;
}
