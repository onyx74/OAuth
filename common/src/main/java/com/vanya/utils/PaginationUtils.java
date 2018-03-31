package com.vanya.utils;


import java.util.Optional;

public class PaginationUtils {
    public static final int INITIAL_PAGE_SIZE = 5;
    public static final int[] PAGE_SIZES = {5, 10, 20};
    public static final int BUTTONS_TO_SHOW = 5;


    public static int getEvalPage(Optional<Integer> page) {
        return (page.orElse(0) < 1) ? PaginationUtils.INITIAL_PAGE_SIZE : page.get() - 1;
    }
}
