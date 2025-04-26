package com.zj.jsonsql.exception;

import com.zj.jsonsql.enums.NoticeEnum;
import lombok.Getter;

@Getter
public class JsonException extends RuntimeException {

    private final NoticeEnum noticeEnum;

    public JsonException(NoticeEnum noticeEnum) {
        super(noticeEnum.getMessage());
        this.noticeEnum = noticeEnum;
    }


}
