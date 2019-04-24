package com.example.utilities.Memo;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.utilities.R;

/**
 * AlertDialog.builder 를 Custom 해서 사용하는 class
 * Used by: MemoNewActivity, MemoModifyActivity
 */
public class CustomDialog {

    private Activity activity;
    private final Dialog dialog;
    private CustomDialogInterface dialogInterface;

    private TextView tv_title, tv_dialog_item1, tv_dialog_item2;
    private Button btn_dialog_item1, btn_dialog_item2;
    private ImageView iv_dialog_item1, iv_dialog_item2;

    CustomDialog(Activity activity, CustomDialogInterface dialogInterface) {
        this.activity = activity;
        this.dialogInterface = dialogInterface;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true); // false : 백버튼, 바깥쪽 눌러도 취소가 안됨.
        dialog.setContentView(R.layout.layout_dialog_custom);

        tv_title = dialog.findViewById(R.id.tv_dialog_title);
        tv_dialog_item1 = dialog.findViewById(R.id.tv_dialog_item1);
        tv_dialog_item2 = dialog.findViewById(R.id.tv_dialog_item2);
        btn_dialog_item1 = dialog.findViewById(R.id.btn_dialog_item1);
        btn_dialog_item2 = dialog.findViewById(R.id.btn_dialog_item2);
        iv_dialog_item1 = dialog.findViewById(R.id.iv_dialog_item1);
        iv_dialog_item2 = dialog.findViewById(R.id.iv_dialog_item2);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setItemName(String name1, String name2) {
        tv_dialog_item1.setText(name1);
        tv_dialog_item2.setText(name2);
    }

    public void setItemIcon(int res1, int res2) {
        iv_dialog_item1.setImageResource(res1);
        iv_dialog_item2.setImageResource(res2);
    }

    public void showDialog() {
        btn_dialog_item1.setOnClickListener(v -> {
            dialogInterface.dialogSelected(0);
            dialog.dismiss();
        });
        btn_dialog_item2.setOnClickListener(v -> {
            dialogInterface.dialogSelected(1);
            dialog.dismiss();
        });

        dialog.show();
    }
}
