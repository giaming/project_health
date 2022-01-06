package org.example.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.example.constant.RedisConstant;
import org.example.dao.SetmealDao;
import org.example.entity.PageResult;
import org.example.entity.QueryPageBean;
import org.example.pojo.Setmeal;
import org.example.service.SetmealService;
import org.example.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2021/12/16 20:50
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_put_path}")  // 从属性文件读取输出目录的路径
    private String outputPath;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();// Theadlocal
        PageHelper.startPage(currentPage, pageSize);  // 分页插件，会在执行sql之前将分页关键字最佳到SQL后面
        Page<Setmeal> page = setmealDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }


    // 新增套餐，同时关联检查组
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 将套餐信息添加到setmeal中，然后将对应关系封装进t_setmeal_checkgroup中
        setmealDao.add(setmeal);
        // 获取套餐id
        Integer setmealId = setmeal.getId();
        // 通过一个方法将套餐信息和检查组的多对多关系存储进t_setmeal_checkgroup中
        this.setSetmealAndCheckGroup(setmealId, checkgroupIds);
        // 完成数据库操作后将图片名称保存到redis
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());

        // 当添加套餐后需要重新生成静态页面
        generateMobileStaticHtml();
    }

    /**
     * 生成当前方法所需的静态页面
     */
    public void generateMobileStaticHtml() {
        // 准备模板文件所需的数据
        List<Setmeal> setmealList = this.findAll();
        // 生成套餐列表静态页面
        generateMObileSetmealListHtml(setmealList);
        // 生成套餐详情静态页面
        generateMobileSetmealDetailHtml(setmealList);
    }

    /**
     * 生成套餐详情静态页面
     * @param setmealList
     */
    private void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        for (Setmeal setmeal :
                setmealList) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("setmeal", this.findById(setmeal.getId()));
            this.generateHtml("mobile_setmeal_detail.ftl", "setmeal_detail_"+setmeal.getId()+".html", dataMap);
        }
    }

    /**
     * 生成套餐列表静态页面
     * @param setmealList
     */
    private void generateMObileSetmealListHtml(List<Setmeal> setmealList) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("setmealList", setmealList);
        this.generateHtml("mobile_setmeal.ftl","m_setmeal.html", dataMap);
    }

    /**
     * 用于生成静态页面
     * @param templateName  模板名称
     * @param htmlPageName
     * @param dataMap  生成页面需要的数据
     */
    public void generateHtml(String templateName, String htmlPageName, Map<String, Object> dataMap){
        // 获得配置文件
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            // 加载模板文件
            Template template = configuration.getTemplate(templateName);
            // 生成数据
            File docFile = new File(outputPath+"\\"+htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            // 输出文件
            template.process(dataMap,
                    out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != out){
                    out.flush();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }

    /**
     * 使用套餐id，查询检查组的id集合，返回list
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    /**
     * 编辑套餐
     *
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        // 使用套餐id，查询数据库对应的套餐，获取数据库存放img
        Setmeal setmeal_db = setmealDao.findById(setmeal.getId());
        String img = setmeal_db.getImg();
        // 如果页面传递的图片名称和数据库存放的图片名称不一致，说明图片更新，需要删除七牛云之前的数据库的图片
        if(setmeal.getImg()!=null && !setmeal.getImg().equals(img)){
            QiniuUtils.deleteFileFromQiniu(img);
        }

        // 1. 根据套餐id删除中间表数据（清理原有关联数据)
        setmealDao.deleteAssociation(setmeal.getId());
        // 2. 向中间表(t_setmeal_checkgroup)插入数据（建立套餐和检查组关联关系)
        this.setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        // 3. 更新套餐基本信息
        setmealDao.edit(setmeal);
        // 完成数据库操作后将图片名称保存到redis
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
    }

    /**
     * 设置套餐和检查组多对多关联关系, 往中间表新增套餐记录
     * @param setmealId
     * @param checkgroupIds
     */
    private void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        if(checkgroupIds!=null && checkgroupIds.length>0){
            for (Integer checkgroupId :
                    checkgroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("setmealId", setmealId);
                map.put("checkgroupId", checkgroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }


    /**
     * 根据套餐id查询
     *
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    /**
     * 根据id删除套餐
     *
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        // 1. 根据套餐id查询数据库对象
        Setmeal setmeal = setmealDao.findById(id);
        // 2. 根据套餐id查询套餐和检查组中间表
        int count = setmealDao.findSetmealAndCheckGroupCountBySetMealId(id);
        // 如果有关联，则不允许删除
        if(count>0){
            throw new RuntimeException("当前套餐和检查组存在关系，不能直接删除");
        }
        // 4. 删除套餐
        setmealDao.deleteById(id);
        // 5. 将七牛云的图片删除
        String img = setmeal.getImg();
        if(!StringUtil.isEmpty(img)){
            QiniuUtils.deleteFileFromQiniu(img);
        }

    }

    /**
     * 获取所有套餐信息
     *
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }


//
//    //查询套餐占比统计数据
//    public List<Map> getSetmealReport() {
//        return setmealDao.getSetmealReport();
//    }
}
