package com.cullen.admin.ctrl.manage;

import cn.hutool.core.date.DateUtil;
import com.cullen.admin.utils.ResultUtil;
import com.cullen.admin.utils.StrUtils;
import com.cullen.admin.utils.page.PageUtil;
import com.cullen.admin.vo.RedisInfo;
import com.cullen.admin.vo.RedisVo;
import com.cullen.admin.vo.Result;
import com.cullen.admin.base.BaseCtrl;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;


/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@RestController
@Api("Redis缓存管理接口")
@RequestMapping("/api/redis")
@Transactional
public class RedisCtrl extends BaseCtrl {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping(value = "/getAllByPage")
    @ApiOperation(value = "分页获取全部")
    public Result<Object> getAllByPage(@RequestParam(required = false) String key,
                                       int pageNumber, int pageSize) {

        List<RedisVo> list = new ArrayList<>();

        if (StrUtils.isNotBlank(key)) {
            key = "*" + key + "*";
        } else {
            key = "*";
        }
        for (String s : redisTemplate.keys(key)) {
            RedisVo redisVo = new RedisVo(s, "");
            list.add(redisVo);
        }

        PageInfo<RedisVo> pageInfo = new PageInfo<>();
        pageInfo.setPageNum(pageNumber);
        pageInfo.setPageSize(pageSize);
        pageInfo.setList(PageUtil.listToPage(pageNumber, pageSize, list));
        pageInfo.setTotal(list.size());

        pageInfo.getList().forEach(e -> {
            String value;
            try {
                value = redisTemplate.opsForValue().get(e.getKey());
                if (value.length() > 150) {
                    value = value.substring(0, 149) + "...";
                }
            } catch (Exception exception) {
                value = "非字符格式数据";
            }
            e.setValue(value);
        });
        return new ResultUtil<>().setData(pageInfo);
    }

    @GetMapping(value = "/getByKey/{key}")
    @ApiOperation(value = "通过key获取")
    public Result<Object> getByKey(@PathVariable String key) {

        String value = redisTemplate.opsForValue().get(key);
        return new ResultUtil<>().setData(value);
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "添加或编辑")
    public Result<Object> save(@RequestParam String key,
                               @RequestParam String value) {

        redisTemplate.opsForValue().set(key, value);
        return new ResultUtil<>().setSuccessMsg("删除成功");
    }

    @DeleteMapping(value = "/delByKeys")
    @ApiOperation(value = "批量删除")
    public Result<Object> delByKeys(@RequestParam String[] keys) {

        for (String key : keys) {
            redisTemplate.delete(key);
        }
        return new ResultUtil<>().setSuccessMsg("删除成功");
    }

    @DeleteMapping(value = "/delAll")
    @ApiOperation(value = "全部删除")
    public Result<Object> delAll() {

        redisTemplate.delete(redisTemplate.keys("*"));
        return new ResultUtil<>().setSuccessMsg("删除成功");
    }

    @GetMapping(value = "/getKeySize")
    @ApiOperation(value = "获取实时key大小")
    public Result<Object> getKeySize() {

        Map<String, Object> map = new HashMap<>(16);
        Jedis jedis = jedisPool.getResource();
        map.put("keySize", jedis.dbSize());
        map.put("time", DateUtil.format(new Date(), "HH:mm:ss"));
        if (jedis != null) {
            jedis.close();
        }
        return new ResultUtil<>().setData(map);
    }

    @GetMapping(value = "/getMemory")
    @ApiOperation(value = "获取实时内存大小")
    public Result<Object> getMemory() {

        Map<String, Object> map = new HashMap<>(16);
        Jedis jedis = jedisPool.getResource();
        String[] strs = jedis.info().split("\n");
        for (String s : strs) {
            String[] detail = s.split(":");
            if ("used_memory".equals(detail[0])) {
                map.put("memory", detail[1].substring(0, detail[1].length() - 1));
                break;
            }
        }
        map.put("time", DateUtil.format(new Date(), "HH:mm:ss"));
        if (jedis != null) {
            jedis.close();
        }
        return new ResultUtil<>().setData(map);
    }

    @GetMapping(value = "/info")
    @ApiOperation(value = "获取Redis信息")
    public Result<Object> info() {

        List<RedisInfo> infoList = new ArrayList<>();

        Jedis jedis = jedisPool.getResource();
        String[] strs = jedis.info().split("\n");
        for (String str1 : strs) {
            RedisInfo redisInfo = new RedisInfo();
            String[] str = str1.split(":");
            if (str.length > 1) {
                String key = str[0];
                String value = str[1];
                redisInfo.setKey(key);
                redisInfo.setValue(value);
                infoList.add(redisInfo);
            }
        }
        if (jedis != null) {
            jedis.close();
        }
        return new ResultUtil<>().setData(infoList);
    }
}
