package com.soma.skinbutler.signup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.soma.skinbutler.R;
import com.soma.skinbutler.common.util.ActionBarManager;
import com.soma.skinbutler.common.util.ImageUtil;
import com.soma.skinbutler.common.util.SimpleDialogBuilder;
import com.soma.skinbutler.login.LoginActivity;
import com.soma.skinbutler.model.User;
import com.soma.skinbutler.serverinterface.request.SignUpRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity  implements SignUpContract.View {
    private final static int PHOTO_CODE = 1001;
    private final static int[] SKIN_TYPES = {User.SkinType.DRYNESS, User.SkinType.NEUTRAL,
            User.SkinType.OILLY, User.SkinType.COMPOUND, User.SkinType.SENSITIVITY};

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_profile)
    ImageView profileImage;
    @BindView(R.id.edit_name)
    EditText nameEdit;
    @BindView(R.id.edit_pw)
    EditText pwEdit;
    @BindView(R.id.edit_pw_confirm)
    EditText pwConfirmEdit;
    @BindView(R.id.spinner_month)
    Spinner monthSpinner;
    @BindView(R.id.spinner_day)
    Spinner daySpinner;
    @BindView(R.id.spinner_year)
    Spinner yearSpinner;
    @BindView(R.id.radio_gender)
    RadioGroup genderRadioGroup;
    @BindView(R.id.btn_male)
    RadioButton maleBtn;
    @BindView(R.id.btn_female)
    RadioButton femaleBtn;
    @BindViews({R.id.btn_dryness, R.id.btn_neutral, R.id.btn_oilly, R.id.btn_compound, R.id.btn_sensitivity})
    List<RadioButton> skinTypeBtns;

    SignUpPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        ActionBarManager.initBackArrowActionbar(this, toolbar, getString(R.string.signUp));

        mPresenter = new SignUpPresenter();
        mPresenter.setView(this);

        genderRadioGroup.setOnCheckedChangeListener(mGenderChangedListener);
        for (RadioButton btn : skinTypeBtns) {
            btn.setOnCheckedChangeListener(mSkinTypeChangedListener);
        }
        profileImage.setBackground(getResources().getDrawable(R.drawable.profile_default));
    }

    protected void setBlackButton(RadioButton btn) {
        btn.setBackgroundResource(R.drawable.black_oval_btn_background);
        btn.setTextColor(ContextCompat.getColor(this, R.color.white));
        btn.setChecked(true);
    }

    protected void setWhiteButton(RadioButton btn) {
        btn.setBackgroundResource(R.drawable.white_oval_btn_background);
        btn.setTextColor(ContextCompat.getColor(this, R.color.textBlack));
        btn.setChecked(false);
    }

    @Override
    public void setDaySpinner(ArrayList<String> data) {
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, data);
        daySpinner.setAdapter(adapter);
    }

    @Override
    public void setYearSpinner(ArrayList<String> data) {
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, data);
        yearSpinner.setAdapter(adapter);
    }

    @Override
    public SignUpRequest collectData(String image) {
        SignUpRequest request = new SignUpRequest();
        request.setName(nameEdit.getText().toString());
        request.setPassword(pwEdit.getText().toString());
        request.setBirthday(getBirthday());
        request.setEmail("");
        request.setSex(getGender());
        request.setProfileImg(image);
        request.setSkinTypeId(getSkinTypeId());
        return request;
    }

    protected String getBirthday() {
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(yearSpinner.getSelectedItem().toString());
        strBuilder.append("-");

        int month = monthSpinner.getSelectedItemPosition() + 1;

        if (month % 10 < 1) {
            strBuilder.append("0");
            strBuilder.append(month);
        } else {
            strBuilder.append(month);
        }

        strBuilder.append("-");
        strBuilder.append(daySpinner.getSelectedItem().toString());

        return strBuilder.toString();
    }

    @User.Gender
    protected int getGender() {
        if (maleBtn.isChecked()) {
            return User.Gender.MALE;
        } else {
            return User.Gender.FEMALE;
        }
    }

    @User.SkinType
    protected int getSkinTypeId() {
        for (int i = 0; i < skinTypeBtns.size(); i++) {
            if (skinTypeBtns.get(i).isChecked()) {
                return SKIN_TYPES[i];
            }
        }
        return SKIN_TYPES[0];
    }

    @Override
    public void goToLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void showErrorDialog(int errorMsgId) {
        SimpleDialogBuilder.makeCustomOneButtonDialogAndShow(this, getString(errorMsgId),
                getLayoutInflater());
    }

    @OnClick(R.id.image_profile)
    public void goToGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK);
        cameraIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        cameraIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(cameraIntent, PHOTO_CODE);
    }

    @OnClick(R.id.btn_sign_up)
    public void signUpBtn() {
        boolean isValid = mPresenter.validate(nameEdit.getText().toString(),
                pwEdit.getText().toString(), pwConfirmEdit.getText().toString());

        if (isValid) {
            mPresenter.signUp();
        }
    }

    private RadioGroup.OnCheckedChangeListener mGenderChangedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
            switch (id) {
                case R.id.btn_male:
                    setBlackButton(maleBtn);
                    setWhiteButton(femaleBtn);
                    break;
                case R.id.btn_female:
                    setBlackButton(femaleBtn);
                    setWhiteButton(maleBtn);
                    break;
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener mSkinTypeChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                for (RadioButton btn : skinTypeBtns) {
                    setWhiteButton(btn);
                }

                compoundButton.setBackgroundResource(R.drawable.black_oval_btn_background);
                compoundButton.setTextColor(ContextCompat.getColor(SignUpActivity.this,
                        R.color.white));
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home || id == android.R.id.home) {
            goToLoginActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    profileImage.setBackgroundDrawable(ImageUtil.bitmapToBitmapDrawable(SignUpActivity.this, data));
                    String path = ImageUtil.getRealPathFromURI(SignUpActivity.this, data.getData());
                    mPresenter.setProfile(ImageUtil.imageToString(new File(path)));
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToLoginActivity();
    }
}