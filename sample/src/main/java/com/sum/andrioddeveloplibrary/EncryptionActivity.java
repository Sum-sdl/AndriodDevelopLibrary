package com.sum.andrioddeveloplibrary;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.sum.andrioddeveloplibrary.App.BaseAppActivity;
import com.sum.andrioddeveloplibrary.encryption.DESUtils;
import com.sum.andrioddeveloplibrary.encryption.Decryptor;
import com.sum.andrioddeveloplibrary.encryption.Encryptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncryptionActivity extends BaseAppActivity {

    TextView tv_content, tv_content2;

    String mData = "你好呀";
    String mKey = "12345Sum";
    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    //DES/CBC/PKCS5Padding


    private Decryptor mDe;
    private Encryptor mEn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_encrpytion;
    }

    @Override
    protected void initParams() {
        tv_content = findViewById(R.id.tv_content);
        tv_content2 = findViewById(R.id.tv_content2);
        try {
            mDe = new Decryptor();
            mEn = new Encryptor();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    add();
                }
            }
        });

        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tv_content.setText(mDe
                            .decryptData("sum1", mEn.getEncryption(), mEn.getIv()));
                } catch (UnrecoverableEntryException | BadPaddingException | NoSuchAlgorithmException | KeyStoreException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | IOException | InvalidAlgorithmParameterException | IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //不可逆过程，来大道部分数据安全性
//                String content = EncryptUtils.encryptSHA256ToString(mData);
//                tv_content2.setText(content);

//                byte[] bytes = EncryptUtils.encryptDES(mData.getBytes(), mKey.getBytes(), ALGORITHM_DES, mKey.getBytes());

                byte[] bytes = DESUtils.encrypt(mData.getBytes(), DESUtils.generateKey());

                if (bytes != null) {
                    try {
                        tv_content2.setText(new String(bytes, "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        findViewById(R.id.btn_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = tv_content2.getText().toString().trim();
                if (!TextUtils.isEmpty(trim)) {

//                    byte[] data = EncryptUtils.encryptDES(mData.getBytes(), mKey.getBytes(), ALGORITHM_DES, mKey.getBytes());
//                    byte[] bytes = EncryptUtils.decryptDES(data, mKey.getBytes(), ALGORITHM_DES, mKey.getBytes());

                    byte[] data = DESUtils.encrypt(mData.getBytes(), DESUtils.generateKey());
                    byte[] bytes = DESUtils.decrypt(data, DESUtils.generateKey());
                    if (bytes != null) {
                        try {
                            tv_content2.setText(new String(bytes, "utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void add() {
        try {
//            KeyStore instance = KeyStore.getInstance("AndroidKeyStore");
//            Enumeration<String> aliases = instance.aliases();

            final byte[] encryptedText = mEn.encryptText("sum1", mData);
            tv_content.setText(Base64.encodeToString(encryptedText, Base64.DEFAULT));

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
