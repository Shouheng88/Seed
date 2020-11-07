package com.seed.data.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 0:01
 */
@Data
@NoArgsConstructor
public class RecognizeResult {

    /**
     * log_id : 5521953171948657672
     * words_result_num : 5
     * words_result : [{"location":{"width":592,"top":241,"left":93,"height":78},"words":"每次在大街上闻到像你的味道,"},{"location":{"width":595,"top":334,"left":90,"height":75},"words":"我都会控制不住地找你再找你。"},{"location":{"width":994,"top":529,"left":84,"height":53},"words":"人都是视觉动物,没有人一上来就想操纵别人的大脑。"},{"location":{"width":960,"top":621,"left":82,"height":57},"words":"每场一见钟情,无非都是性冲动在指引着我们前行。"},{"location":{"width":887,"top":710,"left":78,"height":67},"words":"我们不能否认,视觉让我们有了最开始的欲望。"}]
     */
    private long log_id;
    private int words_result_num;
    private List<WordsResultBean> words_result;

    /**
     * 请求的错误码和错误信息
     */
    private String error_code;
    private String error_msg;

    @Data
    @NoArgsConstructor
    public static class WordsResultBean {
        /**
         * location : {"width":592,"top":241,"left":93,"height":78}
         * words : 每次在大街上闻到像你的味道,
         */
        private LocationBean location;
        private String words;

        @Data
        @NoArgsConstructor
        public static class LocationBean {
            /**
             * width : 592
             * top : 241
             * left : 93
             * height : 78
             */

            private int width;
            private int top;
            private int left;
            private int height;
        }
    }

}
