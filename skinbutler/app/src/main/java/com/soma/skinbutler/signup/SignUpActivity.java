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
import com.soma.skinbutler.serverinterface.request.SignUpRequest;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity  implements SignUpContract.View {
    private final static int PHOTO_CODE = 1001;

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
    @BindView(R.id.btn_dryness)
    RadioButton drynessBtn;
    @BindView(R.id.btn_neutral)
    RadioButton neutralBtn;
    @BindView(R.id.btn_oilly)
    RadioButton oillyBtn;
    @BindView(R.id.btn_compound)
    RadioButton compoundBtn;
    @BindView(R.id.btn_sensitivity)
    RadioButton sensitivityBtn;

    SignUpPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        ActionBarManager.initBackArrowActionbar(this, toolbar, getString(R.string.signUp));

        mPresenter = new SignUpPresenter();
        mPresenter.setView(this, this);

        genderRadioGroup.setOnCheckedChangeListener(mGenderChangedListener);
        drynessBtn.setOnCheckedChangeListener(mSkinTypeChangedListener);
        neutralBtn.setOnCheckedChangeListener(mSkinTypeChangedListener);
        oillyBtn.setOnCheckedChangeListener(mSkinTypeChangedListener);
        compoundBtn.setOnCheckedChangeListener(mSkinTypeChangedListener);
        sensitivityBtn.setOnCheckedChangeListener(mSkinTypeChangedListener);
        profileImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.profile_default));
    }

    RadioGroup.OnCheckedChangeListener mGenderChangedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
            setWhiteButton(maleBtn);
            setWhiteButton(femaleBtn);

            switch (id) {
                case R.id.btn_male:
                    setBlackButton(maleBtn);
                    break;
                case R.id.btn_female:
                    setBlackButton(femaleBtn);
                    break;
            }
        }
    };

    CompoundButton.OnCheckedChangeListener mSkinTypeChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                setWhiteButton(drynessBtn);
                setWhiteButton(neutralBtn);
                setWhiteButton(oillyBtn);
                setWhiteButton(compoundBtn);
                setWhiteButton(sensitivityBtn);

                compoundButton.setBackgroundResource(R.drawable.black_oval_btn_background);
                compoundButton.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.white));
            }
        }
    };

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
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, data);
        daySpinner.setAdapter(adapter);
    }

    @Override
    public void setYearSpinner(ArrayList<String> data) {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, data);
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
        String year = yearSpinner.getSelectedItem().toString();
        String month = monthSpinner.getSelectedItem().toString();

        if (month.equals("January"))
            month = "01";
        else if (month.equals("February"))
            month = "02";
        else if (month.equals("March"))
            month = "03";
        else if (month.equals("April"))
            month = "04";
        else if (month.equals("May"))
            month = "05";
        else if (month.equals("June"))
            month = "06";
        else if (month.equals("July"))
            month = "07";
        else if (month.equals("August"))
            month = "08";
        else if (month.equals("September"))
            month = "09";
        else if (month.equals("October"))
            month = "10";
        else if (month.equals("November"))
            month = "11";
        else if (month.equals("December"))
            month = "12";

        String day = daySpinner.getSelectedItem().toString();
        return year+"-"+month+"-"+day;
    }

    protected int getGender() {
        if (maleBtn.isChecked()) {
            return 0;
        } else {
            return 1;
        }
    }

    protected int getSkinTypeId() {
        if (drynessBtn.isChecked())
            return 1;
        else if (neutralBtn.isChecked())
            return 2;
        else if (oillyBtn.isChecked())
            return 3;
        else if (compoundBtn.isChecked())
            return 4;
        else
            return 5;
    }

    @Override
    public void goToLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void showErrorDialog(String errorMsg) {
        SimpleDialogBuilder.makeCustomOneButtonDialogAndShow(this, errorMsg, getLayoutInflater());
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
        boolean isValid = mPresenter.validate(nameEdit.getText().toString(), pwEdit.getText().toString(), pwConfirmEdit.getText().toString());

        if (isValid) {
            mPresenter.signUp();
        }
    }

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