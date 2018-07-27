package com.zhxh.xtouchsystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import com.zhxh.xtouchsystem.touch.Util;


/**
 * 总统 --> MainActivity
 * 省长 --> CFrameLayout
 * 市长 --> CLinearLayout
 * 农民 --> CTextView
 * [举个通俗易懂的例子]：
 * 总统对省长说：我要吃黄焖鸡
 * 省长对市长说：你做个黄焖鸡
 * 市长对县长说：你做个黄焖鸡
 * 县长对农民说：你做个黄焖鸡
 * ……（农民做呀做，没做出来）
 * 农民说：我尽力了，但真心不会做呀，饶了我吧
 * 县长说：你个笨蛋，下次不找你了，看我来做
 * ……（县长做呀做，没做出来）
 * 县长对市长说：我尽力了，非常抱歉，我不会做
 * 市长说：你个废物，要你何用，只能我自己来做了
 * ……（市长做呀做，做成功了）
 * 市长对省长说：黄焖鸡做好了
 * 省长说：不错，下次有事还找你
 * 省长对总统说：黄焖鸡做好了
 * 总统说：不错，下次有事还找你
 * ---------------------------
 * 总统对省长说：我要吃水煮鱼
 * 省长对市长说：你做个水煮鱼
 * 市长说：县长连黄焖鸡都搞不定，这次就不找他了，我自己亲自来做
 * ……（市长做呀做，又成功了）
 * 市长对省长说：水煮鱼做好了
 * 省长说：不错，下次有事还找你
 * 省长对总统说：水煮鱼做好了
 * 总统说：不错，下次有事还找你
 * ---------------------------
 * 按常理，领导都会把任务向下分派，一旦下面的人把事情做不好，就不会再把后续的任务交给下面的人来做了，只能自己亲自做，如果自己也做不了，就只能告诉上级不能完成任务，上级又会重复他的过程。
 * 另外，领导都有权利拦截任务，对下级隐瞒该任务，而直接自己去做，如果做不成，也只能向上级报告不能完成任务。
 */

/**
 * 1，介绍Android touch system 内部事件传递机制
 * 2，介绍ViewGroup与View绘制过程
 */
public class MainActivity extends AppCompatActivity {
    Button button;

    public static final String TAG = MainActivity.class.getSimpleName();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.d(TAG, "hello");
        button = findViewById(R.id.btn_conflict);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //button.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_big));

/*                int height = button.getHeight();
                int width = button.getWidth();
                float size = button.getTextSize();
                height = height * 4 / 5;
                width = width * 4 / 5;
                size = size * 4 / 5;
                button.setLayoutParams(new LinearLayout.LayoutParams(width, height));

                button.setTextSize(size);*/

                Log.d("zhxh", "btn_conflict-onClick");
                //startActivity(new Intent(MainActivity.this, ConflictMainActivity.class));
            }
        });
        findViewById(R.id.my_text_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("zhxh", "my_text_view-onClick");
            }
        });
        findViewById(R.id.my_text_view).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("zhxh", "my_text_view-onTouch");
                return false;
            }
        });
        findViewById(R.id.my_linear_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("zhxh", "my_linear_layout-onClick");
            }
        });
        findViewById(R.id.my_linear_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("zhxh", "my_linear_layout-onTouch");
                return false;
            }
        });
        findViewById(R.id.my_frame_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("zhxh", "my_frame_layout-onClick");
            }
        });
        findViewById(R.id.my_frame_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("zhxh", "my_linear_layout-onTouch");
                return false;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("zhxh", "[总统]-dispatchTouchEvent-任务<" + Util.actionToString(ev.getAction()) + "> : 需要分派");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean bo = false;
        Log.d("zhxh", "[总统]-onTouchEvent-任务<" + Util.actionToString(ev.getAction()) + "> : 下面都解决不了，下次再也不能靠你们了，哼⋯只能自己尝试一下啦。能解决？" + bo);
        return bo;
    }
}
