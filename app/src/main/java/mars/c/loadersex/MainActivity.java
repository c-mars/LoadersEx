package mars.c.loadersex;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends ListActivity {

    private ArrayAdapter<String> a;
    @Bind(R.id.t)
    TextView t;
    int limit =100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        a = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        Observable.range(0, limit)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> a.add(integer.toString()));

        setListAdapter(a);

        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                t.setText("f:"+firstVisibleItem+" vc:"+visibleItemCount+" tc:"+totalItemCount);
                if(totalItemCount >= limit && (firstVisibleItem>totalItemCount-2*visibleItemCount)){
                    int count=2*visibleItemCount;
                    limit =totalItemCount+count;
                    Observable.range(totalItemCount, count)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(integer -> {a.add(integer.toString()); t.append("+"+integer.toString());});
                }
            }
        });
    }


}
