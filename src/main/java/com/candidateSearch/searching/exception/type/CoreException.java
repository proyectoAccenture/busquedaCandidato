package com.candidateSearch.searching.exception.type;

import com.candidateSearch.searching.exception.globalmessage.GlobalMessage;

public class CoreException extends RuntimeException {
    private final GlobalMessage error;

    protected CoreException(GlobalMessage error) {
        super(error.getMessage());
        this.error = error;
    }

    public GlobalMessage getError() {
        return error;
    }
}
