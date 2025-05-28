package com.candidateSearch.searching.exception.type;

import com.candidateSearch.searching.exception.globalmessage.GlobalMessage;

public class BusinessException extends CoreException {
    public BusinessException(GlobalMessage error) {
        super(error);
    }
}
