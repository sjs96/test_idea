package fixed.Controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadController  extends Controller{
    public void defaultUpload() {
        String defaultUrl = "\\"+PropKit.get("defaultDir");
        String dir = getRequest().getServletContext().getRealPath("/");
        //判断有无文件夹
        File img = new File(dir+defaultUrl);
        if (!img.exists() && !img.isDirectory()) {
            img.mkdirs();
        }

        List<UploadFile> upFiles = getFiles();
        List<Map<String, Object>> fileName = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < upFiles.size(); i++) {
            UploadFile uf = upFiles.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", uf.getFileName());
            map.put("url", defaultUrl + uf.getFileName());
            fileName.add(map);

        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", getPara("name"));
        map.put("list", fileName);
        renderJson(map);
    }
    //RemoveFile(dir+imgUrl + uf.getFileName(),dir+defaultUrl);
    private boolean RemoveFile(String fileName,String destinationFloderUrl)
    {
        File file = new File(fileName);
        File destFloder = new File(destinationFloderUrl);
        //检查目标路径是否合法
        if(destFloder.exists())
        {
            if(destFloder.isFile())
            {
                return false;
            }
        }else
        {
            if(!destFloder.mkdirs())
            {
                return false;
            }
        }
        //检查源文件是否合法
        if(file.isFile() &&file.exists())
        {
            String destinationFile = destinationFloderUrl+"/"+file.getName();
            if(!file.renameTo(new File(destinationFile)))
            {
                return false;
            }
        }else
        {
            return false;
        }
        return true;
    }
    public void img() {

        String imgUrl = "\\"+PropKit.get("imgDir");
        String defaultUrl = "\\"+PropKit.get("defaultDir");
        String thumbnailUrl = "\\"+PropKit.get("thumbnailImgDir");
        String dir = getRequest().getServletContext().getRealPath("/");

        //判断有无文件夹
        File img = new File(dir+imgUrl);
        File thumbnail = new File(dir+thumbnailUrl);
        if (!thumbnail.exists() && !thumbnail.isDirectory()) {
            thumbnail.mkdirs();
        }
        if (!img.exists() && !img.isDirectory()) {
            img.mkdirs();
        }


        List<UploadFile> upFiles = getFiles();
        List<Map<String, Object>> fileName = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < upFiles.size(); i++) {
            UploadFile uf = upFiles.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", uf.getFileName());
            map.put("url", imgUrl + uf.getFileName());
            RemoveFile(dir+defaultUrl + uf.getFileName(),dir+imgUrl);
            fileName.add(map);
            //压缩图片
            try {

                Thumbnails.of(uf.getUploadPath()+"\\"+uf.getFileName()).size(300, 300).toFile(dir+thumbnailUrl+uf.getFileName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", getPara("name"));
        map.put("list", fileName);
        renderJson(map);
    }
}
