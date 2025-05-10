package com.zj.jsonsql.utils;

import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.content.Content;
import com.zj.jsonsql.ui.AnActionButtonImpl;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author jie.zhou
 */
public class ExecutorUtil {

    /**
     * 返回正在运行的 Executor
     *
     * @param id Executor id
     */
    public static Executor getRunExecutorInstance(String id) {
        return ExecutorRegistry.getInstance().getExecutorById(id);
    }

    /**
     * 设置 Content 的 Disposer
     * 绑定按钮销毁
     *
     * @param content            Content
     * @param anActionButtonList 按钮数组
     */
    public static void setContentDisposerAnActionButtonImpl(Content content, List<AnActionButtonImpl> anActionButtonList) {
        Disposable disposable = new Disposable() {
            @Override
            public void dispose() {

            }
        };
        content.setDisposer(disposable);
        if (CollectionUtils.isEmpty(anActionButtonList)) {
            return;
        }
        for (AnActionButtonImpl anActionButton : anActionButtonList) {
            Disposer.register(disposable, anActionButton);
        }
    }
}