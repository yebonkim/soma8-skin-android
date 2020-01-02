package com.soma.skinbutler.camera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.soma.skinbutler.R;
import com.soma.skinbutler.common.util.ActionBarManager;
import com.soma.skinbutler.webview.WebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultActivity extends AppCompatActivity  {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.skinNameTV)
    TextView skinNameTV;
    @BindView(R.id.colorIV)
    ImageView colorIV;
    @BindView(R.id.infoTV)
    TextView infoTV;

    int skin_id;
    String[] skinName = {"봄 라이트", "봄 브라이트", "여름 라이트", "여름 뮤트", "가을 딥", "가을 뮤트", "겨울 딥", "겨울 브라이트"};
    int[] colorImg = {R.drawable.light_spring, R.drawable.spring_bright, R.drawable.summer_light, R.drawable.summer_mute, R.drawable.deep_autumn, R.drawable.fall_mute, R.drawable.winter_deep, R.drawable.winter_bright};
    String[] info = {
            "봄 라이트 톤은 채도가 낮은 색이 어울리는 웜톤이에요.\n\n 대표 연예인은 아이유, 수지, 설리, 송혜교 등이 있어요\n\n 봄 라이트 톤은 채도가 낮은 색 위주로 화장을 해야해요.\n\n 음영화장이나 진한 눈 화장은 어울리지 않고\n\n 눈 화장은 최대한 줄이고 속눈썹을 신경써서 올리고 립 화장에 집중해야 훨씬 자연스러워요\n\n 립스틱 색은 채도가 높지않고 명도가 높은 색상이 어울려요.\n\n 블러셔는 최대한 생략하는 것이 좋으나 흰색이 섞인 핑크가 어울리는 편이에요!\n\n ",

            "봄 브라이트는 채도가 높은 색이 어울리는 웜톤이에요 \n\n 대표 연예인은 조이, 유빈, 제시카 등이 있어요 \n\n  봄 브라이트톤은 눈 쪽에서 색조를 빼고 아이라이너 마스카라로 눈을 또렷하게 만들어야해요.\n\n 아이라인에 색감을 더하지 않았으므로 블러셔는 조금 강조해도 된답니다. \n\n 봄 브라이트의 글리터 펄은 클수록 좋아요",

            "여름 라이트는 채도가 낮고 명도가 높은 푸른색 계열의 색이 어울리는 쿨톤이에요. \n\n 대표 연예인은 태연, 이영애, 손예 등이 있어요. \n\n 여름 라이트는 갈색기가 없는 흑색 머리색이 어울려요.  \n\n 섀도우를 강하게 하면 답답하고 더워보일 수 있으니 조심해야해요.  \n\n 립 컬러 역시 너무 진한 컬러는 피해야 합니다.",

            "여름 뮤트는 채도가 낮고 명도가 매우 낮은 회색빛 계열의 색이 어울리는 쿨톤이에요.  \n\n 대표 연예인은 김연아, 정채연 등이 있어요.  \n\n 여름 뮤트는 갈색기가 없는 흑색 머리색이 어율려요.  \n\n 정돈되고 깨끗한 피부 표현이 중요하며 속눈썹과 아이라인을 강조하는게 좋아요. \n\n 립 컬러는 핑크톤의 색상이 어울려요.",

            "가을 딥 톤은 명도가 낮은 진한 색이 어울리는 웜톤이에요.\n\n" +
            "\n" +
            "또 이름에서 처럼 가을 단풍색과 관련된 색조가 어울려요.\n\n" +
            "\n" +
            "대표 연예인은 하니, 김효진, 김유정 등이 있어요\n\n" +
            "\n" +
            "음영화장이나 진하고 펄이 들어간 눈 화장이 어울려요.\n\n" +
            "\n" +
            "아이라인은 검은색보다 갈색이 어울리며 블러셔는 필수에요.\n\n" +
            "\n" +
            "립스틱 색은 채도가 높지않고 명도가 높은 색상이 어울려요.\n\n" +
            "\n" +
            "흰색이 포함된 핑크는 절대 금물이에요!\n\n",

            "가을 뮤트 톤은 명도와 채도 모두 낮은 진한 색이 어울리는 웜톤이에요.\n\n" +
                    "\n" +
                    "대체로 노란끼가 없는 색상의 화장품이 어울려요.\n\n" +
                    "\n" +
                    "대표 연예인은 크리스탈, 미란다 커 등이 있어요\n\n" +
                    "\n" +
                    "음영화장이나 진하고 펄이 들어간 눈 화장이 어울려요.\n\n" +
                    "\n" +
                    "아이라인은 검은색보다 갈색이 어울리며 블러셔는 필수에요.\n\n" +
                    "\n" +
                    "립스틱 색은 채도가 높지않고 명도가 높은 색상이 어울려요.\n\n" +
                    "\n" +
                    "흰색이 포함된 핑크는 절대 금물이에요!\n\n",

            "겨울 딥톤은 저~중 채도, 저명도의 어두운 색상이 어울리는 쿨톤이에요.\n\n 대표 연예인으로는 김혜수 등이 있어요. \n\n 겨울 딥톤은 대체로 어두운 색상이 어울리는데 흑발, 그레이 렌즈, 어두운 립 컬러등이 베스트 제품이에요.\n\n 깨끗한 피부표현이 중요하며 노란끼, 주황끼가 섞인 표현은 피해야 합니다. \n\n 립은 최대한 진한 색으로 선택하는 것이 좋아요." ,

            "겨울 브라이트는 고채도, 중명도의 색상이 어울리는 쿨톤이에요.\n\n 대표 연예인으로는 이효리 등이 있어요.\n\n 겨울 브라이트 톤은 대비가 이루어지는 블랙/화이트 같은 색상이 어울려요.\n\n 회색, 흰색끼가 섞인 제품은 피해야 하며 가능한 흑발, 검은색 속눈썹, 검은색 아이라인을 추천해요. \n\n  "
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);


        skin_id = getIntent().getIntExtra("skin_id", 1);
        skin_id-=1;
        setData();
        ActionBarManager.initBackArrowActionbar(this, toolbar, skinName[skin_id]);
    }

    protected void setData() {
        skinNameTV.setText(skinName[skin_id]);
        colorIV.setBackground(getDrawable(colorImg[skin_id]));
        infoTV.setText(info[skin_id]);
    }

    protected void goToCameraAcitivity() {
        startActivity(new Intent(this, CameraActivity.class));
        finish();
    }

    @OnClick(R.id.goWebViewBtn)
    public void goToWebView() {
        startActivity(new Intent(this, WebViewActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home || id == android.R.id.home) {
            goToCameraAcitivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToCameraAcitivity();
    }
}