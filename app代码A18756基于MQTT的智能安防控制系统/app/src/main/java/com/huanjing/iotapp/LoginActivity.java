package com.huanjing.iotapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import com.huanjing.iotapp.base.BaseActivity;
import com.huanjing.iotapp.databinding.ActivityLoginBinding;
import com.huanjing.iotapp.utils.Out;


public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;
    @Override
    protected void initView() {
        binding.tvLogin.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.edAccount.getText().toString().trim())||TextUtils.isEmpty(binding.edPsw.getText().toString().trim())){
                Out.toast(context,"请填写用户名和密码~");
                return;
            }
            String uname=binding.edAccount.getText().toString().trim();
            String psw=binding.edPsw.getText().toString().trim();

            if (uname.equals("admin")&&psw.equals("admin")){
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("uname",uname);
                editor.putString("psw",psw);
                editor.commit();
                startActivity(new Intent(context,MainActivity.class));
                finish();
            }else {
                Out.toast(context,"请填写正确的用户名和密码~");
            }

        });
    }
    @Override
    protected View bindView() {
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void loadData() {
        String username=sharedPreferences.getString("uname","");
        String password=sharedPreferences.getString("psw","");
        if (!TextUtils.isEmpty(username)){
            binding.edAccount.setText(username);
            binding.edPsw.setText(password);
        }


    }
}
