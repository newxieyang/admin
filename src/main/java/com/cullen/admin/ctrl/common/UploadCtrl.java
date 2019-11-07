package com.cullen.admin.ctrl.common;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cullen
 * @date 2019-10-14  08:53
 * @email newxieyang@msn.cn
 */
@Slf4j
@RestController
@Api("文件上传接口")
@RequestMapping("/api/upload")
@Transactional
public class UploadCtrl {

//    @Autowired
//    private QiniuUtil qiniuUtil;
//
//    @RequestMapping(value = "/file")
//    @ApiOperation(value = "文件上传")
//    public Result<Object> upload(@RequestParam(required = false) MultipartFile file,
//                                 @RequestParam(required = false) String base64,
//                                 HttpServletRequest request) {
//
//        if(StrUtil.isNotBlank(base64)){
//            // base64上传
//            file = Base64DecodeMultipartFile.base64Convert(base64);
//        }
//        String result = null;
//        String fileName = qiniuUtil.renamePic(file.getOriginalFilename());
//        try {
//            InputStream inputStream = file.getInputStream();
//            //上传七牛云服务器
//            result = qiniuUtil.qiniuInputStreamUpload(inputStream,fileName);
//        } catch (Exception e) {
//            log.error(e.toString());
//            return new ResultUtil<Object>().setErrorMsg(e.toString());
//        }
//
//        return new ResultUtil<Object>().setData(result);
//    }
}
