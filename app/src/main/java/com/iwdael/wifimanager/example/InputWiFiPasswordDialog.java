package com.iwdael.wifimanager.example;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.iwdael.wifimanager.IWifi;
import com.iwdael.wifimanager.WifiManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * wifi密码输入框
 */
public class InputWiFiPasswordDialog extends DialogFragment {

    TextView mTvTitle;
    EditText mEtPassword;
    TextView mTvConnect;
    ImageView mIvClose;
    private boolean isConnecting = false;
    private IWifi wifi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_input_wifi_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (null != bundle) {
            wifi = bundle.getParcelable("info");
        }
        mTvTitle = view.findViewById(R.id.tv_title);
        mEtPassword = view.findViewById(R.id.et_password);
        mTvConnect = view.findViewById(R.id.tv_connect);
        mIvClose = view.findViewById(R.id.iv_close);

        mTvTitle.setText(wifi.name());

        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refreshConnectBtn();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEtPassword.setFocusable(true);
        mEtPassword.setFocusableInTouchMode(true);
        mEtPassword.requestFocus();

        mEtPassword.post(() -> {
            try {
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mIvClose.setOnClickListener(v -> mEtPassword.setText(null));

        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hintKeyBoards(mEtPassword);
                dismissAllowingStateLoss();
            }
        });

        view.findViewById(R.id.tv_connect).setOnClickListener(v -> {
                    Utils.hintKeyBoards(mEtPassword);
                    //连接
                    if (isConnecting) {
                        return;
                    }
                    isConnecting = true;
                    WifiManager.create(getContext()).connectEncryptWifi(wifi, mEtPassword.getText().toString().trim());
                    mTvConnect.setEnabled(!isConnecting);
                    dismissAllowingStateLoss();
                }
        );
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        if (null != window) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = dp2px(getContext(), 280);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
    }

    private boolean isEmpty(String password) {
        return TextUtils.isEmpty(password);
    }

    private boolean isValidPassword(String password) {
        if (isEmpty(password)) {
            return false;
        }

        //6~8位
        int size = password.length();
        return size >= 8;
    }

    private void refreshConnectBtn() {
        String password = mEtPassword.getText().toString().trim();

        mIvClose.setVisibility(isEmpty(password) ? View.GONE : View.VISIBLE);
        mTvConnect.setEnabled(!isConnecting && isValidPassword(password));
    }

    private int dp2px(Context context, int dpValue) {
        if (null == context) {
            return dpValue;
        }

        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
