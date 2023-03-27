package com.zjy.runner.image;

import com.zjy.util.file.FileHighLevelUtil;
import com.zjy.util.image.ImageUtil;

/**
 * @author zjy
 * @date 2022/3/13
 */
public class ImageTest {

    public static void main(String[] args) {


        FileHighLevelUtil.doWithFileUnderPath("/Users/zjy/Desktop/test"
                , file -> ImageUtil.compressFile(file, 0.30f)
                , ImageUtil.IMAGE_FILTER);

    }

}
