package com.example.utilities.Memo;

/**
 * AlertDialog 에 사용되는 Items Class
 * Used by: MemoNewActivity, MemoModifyActivity
 */
public class DialogItem {
    String text;
    int icon;

    DialogItem(String text, int icon) {
        this.text = text;
        this.icon = icon;
    }
}