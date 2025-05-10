package com.zj.jsonsql.ui.dialog.sql;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Editor;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.zj.jsonsql.entity.Field;
import com.zj.jsonsql.entity.JsonInfo;
import com.zj.jsonsql.enums.FuncEnum;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhoujie
 */
public class SqlParamCompletionContributor extends CompletionContributor {

    private final List<String> SQL_KEYWORDS = Stream.of("select", "as", "from", "where", "not", "in", "like", "null", "between",
            "is", "and", "or", "order", "by", "asc", "desc", "distinct", "limit", "group").collect(Collectors.toList());

    public SqlParamCompletionContributor() {
        SQL_KEYWORDS.addAll(Stream.of(FuncEnum.values()).map(v -> v.name().toLowerCase()).collect(Collectors.toList()));
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters,
                                                  @NotNull ProcessingContext context,
                                                  @NotNull CompletionResultSet result) {
                        CompletionResultSet completionResultSet = result.caseInsensitive();
                        for (String sqlKeyword : SQL_KEYWORDS) {
                            completionResultSet.addElement(LookupElementBuilder.create(sqlKeyword));
                        }
                    }
                });
    }

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        result = result.withPrefixMatcher(new PlainPrefixMatcher(findPrefix(parameters)));
        Editor editor = parameters.getEditor();
        JsonInfo jsonInfo = editor.getUserData(SqlEditorFiled.JSON_INFO_KEY);
        if (Objects.nonNull(jsonInfo)) {
            add(jsonInfo, result);
        }
        super.fillCompletionVariants(parameters, result);
    }

    private String findPrefix(CompletionParameters parameters) {
        PsiElement position = parameters.getPosition();
        String text = position.getContainingFile().getText();
        int offset = parameters.getOffset();
        int start = offset - 1;
        while (start > 0 && Character.isLetterOrDigit(text.charAt(start - 1))) {
            start--;
        }
        return text.substring(start, offset);
    }

    public void add(JsonInfo jsonInfo, CompletionResultSet result) {
        String word = result.getPrefixMatcher().getPrefix().toLowerCase();
        CompletionResultSet completionResultSet = result.caseInsensitive();
        // 创建关键字提示框
        List<String> keywords = jsonInfo.getColumns().stream()
                .map(Field::getOriginalFiled)
                .map(SqlNode::toString)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());
        for (String keyword : keywords) {
            String lowerCaseKeyword = keyword.toLowerCase();
            if (!lowerCaseKeyword.equals(word) && lowerCaseKeyword.contains(word)) {
                completionResultSet.addElement(LookupElementBuilder.create(keyword));
            }
        }
    }
}
