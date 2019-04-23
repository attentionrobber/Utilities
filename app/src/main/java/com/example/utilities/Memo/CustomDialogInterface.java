package com.example.utilities.Memo;

/**
 * MemoNew(Modify)Activity 에서 CustomDialog 의 setOnClickListener 를 실행하기 위한 DialogInterface
 * Used by: MemoNewActivity, MemoModifyActivity, CustomDialog
 */
public interface CustomDialogInterface {
    void dialogSelected(int position); // Dialog Item position 0: CAMERA, 1: GALLERY
}
