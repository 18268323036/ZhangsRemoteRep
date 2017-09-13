package com.cy.driver.common.util;

import com.alibaba.dubbo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 */
public class FileOperateUtil implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 3563598481588703153L;

    private static final Logger LOG = LoggerFactory.getLogger(FileOperateUtil.class);

	/**
     * 将上传的文件进行重命名
     */
	public static String rename(String name) {
        String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSSS")
                .format(new Date());

        if (name.indexOf(".") != -1) {
            fileName += name.substring(name.lastIndexOf("."));
        }
        return fileName;
    }
	
    /**
     * 多文件上传
     * @param request
     * @param savePath
     * @throws Exception
     */
    public static List<String> upload(HttpServletRequest request, String savePath) throws Exception {
    	
    	List<String> list = new ArrayList<String>();
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();

        //判断路径是否存在
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        for (Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, MultipartFile> entry = it.next();
            MultipartFile mFile = entry.getValue();
            String fileReName = rename(mFile.getOriginalFilename());

            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new StringBuilder(savePath).append(File.separator).append(fileReName).toString()));
            FileCopyUtils.copy(mFile.getInputStream(), outputStream);
            
            list.add(fileReName);
        }
        return list;
    }

    /**
     * 多文件上传
     * @param request
     * @param savePath
     * @throws Exception
     */
    public static List<String> uploadRname(HttpServletRequest request, String savePath, List<String> listRname) throws Exception {
    	
    	List<String> list = new ArrayList<String>();
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();

        //判断路径是否存在
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        for (Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, MultipartFile> entry = it.next();
            MultipartFile mFile = entry.getValue();
            String fileReName = rename(mFile.getOriginalFilename());

            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new StringBuilder(savePath).append(File.separator).append(fileReName).toString()));
            FileCopyUtils.copy(mFile.getInputStream(), outputStream);
            
            list.add(fileReName);
        }
        return list;
    }
    
    
    /**
     * 下载
     * @param request
     * @param response
     * @param storeName
     * @param contentType
     * @param realName
     * @throws Exception
     */
	public static void download(HttpServletRequest request, HttpServletResponse response, String storeName, String contentType, String realName)
			throws Exception {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		String downLoadPath = storeName;
		long fileLength = new File(downLoadPath).length();

		response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("Content-disposition", "attachment; filename=" + new String(realName.getBytes("utf-8"), "ISO8859-1"));
		response.setHeader("Content-Length", String.valueOf(fileLength));

		bis = new BufferedInputStream(new FileInputStream(downLoadPath));
        bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		bis.close();
		bos.close();
	}

    /**
     *
     * @param request
     * @param response
     * @param realPath
     * @param contentType
     * @throws Exception
     */
    public static HttpServletResponse downloadFromDisk(HttpServletRequest request, HttpServletResponse response, String realPath, String contentType) throws Exception{
        return downloadFromDisk(request, response, realPath, contentType, null);
    }

    public static HttpServletResponse downloadFromDisk(HttpServletRequest request, HttpServletResponse response, String realPath, String contentType, String defaultPath) throws Exception{
        try {
            String downPath = realPath;
            // path是指欲下载的文件的路径。
            File file = new File(realPath);
            if (!StringUtils.isEmpty(defaultPath)) {
                if (!file.exists()) {
                    /** 文件不存在，取默认路径 */
                    file = new File(defaultPath);
                    downPath = defaultPath;
                }
            }
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
//             String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(downPath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (Exception ex) {
            LOG.error("下载文件处理发生异常", ex);
        }
        return response;
    }

    /**
     * 获取上传文件的名称
     * @param request
     * @return
     * @throws Exception
     */
    public static String getUploadFileName(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        boolean isEmpty = multipartFile.isEmpty();
        if (isEmpty) {
            return "";
        }
        return multipartFile.getOriginalFilename();
    }
    
}
