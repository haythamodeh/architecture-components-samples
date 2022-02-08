/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.persistence.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.persistence.R;
import com.example.android.persistence.databinding.ProductFragmentBinding;
import com.example.android.persistence.db.dao.ProductDao;
import com.example.android.persistence.db.entity.ProductEntity;
import com.example.android.persistence.model.Product;
import com.example.android.persistence.viewmodel.ProductViewModel;

import java.util.List;

public class ProductFragment extends Fragment {

    private static final String KEY_PRODUCT_ID = "product_id";

    private ProductFragmentBinding mBinding;

    private CommentAdapter mCommentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.product_fragment, container, false);

        // Create and set the adapter for the RecyclerView.
        mCommentAdapter = new CommentAdapter(mCommentClickCallback);
        mBinding.commentList.setAdapter(mCommentAdapter);

        return mBinding.getRoot();
    }

    private final CommentClickCallback mCommentClickCallback = comment -> {
        // no-op
    };
    public void changeRating(View v) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ProductViewModel.Factory factory = new ProductViewModel.Factory(
                requireActivity().getApplication(), requireArguments().getInt(KEY_PRODUCT_ID));

        final ProductViewModel model = new ViewModelProvider(this, factory)
                .get(ProductViewModel.class);

        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setProductViewModel(model);

        subscribeToModel(model);
        Button btn = (Button) view.findViewById(R.id.editRatingBtn);
        EditText ratingEditText = view.findViewById(R.id.editRating);

        btn.setOnClickListener((View v) -> {
            String ratingText = ratingEditText.getText().toString();
            int rating = Integer.parseInt(ratingText);
            //Toast.makeText(getContext(), ratingText, Toast.LENGTH_SHORT).show();
//            ProductEntity p = new ProductEntity();
//            ProductDao pp = new ProductDao() {
//                @Override
//                public LiveData<List<ProductEntity>> loadAllProducts() {
//                    return null;
//                }
//
//                @Override
//                public void insertAll(List<ProductEntity> products) {
//
//                }
//
//                @Override
//                public void update(int rating, int id) {
//
//                }
//
//                @Override
//                public LiveData<ProductEntity> loadProduct(int productId) {
//                    return null;
//                }
//
//                @Override
//                public ProductEntity loadProductSync(int productId) {
//                    return null;
//                }
//
//                @Override
//                public LiveData<List<ProductEntity>> searchAllProducts(String query) {
//                    return null;
//                }
//            };
//            //requireArguments().getInt(KEY_PRODUCT_ID)
//            pp.update(rating, p.getId());
        });
    }

    private void subscribeToModel(final ProductViewModel model) {
        // Observe comments
        model.getComments().observe(getViewLifecycleOwner(), commentEntities -> {
            if (commentEntities != null) {
                mBinding.setIsLoading(false);
                mCommentAdapter.submitList(commentEntities);
            } else {
                mBinding.setIsLoading(true);
            }
        });
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        mCommentAdapter = null;
        super.onDestroyView();
    }

    /** Creates product fragment for specific product ID */
    public static ProductFragment forProduct(int productId) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }
}
