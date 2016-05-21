package com.erdemorman.mdx.ui.colors;

import com.erdemorman.mdx.data.DataManager;
import com.erdemorman.mdx.data.model.MaterialColor;
import com.erdemorman.mdx.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ColorsPresenter extends BasePresenter<ColorsView> {
    private final DataManager mDataManager;
    private Subscription mSubscription;
    private List<MaterialColor> mColorsCache;

    @Inject
    public ColorsPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();

        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public void loadColors() {
        checkViewAttached();

        if (mColorsCache == null) {
            mSubscription = mDataManager.getMaterialColors()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<MaterialColor>>() {
                        @Override
                        public void call(List<MaterialColor> colors) {
                            mColorsCache = colors;

                            getView().showColors(colors);
                        }
                    });
        } else {
            getView().showColors(mColorsCache);
        }
    }
}
